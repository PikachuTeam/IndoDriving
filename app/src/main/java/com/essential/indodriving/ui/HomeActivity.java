package com.essential.indodriving.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.essential.indodriving.R;

import tatteam.com.app_common.ads.AdsSmallBannerHandler;
import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.CloseAppHandler;

public class HomeActivity extends AppCompatActivity implements CloseAppHandler.OnCloseAppListener {

    private Button buttonLearn;
    private Button buttonTest;
    private FrameLayout adsContainer1;

    private AdsSmallBannerHandler adsSmallBannerHandler;
    private CloseAppHandler closeAppHandler;

    public final static int LEARN_BUTTON = 1, TEST_BUTTON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViews();
        buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("button", LEARN_BUTTON);
                startActivity(intent);
            }
        });

        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("button", TEST_BUTTON);
                startActivity(intent);
            }
        });

        closeAppHandler = new CloseAppHandler(this);
        closeAppHandler.setListener(this);

        setupAds();
    }

    private void findViews() {
        buttonLearn = (Button) findViewById(R.id.buttonLearn);
        buttonTest = (Button) findViewById(R.id.buttonTest);
        adsContainer1 = (FrameLayout) findViewById(R.id.adsContainer1);
    }

    private void setupAds() {
        adsSmallBannerHandler = new AdsSmallBannerHandler(this, adsContainer1, AppConstant.AdsType.SMALL_BANNER_TEST);
        adsSmallBannerHandler.setup();
    }

    @Override
    public void onBackPressed() {
        closeAppHandler.setKeyBackPress(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adsSmallBannerHandler.destroy();
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
