package com.essential.indodriving.base;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.essential.indodriving.R;

import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public abstract class MyBaseFragment extends BaseFragment {

    public final static int BUTTON_RESULT_ID = 1, BUTTON_TUTORIAL_ID=2;

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
                if (enableButtonBack()) {
                    onBackPressed();
                } else if (enableButtonClear()) {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.buttonResult:
                onMenuItemClick(BUTTON_RESULT_ID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main_menu, menu);
//        menu.findItem(R.id.buttonResult).setVisible(enableButtonResult());
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    protected void onMenuItemClick(int id) {
        // do something here
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

    protected abstract String getTitle();

    public MyBaseActivity getMyBaseActivity() {
        return (MyBaseActivity) getBaseActivity();
    }
}
