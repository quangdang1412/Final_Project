package com.example.hcmute.quangvaphong.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity(tableName = "quiz")
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    protected long id;
    private long dateTime;
    private int correctAnswer;
    private int totalAnswer;
}
