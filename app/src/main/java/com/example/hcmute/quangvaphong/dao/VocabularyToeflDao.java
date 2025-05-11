package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.VocabularyToefl;

import java.util.List;

@Dao
public interface VocabularyToeflDao {

    @Insert
    void insertVocabularyToefl(VocabularyToefl vocabulary);

    @Insert
    void insertListVocabularyToefl(List<VocabularyToefl> vocabulary);

    @Query("SELECT * FROM vocabulary_toefl")
    LiveData<List<VocabularyToefl>> getAllVocabularyToefl();

    @Query("SELECT * FROM vocabulary_toefl WHERE IsSave==1")
    List<VocabularyToefl> getSavedVocabularyToefl();
}