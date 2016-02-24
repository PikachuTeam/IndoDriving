package com.essential.indodriving.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import tatteam.com.app_common.sqlite.BaseDataSource;
import tatteam.com.app_common.sqlite.DatabaseLoader;

/**
 * Created by dongc_000 on 2/19/2016.
 */
public class DataSource extends BaseDataSource {

    public final static int TYPE_SIM_A = 1, TYPE_SIM_A_UMUM = 2, TYPE_SIM_B1 = 3, TYPE_SIM_B1_UMUM = 4, TYPE_SIM_B2 = 5, TYPE_SIM_B2_UMUM = 6, TYPE_SIM_C = 7, TYPE_SIM_D = 8;

    public static ArrayList<Question> getAllQuestionByType(int type) {
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        String query = "SELECT Questions. *, Images.ImageData FROM Questions LEFT JOIN  Images ON Questions.ImageId = Images.Id WHERE Questions.Type = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"" + type});
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
        cursor.close();
        DatabaseLoader.getInstance().closeConnection();
        return questions;
    }

    private static String byteArrayToString(byte[] data) {
        return new String(data);
    }
}
