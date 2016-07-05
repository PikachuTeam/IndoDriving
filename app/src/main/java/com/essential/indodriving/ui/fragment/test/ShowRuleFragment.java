package com.essential.indodriving.ui.fragment.test;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.ui.activity.ChooseSimActivity;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;

import java.text.MessageFormat;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ShowRuleFragment extends MyBaseFragment implements View.OnClickListener {

    public final static String TAG_SHOW_RULE_FRAGMENT = "Show Rule Fragment";
    private AppCompatCheckBox checkBoxShowRule;
    private TextView buttonStart;
    private TextView textViewRule;
    private LinearLayout checkBoxContainer;
    private int mType;
    private boolean mChecked;
    private int mExamId;
    private boolean mIsRandom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        mChecked = false;
    }

    @Override
    protected String getTitle() {
        if (mIsRandom) {
            return getString(R.string.title_test);
        } else {
            return MessageFormat.format(getString(R.string.title_package), "" + mExamId);
        }
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_show_rule;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);

        switch (mType) {
            case DrivingDataSource.TYPE_SIM_A:
                textViewRule.setText(getString(R.string.test_rule_sim_a));
                break;
            case DrivingDataSource.TYPE_SIM_A_UMUM:
                textViewRule.setText(getString(R.string.test_rule_sim_a_umum));
                break;
            case DrivingDataSource.TYPE_SIM_B1:
                textViewRule.setText(getString(R.string.test_rule_sim_b1));
                break;
            case DrivingDataSource.TYPE_SIM_B1_UMUM:
                textViewRule.setText(getString(R.string.test_rule_sim_b1_umum));
                break;
            case DrivingDataSource.TYPE_SIM_B2:
                textViewRule.setText(getString(R.string.test_rule_sim_b2));
                break;
            case DrivingDataSource.TYPE_SIM_B2_UMUM:
                textViewRule.setText(getString(R.string.test_rule_sim_b2_umum));
                break;
            case DrivingDataSource.TYPE_SIM_C:
                textViewRule.setText(getString(R.string.test_rule_sim_c));
                break;
            case DrivingDataSource.TYPE_SIM_D:
                textViewRule.setText(getString(R.string.test_rule_sim_d));
                break;
        }
    }

    private void findViews(View rootView) {
        checkBoxShowRule = (AppCompatCheckBox) rootView.findViewById(R.id.checkBoxShowRule);
        buttonStart = (TextView) rootView.findViewById(R.id.buttonStart);
        textViewRule = (TextView) rootView.findViewById(R.id.textViewRule);
        checkBoxContainer = (LinearLayout) rootView.findViewById(R.id.checkBoxContainer);
        setFont(HomeActivity.defaultFont, rootView);
        buttonStart.setOnClickListener(this);
        checkBoxShowRule.setOnClickListener(this);
        checkBoxContainer.setOnClickListener(this);
    }

    private void saveState() {
        MySetting.getInstance().saveStateShowRuleAgain(mType, mChecked);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveState();
    }

    private void getData() {
        Bundle bundle = getArguments();
        mType = bundle.getInt(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_A);
        mExamId = bundle.getInt(Constants.BUNDLE_EXAM_ID, 1);
        mIsRandom = bundle.getBoolean(DoTestFragment.BUNDLE_IS_RANDOM, false);
    }

    private void setFont(Typeface font, View rootView) {
        textViewRule.setTypeface(font);
        buttonStart.setTypeface(font);
        ((TextView) rootView.findViewById(R.id.textViewNotShowAgain)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.textViewTitleTestRule)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.textViewTitleTestRule)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonStart) {
            saveState();
            DoTestFragment fragment = new DoTestFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.BUNDLE_TYPE, mType);
            bundle.putInt(Constants.BUNDLE_EXAM_ID, mExamId);
            bundle.putBoolean(DoTestFragment.BUNDLE_IS_RANDOM, mIsRandom);
            fragment.setArguments(bundle);
            replaceFragment(fragment, TAG_SHOW_RULE_FRAGMENT);
        } else if (v == checkBoxShowRule) {
            if (mChecked) {
                mChecked = false;
                checkBoxShowRule.setChecked(mChecked);
            } else {
                mChecked = true;
                checkBoxShowRule.setChecked(mChecked);
            }
        } else if (v == checkBoxContainer) {
            if (mChecked) {
                mChecked = false;
                checkBoxShowRule.setChecked(mChecked);
            } else {
                mChecked = true;
                checkBoxShowRule.setChecked(mChecked);
            }
        }
    }
}
