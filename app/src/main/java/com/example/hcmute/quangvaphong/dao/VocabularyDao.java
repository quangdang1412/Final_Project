package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hcmute.quangvaphong.models.VocabularyToeic;

import java.util.List;

@Dao
public interface VocabularyDao {

    @Update
    void insertVocabularyToeic(VocabularyToeic vocabularyToeic);

    @Insert
    void insertListVocabularyToeic(List<VocabularyToeic> vocabularyToeic);

    @Query("SELECT * FROM vocabulary_toeic")
    LiveData<List<VocabularyToeic>> getAllVocabularyToeic();

    @Query("SELECT count(*) FROM vocabulary_toeic WHERE IsSave==1")
    int getNumberSavedVocabularyToeic();

    @Query("SELECT count(*) FROM vocabulary_toeic")
    int getNumberVocabularyToeic();

    @Query("SELECT * FROM vocabulary_toeic WHERE word = :word LIMIT 1")
    LiveData<VocabularyToeic> getByWord(String word);
}
