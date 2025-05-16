package com.example.hcmute.quangvaphong.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.Question;

import java.util.List;

public class ListQuestionAdapter extends BaseAdapter {
    private List<Question> data;
    private LayoutInflater inflater;

    public ListQuestionAdapter(Context context, List<Question> data) {
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
        return v;
    }
}
