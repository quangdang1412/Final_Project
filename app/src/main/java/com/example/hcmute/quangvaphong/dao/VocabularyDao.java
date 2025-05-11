package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.VocabularyToeic;

import java.util.List;

@Dao
public interface VocabularyDao {

    @Insert
    void insertVocabulary(VocabularyToeic vocabularyToeic);

    @Insert
    void insertListVocabularyToeic(List<VocabularyToeic> vocabularyToeic);

    @Query("SELECT * FROM vocabulary_toeic")
    LiveData<List<VocabularyToeic>> getAllVocabularyToeic();

    @Query("SELECT * FROM vocabulary_toeic WHERE IsSave==1")
    List<VocabularyToeic> getSavedVocabularyToeic();
}
