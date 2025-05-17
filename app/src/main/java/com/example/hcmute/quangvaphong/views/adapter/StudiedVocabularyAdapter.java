package com.example.hcmute.quangvaphong.views.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;
import com.example.hcmute.quangvaphong.models.Vocabulary;
import com.example.hcmute.quangvaphong.views.DictionaryActivity;
import com.example.hcmute.quangvaphong.views.viewModel.VocabularyViewModel;

import java.util.ArrayList;
import java.util.List;

public class StudiedVocabularyAdapter extends RecyclerView.Adapter<StudiedVocabularyAdapter.VocabViewHolder> implements Filterable {
    private List<StudiedVocabulary> vocabList = null;
    private List<Vocabulary> vocabListFull = null;
    private List<IrregularVerb> irregularVerbList = null;
    private List<IrregularVerb> irregularVerbListFull = null;
    private VocabularyViewModel viewModel;
    private String type;
    private LifecycleOwner lifecycleOwner;
    private boolean isIrregularVerbMode = false;
    public StudiedVocabularyAdapter() {
    }


    public void setVocabularyList(List<StudiedVocabulary> vocabList) {
        this.vocabList = vocabList;
        this.vocabListFull = new ArrayList<>(vocabList);
        this.irregularVerbList = null;
        isIrregularVerbMode = false;
        notifyDataSetChanged();
    }
    public StudiedVocabulary getItem(int position) {
        return vocabList != null && position < vocabList.size() ? vocabList.get(position) : null;
    }

    @Override
    public Filter getFilter() {
        return vocabularyFilter;

    }
    private Filter vocabularyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                if (!isIrregularVerbMode && vocabListFull != null) {
                    results.values = vocabListFull;
                    results.count = vocabListFull.size();
                } else if (isIrregularVerbMode && irregularVerbListFull != null) {
                    results.values = irregularVerbListFull;
                    results.count = irregularVerbListFull.size();
                }
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                if (!isIrregularVerbMode && vocabListFull != null) {
                    List<Vocabulary> filteredList = new ArrayList<>();
                    for (Vocabulary item : vocabListFull) {
                        if (item.getWord().toLowerCase().contains(filterPattern) ||
                                item.getMeaning().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                } else if (isIrregularVerbMode && irregularVerbListFull != null) {
                    List<IrregularVerb> filteredList = new ArrayList<>();
                    for (IrregularVerb item : irregularVerbListFull) {
                        if (item.getWord().toLowerCase().contains(filterPattern) ||
                                item.getMeaning().toLowerCase().contains(filterPattern) ||
                                item.getV2().toLowerCase().contains(filterPattern) ||
                                item.getV3().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values == null) {
                return;
            }

            if (!isIrregularVerbMode) {
                vocabList = (List<StudiedVocabulary>) results.values;
            } else {
                irregularVerbList = (List<IrregularVerb>) results.values;
            }
            notifyDataSetChanged();
        }
    };

    public interface OnStarClickListener {
        void onStarClick(StudiedVocabulary vocab, int position);

    }

    private StudiedVocabularyAdapter.OnStarClickListener starClickListener;

    public void setOnStarClickListener(StudiedVocabularyAdapter.OnStarClickListener listener) {
        this.starClickListener = listener;
    }

    @NonNull
    @Override
    public StudiedVocabularyAdapter.VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
        return new StudiedVocabularyAdapter.VocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudiedVocabularyAdapter.VocabViewHolder holder, int position) {
        StudiedVocabulary vocab = vocabList.get(position);
        if (!TextUtils.isEmpty(vocab.getV2())) {
            holder.wordV2Text.setText("V2: " + vocab.getV2());
            holder.wordV2Text.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(vocab.getV3())) {
            holder.wordV3Text.setText("V3: " + vocab.getV3());
            holder.wordV3Text.setVisibility(View.VISIBLE);
        }
        holder.wordText.setText(vocab.getWord());
        holder.pronunciationText.setText(vocab.getPronunciation());
        holder.meaningText.setText(vocab.getMeaning());
        boolean saved = false;
        if (vocab != null)
            saved = vocabList.get(position).getIsSave();

        if (saved) {
            holder.starIcon.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.starIcon.setImageResource(android.R.drawable.btn_star_big_off);
        }
        holder.starIcon.setOnClickListener(v -> {
            if (starClickListener != null) {
                starClickListener.onStarClick(vocab, position);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DictionaryActivity.class);
            intent.putExtra("word", vocab.getWord());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vocabList == null ? 0 : vocabList.size();
    }

    static class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView wordText, wordV2Text, wordV3Text, pronunciationText, meaningText;
        ImageView starIcon;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.word_text);
            wordV2Text = itemView.findViewById(R.id.word_v2_text);
            wordV3Text = itemView.findViewById(R.id.word_v3_text);
            pronunciationText = itemView.findViewById(R.id.pronunciation_text);
            meaningText = itemView.findViewById(R.id.meaning_text);
            starIcon = itemView.findViewById(R.id.star_icon);
        }
    }
}
