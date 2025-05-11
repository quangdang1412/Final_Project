package com.example.hcmute.quangvaphong.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.Vocabulary;
import com.example.hcmute.quangvaphong.views.adapter.VocabularyAdapter;
import com.example.hcmute.quangvaphong.views.adapter.VocabularyViewModel;

import java.util.ArrayList;
import java.util.List;

public class VocabularyActivity extends AppCompatActivity {

    private VocabularyAdapter adapter;
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

        adapter = new VocabularyAdapter();
        RecyclerView recyclerView = findViewById(R.id.vocabulary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new VocabularyViewModel(this.getApplication(), value);
        if (value.equals("toeic"))
            viewModel.getAllVocabularyToeic().observe(this, toeicList -> {
                List<Vocabulary> vocabList = new ArrayList<>(toeicList);
                Log.d("hahaha", String.valueOf(vocabList.size()));
                adapter.setVocabularyList(vocabList);
            });
        else if (value.equals("toefl"))
            viewModel.getAllVocabularyToefl().observe(this, toeicList -> {
                List<Vocabulary> vocabList = new ArrayList<>(toeicList);
                adapter.setVocabularyList(vocabList);
            });
        else if (value.equals("ielts"))
            viewModel.getAllVocabularyIelts().observe(this, toeicList -> {
                List<Vocabulary> vocabList = new ArrayList<>(toeicList);
                adapter.setVocabularyList(vocabList);
            });
        else if (value.equals("oxford"))
            viewModel.getAllVocabularyOxford().observe(this, toeicList -> {
                List<Vocabulary> vocabList = new ArrayList<>(toeicList);
                adapter.setVocabularyList(vocabList);
            });
        else
            viewModel.getAllIrregularVerb().observe(this, toeicList -> {
                List<IrregularVerb> vocabList = new ArrayList<>(toeicList);
                adapter.setIrregularVerbList(vocabList);
            });
    }
}