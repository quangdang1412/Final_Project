package com.example.hcmute.quangvaphong.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.hcmute.quangvaphong.dao.IrregularVerbDao;
import com.example.hcmute.quangvaphong.dao.VocabularyDao;
import com.example.hcmute.quangvaphong.dao.VocabularyIeltsDao;
import com.example.hcmute.quangvaphong.dao.VocabularyOxfordDao;
import com.example.hcmute.quangvaphong.dao.VocabularyToeflDao;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.VocabularyIelts;
import com.example.hcmute.quangvaphong.models.VocabularyOxford;
import com.example.hcmute.quangvaphong.models.VocabularyToefl;
import com.example.hcmute.quangvaphong.models.VocabularyToeic;

@Database(entities = {VocabularyToeic.class, VocabularyToefl.class, VocabularyIelts.class, IrregularVerb.class, VocabularyOxford.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VocabularyDao vocabularyDao();

    public abstract VocabularyToeflDao vocabularyToeflDao();

    public abstract VocabularyIeltsDao vocabularyIeltsDao();

    public abstract IrregularVerbDao irregularVerbDao();

    public abstract VocabularyOxfordDao vocabularyOxfordDao();

}
