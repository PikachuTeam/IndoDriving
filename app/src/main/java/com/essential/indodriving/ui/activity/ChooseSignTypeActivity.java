package com.essential.indodriving.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.SignDataSource;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.FirstBaseActivity;

/**
 * Created by yue on 04/07/2016.
 */
public class ChooseSignTypeActivity extends FirstBaseActivity  {

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
    public void onBackPressed() {
        if (floatingActionsMenu.isExpanded()) floatingActionsMenu.collapse();
        else {
            super.onBackPressed();
            closeActivityWithAnimation();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent(this, SignMainActivity.class);
        switch (v.getId()) {
            case R.id.button_prohibition_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, SignDataSource.GROUP_PROHIBITION_SIGN);
                startActivityWithAnimation(intent);
                break;
            case R.id.button_warning_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, SignDataSource.GROUP_WARNING_SIGN);
                startActivityWithAnimation(intent);
                break;
            case R.id.button_command_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, SignDataSource.GROUP_COMMAND_SIGN);
                startActivityWithAnimation(intent);
                break;
            case R.id.button_direction_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, SignDataSource.GROUP_DIRECTION_SIGN);
                startActivityWithAnimation(intent);
                break;
            case R.id.button_additional_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, SignDataSource.GROUP_ADDITIONAL_SIGN);
                startActivityWithAnimation(intent);
                break;
            case R.id.button_all_sign:
                intent.putExtra(Constants.BUNDLE_SIGN_TYPE, SignDataSource.GROUP_ALL);
                startActivityWithAnimation(intent);
                break;
        }
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
