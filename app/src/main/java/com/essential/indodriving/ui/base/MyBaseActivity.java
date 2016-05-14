package com.essential.indodriving.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.R;
import com.essential.indodriving.ui.activity.HomeActivity;

import tatteam.com.app_common.ads.AdsBigBannerHandler;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;
import tatteam.com.app_common.ui.activity.BaseActivity;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public abstract class MyBaseActivity extends BaseActivity {

    public final static boolean ADS_ENABLE = true;
    public final static int BIG_ADS_SHOWING_INTERVAL = 20;
    public static int count;
    private Toolbar toolbar;
    private TextView textViewTitle;
    private TextView buttonTutorial;
    private TextView buttonResult;
    private TextView mButtonWrittenTest;
    private LinearLayout buttonShare;
    private CoordinatorLayout mainCoordinatorLayout;
    private FrameLayout adsContainer;
    private boolean isProVersion;
    private AdsSmallBannerHandler adsSmallBannerHandler;
    private AdsBigBannerHandler adsBigBannerHandler;

    public CoordinatorLayout getMainCoordinatorLayout() {
        return mainCoordinatorLayout;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateContentView() {
        findViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Menu Sim.ttf");
        setFont(font);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        isProVersion = sharedPreferences.getBoolean(HomeActivity.PREF_IS_PRO_VERSION, BuildConfig.IS_PRO_VERSION);
        setupAds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adsSmallBannerHandler != null) {
            adsSmallBannerHandler.destroy();
        }
        if (adsBigBannerHandler != null) {
            adsBigBannerHandler.destroy();
        }
    }

    public static void startActivityAnimation(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_slide_right_enter, R.anim.activity_slide_left_exit);
    }

    public static void finishActivityAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_slide_left_enter, R.anim.activity_slide_right_exit);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbarTitle(String title) {
        textViewTitle.setText(title);
    }

    public void enableButtonTutorial(boolean isVisible) {
        if (isVisible) {
            buttonTutorial.setVisibility(View.VISIBLE);
        } else {
            buttonTutorial.setVisibility(View.GONE);
        }
    }

    public void enableButtonResult(boolean isVisible) {
        if (isVisible) {
            buttonResult.setVisibility(View.VISIBLE);
        } else {
            buttonResult.setVisibility(View.GONE);
        }
    }

    public void enableButtonShare(boolean isVisible) {
        if (isVisible) {
            buttonShare.setVisibility(View.VISIBLE);
        } else {
            buttonShare.setVisibility(View.GONE);
        }
    }

    public void enableButtonWrittenTest(boolean isVisible) {
        if (isVisible) {
            mButtonWrittenTest.setVisibility(View.VISIBLE);
        } else {
            mButtonWrittenTest.setVisibility(View.GONE);
        }
    }

    public void showBigAdsIfNeeded() {
        if (!isProVersion) {
            if (ADS_ENABLE) {
                count++;
                if (count % BIG_ADS_SHOWING_INTERVAL == 0) {
                    adsBigBannerHandler.show();
                }
            }
        }
    }

    private void setupAds() {
        if (isProVersion) {
            if (ADS_ENABLE) {
                adsContainer.setVisibility(View.GONE);
                if (adsSmallBannerHandler != null) {
                    adsSmallBannerHandler.destroy();
                }
                if (adsBigBannerHandler != null) {
                    adsBigBannerHandler.destroy();
                }
            } else {
                adsContainer.setVisibility(View.GONE);
            }
        } else {
            count = 0;
            if (ADS_ENABLE) {
                adsSmallBannerHandler = new AdsSmallBannerHandler(this, adsContainer
                        , AppConstant.AdsType.SMALL_BANNER_DRIVING_TEST);
                adsSmallBannerHandler.setup();
                adsBigBannerHandler = new AdsBigBannerHandler(this, AppConstant.AdsType.BIG_BANNER_DRIVING_TEST);
                adsBigBannerHandler.setup();
            } else {
                adsContainer.setVisibility(View.GONE);
            }
        }
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonTutorial = (TextView) findViewById(R.id.buttonTutorial);
        buttonResult = (TextView) findViewById(R.id.buttonResult);
        buttonShare = (LinearLayout) findViewById(R.id.buttonShare);
        mButtonWrittenTest = (TextView) findViewById(R.id.buttonWrittenTest);
        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);
        adsContainer = (FrameLayout) findViewById(R.id.adsContainer2);
        buttonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentFragment().onMenuItemClick(MyBaseFragment.BUTTON_TUTORIAL);
            }
        });
        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentFragment().onMenuItemClick(MyBaseFragment.BUTTON_RESULT);
            }
        });
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentFragment().sharingEvent();
            }
        });
        mButtonWrittenTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentFragment().onMenuItemClick(MyBaseFragment.BUTTON_WRITTEN_TEST);
            }
        });
    }

    private void setFont(Typeface font) {
        textViewTitle.setTypeface(font);
        buttonResult.setTypeface(font);
        buttonTutorial.setTypeface(font);
        mButtonWrittenTest.setTypeface(font);
    }

    private MyBaseFragment getMyCurrentFragment() {
        MyBaseFragment fragment = (MyBaseFragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
        return fragment;
    }
}
