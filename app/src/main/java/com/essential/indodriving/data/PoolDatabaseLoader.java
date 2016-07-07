package com.essential.indodriving.data;

import android.content.Context;

import tatteam.com.app_common.sqlite.v2.DatabaseLoaderV2;

/**
 * Created by ThanhNH-Mac on 6/24/16.
 */
public class PoolDatabaseLoader {

    private static final String DATABASE_INDO_DRIVING = "indo_driving_2.db";
    private static final String DATABASE_INDO_SIGN = "indo_sign.db";

    private static PoolDatabaseLoader instance;

    private DatabaseLoaderV2 indoDrivingDatabaseLoader;
    private DatabaseLoaderV2 indoSignDatabaseLoader;

    private PoolDatabaseLoader() {
    }

    public static PoolDatabaseLoader getInstance() {
        if (instance == null) {
            instance = new PoolDatabaseLoader();
        }
        return instance;
    }

    public void initIfNeeded(Context context) {
        indoDrivingDatabaseLoader = new DatabaseLoaderV2(context, DATABASE_INDO_DRIVING);
        indoDrivingDatabaseLoader.initIfNeeded();

        indoSignDatabaseLoader = new DatabaseLoaderV2(context, DATABASE_INDO_SIGN);
        indoSignDatabaseLoader.initIfNeeded();

    }

    public DatabaseLoaderV2 getIndoDrivingDatabaseLoader() {
        return indoDrivingDatabaseLoader;
    }

    public DatabaseLoaderV2 getIndoSignDatabaseLoader() {
        return indoSignDatabaseLoader;
    }

}
