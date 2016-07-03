package com.essential.indodriving.data.sign;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.essential.indodriving.data.PoolDatabaseLoader;

import java.util.ArrayList;

/**
 * Created by ThanhNH-Mac on 7/3/16.
 */
public class SignDataSource {

    public static final String GROUP_All = null;
    public static final String GROUP_Nomor_Rute = "Nomor Rute";
    public static final String GROUP_Rambu_Larangan = "Rambu Larangan";
    public static final String GROUP_Rambu_Peringatan = "Rambu Peringatan";
    public static final String GROUP_Rambu_Perintah = "Rambu Perintah";
    public static final String GROUP_Rambu_Petunjuk = "Rambu Petunjuk";
    public static final String GROUP_Rambu_Tambahan = "Rambu Tambahan";

    private static SQLiteDatabase openConnection() {
        return PoolDatabaseLoader.getInstance().getIndoSignDatabaseLoader().openConnection();
    }

    private static void closeConnection() {
        PoolDatabaseLoader.getInstance().getIndoSignDatabaseLoader().closeConnection();
    }

    public static ArrayList<Sign> getSigns(String groupName) {
        ArrayList<Sign> signs = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        String query = "SELECT * from sign";
        if (groupName != null) {
            query = "SELECT * from sign where sign.sign_group = '" + groupName + "'";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sign sign = new Sign();
            sign.sign_group = cursor.getString(1);
            sign.number = cursor.getString(2);
            sign.image = cursor.getBlob(3);
            sign.definition = cursor.getString(4);
            signs.add(sign);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return signs;
    }

}
