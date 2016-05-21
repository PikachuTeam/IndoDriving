package com.essential.indodriving.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.R;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseActivity;
import com.essential.indodriving.data.DataSource;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.util.CloseAppHandler;
import tatteam.com.app_common.util.CommonUtil;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, CloseAppHandler.OnCloseAppListener {

    public final static String PREF_IS_PRO_VERSION = "is_pro_version";
    public final static String PACKAGE_NAME_FREE_VER = "com.essential.usdriving.free";
    public final static String PACKAGE_NAME_PRO_VER = "com.essential.usdriving.pro";
    public static Typeface defaultFont;
    private LinearLayout buttonLearnSimA;
    private LinearLayout buttonLearnSimAUmum;
    private LinearLayout buttonLearnSimB1;
    private LinearLayout buttonLearnSimB1Umum;
    private LinearLayout buttonLearnSimB2;
    private LinearLayout buttonLearnSimB2Umum;
    private LinearLayout buttonLearnSimC;
    private LinearLayout buttonLearnSimD;
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton mFabRateUs;
    private FloatingActionButton mFabMoreApps;
    private FloatingActionButton mFabShare;
    private View mOverlayView;
    private CoordinatorLayout coordinatorLayout;
    private ImageView banner;
    private CollapsingToolbarLayout toolbar_layout;
    private CloseAppHandler closeAppHandler;
    private int number;
    private boolean isProVersion;
    private View.OnClickListener mFloatingMenuItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mFabMoreApps) {
                AppCommon.getInstance().openMoreAppDialog(HomeActivity.this);
            } else if (v == mFabRateUs) {
                Uri uri = Uri.parse("market://details?id=" + PACKAGE_NAME_FREE_VER);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + PACKAGE_NAME_FREE_VER)));
                }
                SharedPreferences.Editor editor = getSharedPreferences(
                        Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(Constants.PREF_IS_RATE_APP, true);
                editor.commit();
            } else if (v == mFabShare) {
                sharingEvent();
            }
        }
    };
    private FloatingActionsMenu.OnFloatingActionsMenuUpdateListener mOnFloatingActionsMenuUpdateListener =
            new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
        @Override
        public void onMenuExpanded() {
            mOverlayView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onMenuCollapsed() {
            mOverlayView.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();
        toolbar_layout.setExpandedTitleTextAppearance(R.style.ExpandedCollapsingToolbar);
        defaultFont = Typeface.createFromAsset(getAssets(), "fonts/Menu Sim.ttf");
        setFont(defaultFont);
        closeAppHandler = new CloseAppHandler(this, false);
        closeAppHandler.setListener(this);
        loadState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        number = 0;
    }

    @Override
    public void onBackPressed() {
        if (mFloatingActionsMenu.isExpanded()) {
            mFloatingActionsMenu.collapse();
        } else {
            closeAppHandler.setKeyBackPress(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        if (v == buttonLearnSimA) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_A);
        } else if (v == buttonLearnSimAUmum) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_A_UMUM);
        } else if (v == buttonLearnSimB1) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_B1);
        } else if (v == buttonLearnSimB1Umum) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_B1_UMUM);
        } else if (v == buttonLearnSimB2) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_B2);
        } else if (v == buttonLearnSimB2Umum) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_B2_UMUM);
        } else if (v == buttonLearnSimC) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_C);
        } else if (v == buttonLearnSimD) {
            intent.putExtra(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_D);
        }
        MyBaseActivity.startActivityAnimation(this, intent);
    }

    @Override
    public void onRateAppDialogClose() {
        finish();
    }

    @Override
    public void onTryToCloseApp() {
        Snackbar.make(coordinatorLayout, getResources().
                getText(R.string.press_again_to_exit), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onReallyWantToCloseApp() {
        finish();
    }

    private void findViews() {
        buttonLearnSimA = (LinearLayout) findViewById(R.id.buttonLearnSimA);
        buttonLearnSimAUmum = (LinearLayout) findViewById(R.id.buttonLearnSimAUmum);
        buttonLearnSimB1 = (LinearLayout) findViewById(R.id.buttonLearnSimB1);
        buttonLearnSimB1Umum = (LinearLayout) findViewById(R.id.buttonLearnSimB1Umum);
        buttonLearnSimB2 = (LinearLayout) findViewById(R.id.buttonLearnSimB2);
        buttonLearnSimB2Umum = (LinearLayout) findViewById(R.id.buttonLearnSimB2Umum);
        buttonLearnSimC = (LinearLayout) findViewById(R.id.buttonLearnSimC);
        buttonLearnSimD = (LinearLayout) findViewById(R.id.buttonLearnSimD);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        banner = (ImageView) findViewById(R.id.banner);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floating_actions_menu);
        mFabMoreApps = (FloatingActionButton) findViewById(R.id.fab_more_apps);
        mFabRateUs = (FloatingActionButton) findViewById(R.id.fab_rate_us);
        mFabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        mOverlayView = findViewById(R.id.view_overlay);
        buttonLearnSimA.setOnClickListener(this);
        buttonLearnSimAUmum.setOnClickListener(this);
        buttonLearnSimB1.setOnClickListener(this);
        buttonLearnSimB1Umum.setOnClickListener(this);
        buttonLearnSimB2.setOnClickListener(this);
        buttonLearnSimB2Umum.setOnClickListener(this);
        buttonLearnSimC.setOnClickListener(this);
        buttonLearnSimD.setOnClickListener(this);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isProVersion) {
                    number++;
                    if (number == Constants.PRESSING_TIMES) {
                        isProVersion = true;
                        Snackbar.make(coordinatorLayout, getString(R.string.hacked), Snackbar.LENGTH_SHORT).show();
                        saveState();
                    }
                }
            }
        });
        mOverlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFloatingActionsMenu.isExpanded()) {
                    mOverlayView.setVisibility(View.GONE);
                    mFloatingActionsMenu.collapse();
                }
            }
        });
        mFloatingActionsMenu.setOnFloatingActionsMenuUpdateListener(mOnFloatingActionsMenuUpdateListener);
        mFabMoreApps.setOnClickListener(mFloatingMenuItemClickListener);
        mFabShare.setOnClickListener(mFloatingMenuItemClickListener);
        mFabRateUs.setOnClickListener(mFloatingMenuItemClickListener);
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_IS_PRO_VERSION, isProVersion);
        editor.commit();
    }

    private void loadState() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        isProVersion = sharedPreferences.getBoolean(PREF_IS_PRO_VERSION, BuildConfig.IS_PRO_VERSION);
    }

    private void setFont(Typeface font) {
        ((TextView) findViewById(R.id.textViewSimA)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimAUmum)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB1)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB1Umum)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB2)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimB2Umum)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimC)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewSimD)).setTypeface(font);
    }

    private void sharingEvent() {
        String androidLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String sharedText = getString(R.string.app_name) + ".\nAndroid: " + androidLink;
        CommonUtil.sharePlainText(this, sharedText);
    }
}
