package com.example.hcmute.quangvaphong.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

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
@Entity(tableName = "question")
public class Question implements Serializable {
    @PrimaryKey(autoGenerate = true)
    protected int id;
    private long quizId;
    private String question;
    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private int correctAnswer;
    private int selectedAnswer;
}
