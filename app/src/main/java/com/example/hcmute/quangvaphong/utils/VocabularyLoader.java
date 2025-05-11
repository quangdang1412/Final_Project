package com.example.hcmute.quangvaphong.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.Vocabulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VocabularyLoader {

    public static <T extends Vocabulary> List<T> loadVocabularyFromAssets(Context context, String fileName, VocabularyFactory<T> factory) {
        List<T> vocabularyList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts.length == 3) {
                    String word = parts[0];
                    String pronunciation = parts[1];
                    String meaning = parts[2];

                    T vocab = factory.create(word, pronunciation, meaning);
                    vocabularyList.add(vocab);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vocabularyList;
    }

    public static List<IrregularVerb> loadIrregularVerbFromAssets(Context context, String fileName) {
        List<IrregularVerb> vocabularyList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("#");
                IrregularVerb vocab = parseLine(line);
                if (vocab != null) {
                    vocabularyList.add(vocab);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vocabularyList;
    }

    private static IrregularVerb parseLine(String line) {
        String[] parts = line.split("#");
        if (parts.length != 3) return null;

        String formsPart = parts[0].trim();
        String pronunciation = parts[1].trim();
        String meaning = parts[2].trim();

        String[] verbForms = formsPart.split("\t");
        if (verbForms.length != 3) return null;

        String baseForm = verbForms[0].trim();
        String pastSimple = verbForms[1].trim();
        String pastParticiple = verbForms[2].trim();

        return IrregularVerb.builder()
                .word(baseForm)
                .v2(pastSimple)
                .v3(pastParticiple)
                .meaning(meaning)
                .pronunciation(pronunciation)
                .isSave(false).build();
    }
}

