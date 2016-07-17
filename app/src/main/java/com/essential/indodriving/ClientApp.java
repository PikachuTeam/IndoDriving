package com.essential.indodriving;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.BaseApplication;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class ClientApp extends BaseApplication {

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
