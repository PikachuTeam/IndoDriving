package com.essential.indodriving.ui.fragment.sign;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;

/**
 * Created by yue on 07/07/2016.
 */
public class SignChooseItemFragment extends MyBaseFragment implements View.OnClickListener {

    private int type;

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        getData();
        setListeners(rootView);
        setFonts(rootView);
    }

    @Override
    protected String getTitle() {
        switch (type) {
            case Constants.TYPE_PROHIBITION_SIGN:
                return getString(R.string.prohibition_sign);
            case Constants.TYPE_WARNING_SIGN:
                return getString(R.string.warning_sign);
            case Constants.TYPE_DIRECTION_SIGN:
                return getString(R.string.direction_sign);
            case Constants.TYPE_ADDITIONAL_SIGN:
                return getString(R.string.addition_sign);
            case Constants.TYPE_ALL_SIGN:
                return getString(R.string.all_sign);
            case Constants.TYPE_COMMAND_SIGN:
                return getString(R.string.command_sign);
            default:
                return getString(R.string.prohibition_sign);
        }
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_sign_choose_item;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_learn_sign:
            case R.id.button_learn_sign:
                break;
            case R.id.linear_unlimited_test:
            case R.id.button_unlimited_test:
                break;
            case R.id.linear_simulation_test:
            case R.id.button_simulation_test:
                break;
            case R.id.linear_matching:
            case R.id.button_matching:
                break;
        }
    }

    @Override
    protected boolean enableButtonShare() {
        return true;
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle != null ?
                bundle.getInt(Constants.BUNDLE_SIGN_TYPE) : Constants.TYPE_PROHIBITION_SIGN;
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
