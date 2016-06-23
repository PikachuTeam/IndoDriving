package com.essential.indodriving.ui.activity;

import android.content.Context;
import android.content.Intent;
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
public class MainActivity extends MyBaseActivity  {

    private int mType;

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




}
