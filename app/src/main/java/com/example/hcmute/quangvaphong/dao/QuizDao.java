package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.Quiz;

import java.util.List;

@Dao
public interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertQuiz(Quiz quiz);

    @Query("SELECT * FROM quiz")
    List<Quiz> getAllQuizzes();

    @Query("SELECT * FROM quiz WHERE id = :quizId")
    Quiz getQuizById(int quizId);

    @Delete
    void deleteQuiz(Quiz quiz);

    @Query("SELECT * FROM quiz WHERE dateTime BETWEEN :startTimestamp AND :endTimestamp ORDER BY dateTime DESC")
    LiveData<List<Quiz>> getQuizzesByMonth(long startTimestamp, long endTimestamp);
}
