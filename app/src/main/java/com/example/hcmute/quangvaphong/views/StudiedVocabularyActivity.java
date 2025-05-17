package com.example.hcmute.quangvaphong.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;
import com.example.hcmute.quangvaphong.views.adapter.StudiedVocabularyAdapter;
import com.example.hcmute.quangvaphong.views.viewModel.VocabularyViewModel;

import java.util.ArrayList;
import java.util.List;

public class StudiedVocabularyActivity extends AppCompatActivity {

    private StudiedVocabularyAdapter adapter;
    private VocabularyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toeic_directory);
        Intent intent = getIntent();
        String value = intent.getStringExtra("type");

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        adapter = new StudiedVocabularyAdapter();
        RecyclerView recyclerView = findViewById(R.id.vocabulary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new VocabularyViewModel(this.getApplication());
        viewModel.getAllStudiedVocabulary().observe(this, toeicList -> {
            List<StudiedVocabulary> vocabList = new ArrayList<>(toeicList);
            adapter.setVocabularyList(vocabList);
        });
        viewModel.getAllVocabularyToeic().observe(this, toeicList -> {

        });
        viewModel.getAllVocabularyToefl().observe(this, toeicList -> {

        });
        viewModel.getAllVocabularyIelts().observe(this, toeicList -> {

        });
        viewModel.getAllVocabularyOxford().observe(this, toeicList -> {

        });
        viewModel.getAllIrregularVerb().observe(this, toeicList -> {

        });


        adapter.setOnStarClickListener(new StudiedVocabularyAdapter.OnStarClickListener() {
            @Override
            public void onStarClick(StudiedVocabulary vocab1, int position) {
                LiveData<StudiedVocabulary> liveData = viewModel.getStudiedVocabularyByWord(vocab1.getWord());
                liveData.observe(StudiedVocabularyActivity.this, new androidx.lifecycle.Observer<StudiedVocabulary>() {
                    @Override
                    public void onChanged(StudiedVocabulary studiedVocab) {
                        liveData.removeObserver(this);
                        vocab1.setIsSave(false);
                        Log.d("hehehe", vocab1.getType() + " " + vocab1.getWord());
                        viewModel.updateVocabulary(vocab1, vocab1.getType());
                        viewModel.deleteStudiedVocabulary(vocab1);
                        Toast.makeText(getApplicationContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}