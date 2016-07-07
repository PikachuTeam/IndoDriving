package com.essential.indodriving.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anjlab.android.iab.v3.TransactionDetails;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.FirstBaseActivity;
import com.essential.indodriving.ui.widget.UpgradeToProVerDialog;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by yue on 04/07/2016.
 */
public class ChooseSignTypeActivity extends FirstBaseActivity implements
        BaseConfirmDialog.OnConfirmDialogButtonClickListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        toolbar.setTitle(R.string.title_sign);
        setSupportActionBar(toolbar);
        enableBackButton();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_choose_sign_type;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (floatingActionsMenu.isExpanded()) floatingActionsMenu.collapse();
        else super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SignMainActivity.class);
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
                CommonUtil.openApplicationOnGooglePlay(this, Constants.PACKAGE_NAME_FREE_VER);
                break;
            case R.id.fab_share:
                sharingEvent();
                break;
            case R.id.button_prohibition_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, Constants.TYPE_PROHIBITION_SIGN);
                startActivity(intent);
                break;
            case R.id.button_warning_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, Constants.TYPE_WARNING_SIGN);
                startActivity(intent);
                break;
            case R.id.button_command_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, Constants.TYPE_COMMAND_SIGN);
                startActivity(intent);
                break;
            case R.id.button_direction_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, Constants.TYPE_DIRECTION_SIGN);
                startActivity(intent);
                break;
            case R.id.button_additional_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, Constants.TYPE_ADDITIONAL_SIGN);
                startActivity(intent);
                break;
            case R.id.button_all_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, Constants.TYPE_ALL_SIGN);
                startActivity(intent);
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
        billingInitialized();
    }

    private void findViews() {
        // Find view
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set listener
        findViewById(R.id.button_prohibition_sign).setOnClickListener(this);
        findViewById(R.id.button_additional_sign).setOnClickListener(this);
        findViewById(R.id.button_command_sign).setOnClickListener(this);
        findViewById(R.id.button_direction_sign).setOnClickListener(this);
        findViewById(R.id.button_warning_sign).setOnClickListener(this);
        findViewById(R.id.button_all_sign).setOnClickListener(this);

        // Set font
        ((TextView) findViewById(R.id.text_prohibition_sign)).setTypeface(HomeActivity.defaultFont);
        ((TextView) findViewById(R.id.text_warning_sign)).setTypeface(HomeActivity.defaultFont);
        ((TextView) findViewById(R.id.text_command_sign)).setTypeface(HomeActivity.defaultFont);
        ((TextView) findViewById(R.id.text_direction_sign)).setTypeface(HomeActivity.defaultFont);
        ((TextView) findViewById(R.id.text_additional_sign)).setTypeface(HomeActivity.defaultFont);
        ((TextView) findViewById(R.id.text_all_sign)).setTypeface(HomeActivity.defaultFont);
    }
}
