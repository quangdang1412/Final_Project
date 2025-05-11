package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.VocabularyOxford;

import java.util.List;

@Dao
public interface VocabularyOxfordDao {
    @Insert
    void insertVocabularyOxford(VocabularyOxford vocabulary);

    @Insert
    void insertListVocabularyOxford(List<VocabularyOxford> vocabulary);

    @Query("SELECT * FROM vocabulary_oxford")
    LiveData<List<VocabularyOxford>> getAllVocabularyOxford();

    @Query("SELECT * FROM vocabulary_oxford WHERE IsSave==1")
    List<VocabularyOxford> getSavedVocabularyOxford();
}
