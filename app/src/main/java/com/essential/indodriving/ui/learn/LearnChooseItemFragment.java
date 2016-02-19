package com.essential.indodriving.ui.learn;

import android.os.Bundle;
import android.view.View;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class LearnChooseItemFragment extends MyBaseFragment {
    @Override
    protected String getTitle() {
        return getString(R.string.title_learn_choose_item);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_choose_item;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {

    }
}
