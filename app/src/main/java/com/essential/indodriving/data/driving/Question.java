package com.essential.indodriving.data.driving;

/**
 * Created by dongc_000 on 2/19/2016.
 */
public class Question {

    public int index;
    public int id;
    public String question;
    public String answer1, answer2, answer3, answer4;
    public int correctAnswer;
    public int type;
    public int answer;
    public byte[] imageData;
    public int fixedAnswer;
    public int isSentFixedAnswer;
    public boolean isAds;

    public Question() {
        answer = -1;
    }
}
