package com.essential.indodriving.ui.test;

import android.os.Bundle;
import android.view.View;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class TestChooseSimFragment extends MyBaseFragment {
    @Override
    protected String getTitle() {
        return "Ahihi";
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_test_choose_sim;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {

    }
}
