package com.essential.indodriving.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import tatteam.com.app_common.ads.AdsBigBannerHandler;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;
import tatteam.com.app_common.ui.activity.BaseActivity;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public abstract class MyBaseActivity extends BaseActivity implements BillingProcessor.IBillingHandler {

    public final static int BIG_ADS_SHOWING_INTERVAL = 20;
    public static AppConstant.AdsType ADS_SMALL;
    public static AppConstant.AdsType ADS_BIG;
    public static AppConstant.AdsType ADS_NATIVE_EXPRESS;
    public static AppConstant.AdsType ADS_NATIVE_EXPRESS_CONTENT;
    public static AppConstant.AdsType ADS_NATIVE_EXPRESS_INSTALL;

    public static int count;
    private Toolbar toolbar;
    private TextView textViewTitle;
    private TextView buttonTutorial;
    private TextView buttonResult;
    private TextView mButtonModifyAnswer;
    private LinearLayout buttonShare;
    private CoordinatorLayout mainCoordinatorLayout;
    private FrameLayout adsContainer;
    private AdsSmallBannerHandler adsSmallBannerHandler;
    private AdsBigBannerHandler adsBigBannerHandler;
    private BillingProcessor mBillingProcessor;
    private FirebaseAnalytics firebaseAnalytics;


    public static void startActivityAnimation(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_slide_right_enter, R.anim.activity_slide_left_exit);
    }

    public static void finishActivityAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_slide_left_enter, R.anim.activity_slide_right_exit);
    }

    public CoordinatorLayout getMainCoordinatorLayout() {
        return mainCoordinatorLayout;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateContentView() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());

        findViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Menu Sim.ttf");
        setFont(font);
        if (!MySetting.getInstance().isProVersion() && BillingProcessor.isIabServiceAvailable(this)) {
            mBillingProcessor = new BillingProcessor(this, Constants.DEV_KEY, this);
        }
        setupAds();
    }

    @Override
    protected void onDestroy() {
        if (adsSmallBannerHandler != null) {
            adsSmallBannerHandler.destroy();
        }
        if (adsBigBannerHandler != null) {
            adsBigBannerHandler.destroy();
        }
        if (mBillingProcessor != null) {
            mBillingProcessor.release();
        }
        super.onDestroy();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getButtonModifyAnswer() {
        return mButtonModifyAnswer;
    }

    public void setToolbarTitle(String title) {
        textViewTitle.setText(title);
    }

    public void enableButtonTutorial(boolean isVisible) {
        buttonTutorial.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void enableButtonResult(boolean isVisible) {
        buttonResult.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void enableButtonShare(boolean isVisible) {
        buttonShare.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void enableButtonModifyAnswer(boolean isVisible) {
        mButtonModifyAnswer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void showBigAdsIfNeeded() {
        if (false && !MySetting.getInstance().isProVersion()) {
            count++;
            if (count % BIG_ADS_SHOWING_INTERVAL == 0) {
                adsBigBannerHandler.show();
            }
        }
    }

    private void setupAds() {
        if (MySetting.getInstance().isProVersion()) {
            adsContainer.setVisibility(View.GONE);
        } else {
            count = 0;
            adsSmallBannerHandler = new AdsSmallBannerHandler(this, adsContainer, ADS_SMALL);
            adsSmallBannerHandler.setup();
            adsBigBannerHandler = new AdsBigBannerHandler(this, ADS_BIG);
            adsBigBannerHandler.setup();
        }
    }

    private void stopAdvertising() {
        if (adsContainer != null) {
            adsContainer.setVisibility(View.GONE);
        }
        if (adsBigBannerHandler != null) {
            adsBigBannerHandler.destroy();
        }
        if (adsBigBannerHandler != null) {
            adsBigBannerHandler.destroy();
        }
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonTutorial = (TextView) findViewById(R.id.buttonTutorial);
        buttonResult = (TextView) findViewById(R.id.buttonResult);
        buttonShare = (LinearLayout) findViewById(R.id.buttonShare);
        mButtonModifyAnswer = (TextView) findViewById(R.id.button_modify_answer);
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
        mButtonModifyAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentFragment().onMenuItemClick(MyBaseFragment.BUTTON_MODIFY_ANSWER);
            }
        });
    }

    private void setFont(Typeface font) {
        textViewTitle.setTypeface(font);
        buttonResult.setTypeface(font);
        buttonTutorial.setTypeface(font);
        mButtonModifyAnswer.setTypeface(font);
    }

    public MyBaseFragment getMyCurrentFragment() {
        return (MyBaseFragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
    }


    public void handlePurchasing() {
        if (mBillingProcessor != null && mBillingProcessor.isInitialized()) {
            if (!mBillingProcessor.isPurchased(Constants.PURCHASE_PRO_VERSION_ID)) {
                mBillingProcessor.purchase(this, Constants.PURCHASE_PRO_VERSION_ID);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mBillingProcessor != null && !mBillingProcessor.
                handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (Constants.PURCHASE_PRO_VERSION_ID.equals(productId)) {
            MySetting.getInstance().setProVersion(true);
            getMyCurrentFragment().refreshUI();
            stopAdvertising();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
    }

    public void sendItemChosenLog(String itemCategory, String itemName) {
        if (firebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, itemCategory);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }
}
