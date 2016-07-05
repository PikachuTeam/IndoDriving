package com.essential.indodriving.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.widget.UpgradeToProVerDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.util.CloseAppHandler;
import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by yue on 04/07/2016.
 */
public class HomeActivity extends AppCompatActivity implements
        CloseAppHandler.OnCloseAppListener, View.OnClickListener,
        BaseConfirmDialog.OnConfirmDialogButtonClickListener, BillingProcessor.IBillingHandler, FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    public final static String PACKAGE_NAME_FREE_VER = "com.essential.indodriving.free";
    private FloatingActionsMenu floatingActionsMenu;
    private CoordinatorLayout coordinatorLayout;
    private View buttonProVer;
    private View overlayView;
    private View imageProVer;
    private BillingProcessor billingProcessor;
    private CloseAppHandler closeAppHandler;
    public static Typeface defaultFont;
    private boolean isProVer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        defaultFont = Typeface.createFromAsset(getAssets(), "fonts/Menu Sim.ttf");
        isProVer = MySetting.getInstance().isProVersion();
        findViews();
        closeAppHandler = new CloseAppHandler(this, false);
        closeAppHandler.setListener(this);
        if (BuildConfig.DEBUG) {
            billingProcessor = new BillingProcessor(this, Constants.DEV_KEY, this);
        } else {
            if (!MySetting.getInstance().isProVersion() &&
                    BillingProcessor.isIabServiceAvailable(this)) {
                billingProcessor = new BillingProcessor(this, Constants.DEV_KEY, this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    @Override
    protected void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (billingProcessor != null &&
                !billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (floatingActionsMenu.isExpanded()) floatingActionsMenu.collapse();
        else closeAppHandler.setKeyBackPress(this);
    }

    @Override
    public void onRateAppDialogClose() {
        finish();
    }

    @Override
    public void onTryToCloseApp() {
        Snackbar.make(coordinatorLayout, getResources().
                getText(R.string.press_again_to_exit), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onReallyWantToCloseApp() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pro_ver:
                UpgradeToProVerDialog dialog = new UpgradeToProVerDialog(this);
                dialog.setOnConfirmDialogButtonClickListener(this);
                dialog.show();
                break;
            case R.id.view_overlay:
                floatingActionsMenu.collapse();
                break;
            case R.id.fab_more_apps:
                AppCommon.getInstance().openMoreAppDialog(this);
                break;
            case R.id.fab_rate_us:
                CommonUtil.openApplicationOnGooglePlay(this, PACKAGE_NAME_FREE_VER);
                break;
            case R.id.fab_share:
                sharingEvent();
                break;
            case R.id.button_sign:
                break;
            case R.id.button_theory:
                startActivity(new Intent(this, ChooseSimActivity.class));
                break;
        }
    }

    @Override
    public void onConfirmDialogButtonClick(BaseConfirmDialog.ConfirmButton button,
                                           @BaseConfirmDialog.DialogTypeDef int dialogType,
                                           BaseConfirmDialog dialog) {
        switch (button) {
            case OK:
                dialog.dismiss();
                if (billingProcessor != null && billingProcessor.isInitialized()) {
                    if (!billingProcessor.isPurchased(Constants.PURCHASE_PRO_VERSION_ID)) {
                        billingProcessor.purchase(this, Constants.PURCHASE_PRO_VERSION_ID);
                    }
                }
                break;
            case CANCEL:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (Constants.PURCHASE_PRO_VERSION_ID.equals(productId)) {
            MySetting.getInstance().setProVersion(true);
            refreshUI();
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
        boolean isProVersion = billingProcessor.isPurchased(Constants.PURCHASE_PRO_VERSION_ID);
        MySetting.getInstance().setProVersion(isProVersion);
        refreshUI();
    }

    @Override
    public void onMenuExpanded() {
        overlayView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMenuCollapsed() {
        overlayView.setVisibility(View.GONE);
    }

    private void findViews() {
        // Find views
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floating_actions_menu);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
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
        findViewById(R.id.button_theory).setOnClickListener(this);
        findViewById(R.id.button_sign).setOnClickListener(this);

        // Set font
        ((TextView) findViewById(R.id.text_sign_title)).setTypeface(defaultFont);
        ((TextView) findViewById(R.id.text_theory_title)).setTypeface(defaultFont);
    }

    private void refreshUI() {
        imageProVer.setVisibility(isProVer ? View.VISIBLE : View.GONE);
        buttonProVer.setVisibility(isProVer ? View.GONE : View.VISIBLE);
    }

    private void sharingEvent() {
        String androidLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String sharedText = getString(R.string.app_name) + ".\nAndroid: " + androidLink;
        CommonUtil.sharePlainText(this, sharedText);
    }
}
