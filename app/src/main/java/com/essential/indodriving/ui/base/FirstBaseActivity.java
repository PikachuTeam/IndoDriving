package com.essential.indodriving.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.ui.widget.UpgradeToProVerDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by yue on 05/07/2016.
 */
public abstract class FirstBaseActivity extends AppCompatActivity implements
        BillingProcessor.IBillingHandler, View.OnClickListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, BaseConfirmDialog.OnConfirmDialogButtonClickListener {

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

    protected void refreshUI() {
        isProVer = MySetting.getInstance().isProVersion();
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


    protected void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

    protected void startActivityWithAnimation(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_right_enter, R.anim.activity_slide_left_exit);
    }

    protected void closeActivityWithAnimation() {
        overridePendingTransition(R.anim.activity_slide_left_enter, R.anim.activity_slide_right_exit);
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (billingProcessor != null &&
                !billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                CommonUtil.openApplicationOnGooglePlay(this, Constants.PACKAGE_NAME_FREE_VER);
                break;
            case R.id.fab_share:
                sharingEvent();
                break;
        }
    }

    @Override
    public void onConfirmDialogButtonClick(
            BaseConfirmDialog.ConfirmButton button, @BaseConfirmDialog.DialogTypeDef int dialogType,
            BaseConfirmDialog dialog) {
        switch (button) {
            case OK:
                dialog.dismiss();
                purchaseApp();
                break;
            case CANCEL:
                dialog.dismiss();
                break;
        }
    }


    @Override
    public void onMenuExpanded() {
        overlayView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMenuCollapsed() {
        overlayView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}