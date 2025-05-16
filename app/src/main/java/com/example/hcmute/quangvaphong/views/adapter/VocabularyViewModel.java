package com.example.hcmute.quangvaphong.views.adapter;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.Question;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;
import com.example.hcmute.quangvaphong.models.Vocabulary;
import com.example.hcmute.quangvaphong.models.VocabularyIelts;
import com.example.hcmute.quangvaphong.models.VocabularyOxford;
import com.example.hcmute.quangvaphong.models.VocabularyToefl;
import com.example.hcmute.quangvaphong.models.VocabularyToeic;
import com.example.hcmute.quangvaphong.repository.VocabularyRepository;

import java.util.List;

public class VocabularyViewModel extends AndroidViewModel {
    private VocabularyRepository repository;
    private LiveData<List<VocabularyToeic>> toeicList;
    private LiveData<List<VocabularyToefl>> toeflList;
    private LiveData<List<VocabularyIelts>> ieltsList;
    private LiveData<List<VocabularyOxford>> oxfordList;
    private LiveData<List<IrregularVerb>> irregularVerbList;
    private LiveData<List<StudiedVocabulary>> studiedVocabList;
    private List<Question> quizResults;

    public VocabularyViewModel(@NonNull Application application) {
        super(application);
        repository = new VocabularyRepository(application.getApplicationContext());
        studiedVocabList = repository.getAllStudiedVocabulary();
        toeicList = repository.getAllVocabularyToeic();
        toeflList = repository.getAllVocabularyToefl();
        ieltsList = repository.getAllVocabularyIelts();
        oxfordList = repository.getAllVocabularyOxford();
        irregularVerbList = repository.getAllIrregularVerb();


    }

    public void addStudiedVocabulary(StudiedVocabulary vocabulary) {
        repository.insertStudiedVocabulary(vocabulary);
    }

    public void deleteStudiedVocabulary(StudiedVocabulary vocabulary) {
        repository.deleteStudiedVocabulary(vocabulary);
    }

    public void updateVocabulary(Vocabulary vocabulary, String value) {
        Log.d("hehehehe", value);
        if (value.equals("toeic")) {
            VocabularyToeic a = toeicList.getValue().stream().filter(vocabularyToeic -> vocabularyToeic.getWord().equals(vocabulary.getWord())).findFirst().orElse(null);
            a.setIsSave(vocabulary.getIsSave());
            repository.updateVocabularyToeic(a);
        } else if (value.equals("toefl")) {
            VocabularyToefl a = toeflList.getValue().stream().filter(vocabularyToeic -> vocabularyToeic.getWord().equals(vocabulary.getWord())).findFirst().orElse(null);
            a.setIsSave(vocabulary.getIsSave());
            repository.updateVocabularyToefl(a);
        } else if (value.equals("ielts")) {
            VocabularyIelts a = ieltsList.getValue().stream().filter(vocabularyToeic -> vocabularyToeic.getWord().equals(vocabulary.getWord())).findFirst().orElse(null);
            a.setIsSave(vocabulary.getIsSave());
            repository.updateVocabularyIelts(a);
        } else if (value.equals("oxford")) {
            VocabularyOxford a = oxfordList.getValue().stream().filter(vocabularyToeic -> vocabularyToeic.getWord().equals(vocabulary.getWord())).findFirst().orElse(null);
            a.setIsSave(vocabulary.getIsSave());
            repository.updateVocabularyOxford(a);
        } else {
            IrregularVerb a = irregularVerbList.getValue().stream().filter(vocabularyToeic -> vocabularyToeic.getWord().equals(vocabulary.getWord())).findFirst().orElse(null);
            a.setIsSave(vocabulary.getIsSave());
            repository.updateIrregularVerb(a);
        }

    }

    public LiveData<StudiedVocabulary> getStudiedVocabularyByWord(String word) {
        return repository.getStudiedVocabularyByWord(word);
    }

    public LiveData<List<VocabularyToeic>> getAllVocabularyToeic() {
        return toeicList;
    }

    public LiveData<List<VocabularyToefl>> getAllVocabularyToefl() {
        return toeflList;
    }

    public LiveData<List<VocabularyIelts>> getAllVocabularyIelts() {
        return ieltsList;
    }

    public LiveData<List<VocabularyOxford>> getAllVocabularyOxford() {
        return oxfordList;
    }

    public LiveData<List<IrregularVerb>> getAllIrregularVerb() {
        return irregularVerbList;
    }

    public LiveData<List<StudiedVocabulary>> getAllStudiedVocabulary() {
        return studiedVocabList;
    }

    public void setQuizResults(List<Question> results) {
        this.quizResults = results;
    }

    public List<Question> getQuizResults() {
        return quizResults;
    }
}
