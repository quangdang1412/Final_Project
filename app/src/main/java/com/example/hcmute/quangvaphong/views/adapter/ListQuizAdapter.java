package com.example.hcmute.quangvaphong.views.adapter;

import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.Quiz;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListQuizAdapter extends BaseAdapter {
    final List<Quiz> listQuiz;

    public ListQuizAdapter(List<Quiz> listProduct) {
        this.listQuiz = listProduct;
    }

    @Override
    public int getCount() {
        return listQuiz != null ? listQuiz.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return listQuiz.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listQuiz.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewQuiz;
        if (convertView == null) {
            viewQuiz = View.inflate(parent.getContext(), R.layout.quiz_item, null);
        } else
            viewQuiz = convertView;

        Quiz quiz = (Quiz) getItem(position);

        ((TextView) viewQuiz.findViewById(R.id.score)).setText("Total: " + quiz.getCorrectAnswer() + "/" + quiz.getTotalAnswer());
        ((TextView) viewQuiz.findViewById(R.id.date)).setText(getDate(quiz.getDateTime()));

        return viewQuiz;
    }

    public String getDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(date));
    }
}
