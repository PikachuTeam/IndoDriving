package com.essential.indodriving.ui.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.SignDataSource;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.SecondBaseActivity;
import com.essential.indodriving.ui.fragment.sign.SignChooseItemFragment;

import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by yue on 07/07/2016.
 */
public class SignMainActivity extends SecondBaseActivity {

    private String type;
    private View actionLearningSignByCard;
    private View actionLearningSignByList;

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();
        getData();
        findViews();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        SignChooseItemFragment fragment = new SignChooseItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_SIGN_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main_sign;
    }

    public View getActionLearningSignByCard(){
        return actionLearningSignByCard;
    }

    public View getActionLearningSignByList(){
        return actionLearningSignByList;
    }

    private void getData() {
        Intent intent = getIntent();
        type = intent != null ?
                intent.getStringExtra(Constants.BUNDLE_SIGN_TYPE) :
                SignDataSource.GROUP_PROHIBITION_SIGN;
    }

    private void findViews() {
        actionLearningSignByCard = findViewById(R.id.action_learning_sign_by_card);
        actionLearningSignByList = findViewById(R.id.action_learning_sign_by_list);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_enter, R.anim.activity_slide_right_exit);
    }
}
