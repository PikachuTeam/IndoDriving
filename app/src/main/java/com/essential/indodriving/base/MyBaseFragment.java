package com.essential.indodriving.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.essential.indodriving.R;

import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public abstract class MyBaseFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyBaseActivity().getSupportActionBar().setTitle(getTitle());
        getMyBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (enableIndicator()) {
            getMyBaseActivity().getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getActivity(), "Aloha", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean enableIndicator() {
        return true;
    }

    protected abstract String getTitle();

    public MyBaseActivity getMyBaseActivity() {
        return (MyBaseActivity) getBaseActivity();
    }
}
