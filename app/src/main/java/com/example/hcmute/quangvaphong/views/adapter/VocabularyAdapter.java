package com.example.hcmute.quangvaphong.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.Vocabulary;
import com.example.hcmute.quangvaphong.views.viewModel.VocabularyViewModel;

import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabViewHolder> {
    private List<Vocabulary> vocabList = null;
    private List<IrregularVerb> irregularVerbList = null;
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
        this.irregularVerbList = null;
        isIrregularVerbMode = false;
        notifyDataSetChanged();
    }

    public void setIrregularVerbList(List<IrregularVerb> irregularVerbList) {
        this.irregularVerbList = irregularVerbList;
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

