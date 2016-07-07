package com.essential.indodriving.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by yue on 05/07/2016.
 */
public abstract class FirstBaseActivity extends AppCompatActivity implements
        BillingProcessor.IBillingHandler, View.OnClickListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    protected FloatingActionsMenu floatingActionsMenu;
    protected View overlayView;
    protected View buttonProVer;
    protected View imageProVer;
    protected BillingProcessor billingProcessor;
    protected boolean isProVer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        isProVer = MySetting.getInstance().isProVersion();
        findViews();
        if (BuildConfig.DEBUG) {
            billingProcessor = new BillingProcessor(this, Constants.DEV_KEY, this);
        } else {
            if (!MySetting.getInstance().isProVersion() &&
                    BillingProcessor.isIabServiceAvailable(this)) {
                billingProcessor = new BillingProcessor(this, Constants.DEV_KEY, this);
            }
        }
    }

    protected abstract int getLayoutResId();

    private void findViews() {
        // Find views
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floating_actions_menu);
        buttonProVer = findViewById(R.id.button_pro_ver);
        overlayView = findViewById(R.id.view_overlay);
        imageProVer = findViewById(R.id.image_100_pro);

        // Set listener
        buttonProVer.setOnClickListener(this);
        overlayView.setOnClickListener(this);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(this);
        findViewById(R.id.fab_more_apps).setOnClickListener(this);
        findViewById(R.id.fab_rate_us).setOnClickListener(this);
        findViewById(R.id.fab_share).setOnClickListener(this);
    }

    protected void refreshUI() {
        imageProVer.setVisibility(isProVer ? View.VISIBLE : View.GONE);
        buttonProVer.setVisibility(isProVer ? View.GONE : View.VISIBLE);
    }

    protected void sharingEvent() {
        String androidLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String sharedText = getString(R.string.app_name) + ".\nAndroid: " + androidLink;
        CommonUtil.sharePlainText(this, sharedText);
    }

    protected void purchaseApp() {
        if (billingProcessor != null && billingProcessor.isInitialized()) {
            if (!billingProcessor.isPurchased(Constants.PURCHASE_PRO_VERSION_ID)) {
                billingProcessor.purchase(this, Constants.PURCHASE_PRO_VERSION_ID);
            }
        }
    }

    protected void billingInitialized() {
        boolean isProVersion = billingProcessor.isPurchased(Constants.PURCHASE_PRO_VERSION_ID);
        MySetting.getInstance().setProVersion(isProVersion);
        refreshUI();
    }

    protected void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }
}