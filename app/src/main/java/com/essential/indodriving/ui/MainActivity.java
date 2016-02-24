package com.essential.indodriving.ui;

import android.widget.FrameLayout;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseActivity;
import com.essential.indodriving.ui.home.HomeFragment;

import tatteam.com.app_common.ads.AdsSmallBannerHandler;
import tatteam.com.app_common.ui.fragment.BaseFragment;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class MainActivity extends MyBaseActivity {

    private int button;
    private AdsSmallBannerHandler adsSmallBannerHandler;
    private FrameLayout adsContainer;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        return new HomeFragment();
    }

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();
        button = getIntent().getIntExtra("button", 1);
        adsContainer = (FrameLayout) findViewById(R.id.adsContainer2);
        adsSmallBannerHandler = new AdsSmallBannerHandler(this, adsContainer, AppConstant.AdsType.SMALL_BANNER_TEST);
        adsSmallBannerHandler.setup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adsSmallBannerHandler.destroy();
    }
}
