package com.example.hcmute.quangvaphong.views.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hcmute.quangvaphong.models.Question;
import com.example.hcmute.quangvaphong.models.Quiz;
import com.example.hcmute.quangvaphong.repository.QuizRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizViewModel extends AndroidViewModel {

    private final QuizRepository repository;
    private final ExecutorService executorService;

    private final MutableLiveData<List<Question>> quizResults = new MutableLiveData<>();

    public QuizViewModel(@NonNull Application application) {
        super(application);
        repository = new QuizRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Question>> getQuizResults() {
        return quizResults;
    }

    public LiveData<List<Quiz>> getListQuizByMonth(long startTimestamp, long endTimestamp) {
        return repository.getQuizzesByMonth(startTimestamp, endTimestamp);
    }

    public void setQuizResults(List<Question> questions) {
        quizResults.setValue(questions);
    }

    public void saveQuizAndQuestions(Quiz quiz, List<Question> questions) {
        executorService.execute(() -> {
            long quizId = repository.insertQuiz(quiz);
            for (Question q : questions) {
                q.setQuizId(quizId);
            }

            repository.insertQuestions(questions);
        });
    }

    public void loadQuestionsByQuizId(long quizId) {
        executorService.execute(() -> {
            List<Question> questions = repository.getQuestionsByQuizId(quizId);
            quizResults.postValue(questions);
        });
    }

    public List<Quiz> getAllQuizzesSync() {
        return repository.getAllQuizzes();
    }

}
