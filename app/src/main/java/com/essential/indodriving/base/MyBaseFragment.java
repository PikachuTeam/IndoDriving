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

    public final static int BUTTON_RESULT_ID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyBaseActivity().getSupportActionBar().setTitle(getTitle());
        getMyBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(enableIndicator());
        if (enableIndicator()) {
            getMyBaseActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.buttonResult:
                onMenuItemClick(BUTTON_RESULT_ID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.buttonResult).setVisible(enableButtonResult());
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void onMenuItemClick(int id) {
        // do something here
    }

    protected boolean enableIndicator() {
        return true;
    }

    protected boolean enableButtonResult() {
        return false;
    }

    protected abstract String getTitle();

    public MyBaseActivity getMyBaseActivity() {
        return (MyBaseActivity) getBaseActivity();
    }
}
