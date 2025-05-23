package com.example.hcmute.quangvaphong.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.VocabularyIelts;

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

    @Query("SELECT count(*) FROM irregular_verb WHERE IsSave==1")
    int getNumberSavedIrregularVerb();

    @Query("SELECT count(*) FROM irregular_verb")
    int getNumberIrregularVerb();

    @Query("SELECT * FROM irregular_verb")
    List<IrregularVerb> getAllVocabularyIrregularSync();
}
