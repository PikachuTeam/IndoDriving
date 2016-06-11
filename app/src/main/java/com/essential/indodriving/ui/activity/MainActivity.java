package com.essential.indodriving.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.essential.indodriving.R;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseActivity;
import com.essential.indodriving.ui.fragment.ChooseItemFragment;

import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class MainActivity extends MyBaseActivity implements BillingProcessor.IBillingHandler {

    private BillingProcessor mBillingProcessor;
    private int mType;
    private boolean mIsProVer;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        ChooseItemFragment fragment = new ChooseItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, mType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();
        mType = getIntent().getIntExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_A);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        mIsProVer = sharedPreferences.getBoolean(HomeActivity.PREF_IS_PRO_VERSION, false);
        if (!mIsProVer && BillingProcessor.isIabServiceAvailable(this)) {
            mBillingProcessor = new BillingProcessor(this, Constants.DEV_KEY, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_enter, R.anim.activity_slide_right_exit);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (Constants.PURCHASE_PRO_VERSION_ID.equals(productId)) {
            mIsProVer = true;
            SharedPreferences sharedPreferences = getSharedPreferences(
                    Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(HomeActivity.PREF_IS_PRO_VERSION, mIsProVer);
            editor.commit();
            getMyCurrentFragment().refreshUI();
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

    public void handlePurchasing() {
        if (mBillingProcessor != null && mBillingProcessor.isInitialized()) {
            if (!mBillingProcessor.isPurchased(Constants.PURCHASE_PRO_VERSION_ID)) {
                mBillingProcessor.purchase(this, Constants.PURCHASE_PRO_VERSION_ID);
            }
        }
    }
}
