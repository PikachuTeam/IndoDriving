package com.essential.indodriving.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.essential.indodriving.R;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.SecondBaseActivity;
import com.essential.indodriving.ui.fragment.sign.SignChooseItemFragment;

import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by yue on 07/07/2016.
 */
public class SignMainActivity extends SecondBaseActivity {

    private int type;

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();
        getData();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        SignChooseItemFragment fragment = new SignChooseItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_SIGN_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main_sign;
    }

    private void getData() {
        Intent intent = getIntent();
        type = intent != null ?
                intent.getIntExtra(Constants.BUNDLE_SIGN_TYPE, Constants.TYPE_PROHIBITION_SIGN) :
                Constants.TYPE_PROHIBITION_SIGN;
    }
}
