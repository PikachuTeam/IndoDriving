package com.essential.indodriving.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.essential.indodriving.R;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.ui.activity.BaseSplashActivity;
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
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().increaseLaunchTime();
        AppCommon.getInstance().syncAdsIfNeeded(AppConstant.AdsType.SMALL_BANNER_TEST, AppConstant.AdsType.BIG_BANNER_TEST);
        DatabaseLoader.getInstance().createIfNeeded(getApplicationContext(), "indo_driving.db");
    }

    @Override
    protected void onFinishInitAppCommon() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
