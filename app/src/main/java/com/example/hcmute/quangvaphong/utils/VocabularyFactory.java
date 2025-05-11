package com.example.hcmute.quangvaphong.utils;

import com.example.hcmute.quangvaphong.models.Vocabulary;

public interface VocabularyFactory<T extends Vocabulary> {
    T create(String word, String pronunciation, String meaning);
}
