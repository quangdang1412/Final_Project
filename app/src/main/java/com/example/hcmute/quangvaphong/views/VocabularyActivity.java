package com.example.hcmute.quangvaphong.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;
import com.example.hcmute.quangvaphong.models.Vocabulary;
import com.example.hcmute.quangvaphong.views.adapter.VocabularyAdapter;
import com.example.hcmute.quangvaphong.views.viewModel.VocabularyViewModel;

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
        EditText searchText = findViewById(R.id.search_input);

        searchText.addTextChangedListener(new android.text.TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Từ điển " + value.toUpperCase());

        viewModel = new VocabularyViewModel(this.getApplication());
        adapter = new VocabularyAdapter(viewModel, value, this);
        RecyclerView recyclerView = findViewById(R.id.vocabulary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (value.equals("toeic"))
            viewModel.getAllVocabularyToeic().observe(this, toeicList -> {
                List<Vocabulary> vocabList = new ArrayList<>(toeicList);
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

        adapter.setOnStarClickListener(new VocabularyAdapter.OnStarClickListener() {
            @Override
            public void onStarClick(Vocabulary vocab1, int position) {
                LiveData<StudiedVocabulary> liveData = viewModel.getStudiedVocabularyByWord(vocab1.getWord());
                liveData.observe(VocabularyActivity.this, new androidx.lifecycle.Observer<StudiedVocabulary>() {
                    @Override
                    public void onChanged(StudiedVocabulary studiedVocab) {
                        liveData.removeObserver(this);
                        if (studiedVocab == null) {
                            StudiedVocabulary newVocab = new StudiedVocabulary();
                            newVocab.setWord(vocab1.getWord());
                            newVocab.setMeaning(vocab1.getMeaning());
                            newVocab.setPronunciation(vocab1.getPronunciation());
                            newVocab.setIsSave(true);
                            newVocab.setType(value);
                            vocab1.setIsSave(true);
                            viewModel.updateVocabulary(vocab1, value);
                            viewModel.addStudiedVocabulary(newVocab);
                            Toast.makeText(getApplicationContext(), "Đã thêm vào từ đã học", Toast.LENGTH_SHORT).show();
                        } else {
                            vocab1.setIsSave(false);
                            viewModel.updateVocabulary(vocab1, value);
                            viewModel.deleteStudiedVocabulary(studiedVocab);
                            Toast.makeText(getApplicationContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onStarClick(IrregularVerb vocab1, int position) {
                LiveData<StudiedVocabulary> liveData = viewModel.getStudiedVocabularyByWord(vocab1.getWord());
                liveData.observe(VocabularyActivity.this, new androidx.lifecycle.Observer<StudiedVocabulary>() {
                    @Override
                    public void onChanged(StudiedVocabulary studiedVocab) {
                        liveData.removeObserver(this);

                        if (studiedVocab == null) {
                            StudiedVocabulary newVocab = new StudiedVocabulary();

                            IrregularVerb vocab = vocab1;
                            newVocab.setV2(vocab.getV2());
                            newVocab.setV3(vocab.getV3());
                            newVocab.setWord(vocab.getWord());
                            newVocab.setMeaning(vocab.getMeaning());
                            newVocab.setPronunciation(vocab.getPronunciation());
                            newVocab.setIsSave(true);
                            newVocab.setType(value);
                            vocab.setIsSave(true);
                            viewModel.updateVocabulary(vocab, value);
                            viewModel.addStudiedVocabulary(newVocab);
                            Toast.makeText(VocabularyActivity.this, "Đã thêm vào từ đã học", Toast.LENGTH_SHORT).show();
                        } else {

                            IrregularVerb vocab = vocab1;
                            vocab.setIsSave(false);
                            viewModel.updateVocabulary(vocab, value);
                            viewModel.deleteStudiedVocabulary(studiedVocab);
                            Toast.makeText(getApplicationContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}