package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hcmute.quangvaphong.models.VocabularyOxford;

import java.util.List;

@Dao
public interface VocabularyOxfordDao {
    @Update
    void insertVocabularyOxford(VocabularyOxford vocabulary);

    @Insert
    void insertListVocabularyOxford(List<VocabularyOxford> vocabulary);

    @Query("SELECT * FROM vocabulary_oxford")
    LiveData<List<VocabularyOxford>> getAllVocabularyOxford();

    @Query("SELECT count(*) FROM vocabulary_oxford WHERE IsSave==1")
    int getNumberSavedVocabularyOxford();

    @Query("SELECT count(*) FROM vocabulary_oxford")
    int getNumberVocabularyOxford();

    @Query("SELECT * FROM vocabulary_oxford WHERE word = :word LIMIT 1")
    LiveData<VocabularyOxford> getByWord(String word);
}
