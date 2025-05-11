package com.example.hcmute.quangvaphong.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(tableName = "vocabulary_toeic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VocabularyToeic extends Vocabulary {
    @PrimaryKey(autoGenerate = true)
    protected int id;
}
