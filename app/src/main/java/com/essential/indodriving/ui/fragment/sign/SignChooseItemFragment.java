package com.essential.indodriving.ui.fragment.sign;

import android.os.Bundle;
import android.view.View;

import com.essential.indodriving.R;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;

/**
 * Created by yue on 07/07/2016.
 */
public class SignChooseItemFragment extends MyBaseFragment {

    private int type;

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        getData();
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

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle != null ?
                bundle.getInt(Constants.BUNDLE_SIGN_TYPE) : Constants.TYPE_PROHIBITION_SIGN;
    }
}
