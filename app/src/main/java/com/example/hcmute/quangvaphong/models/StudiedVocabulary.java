package com.example.hcmute.quangvaphong.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(tableName = "studied_vocabulary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudiedVocabulary extends Vocabulary {
    @PrimaryKey(autoGenerate = true)
    protected int id;
    private String v2;
    private String v3;
    private String type;
}
