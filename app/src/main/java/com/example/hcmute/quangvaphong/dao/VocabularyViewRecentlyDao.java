package com.example.hcmute.quangvaphong.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.VocabularyViewRecently;

import java.util.List;

@Dao
public interface VocabularyViewRecentlyDao {
    @Insert
    void insertVocabularyViewRecently(VocabularyViewRecently vocabulary);

    @Query("SELECT * FROM vocabulary_view_recently ORDER BY timestamp DESC LIMIT :limit")
    List<VocabularyViewRecently> getRecentViews(int limit);
}
