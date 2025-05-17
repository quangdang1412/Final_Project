package com.example.hcmute.quangvaphong.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.Question;
import com.example.hcmute.quangvaphong.models.Quiz;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;
import com.example.hcmute.quangvaphong.views.adapter.ListQuestionAdapter;
import com.example.hcmute.quangvaphong.views.viewModel.QuizViewModel;
import com.example.hcmute.quangvaphong.views.viewModel.VocabularyViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {
    private List<Question> data;
    private String quizID;
    private VocabularyViewModel viewModel;
    private QuizViewModel quizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ListView listView = findViewById(R.id.listview);
        Button submitBtn = findViewById(R.id.submitBtn);
        TextView title = findViewById(R.id.title);
        viewModel = new VocabularyViewModel(this.getApplication());
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        generateQuestionsFromStudiedVocabulary(20, questions -> {
            runOnUiThread(() -> {
                if (questions == null || questions.isEmpty()) {
                    Toast.makeText(this, "Không đủ dữ liệu để tạo quiz!", Toast.LENGTH_SHORT).show();
                } else {
                    this.data = questions;
                    ListQuestionAdapter adapter = new ListQuestionAdapter(this, data);
                    listView.setAdapter(adapter);
                }
            });
        });

        submitBtn.setOnClickListener(v -> {
            if (data == null || data.isEmpty()) {
                Toast.makeText(this, "Chưa có câu hỏi để nộp!", Toast.LENGTH_SHORT).show();
                return;
            }

            int correct = 0;
            for (Question q : data) {
                if (q.getSelectedAnswer() == -1) {
                    Toast.makeText(this, "Vui lòng trả lời hết các câu hỏi trước khi nộp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (q.getCorrectAnswer() == q.getSelectedAnswer())
                    correct++;
            }

            Quiz quiz = new Quiz();
            quiz.setDateTime(System.currentTimeMillis());
            quiz.setCorrectAnswer(correct);
            quiz.setTotalAnswer(data.size());

            quizViewModel.setQuizResults(data);
            quizViewModel.saveQuizAndQuestions(quiz, data);

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Nộp bài")
                    .setMessage("Bạn đã nộp bài thành công!")
                    .setPositiveButton("Xem kết quả", (dialog, which) -> {
                        android.content.Intent intent = new android.content.Intent(QuizActivity.this, ResultActivity.class);
                        intent.putExtra("quiz_results", new ArrayList<>(data));
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Đóng", null)
                    .show();
        });
    }

    public interface QuestionsCallback {
        void onQuestionsGenerated(List<Question> questions);
    }

    public void generateQuestionsFromStudiedVocabulary(int count, QuestionsCallback callback) {
        viewModel.getAllStudiedVocabulary().observe(this, new androidx.lifecycle.Observer<List<StudiedVocabulary>>() {
            @Override
            public void onChanged(List<StudiedVocabulary> toeicList) {
                viewModel.getAllStudiedVocabulary().removeObserver(this);
                if (toeicList == null || toeicList.size() < 4) {
                    callback.onQuestionsGenerated(null);
                    return;
                }

                List<StudiedVocabulary> allWords = new ArrayList<>(toeicList);
                List<Question> result = new ArrayList<>();
                Random random = new Random();
                Set<String> usedWords = new HashSet<>();

                int maxAttempts = count * 3;

                while (result.size() < count && maxAttempts > 0) {
                    maxAttempts--;

                    StudiedVocabulary questionWord = allWords.get(random.nextInt(allWords.size()));
                    String word = questionWord.getWord();
                    String meaning = questionWord.getMeaning();

                    if (usedWords.contains(word)) continue;

                    List<String> options = new ArrayList<>();
                    options.add(meaning);

                    int optionAttempts = 10;
                    while (options.size() < 4 && optionAttempts-- > 0) {
                        StudiedVocabulary other = allWords.get(random.nextInt(allWords.size()));
                        String otherMeaning = other.getMeaning();
                        if (!otherMeaning.equals(meaning) && !options.contains(otherMeaning)) {
                            options.add(otherMeaning);
                        }
                    }

                    if (options.size() < 4) continue;

                    Collections.shuffle(options);
                    int correctIndex = options.indexOf(meaning);

                    Question q = Question.builder()
                            .question(word)
                            .opt1(options.get(0))
                            .opt2(options.get(1))
                            .opt3(options.get(2))
                            .opt4(options.get(3))
                            .correctAnswer(correctIndex + 1)
                            .selectedAnswer(-1)
                            .build();

                    result.add(q);
                    usedWords.add(word);
                }

                callback.onQuestionsGenerated(result);
            }
        });
    }
}