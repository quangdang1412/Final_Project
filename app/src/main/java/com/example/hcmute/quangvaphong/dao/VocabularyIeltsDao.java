package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.VocabularyIelts;

import java.util.List;

@Dao
public interface VocabularyIeltsDao {
    @Insert
    void insertVocabularyIelts(VocabularyIelts vocabulary);

    @Insert
    void insertListVocabularyIelts(List<VocabularyIelts> vocabulary);

    @Query("SELECT * FROM vocabulary_ielts")
    LiveData<List<VocabularyIelts>> getAllVocabularyIelts();

    @Query("SELECT * FROM vocabulary_ielts WHERE IsSave==1")
    List<VocabularyIelts> getSavedVocabularyIelts();
}
