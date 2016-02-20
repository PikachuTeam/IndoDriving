package com.essential.indodriving.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;

import tatteam.com.app_common.sqlite.DatabaseLoader;

/**
 * Created by dongc_000 on 2/19/2016.
 */
public class DatabaseHelper {

    private static DatabaseHelper instance;

    private DatabaseHelper() {
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public ArrayList<Question> getAllQuestionByType(int type) {
        ArrayList<Question> questions = new ArrayList<>();
        String query = "SELECT Questions. *, Images.ImageData FROM Questions LEFT JOIN  Images ON Questions.ImageId = Images.Id WHERE Questions.Type = ?";
        Cursor cursor = DatabaseLoader.getInstance().openConnection().rawQuery(query, new String[]{"" + type});
        cursor.moveToFirst();
        int tmp = 1;
        while (!cursor.isAfterLast()) {
            Question question = new Question();
            question.index = tmp;
            question.question = byteArrayToString(cursor.getBlob(2));
            question.answer1 = cursor.getString(3);
            question.answer2 = cursor.getString(4);
            question.answer3 = cursor.getString(5);
            question.answer4 = cursor.getString(6);
            question.correctAnswer = cursor.getInt(7);
            byte[] imgData = cursor.getBlob(9);
            question.image = imgData != null ? BitmapFactory.decodeByteArray(imgData, 0, imgData.length) : null;
            question.type = cursor.getInt(1);
            questions.add(question);
            cursor.moveToNext();
            tmp++;
        }
        return questions;
    }

    public String byteArrayToString(byte[] data) {
        return new String(data);
    }
}
