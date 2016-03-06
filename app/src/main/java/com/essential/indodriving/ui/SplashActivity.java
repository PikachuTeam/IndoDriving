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
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class SplashActivity extends BaseSplashActivity {

    private RelativeLayout background, keyboard, rain, text;
    private ObjectAnimator keyScaleX, keyScaleY, backgroundScaleX, backgroundScaleY;
    private AnimatorSet animationZoomBackground;
    private Animation animationDownBackground;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreateContentView() {
        findViews();
        animBackground();
        animLogo();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationZoomBackground.cancel();
        animationDownBackground.cancel();
    }

    private void animBackground() {
        backgroundScaleX = ObjectAnimator.ofFloat(background, "scaleX", 1.2f, 1f);
        backgroundScaleY = ObjectAnimator.ofFloat(background, "scaleY", 1.2f, 1f);
        backgroundScaleX.setDuration(2000);
        backgroundScaleY.setDuration(2000);
        animationZoomBackground = new AnimatorSet();
        animationZoomBackground.play(backgroundScaleX).with(backgroundScaleY);
        animationZoomBackground.start();
        animationDownBackground = AnimationUtils.loadAnimation(this, R.anim.translate_background_center_to_bot);
        background.startAnimation(animationDownBackground);
    }

    private void animText() {
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.translate_text_bot_to_center);
        hyperspaceJump.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animRain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        text.startAnimation(hyperspaceJump);

    }

    private void animRain() {
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.translate_rain_top_to_center);
        rain.startAnimation(hyperspaceJump);
        hyperspaceJump.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void findViews() {
        background = (RelativeLayout) findViewById(R.id.background);
        keyboard = (RelativeLayout) findViewById(R.id.key_broad);
        rain = (RelativeLayout) findViewById(R.id.rain);
        text = (RelativeLayout) findViewById(R.id.essential);
    }

    public void animLogo() {
        keyScaleX = ObjectAnimator.ofFloat(keyboard, "scaleX", 2f, 1f);
        keyScaleY = ObjectAnimator.ofFloat(keyboard, "scaleY", 2f, 1f);
        keyScaleX.setDuration(1000);
        keyScaleY.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(keyScaleX).with(keyScaleY);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animText();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}
