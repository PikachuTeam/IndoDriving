package com.essential.indodriving.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/19/2016.
 */
public class DataSource {

    public final static int TYPE_SIM_A = 1, TYPE_SIM_A_UMUM = 2, TYPE_SIM_B1 = 3,
            TYPE_SIM_B1_UMUM = 4, TYPE_SIM_B2 = 5, TYPE_SIM_B2_UMUM = 6, TYPE_SIM_C = 7, TYPE_SIM_D = 8;
    public final static int ANSWER_A = 0, ANSWER_B = 1, ANSWER_C = 2, ANSWER_D = 3, ANSWER_NOT_CHOSEN = -1;
    public final static String TABLE_EXAMS = "Exams", KEY_SCORE = "LastScore";

    private static SQLiteDatabase openConnection() {
        return PoolDatabaseLoader.getInstance().getIndoDatabaseLoader().openConnection();
    }

    private static void closeConnection() {
        PoolDatabaseLoader.getInstance().getIndoDatabaseLoader().closeConnection();
    }


    public static ArrayList<Question> getAllQuestionByType(int type) {
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        String query = "SELECT Questions. *, Images.ImageData FROM Questions " +
                "LEFT JOIN  Images ON Questions.ImageId = Images.Id WHERE Questions.Type = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"" + type});
        cursor.moveToFirst();
        int tmp = 1;
        while (!cursor.isAfterLast()) {
            Question question = new Question();
            question.index = tmp;
            question.question = fixQuestion(byteArrayToString(cursor.getBlob(2)));
            question.answer1 = fixAnswer(cursor.getString(3));
            question.answer2 = fixAnswer(cursor.getString(4));
            question.answer3 = fixAnswer(cursor.getString(5));
            question.answer4 = fixAnswer(cursor.getString(6));
            question.correctAnswer = cursor.getInt(7);
            question.fixedAnswer = cursor.getInt(9);
//            question.isSentFixedAnswer = cursor.getInt(10);
            question.imageData = cursor.getBlob(11);
            question.type = cursor.getInt(1);
            questions.add(question);
            cursor.moveToNext();
            tmp++;
        }
        cursor.close();
        closeConnection();
        return questions;
    }

    public static ArrayList<QuestionPackage> getQuestionPackagesByType(int type) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        ArrayList<QuestionPackage> questionPackages = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select distinct ExamId, LastScore from Exams where Type=? " +
                "order by ExamId asc", new String[]{"" + type});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QuestionPackage questionPackage = new QuestionPackage();
            questionPackage.index = cursor.getInt(0);
            questionPackage.lastScore = cursor.getInt(1);
            questionPackages.add(questionPackage);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return questionPackages;
    }

    public static ArrayList<Question> getQuestionsByTypeAndExamId(int type, int examId,
                                                                  boolean isRandom, int numberOfQuestions) {
        ArrayList<Question> questions = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        String query;
        Cursor cursor;
        if (!isRandom) {
            query = "select tmp.* from Exams, (SELECT Questions. *, Images.ImageData FROM Questions LEFT JOIN " +
                    "Images ON Questions.ImageId = Images.Id) as tmp where " +
                    "Exams.ExamId=? and Exams.Type=? and Exams.QuestionId=tmp.Id";
            cursor = sqLiteDatabase.rawQuery(query, new String[]{"" + examId, "" + type});
        } else {
            query = "SELECT Questions. *, Images.ImageData FROM Questions LEFT JOIN " +
                    "Images ON Questions.ImageId = Images.Id WHERE Questions.Type = ? " +
                    "order by Random() limit " + numberOfQuestions;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{"" + type});
        }
        cursor.moveToFirst();
        int tmp = 1;
        while (!cursor.isAfterLast()) {
            Question question = new Question();
            question.index = tmp;
            question.question = fixQuestion(byteArrayToString(cursor.getBlob(2)));
            question.answer1 = fixAnswer(cursor.getString(3));
            question.answer2 = fixAnswer(cursor.getString(4));
            question.answer3 = fixAnswer(cursor.getString(5));
            question.answer4 = fixAnswer(cursor.getString(6));
            question.correctAnswer = cursor.getInt(7);
            question.fixedAnswer = cursor.getInt(9);
//            question.isSentFixedAnswer = cursor.getInt(10);
            question.imageData = cursor.getBlob(11);
            question.type = cursor.getInt(1);
            questions.add(question);
            cursor.moveToNext();
            tmp++;
        }
        cursor.close();
        closeConnection();
        return questions;
    }

    public static boolean saveScore(int examId, String type, int score) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score);
        sqLiteDatabase.update(TABLE_EXAMS, values, "ExamId = ? and Type = ?", new String[]{"" + examId, type});
        closeConnection();
        return true;
    }

    private static String byteArrayToString(byte[] data) {
        return new String(data);
    }

    private static String fixQuestion(String question) {
        String[] tmp = question.split(" ");
        question = "";
        for (int i = 0; i < tmp.length; i++) {
            if (!tmp[i].contains("#")) {
                if (tmp[i].contains("?")) {
                    tmp[i] = ":";
                }
                if (tmp[i].contains("&")) {
                    tmp[i] = tmp[i].replace('&', ':');
                }
                question += tmp[i] + " ";
            } else {
                if (tmp[i].contains("&")) {
                    tmp[i] = ":";
                    question += tmp[i] + " ";
                }
            }
        }
        question = question.replace(';', ' ');
        return question.replace(" :", ":").trim();
    }

    private static String fixAnswer(String answer) {
        if (answer != null) {
            String[] tmp = answer.split(" ");
            answer = "";
            for (int i = 0; i < tmp.length; i++) {
                if (!tmp[i].contains("#")) {
                    answer += tmp[i] + " ";
                }
            }
            answer = answer.replace(';', ' ');
            return answer.trim();
        } else {
            return null;
        }
    }
}
