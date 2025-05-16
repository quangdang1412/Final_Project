package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.StudiedVocabulary;

import java.util.List;

@Dao
public interface StudiedVocabularyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudiedVocabulary(StudiedVocabulary vocabulary);

    @Delete
    void deleteStudiedVocabulary(StudiedVocabulary vocabulary);

    @Query("SELECT * FROM studied_vocabulary")
    LiveData<List<StudiedVocabulary>> getAllStudiedVocabulary();

    @Query("SELECT * FROM studied_vocabulary WHERE word = :word LIMIT 1")
    LiveData<StudiedVocabulary> getByWord(String word);
}
