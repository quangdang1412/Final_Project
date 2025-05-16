package com.example.hcmute.quangvaphong.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.VocabularyIelts;
import com.example.hcmute.quangvaphong.models.VocabularyOxford;
import com.example.hcmute.quangvaphong.models.VocabularyToefl;
import com.example.hcmute.quangvaphong.models.VocabularyToeic;
import com.example.hcmute.quangvaphong.repository.VocabularyRepository;
import com.example.hcmute.quangvaphong.utils.PrefsUtil;
import com.example.hcmute.quangvaphong.utils.VocabularyLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView toeicBtn, toeflBtn, ieltsBtn, irreBtn, oxfordBtn;
    private CardView yourWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toeicBtn = findViewById(R.id.toeic_vocab_text);
        toeflBtn = findViewById(R.id.toefl_vocab_text);
        ieltsBtn = findViewById(R.id.ielts_vocab_text);
        oxfordBtn = findViewById(R.id.oxford_vocab_text);
        irreBtn = findViewById(R.id.irregular_verbs_text);
        yourWord = findViewById(R.id.your_words_card);
        setEvent();

        if (PrefsUtil.isFirstRun(this)) {
            VocabularyRepository repository = new VocabularyRepository(this);
            loadDataFromFile(repository);
            new Thread(() -> {
            }).start();
        }
    }

    private void setEvent() {
        findViewById(R.id.translate_doc_card).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TranslateDocumentActivity.class);
            startActivity(intent);
        });
        yourWord.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StudiedVocabularyActivity.class);
            intent.putExtra("type", "yourword");
            startActivity(intent);
        });
        toeicBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
            intent.putExtra("type", "toeic");
            startActivity(intent);
        });

        toeflBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
            intent.putExtra("type", "toefl");
            startActivity(intent);
        });

        ieltsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
            intent.putExtra("type", "ielts");
            startActivity(intent);
        });

        oxfordBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
            intent.putExtra("type", "oxford");
            startActivity(intent);
        });

        irreBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
            intent.putExtra("type", "irre");
            startActivity(intent);
        });
    }

    private void loadDataFromFile(VocabularyRepository repository) {
        List<VocabularyToefl> toeflList = VocabularyLoader.loadVocabularyFromAssets(
                getApplicationContext(),
                "toefl.txt",
                (word, pronunciation, meaning) -> VocabularyToefl.builder()
                        .word(word)
                        .pronunciation(pronunciation)
                        .meaning(meaning)
                        .isSave(false)
                        .build());
        List<VocabularyToeic> vocabularyToeicList = VocabularyLoader.loadVocabularyFromAssets(
                getApplicationContext(),
                "toeic.txt",
                (word, pronunciation, meaning) -> VocabularyToeic.builder()
                        .word(word)
                        .pronunciation(pronunciation)
                        .meaning(meaning)
                        .isSave(false)
                        .build());
        List<VocabularyIelts> vocabularyIeltsList = VocabularyLoader.loadVocabularyFromAssets(
                getApplicationContext(),
                "ielts.txt",
                (word, pronunciation, meaning) -> VocabularyIelts.builder()
                        .word(word)
                        .pronunciation(pronunciation)
                        .meaning(meaning)
                        .isSave(false)
                        .build());

        List<VocabularyOxford> vocabularyOxfordList = VocabularyLoader.loadVocabularyFromAssets(
                getApplicationContext(),
                "essential3000.txt",
                (word, pronunciation, meaning) -> VocabularyOxford.builder()
                        .word(word)
                        .pronunciation(pronunciation)
                        .meaning(meaning)
                        .isSave(false)
                        .build());
        List<IrregularVerb> irregularVerbList = VocabularyLoader.loadIrregularVerbFromAssets(getApplicationContext(),
                "irregverbs.txt");
        if (vocabularyToeicList != null && !vocabularyToeicList.isEmpty()) {
            repository.insertListVocabularyToeic(vocabularyToeicList);
            repository.insertListVocabularyToefl(toeflList);
            repository.insertListVocabularyIelts(vocabularyIeltsList);
            repository.insertListVocabularyOxford(vocabularyOxfordList);
            repository.insertListIrregularVerb(irregularVerbList);
            PrefsUtil.setFirstRun(this, false);
        } else {
            System.err.println("Không load được dữ liệu từ file assets.");
        }
    }
}