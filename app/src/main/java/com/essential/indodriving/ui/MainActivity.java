package com.essential.indodriving.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseActivity;
import com.essential.indodriving.data.DataSource;

import tatteam.com.app_common.ads.AdsSmallBannerHandler;
import tatteam.com.app_common.ui.fragment.BaseFragment;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class MainActivity extends MyBaseActivity {

    private AdsSmallBannerHandler adsSmallBannerHandler;
    private FrameLayout adsContainer;
    private int type;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        ChooseItemFragment fragment = new ChooseItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();

        type = getIntent().getIntExtra("Type", DataSource.TYPE_SIM_A);

        adsContainer = (FrameLayout) findViewById(R.id.adsContainer2);
        adsSmallBannerHandler = new AdsSmallBannerHandler(this, adsContainer, AppConstant.AdsType.SMALL_BANNER_TEST);
        adsSmallBannerHandler.setup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adsSmallBannerHandler.destroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_enter, R.anim.activity_slide_right_exit);
    }
}
