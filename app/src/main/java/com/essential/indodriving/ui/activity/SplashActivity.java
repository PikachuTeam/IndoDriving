package com.essential.indodriving.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.essential.indodriving.ui.base.Constants;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.ui.activity.EssentialSplashActivity;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class SplashActivity extends EssentialSplashActivity {

    private final static String DATABASE_NAME = "indo_driving.db";

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

        loadConfig();
    }

    @Override
    protected void onFinishInitAppCommon() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadConfig() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(Constants.PREF_RATE_TO_UNLOCK, false)) {
            Ion.with(getApplicationContext())
                    .load(Constants.URL_APP_CONFIG)
                    .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (result != null) {
                        boolean rate_to_unlock = result.get("rate_to_unlock").getAsBoolean();
                        if (rate_to_unlock) {
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.PREF_RATE_TO_UNLOCK, rate_to_unlock);
                            editor.commit();
                        }
                    }
                }
            });
        }
    }
}
