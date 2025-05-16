package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hcmute.quangvaphong.models.IrregularVerb;

import java.util.List;

@Dao
public interface IrregularVerbDao {
    @Update
    void insertIrregularVerb(IrregularVerb vocabularyToeic);

    @Insert
    void insertListIrregularVerb(List<IrregularVerb> vocabularyToeic);

    @Query("SELECT * FROM irregular_verb")
    LiveData<List<IrregularVerb>> getAllIrregularVerb();

    @Query("SELECT * FROM irregular_verb WHERE word = :word LIMIT 1")
    LiveData<IrregularVerb> getByWord(String word);
}
