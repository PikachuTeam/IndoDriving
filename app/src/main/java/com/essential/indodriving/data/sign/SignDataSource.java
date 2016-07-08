package com.essential.indodriving.data.sign;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.essential.indodriving.data.PoolDatabaseLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by ThanhNH-Mac on 7/3/16.
 */
public class SignDataSource {

    public static final String GROUP_ALL = null;
    public static final String GROUP_PROHIBITION_SIGN = "Rambu Larangan";
    public static final String GROUP_WARNING_SIGN = "Rambu Peringatan";
    public static final String GROUP_COMMAND_SIGN = "Rambu Perintah";
    public static final String GROUP_DIRECTION_SIGN = "Rambu Petunjuk";
    public static final String GROUP_ADDITIONAL_SIGN = "Rambu Tambahan";
    private final static String COLUMN_ID = "id";
    private final static String COLUMN_SIGN_GROUP = "sign_group";
    private final static String COLUMN_NUMBER = "number";
    private final static String COLUMN_IMAGE = "image";
    private final static String COLUMN_DEFINITION = "definition";

    public static final int TOTAL_QUESTION = 20;
    public static final int TOTAL_ANSWER = 4;
    public static final int ID_AROUND = TOTAL_ANSWER;

    private static SQLiteDatabase openConnection() {
        return PoolDatabaseLoader.getInstance().getIndoSignDatabaseLoader().openConnection();
    }

    private static void closeConnection() {
        PoolDatabaseLoader.getInstance().getIndoSignDatabaseLoader().closeConnection();
    }

    public static ArrayList<Sign> getSigns(String groupName) {
        return getSigns(groupName, false, -1);
    }

    public static ArrayList<Sign> getSigns(String groupName, boolean random, int limit) {
        ArrayList<Sign> signs = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        String query = "SELECT * from sign";
        if (groupName != null) {
            query = "SELECT * from sign where " + COLUMN_SIGN_GROUP + " = '" + groupName + "'";
        }
        if (random) {
            query = query.concat(" order by random()");
        }
        if (limit > 0) {
            query = query.concat(" LIMIT " + limit);
        }
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        HashMap<String, Integer> columnIndexes = new HashMap<>();
        columnIndexes.put(COLUMN_ID, cursor.getColumnIndex(COLUMN_ID));
        columnIndexes.put(COLUMN_SIGN_GROUP, cursor.getColumnIndex(COLUMN_SIGN_GROUP));
        columnIndexes.put(COLUMN_NUMBER, cursor.getColumnIndex(COLUMN_NUMBER));
        columnIndexes.put(COLUMN_IMAGE, cursor.getColumnIndex(COLUMN_IMAGE));
        columnIndexes.put(COLUMN_DEFINITION, cursor.getColumnIndex(COLUMN_DEFINITION));
        while (!cursor.isAfterLast()) {
            Sign sign = new Sign();
            sign.id = cursor.getInt(columnIndexes.get(COLUMN_ID));
            sign.sign_group = cursor.getString(columnIndexes.get(COLUMN_SIGN_GROUP));
            sign.number = cursor.getString(columnIndexes.get(COLUMN_NUMBER));
            sign.image = cursor.getBlob(columnIndexes.get(COLUMN_IMAGE));
            sign.definition = cursor.getString(columnIndexes.get(COLUMN_DEFINITION));
            signs.add(sign);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return signs;
    }

    public static ArrayList<SignQuestion> getQuestions(String groupName) {
        ArrayList<Sign> signs = getSigns(groupName, true, TOTAL_QUESTION);
        ArrayList<SignQuestion> questions = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        for (Sign sign : signs) {
            SignQuestion question = new SignQuestion();
            questions.add(question);
            question.signId = sign.id;
            question.image = sign.image;
            ArrayList<String> defArray = new ArrayList<>();
            defArray.add(sign.definition);
            int min = sign.id - ID_AROUND;
            int max = sign.id + ID_AROUND;
            int limit = TOTAL_ANSWER - 1;
            String query = "select tbl_around.definition from (select * from sign where sign.id >= " +
                    min + " and sign.id <= " + max + " and sign.id != " + sign.id + ") as tbl_around order by random() limit " + limit;
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String def = cursor.getString(0);
                defArray.add(def);
                cursor.moveToNext();
            }
            cursor.close();
            question.answerArray = new String[defArray.size()];
            for (int i = 0; i < question.answerArray.length; i++) {
                int defIndex = 0;
                if (defArray.size() > 1) {
                    defIndex = new Random().nextInt(defArray.size());
                }
                String def = defArray.get(defIndex);
                defArray.remove(defIndex);
                question.answerArray[i] = def;
                if (def == sign.definition) {
                    question.correctAnswer = i;
                }
            }
        }
        closeConnection();
        return questions;
    }

}
