package com.essential.indodriving;

import android.app.Application;

import tatteam.com.app_common.AppCommon;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class ClientApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        MySetting.getInstance().initIfNeeded(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        AppCommon.getInstance().destroy();
        super.onTerminate();
    }
}
