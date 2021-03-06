package com.essential.indodriving.ui.base;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;

import tatteam.com.app_common.ui.fragment.BaseFragment;
import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public abstract class MyBaseFragment extends BaseFragment {

    public final static int BUTTON_RESULT = 1, BUTTON_TUTORIAL = 2, BUTTON_MODIFY_ANSWER = 3;

    protected boolean isRated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyBaseActivity().getToolbar().setVisibility(View.VISIBLE);
        getMyBaseActivity().setToolbarTitle(getTitle());
        getMyBaseActivity().enableButtonResult(enableButtonResult());
        getMyBaseActivity().enableButtonTutorial(enableButtonTutorial());
        getMyBaseActivity().enableButtonShare(enableButtonShare());
        getMyBaseActivity().enableButtonModifyAnswer(enableButtonModifyAnswer());
        getMyBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(enableIndicator());
        if (enableButtonBack()) {
            getMyBaseActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
        if (enableButtonClear()) {
            getMyBaseActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (enableButtonBack()) onBackPressed();
                else if (enableButtonClear()) getFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sharingEvent() {
        String androidLink = "https://play.google.com/store/apps/details?id=" +
                getActivity().getPackageName();
        String sharedText = getString(R.string.app_name) + ".\nAndroid: " + androidLink;
        CommonUtil.sharePlainText(getActivity(), sharedText);
    }

    public void shareFacebook() {
        getMyBaseActivity().shareFacebook();
    }

    public void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getActivity().getPackageName())));
        }
        isRated = true;
        MySetting.getInstance().setRated();
    }

    public SecondBaseActivity getMyBaseActivity() {
        return (SecondBaseActivity) getBaseActivity();
    }

    public TextView getButtonModifyAnswer() {
        return getMyBaseActivity().getButtonModifyAnswer();
    }

    protected void onMenuItemClick(int id) {
        // do something here
    }

    protected boolean enableButtonShare() {
        return false;
    }

    protected boolean enableIndicator() {
        return true;
    }

    protected boolean enableButtonResult() {
        return false;
    }

    protected boolean enableButtonClear() {
        return false;
    }

    protected boolean enableButtonBack() {
        return true;
    }

    protected boolean enableButtonTutorial() {
        return false;
    }

    protected boolean enableButtonModifyAnswer() {
        return false;
    }

    protected abstract String getTitle();

    public void refreshUI() {
    }

    public void sendItemChosenLog(String itemCategory, String itemName) {
        getMyBaseActivity().sendItemChosenLog(itemCategory, itemName);
    }
}
