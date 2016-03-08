package com.essential.indodriving.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;

import tatteam.com.app_common.ui.activity.BaseActivity;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public abstract class MyBaseActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView textViewTitle;
    private TextView buttonTutorial;
    private TextView buttonResult;
    private LinearLayout buttonShare;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateContentView() {
        findViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Menu Sim.ttf");
        setFont(font);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbarTitle(String title) {
        textViewTitle.setText(title);
    }

    public void enableButtonTutorial(boolean isVisible) {
        if (isVisible) {
            buttonTutorial.setVisibility(View.VISIBLE);
        } else {
            buttonTutorial.setVisibility(View.GONE);
        }
    }

    public void enableButtonResult(boolean isVisible) {
        if (isVisible) {
            buttonResult.setVisibility(View.VISIBLE);
        } else {
            buttonResult.setVisibility(View.GONE);
        }
    }

    public void enableButtonShare(boolean isVisible) {
        if (isVisible) {
            buttonShare.setVisibility(View.VISIBLE);
        } else {
            buttonShare.setVisibility(View.GONE);
        }
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonTutorial = (TextView) findViewById(R.id.buttonTutorial);
        buttonResult = (TextView) findViewById(R.id.buttonResult);
        buttonShare = (LinearLayout) findViewById(R.id.buttonShare);

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
                getMyCurrentFragment().onMenuItemClick(MyBaseFragment.BUTTON_SHARE);
            }
        });
    }

    private void setFont(Typeface font) {
        textViewTitle.setTypeface(font);
        buttonResult.setTypeface(font);
        buttonTutorial.setTypeface(font);
    }

    private MyBaseFragment getMyCurrentFragment() {
        MyBaseFragment fragment = (MyBaseFragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
        return fragment;
    }

    public static void startActivityAnimation(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_slide_right_enter, R.anim.activity_slide_left_exit);
    }

    public static void finishActivityAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_slide_left_enter, R.anim.activity_slide_right_exit);
    }
}
