package com.essential.indodriving.ui.fragment.sign;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.SignDataSource;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.activity.SignMainActivity;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.fragment.sign.learn.LearnSignByListFragment;
import com.essential.indodriving.ui.fragment.sign.test.SignDoTestFragment;
import com.essential.indodriving.ui.fragment.sign.test.SignUnlimitedTestFragment;

/**
 * Created by yue on 07/07/2016.
 */
public class SignChooseItemFragment extends MyBaseFragment implements View.OnClickListener {

    public final static String TAG_SIGN_CHOOSE_ITEM_FRAGMENT = "Fragment Sign Choose Item";
    private String type;

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        getData();
        setListeners(rootView);
        setFonts(rootView);
        ((SignMainActivity) getMyBaseActivity()).getActionLearningSignByCard().setVisibility(View.GONE);
        ((SignMainActivity) getMyBaseActivity()).getActionLearningSignByList().setVisibility(View.GONE);
    }

    @Override
    protected String getTitle() {
        return type;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_sign_choose_item;
    }

    @Override
    public void onClick(View v) {
        MyBaseFragment fragment=null;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_SIGN_TYPE, type);
        switch (v.getId()) {
            case R.id.linear_learn_sign:
            case R.id.button_learn_sign:
                fragment = new LearnSignByListFragment();
                break;
            case R.id.linear_unlimited_test:
            case R.id.button_unlimited_test:
                fragment=new SignUnlimitedTestFragment();
                break;
            case R.id.linear_simulation_test:
            case R.id.button_simulation_test:
                fragment=new SignDoTestFragment();
                break;
            case R.id.linear_matching:
            case R.id.button_matching:
                break;
        }
        if(fragment!=null){
            fragment.setArguments(bundle);
            replaceFragment(fragment, TAG_SIGN_CHOOSE_ITEM_FRAGMENT);
        }
    }

    @Override
    protected boolean enableButtonShare() {
        return true;
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle != null ?
                bundle.getString(Constants.BUNDLE_SIGN_TYPE) : SignDataSource.GROUP_PROHIBITION_SIGN;
    }

    private void setListeners(View rootView) {
        rootView.findViewById(R.id.linear_learn_sign).setOnClickListener(this);
        rootView.findViewById(R.id.button_learn_sign).setOnClickListener(this);
        rootView.findViewById(R.id.linear_unlimited_test).setOnClickListener(this);
        rootView.findViewById(R.id.button_unlimited_test).setOnClickListener(this);
        rootView.findViewById(R.id.linear_simulation_test).setOnClickListener(this);
        rootView.findViewById(R.id.button_simulation_test).setOnClickListener(this);
        rootView.findViewById(R.id.linear_matching).setOnClickListener(this);
        rootView.findViewById(R.id.button_matching).setOnClickListener(this);
    }

    private void setFonts(View rootView) {
        ((TextView) rootView.findViewById(R.id.text_learn_sign)).
                setTypeface(HomeActivity.defaultFont);
        ((TextView) rootView.findViewById(R.id.text_unlimited_test)).
                setTypeface(HomeActivity.defaultFont);
        ((TextView) rootView.findViewById(R.id.text_simulation_test)).
                setTypeface(HomeActivity.defaultFont);
        ((TextView) rootView.findViewById(R.id.text_matching)).
                setTypeface(HomeActivity.defaultFont);
    }
}
