package com.example.hcmute.quangvaphong.views;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.Quiz;
import com.example.hcmute.quangvaphong.receivers.NotificationReceiver;
import com.example.hcmute.quangvaphong.receivers.QuizAlarmNotificationReceiver;
import com.example.hcmute.quangvaphong.utils.ReceiverCalls;
import com.example.hcmute.quangvaphong.views.adapter.ListQuizAdapter;
import com.example.hcmute.quangvaphong.views.viewModel.QuizViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListQuizActivity extends AppCompatActivity {

    private List<Quiz> listQuiz = new ArrayList<>();
    private ListQuizAdapter listQuizAdapter;
    private ListView listViewProcess;
    private LocalDate selectedDate;

    private AutoCompleteTextView setDateCalendar;
    private QuizViewModel quizViewModel;

    private FloatingActionButton newQuizBtn;
    private ImageButton alarmQuizBtn;
    private View notificationDot;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_quiz);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        listViewProcess = findViewById(R.id.listProcessView);
        setDateCalendar = findViewById(R.id.setDateCalendar);
        newQuizBtn = findViewById(R.id.fabAddQuiz);
        alarmQuizBtn = findViewById(R.id.alarmQuizBtn);
        notificationDot = findViewById(R.id.fab_dot);

        selectedDate = LocalDate.now();
        setDateCalendar.setText(monthYearFromDate(selectedDate));

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        listQuizAdapter = new ListQuizAdapter(listQuiz);
        listViewProcess.setAdapter(listQuizAdapter);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        boolean isSetQuizAlarm = sharedPreferences.getBoolean("isSetQuizAlarm", false);
        if (isSetQuizAlarm) {
            notificationDot.setVisibility(View.VISIBLE);
        } else {
            notificationDot.setVisibility(View.GONE);
        }

        long[] range1 = getMonthRangeTimestamps(selectedDate.getYear(), selectedDate.getMonthValue());
        quizViewModel.getListQuizByMonth(range1[0], range1[1]).observe(this, quizzes -> {
            listQuiz.clear();
            listQuiz.addAll(quizzes);
            listQuizAdapter.notifyDataSetChanged();
        });
        setDateCalendar.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(setDateCalendar.getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, day);
                        setDateCalendar.setText(monthYearFromDate(selectedDate));
                        listQuiz.clear();
                        long[] range = getMonthRangeTimestamps(selectedDate.getYear(), selectedDate.getMonthValue());
                        quizViewModel.getListQuizByMonth(range[0], range[1]).observe(this, quizzes -> {
                            listQuiz.addAll(quizzes);
                            listQuizAdapter.notifyDataSetChanged();
                        });
                    }, year, month, day);
            ;
            datePickerDialog.show();
        });

        newQuizBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ListQuizActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        alarmQuizBtn.setOnClickListener(v -> {

            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (TimePicker view, int hourOfDay, int minute1) -> {
                        String time = String.format("%02d:%02d", hourOfDay, minute1);
                        Toast.makeText(this, "Đã cài giờ thực hiện quiz hàng ngày: " + time, Toast.LENGTH_SHORT).show();

                        ReceiverCalls.scheduleNotification(this, hourOfDay, minute1);

                        sharedPreferences.edit()
                                .putBoolean("isSetQuizAlarm", true)
                                .apply();

                        notificationDot.setVisibility(View.VISIBLE);

                    },
                    hour,
                    minute,
                    true
            );

            timePickerDialog.show();
        });

        listViewProcess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quiz selectedQuiz = listQuiz.get(i);
                Intent intent = new Intent(ListQuizActivity.this, ResultActivity.class);
                intent.putExtra("quizId", selectedQuiz.getId());
                startActivity(intent);
            }
        });
    }



    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private long[] getMonthRangeTimestamps(int year, int month) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month - 1, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long end = calendar.getTimeInMillis();

        return new long[]{start, end};
    }
}