package com.essential.indodriving.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.FirstBaseActivity;

public class ChooseSimActivity extends FirstBaseActivity implements
        View.OnClickListener, BillingProcessor.IBillingHandler {

    private Toolbar toolbar;
    private View btn_consume;
    private CoordinatorLayout coordinatorLayout;
    private ImageView banner;
    private CollapsingToolbarLayout toolbar_layout;
    private int number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        toolbar_layout.setExpandedTitleTextAppearance(R.style.ExpandedCollapsingToolbar);
        setFont(HomeActivity.defaultFont);
        setSupportActionBar(toolbar);
        enableBackButton();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_choose_sim;
    }

    @Override
    protected void onResume() {
        super.onResume();
        number = 0;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent(ChooseSimActivity.this, TheoryMainActivity.class);
        switch (v.getId()) {
            case R.id.buttonLearnSimA:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_A);
                startActivityWithAnimation(intent);
                break;
            case R.id.buttonLearnSimAUmum:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_A_UMUM);
                startActivityWithAnimation(intent);
                break;
            case R.id.buttonLearnSimB1:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_B1);
                startActivityWithAnimation(intent);
                break;
            case R.id.buttonLearnSimB1Umum:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_B1_UMUM);
                startActivityWithAnimation(intent);
                break;
            case R.id.buttonLearnSimB2:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_B2);
                startActivityWithAnimation(intent);
                break;
            case R.id.buttonLearnSimB2Umum:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_B2_UMUM);
                startActivityWithAnimation(intent);
                break;
            case R.id.buttonLearnSimC:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_C);
                startActivityWithAnimation(intent);
                break;
            case R.id.buttonLearnSimD:
                intent.putExtra(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_D);
                startActivityWithAnimation(intent);
                break;
        }

    }



    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        banner = (ImageView) findViewById(R.id.banner);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        btn_consume = findViewById(R.id.btn_consume);

        findViewById(R.id.buttonLearnSimA).setOnClickListener(this);
        findViewById(R.id.buttonLearnSimAUmum).setOnClickListener(this);
        findViewById(R.id.buttonLearnSimB1).setOnClickListener(this);
        findViewById(R.id.buttonLearnSimB1Umum).setOnClickListener(this);
        findViewById(R.id.buttonLearnSimB2).setOnClickListener(this);
        findViewById(R.id.buttonLearnSimB2Umum).setOnClickListener(this);
        findViewById(R.id.buttonLearnSimC).setOnClickListener(this);
        findViewById(R.id.buttonLearnSimD).setOnClickListener(this);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MySetting.getInstance().isProVersion()) {
                    number++;
                    if (number == Constants.PRESSING_TIMES) {
                        Snackbar.make(coordinatorLayout, getString(R.string.hacked), Snackbar.LENGTH_SHORT).show();
                        MySetting.getInstance().setProVersion(true);
                        refreshUI();
                    }
                }
            }
        });

        btn_consume.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);
        btn_consume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billingProcessor != null && billingProcessor.isInitialized()) {
                    if (billingProcessor.isPurchased(Constants.PURCHASE_PRO_VERSION_ID)) {
                        boolean consumeResult = billingProcessor.consumePurchase(Constants.PURCHASE_PRO_VERSION_ID);
                        if (consumeResult) {
                            MySetting.getInstance().setProVersion(false);
                            refreshUI();
                        }
                    }
                }
            }
        });

    }

    private void setFont(Typeface font) {
        ((TextView) findViewById(R.id.textViewSimA)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimAUmum)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB1)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB1Umum)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB2)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB2Umum)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimC)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimD)).setTypeface(font);
    }



    @Override
    public void onBackPressed() {
        if (floatingActionsMenu.isExpanded()) floatingActionsMenu.collapse();
        else {
            super.onBackPressed();
            closeActivityWithAnimation();
        }
    }
}
