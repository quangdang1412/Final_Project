package com.example.hcmute.quangvaphong.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.Question;

import java.util.List;

public class ListResultQuestionAdapter extends BaseAdapter {
    private List<Question> data;
    private LayoutInflater inflater;

    public ListResultQuestionAdapter(Context context, List<Question> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.question, null);
        TextView question = v.findViewById(R.id.question);
        RadioButton opt1 = v.findViewById(R.id.option1);
        RadioButton opt2 = v.findViewById(R.id.option2);
        RadioButton opt3 = v.findViewById(R.id.option3);
        RadioButton opt4 = v.findViewById(R.id.option4);
        TextView result = v.findViewById(R.id.result);

        question.setText(data.get(position).getQuestion());
        opt1.setText(data.get(position).getOpt1());
        opt2.setText(data.get(position).getOpt2());
        opt3.setText(data.get(position).getOpt3());
        opt4.setText(data.get(position).getOpt4());

        opt1.setOnCheckedChangeListener((compoundBtn, bl) -> {
            if (bl)
                data.get(position).setSelectedAnswer(1);
        });
        opt2.setOnCheckedChangeListener((compoundBtn, bl) -> {
            if (bl)
                data.get(position).setSelectedAnswer(2);
        });
        opt3.setOnCheckedChangeListener((compoundBtn, bl) -> {
            if (bl)
                data.get(position).setSelectedAnswer(3);
        });
        opt4.setOnCheckedChangeListener((compoundBtn, bl) -> {
            if (bl)
                data.get(position).setSelectedAnswer(4);
        });

        switch (data.get(position).getSelectedAnswer()) {
            case 1:
                opt1.setChecked(true);
                break;
            case 2:
                opt2.setChecked(true);
                break;
            case 3:
                opt3.setChecked(true);
                break;
            case 4:
                opt4.setChecked(true);
                break;
        }
        opt1.setEnabled(false);
        opt2.setEnabled(false);
        opt3.setEnabled(false);
        opt4.setEnabled(false);

        result.setVisibility(View.VISIBLE);

        if (data.get(position).getSelectedAnswer() == data.get(position).getCorrectAnswer()) {
            result.setBackgroundResource(R.drawable.green_background);
            result.setTextColor(ContextCompat.getColor(inflater.getContext(), R.color.green_dark));
            result.setText("Correct Answer");
        } else {
            result.setBackgroundResource(R.drawable.red_background);
            result.setTextColor(ContextCompat.getColor(inflater.getContext(), R.color.red_dark));
            result.setText("Wrong Answer");

            switch (data.get(position).getCorrectAnswer()) {
                case 1:
                    opt1.setBackgroundResource(R.drawable.green_background);
                    break;
                case 2:
                    opt2.setBackgroundResource(R.drawable.green_background);
                    break;
                case 3:
                    opt3.setBackgroundResource(R.drawable.green_background);
                    break;
                case 4:
                    opt4.setBackgroundResource(R.drawable.green_background);
                    break;
            }

        }
        return v;
    }
}
