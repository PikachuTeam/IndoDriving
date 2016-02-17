package com.essential.indodriving.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.essential.indodriving.R;

import tatteam.com.app_common.ui.activity.BaseActivity;
import tatteam.com.app_common.ui.fragment.BaseFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateContentView() {

    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        return null;
    }
}
