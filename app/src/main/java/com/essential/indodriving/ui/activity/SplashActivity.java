package com.essential.indodriving.ui.activity;

import android.content.Intent;

import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.data.PoolDatabaseLoader;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.ui.activity.EssentialSplashActivity;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class SplashActivity extends EssentialSplashActivity {

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();
    }

    @Override
    protected void onInitAppCommon() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3786715234447481~5340872855");

        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().increaseLaunchTime();
        if (BuildConfig.DEBUG) {
            MyBaseActivity.ADS_SMALL = AppConstant.AdsType.SMALL_BANNER_TEST;
            MyBaseActivity.ADS_BIG = AppConstant.AdsType.BIG_BANNER_TEST;
            MyBaseActivity.ADS_NATIVE_EXPRESS = AppConstant.AdsType.NATIVE_EXPRESS_TEST;
            MyBaseActivity.ADS_NATIVE_EXPRESS_CONTENT = AppConstant.AdsType.NATIVE_EXPRESS_TEST;
            MyBaseActivity.ADS_NATIVE_EXPRESS_INSTALL = AppConstant.AdsType.NATIVE_EXPRESS_TEST;
            MyBaseActivity.ADS_BIG_NATIVE_EXPRESS = AppConstant.AdsType.NATIVE_EXPRESS_TEST;
        } else {
            MyBaseActivity.ADS_SMALL = AppConstant.AdsType.SMALL_BANNER_DRIVING_TEST;
            MyBaseActivity.ADS_BIG = AppConstant.AdsType.BIG_BANNER_DRIVING_TEST;
            MyBaseActivity.ADS_NATIVE_EXPRESS = AppConstant.AdsType.NATIVE_EXPRESS_DRIVING_TEST;
            MyBaseActivity.ADS_NATIVE_EXPRESS_CONTENT = AppConstant.AdsType.NATIVE_EXPRESS_CONTENT_DRIVING_TEST;
            MyBaseActivity.ADS_NATIVE_EXPRESS_INSTALL = AppConstant.AdsType.NATIVE_EXPRESS_INSTALL_DRIVING_TEST;
            MyBaseActivity.ADS_BIG_NATIVE_EXPRESS = AppConstant.AdsType.BIG_NATIVE_EXPRESS_DRIVING_TEST;
        }

        AppCommon.getInstance().syncAdsIfNeeded(MyBaseActivity.ADS_SMALL,
                MyBaseActivity.ADS_BIG,
                MyBaseActivity.ADS_NATIVE_EXPRESS,
                MyBaseActivity.ADS_NATIVE_EXPRESS_CONTENT,
                MyBaseActivity.ADS_NATIVE_EXPRESS_INSTALL,
                MyBaseActivity.ADS_BIG_NATIVE_EXPRESS);
        PoolDatabaseLoader.getInstance().initIfNeeded(getApplicationContext());
        loadConfig();

    }

    @Override
    protected void onFinishInitAppCommon() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void loadConfig() {
        if (!MySetting.getInstance().isEnableRateToUnlock()) {
            Ion.with(getApplicationContext())
                    .load(Constants.URL_APP_CONFIG)
                    .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (result != null) {
                        boolean rate_to_unlock = result.get("rate_to_unlock").getAsBoolean();
                        if (rate_to_unlock) MySetting.getInstance().setRateToUnlock(rate_to_unlock);
                    }
                }
            });
        }
    }
}
