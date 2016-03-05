package com.essential.indodriving.base;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonTutorial = (TextView) findViewById(R.id.buttonTutorial);
        buttonResult = (TextView) findViewById(R.id.buttonResult);

        buttonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentFragment().onMenuItemClick(MyBaseFragment.BUTTON_TUTORIAL_ID);
            }
        });

        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyCurrentFragment().onMenuItemClick(MyBaseFragment.BUTTON_RESULT_ID);
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
}
