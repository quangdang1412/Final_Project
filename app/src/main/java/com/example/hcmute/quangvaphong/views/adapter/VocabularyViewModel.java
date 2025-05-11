package com.example.hcmute.quangvaphong.views.adapter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hcmute.quangvaphong.models.IrregularVerb;
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

    public VocabularyViewModel(@NonNull Application application, String type) {
        super(application);
        repository = new VocabularyRepository(application.getApplicationContext());
        if (type.equals("toeic"))
            toeicList = repository.getAllVocabularyToeic();
        else if (type.equals("toefl"))
            toeflList = repository.getAllVocabularyToefl();
        else if (type.equals("ielts"))
            ieltsList = repository.getAllVocabularyIelts();
        else if (type.equals("oxford"))
            oxfordList = repository.getAllVocabularyOxford();
        else
            irregularVerbList = repository.getAllIrregularVerb();


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
}
