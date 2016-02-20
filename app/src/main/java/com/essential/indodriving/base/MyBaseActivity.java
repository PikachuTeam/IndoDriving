package com.essential.indodriving.base;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.essential.indodriving.R;

import tatteam.com.app_common.ui.activity.BaseActivity;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public abstract class MyBaseActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateContentView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public Toolbar getToolbar(){
        return toolbar;
    }
}
