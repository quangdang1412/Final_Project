package com.example.hcmute.quangvaphong.views.adapter;

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
import com.example.hcmute.quangvaphong.models.Vocabulary;
import com.example.hcmute.quangvaphong.views.viewModel.VocabularyViewModel;

import java.util.ArrayList;
import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabViewHolder> implements Filterable {
    private List<Vocabulary> vocabList = null;
    private List<Vocabulary> vocabListFull = null;
    private List<IrregularVerb> irregularVerbList = null;
    private List<IrregularVerb> irregularVerbListFull = null;
    private VocabularyViewModel viewModel;
    private String type;
    private LifecycleOwner lifecycleOwner;
    private boolean isIrregularVerbMode = false;

    public VocabularyAdapter(VocabularyViewModel viewModel, String type, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.type = type;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setVocabularyList(List<Vocabulary> vocabList) {
        this.vocabList = vocabList;
        this.vocabListFull = new ArrayList<>(vocabList);
        this.irregularVerbList = null;
        isIrregularVerbMode = false;
        notifyDataSetChanged();
    }

    public void setIrregularVerbList(List<IrregularVerb> irregularVerbList) {
        this.irregularVerbList = irregularVerbList;
        this.irregularVerbListFull = new ArrayList<>(irregularVerbList);
        this.vocabList = null;
        isIrregularVerbMode = true;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
        return new VocabViewHolder(view);
    }

    public interface OnStarClickListener {
        void onStarClick(Vocabulary vocab, int position);

        void onStarClick(IrregularVerb vocab, int position);
    }

    private OnStarClickListener starClickListener;

    public void setOnStarClickListener(OnStarClickListener listener) {
        this.starClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        if (!isIrregularVerbMode && vocabList != null) {
            Vocabulary vocab = vocabList.get(position);
            holder.wordText.setText(vocab.getWord());
            holder.pronunciationText.setText(vocab.getPronunciation());
            holder.meaningText.setText(vocab.getMeaning());

            holder.wordV2Text.setVisibility(View.GONE);
            holder.wordV3Text.setVisibility(View.GONE);
        } else if (isIrregularVerbMode && irregularVerbList != null) {
            IrregularVerb vocab = irregularVerbList.get(position);
            holder.wordText.setText(vocab.getWord());
            holder.wordV2Text.setText("V2: " + vocab.getV2());
            holder.wordV3Text.setText("V3: " + vocab.getV3());
            holder.pronunciationText.setText(vocab.getPronunciation());
            holder.meaningText.setText(vocab.getMeaning());

            holder.wordV2Text.setVisibility(View.VISIBLE);
            holder.wordV3Text.setVisibility(View.VISIBLE);
        }
        boolean saved = false;
        if (!isIrregularVerbMode && vocabList != null) {
            saved = vocabList.get(position).getIsSave();
        } else if (isIrregularVerbMode && irregularVerbList != null) {
            saved = irregularVerbList.get(position).getIsSave();
        }

        if (saved) {
            holder.starIcon.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.starIcon.setImageResource(android.R.drawable.btn_star_big_off);
        }
        holder.starIcon.setOnClickListener(v -> {
            if (!isIrregularVerbMode && vocabList != null) {
                starClickListener.onStarClick(vocabList.get(position), position);
            } else if (isIrregularVerbMode && irregularVerbList != null) {
                starClickListener.onStarClick(irregularVerbList.get(position), position);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            String word;
            if (!isIrregularVerbMode && vocabList != null) {
                word = vocabList.get(position).getWord();
            } else if (isIrregularVerbMode && irregularVerbList != null) {
                word = irregularVerbList.get(position).getWord();
            } else {
                return;
            }
            android.content.Intent intent = new android.content.Intent(v.getContext(),
                    com.example.hcmute.quangvaphong.views.DictionaryActivity.class);
            intent.putExtra("word", word);
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (isIrregularVerbMode && irregularVerbList != null) {
            return irregularVerbList.size();
        } else if (vocabList != null) {
            return vocabList.size();
        }
        return 0;
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
                vocabList = (List<Vocabulary>) results.values;
            } else {
                irregularVerbList = (List<IrregularVerb>) results.values;
            }
            notifyDataSetChanged();
        }
    };

    static class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView wordText, wordV2Text, wordV3Text, pronunciationText, meaningText;
        ImageView speakerIcon, starIcon;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.word_text);
            wordV2Text = itemView.findViewById(R.id.word_v2_text);
            wordV3Text = itemView.findViewById(R.id.word_v3_text);
            pronunciationText = itemView.findViewById(R.id.pronunciation_text);
            meaningText = itemView.findViewById(R.id.meaning_text);
            speakerIcon = itemView.findViewById(R.id.speaker_icon);
            starIcon = itemView.findViewById(R.id.star_icon);
        }
    }

}
