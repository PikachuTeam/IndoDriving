package com.essential.indodriving.ui;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseActivity;
import com.essential.indodriving.ui.learn.LearnChooseSimFragment;
import com.essential.indodriving.ui.test.TestChooseSimFragment;

import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class MainActivity extends MyBaseActivity {

    private int button;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        return button == HomeActivity.LEARN_BUTTON ? new LearnChooseSimFragment() : new TestChooseSimFragment();
    }

    @Override
    protected void onCreateContentView() {
        super.onCreateContentView();
        button = getIntent().getIntExtra("button", 1);
    }
}
