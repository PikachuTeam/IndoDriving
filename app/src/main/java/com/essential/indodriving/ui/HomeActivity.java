package com.essential.indodriving.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essential.indodriving.R;
import com.essential.indodriving.data.DataSource;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;
import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.CloseAppHandler;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, CloseAppHandler.OnCloseAppListener {

    private LinearLayout buttonLearnSimA;
    private LinearLayout buttonLearnSimAUmum;
    private LinearLayout buttonLearnSimB1;
    private LinearLayout buttonLearnSimB1Umum;
    private LinearLayout buttonLearnSimB2;
    private LinearLayout buttonLearnSimB2Umum;
    private LinearLayout buttonLearnSimC;
    private LinearLayout buttonLearnSimD;
    private FrameLayout adsContainer;
    private FloatingActionButton fab;

    private AdsSmallBannerHandler adsSmallBannerHandler;
    private CloseAppHandler closeAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViews();

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Menu Sim.ttf");
        setFont(font);

        adsSmallBannerHandler = new AdsSmallBannerHandler(this, adsContainer, AppConstant.AdsType.SMALL_BANNER_TEST);
        adsSmallBannerHandler.setup();

        closeAppHandler = new CloseAppHandler(this);
        closeAppHandler.setListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adsSmallBannerHandler.destroy();
    }

    @Override
    public void onBackPressed() {
        closeAppHandler.setKeyBackPress(this);
    }

    private void findViews() {
        buttonLearnSimA = (LinearLayout) findViewById(R.id.buttonLearnSimA);
        buttonLearnSimAUmum = (LinearLayout) findViewById(R.id.buttonLearnSimAUmum);
        buttonLearnSimB1 = (LinearLayout) findViewById(R.id.buttonLearnSimB1);
        buttonLearnSimB1Umum = (LinearLayout) findViewById(R.id.buttonLearnSimB1Umum);
        buttonLearnSimB2 = (LinearLayout) findViewById(R.id.buttonLearnSimB2);
        buttonLearnSimB2Umum = (LinearLayout) findViewById(R.id.buttonLearnSimB2Umum);
        buttonLearnSimC = (LinearLayout) findViewById(R.id.buttonLearnSimC);
        buttonLearnSimD = (LinearLayout) findViewById(R.id.buttonLearnSimD);
        adsContainer = (FrameLayout) findViewById(R.id.adsContainer1);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        buttonLearnSimA.setOnClickListener(this);
        buttonLearnSimAUmum.setOnClickListener(this);
        buttonLearnSimB1.setOnClickListener(this);
        buttonLearnSimB1Umum.setOnClickListener(this);
        buttonLearnSimB2.setOnClickListener(this);
        buttonLearnSimB2Umum.setOnClickListener(this);
        buttonLearnSimC.setOnClickListener(this);
        buttonLearnSimD.setOnClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCommon.getInstance().openMoreAppDialog(HomeActivity.this);
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
    public void onClick(View v) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);

        if (v == buttonLearnSimA) {
            intent.putExtra("Type", DataSource.TYPE_SIM_A);
        } else if (v == buttonLearnSimAUmum) {
            intent.putExtra("Type", DataSource.TYPE_SIM_A_UMUM);
        } else if (v == buttonLearnSimB1) {
            intent.putExtra("Type", DataSource.TYPE_SIM_B1);
        } else if (v == buttonLearnSimB1Umum) {
            intent.putExtra("Type", DataSource.TYPE_SIM_B1_UMUM);
        } else if (v == buttonLearnSimB2) {
            intent.putExtra("Type", DataSource.TYPE_SIM_B2);
        } else if (v == buttonLearnSimB2Umum) {
            intent.putExtra("Type", DataSource.TYPE_SIM_B2_UMUM);
        } else if (v == buttonLearnSimC) {
            intent.putExtra("Type", DataSource.TYPE_SIM_C);
        } else if (v == buttonLearnSimD) {
            intent.putExtra("Type", DataSource.TYPE_SIM_D);
        }

        startActivity(intent);
    }

    @Override
    public void onRateAppDialogClose() {

    }

    @Override
    public void onTryToCloseApp() {
        Toast.makeText(this, closeAppHandler.getDefaultExitMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReallyWantToCloseApp() {
        finish();
    }
}
