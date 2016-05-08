package com.essential.indodriving.ui;

import android.content.Intent;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.ui.activity.EssentialSplashActivity;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class SplashActivity extends EssentialSplashActivity {

    private final static String DATABASE_NAME="indo_driving.db";

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();
    }

    @Override
    protected void onInitAppCommon() {
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().increaseLaunchTime();
        AppCommon.getInstance().syncAdsIfNeeded(AppConstant.AdsType.SMALL_BANNER_DRIVING_TEST
                , AppConstant.AdsType.BIG_BANNER_DRIVING_TEST);
        DatabaseLoader.getInstance().createIfNeeded(getApplicationContext(), DATABASE_NAME);
    }

    @Override
    protected void onFinishInitAppCommon() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
