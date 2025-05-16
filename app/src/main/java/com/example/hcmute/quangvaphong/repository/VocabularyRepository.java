package com.example.hcmute.quangvaphong.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.hcmute.quangvaphong.dao.IrregularVerbDao;
import com.example.hcmute.quangvaphong.dao.StudiedVocabularyDao;
import com.example.hcmute.quangvaphong.dao.VocabularyDao;
import com.example.hcmute.quangvaphong.dao.VocabularyIeltsDao;
import com.example.hcmute.quangvaphong.dao.VocabularyOxfordDao;
import com.example.hcmute.quangvaphong.dao.VocabularyToeflDao;
import com.example.hcmute.quangvaphong.database.AppDatabase;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;
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

    private StudiedVocabularyDao studiedVocabularyDao;


    public VocabularyRepository(Context context) {
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "vocabulary_database").fallbackToDestructiveMigration().build();
        vocabularyDao = database.vocabularyDao();
        vocabularyToeflDao = database.vocabularyToeflDao();
        vocabularyIeltsDao = database.vocabularyIeltsDao();
        vocabularyOxfordDao = database.vocabularyOxfordDao();
        irregularVerbDao = database.irregularVerbDao();
        studiedVocabularyDao = database.studiedVocabularyDao();
    }

    public void insertListVocabularyToeic(List<VocabularyToeic> vocabularyToeicList) {
        new Thread(() -> {
            vocabularyDao.insertListVocabularyToeic(vocabularyToeicList);
        }).start();
    }

    public void updateVocabularyToeic(VocabularyToeic vocabularyToeicList) {
        new Thread(() -> {
            Log.d("hehehehe", "truoc khi update");
            vocabularyDao.insertVocabularyToeic(vocabularyToeicList);
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

    public void updateVocabularyToefl(VocabularyToefl vocabularyList) {
        new Thread(() -> {
            vocabularyToeflDao.insertVocabularyToefl(vocabularyList);
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

    public void updateVocabularyIelts(VocabularyIelts vocabularyToeicList) {
        new Thread(() -> {
            vocabularyIeltsDao.insertVocabularyIelts(vocabularyToeicList);
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

    public void updateVocabularyOxford(VocabularyOxford vocabularyToeicList) {
        new Thread(() -> {
            vocabularyOxfordDao.insertVocabularyOxford(vocabularyToeicList);
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

    public void updateIrregularVerb(IrregularVerb vocabularyToeicList) {
        new Thread(() -> {
            irregularVerbDao.insertIrregularVerb(vocabularyToeicList);
        }).start();
    }

    public LiveData<List<IrregularVerb>> getAllIrregularVerb() {
        return irregularVerbDao.getAllIrregularVerb();
    }

    public void insertStudiedVocabulary(StudiedVocabulary vocabularyToeicList) {
        new Thread(() -> {
            studiedVocabularyDao.insertStudiedVocabulary(vocabularyToeicList);
        }).start();
    }

    public void deleteStudiedVocabulary(StudiedVocabulary vocabularyToeicList) {
        new Thread(() -> {
            studiedVocabularyDao.deleteStudiedVocabulary(vocabularyToeicList);
        }).start();
    }

    public LiveData<List<StudiedVocabulary>> getAllStudiedVocabulary() {
        return studiedVocabularyDao.getAllStudiedVocabulary();
    }

    public LiveData<StudiedVocabulary> getStudiedVocabularyByWord(String word) {
        return studiedVocabularyDao.getByWord(word);
    }

    public LiveData<IrregularVerb> getIrregularVerbByWord(String word) {
        return irregularVerbDao.getByWord(word);
    }

    public LiveData<VocabularyOxford> getVocabularyOxfordByWord(String word) {
        return vocabularyOxfordDao.getByWord(word);
    }

    public LiveData<VocabularyToeic> getVocabularyToeicByWord(String word) {
        return vocabularyDao.getByWord(word);
    }

    public LiveData<VocabularyToefl> getVocabularyToeflByWord(String word) {
        return vocabularyToeflDao.getByWord(word);
    }

    public LiveData<VocabularyIelts> getVocabularyIeltsByWord(String word) {
        return vocabularyIeltsDao.getByWord(word);
    }
}
