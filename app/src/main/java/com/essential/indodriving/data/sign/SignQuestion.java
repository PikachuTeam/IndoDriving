package com.essential.indodriving.data.sign;

import com.essential.indodriving.data.driving.DrivingDataSource;

/**
 * Created by ThanhNH-Mac on 7/7/16.
 */
public class SignQuestion {
    public int signId;
    public byte[] image;
    public String[] answerArray;
    public boolean isAds;
    public int answer;
    public int correctAnswer;
    public int index;

    public SignQuestion() {
        answer = DrivingDataSource.ANSWER_NOT_CHOSEN;
    }
}
