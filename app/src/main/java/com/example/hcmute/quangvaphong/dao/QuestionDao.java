package com.example.hcmute.quangvaphong.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<Question> questions);

    @Query("SELECT * FROM question WHERE quizId = :quizId")
    List<Question> getQuestionsByQuizId(long quizId);

    @Delete
    void deleteQuestions(List<Question> questions);
}
