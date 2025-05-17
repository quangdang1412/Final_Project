package com.example.hcmute.quangvaphong.views;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.Question;
import com.example.hcmute.quangvaphong.views.adapter.ListResultQuestionAdapter;
import com.example.hcmute.quangvaphong.views.viewModel.QuizViewModel;

import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private List<Question> data;

    private QuizViewModel quizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        TextView title = findViewById(R.id.title);
        ListView listView = findViewById(R.id.listview);
        TextView total = findViewById(R.id.total);

        long quizId = getIntent().getLongExtra("quizId", -1);
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        if (quizId != -1) {
            quizViewModel.loadQuestionsByQuizId(quizId);

            quizViewModel.getQuizResults().observe(this, questions -> {
                if (questions != null && !questions.isEmpty()) {
                    ListResultQuestionAdapter adapter = new ListResultQuestionAdapter(this, questions);
                    listView.setAdapter(adapter);

                    long correct = questions.stream()
                            .filter(q -> q.getCorrectAnswer() == q.getSelectedAnswer())
                            .count();
                    total.setText("Điểm: " + correct + "/" + questions.size());
                }
            });
        } else {
            data = (List<Question>) getIntent().getSerializableExtra("quiz_results");
            ListResultQuestionAdapter adapter = new ListResultQuestionAdapter(this, data);
            listView.setAdapter(adapter);
            int correctAns = 0;
            for (Question a : data) {
                if (a.getSelectedAnswer() == a.getCorrectAnswer())
                    correctAns++;
            }
            total.setText("Điểm: " + correctAns + "/" + data.size());
        }
    }

}