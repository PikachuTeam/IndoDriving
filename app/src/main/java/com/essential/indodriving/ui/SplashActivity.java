package com.essential.indodriving.ui;

import android.content.Intent;

import com.essential.indodriving.R;

import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.ui.activity.BaseSplashActivity;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class SplashActivity extends BaseSplashActivity {
    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreateContentView() {

    }

    @Override
    protected void onInitAppCommon() {
        DatabaseLoader.getInstance().createIfNeeded(getApplicationContext(), "indo_driving.db");
        DatabaseLoader.getInstance().openConnection();
    }

    @Override
    protected void onFinishInitAppCommon() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
