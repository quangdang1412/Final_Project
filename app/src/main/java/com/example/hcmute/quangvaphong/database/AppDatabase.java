package com.example.hcmute.quangvaphong.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hcmute.quangvaphong.dao.IrregularVerbDao;
import com.example.hcmute.quangvaphong.dao.QuestionDao;
import com.example.hcmute.quangvaphong.dao.QuizDao;
import com.example.hcmute.quangvaphong.dao.StudiedVocabularyDao;
import com.example.hcmute.quangvaphong.dao.VocabularyDao;
import com.example.hcmute.quangvaphong.dao.VocabularyIeltsDao;
import com.example.hcmute.quangvaphong.dao.VocabularyOxfordDao;
import com.example.hcmute.quangvaphong.dao.VocabularyToeflDao;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.Question;
import com.example.hcmute.quangvaphong.models.Quiz;
import com.example.hcmute.quangvaphong.models.StudiedVocabulary;
import com.example.hcmute.quangvaphong.models.VocabularyIelts;
import com.example.hcmute.quangvaphong.models.VocabularyOxford;
import com.example.hcmute.quangvaphong.models.VocabularyToefl;
import com.example.hcmute.quangvaphong.models.VocabularyToeic;

@Database(entities = {VocabularyToeic.class, VocabularyToefl.class, VocabularyIelts.class, IrregularVerb.class, VocabularyOxford.class, StudiedVocabulary.class, Quiz.class, Question.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "vocabulary_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract VocabularyDao vocabularyDao();

    public abstract VocabularyToeflDao vocabularyToeflDao();

    public abstract VocabularyIeltsDao vocabularyIeltsDao();

    public abstract IrregularVerbDao irregularVerbDao();

    public abstract VocabularyOxfordDao vocabularyOxfordDao();

    public abstract StudiedVocabularyDao studiedVocabularyDao();

    public abstract QuizDao quizDao();

    public abstract QuestionDao questionDao();

}
