package com.essential.indodriving.data;

import android.content.Context;

import tatteam.com.app_common.sqlite.v2.DatabaseLoaderV2;

/**
 * Created by ThanhNH-Mac on 6/24/16.
 */
public class PoolDatabaseLoader {

    private static final String DATABASE_INDO_DRIVING = "indo_driving_2.db";

    private static PoolDatabaseLoader instance;

    private DatabaseLoaderV2 indoDatabaseLoader;

    private PoolDatabaseLoader() {
    }

    public static PoolDatabaseLoader getInstance() {
        if (instance == null) {
            instance = new PoolDatabaseLoader();
        }
        return instance;
    }

    public void initIfNeeded(Context context) {
        indoDatabaseLoader = new DatabaseLoaderV2(context, DATABASE_INDO_DRIVING);
        indoDatabaseLoader.initIfNeeded();

    }

    public DatabaseLoaderV2 getIndoDatabaseLoader() {
        return indoDatabaseLoader;
    }

}
