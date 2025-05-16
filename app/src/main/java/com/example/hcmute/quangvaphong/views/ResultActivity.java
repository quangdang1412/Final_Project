package com.example.hcmute.quangvaphong.views;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.Question;
import com.example.hcmute.quangvaphong.views.adapter.ListResultQuestionAdapter;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private List<Question> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        TextView title = findViewById(R.id.title);
        ListView listView = findViewById(R.id.listview);
        TextView total = findViewById(R.id.total);
        data = (List<Question>) getIntent().getSerializableExtra("quiz_results");
        ListResultQuestionAdapter adapter = new ListResultQuestionAdapter(this, data);
        listView.setAdapter(adapter);
        int correctAns = 0;
        for (Question a : data) {
            if (a.getSelectedAnswer() == a.getCorrectAnswer())
                correctAns++;
        }
        total.setText("Total " + correctAns + "/" + data.size());
    }

}