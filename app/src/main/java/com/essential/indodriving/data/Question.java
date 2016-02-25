package com.essential.indodriving.data;

import android.graphics.Bitmap;

/**
 * Created by dongc_000 on 2/19/2016.
 */
public class Question {

    public int index;
    public String question;
    public String answer1, answer2, answer3, answer4;
    public int correctAnswer;
    public Bitmap image;
    public int type;
    public int answer;

    public Question() {
        answer = -1;
    }
}
