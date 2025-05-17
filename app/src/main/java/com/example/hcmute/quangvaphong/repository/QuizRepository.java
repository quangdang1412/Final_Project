package com.example.hcmute.quangvaphong.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.hcmute.quangvaphong.dao.QuestionDao;
import com.example.hcmute.quangvaphong.dao.QuizDao;
import com.example.hcmute.quangvaphong.database.AppDatabase;
import com.example.hcmute.quangvaphong.models.Question;
import com.example.hcmute.quangvaphong.models.Quiz;

import java.util.List;

public class QuizRepository {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;

    public QuizRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        quizDao = db.quizDao();
        questionDao = db.questionDao();
    }

    public long insertQuiz(Quiz quiz) {
        return quizDao.insertQuiz(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizDao.getAllQuizzes();
    }

    public Quiz getQuizById(int id) {
        return quizDao.getQuizById(id);
    }

    public void deleteQuiz(Quiz quiz) {
        quizDao.deleteQuiz(quiz);
    }

    public void insertQuestions(List<Question> questions) {
        questionDao.insertQuestions(questions);
    }

    public List<Question> getQuestionsByQuizId(long quizId) {
        return questionDao.getQuestionsByQuizId(quizId);
    }

    public void deleteQuestions(List<Question> questions) {
        questionDao.deleteQuestions(questions);
    }

    public LiveData<List<Quiz>> getQuizzesByMonth(long startTimestamp, long endTimestamp) {
        return quizDao.getQuizzesByMonth(startTimestamp, endTimestamp);
    }
}
