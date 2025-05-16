package com.example.hcmute.quangvaphong.views.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;

import java.util.List;

public class StudiedVocabularyAdapter extends RecyclerView.Adapter<StudiedVocabularyAdapter.VocabViewHolder> {
    private List<StudiedVocabulary> vocabList = null;


    public StudiedVocabularyAdapter() {
    }

    public void setVocabularyList(List<StudiedVocabulary> vocabList) {
        this.vocabList = vocabList;
        notifyDataSetChanged();
    }

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
            starClickListener.onStarClick(vocab, position);
        });
    }

    @Override
    public int getItemCount() {
        return vocabList == null ? 0 : vocabList.size();
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
