package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hcmute.quangvaphong.models.IrregularVerb;

import java.util.List;

@Dao
public interface IrregularVerbDao {
    @Insert
    void insertIrregularVerb(IrregularVerb vocabularyToeic);

    @Insert
    void insertListIrregularVerb(List<IrregularVerb> vocabularyToeic);

    @Query("SELECT * FROM irregular_verb")
    LiveData<List<IrregularVerb>> getAllIrregularVerb();
}
