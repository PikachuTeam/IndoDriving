package com.essential.indodriving.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.ui.base.FirstBaseActivity;

import tatteam.com.app_common.util.CloseAppHandler;

/**
 * Created by yue on 04/07/2016.
 */
public class HomeActivity extends FirstBaseActivity implements
        CloseAppHandler.OnCloseAppListener {

    public static Typeface defaultFont;
    private CoordinatorLayout coordinatorLayout;
    private CloseAppHandler closeAppHandler;
    protected View imageNew;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultFont = Typeface.createFromAsset(getAssets(), "fonts/Menu Sim.ttf");
        findViews();
        closeAppHandler = new CloseAppHandler(this, false);
        closeAppHandler.setListener(this);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
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
        super.onClick(v);
        switch (v.getId()) {
            case R.id.button_sign:
                startActivityWithAnimation(new Intent(this, ChooseSignTypeActivity.class));
                MySetting.getInstance().setSignNew(false);
                break;
            case R.id.button_theory:
                startActivityWithAnimation(new Intent(this, ChooseSimActivity.class));
                break;
        }
    }



    private void findViews() {
//        // Find views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        imageNew = findViewById(R.id.img_new);

// Set listener
        findViewById(R.id.button_theory).setOnClickListener(this);
        findViewById(R.id.button_sign).setOnClickListener(this);

        // Set font
        ((TextView) findViewById(R.id.text_theory_title)).setTypeface(defaultFont);
        ((TextView) findViewById(R.id.text_sign_title)).setTypeface(defaultFont);
    }

    @Override
    protected void refreshUI() {
        super.refreshUI();

        if (!MySetting.getInstance().isSignNew()) {
            imageNew.setVisibility(View.GONE);
        } else {
            imageNew.setVisibility(View.VISIBLE);
        }
    }
}
