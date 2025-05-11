package com.example.hcmute.quangvaphong.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.hcmute.quangvaphong.dao.IrregularVerbDao;
import com.example.hcmute.quangvaphong.dao.VocabularyDao;
import com.example.hcmute.quangvaphong.dao.VocabularyIeltsDao;
import com.example.hcmute.quangvaphong.dao.VocabularyOxfordDao;
import com.example.hcmute.quangvaphong.dao.VocabularyToeflDao;
import com.example.hcmute.quangvaphong.database.AppDatabase;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.VocabularyIelts;
import com.example.hcmute.quangvaphong.models.VocabularyOxford;
import com.example.hcmute.quangvaphong.models.VocabularyToefl;
import com.example.hcmute.quangvaphong.models.VocabularyToeic;

import java.util.List;

public class VocabularyRepository {

    private VocabularyDao vocabularyDao;
    private VocabularyToeflDao vocabularyToeflDao;
    private VocabularyIeltsDao vocabularyIeltsDao;
    private VocabularyOxfordDao vocabularyOxfordDao;
    private IrregularVerbDao irregularVerbDao;

    public VocabularyRepository(Context context) {
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "vocabulary_database").fallbackToDestructiveMigration().build();
        vocabularyDao = database.vocabularyDao();
        vocabularyToeflDao = database.vocabularyToeflDao();
        vocabularyIeltsDao = database.vocabularyIeltsDao();
        vocabularyOxfordDao = database.vocabularyOxfordDao();
        irregularVerbDao = database.irregularVerbDao();
    }

    public void insertListVocabularyToeic(List<VocabularyToeic> vocabularyToeicList) {
        new Thread(() -> {
            vocabularyDao.insertListVocabularyToeic(vocabularyToeicList);
        }).start();
    }

    public LiveData<List<VocabularyToeic>> getAllVocabularyToeic() {
        return vocabularyDao.getAllVocabularyToeic();
    }

    public void insertListVocabularyToefl(List<VocabularyToefl> vocabularyList) {
        new Thread(() -> {
            vocabularyToeflDao.insertListVocabularyToefl(vocabularyList);
        }).start();
    }

    public LiveData<List<VocabularyToefl>> getAllVocabularyToefl() {
        return vocabularyToeflDao.getAllVocabularyToefl();
    }

    public void insertListVocabularyIelts(List<VocabularyIelts> vocabularyToeicList) {
        new Thread(() -> {
            vocabularyIeltsDao.insertListVocabularyIelts(vocabularyToeicList);
        }).start();
    }

    public LiveData<List<VocabularyIelts>> getAllVocabularyIelts() {
        return vocabularyIeltsDao.getAllVocabularyIelts();
    }

    public void insertListVocabularyOxford(List<VocabularyOxford> vocabularyToeicList) {
        new Thread(() -> {
            vocabularyOxfordDao.insertListVocabularyOxford(vocabularyToeicList);
        }).start();
    }

    public LiveData<List<VocabularyOxford>> getAllVocabularyOxford() {
        return vocabularyOxfordDao.getAllVocabularyOxford();
    }

    public void insertListIrregularVerb(List<IrregularVerb> vocabularyToeicList) {
        new Thread(() -> {
            irregularVerbDao.insertListIrregularVerb(vocabularyToeicList);
        }).start();
    }

    public LiveData<List<IrregularVerb>> getAllIrregularVerb() {
        return irregularVerbDao.getAllIrregularVerb();
    }
}
