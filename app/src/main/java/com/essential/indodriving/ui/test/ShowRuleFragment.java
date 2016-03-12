package com.essential.indodriving.ui.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.ui.HomeActivity;

import java.text.MessageFormat;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ShowRuleFragment extends MyBaseFragment implements View.OnClickListener {

    private AppCompatCheckBox checkBoxShowRule;
    private TextView buttonStart;
    private TextView textViewRule;
    private LinearLayout checkBoxContainer;

    private int type;
    private boolean checked;
    private int examId;
    private boolean isRandom;

    public final static String SHOW_RULE_FRAGMENT_TAG = "Show Rule Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

        checked = false;
    }

    @Override
    protected String getTitle() {
        if (isRandom) {
            return getString(R.string.title_test);
        } else {
            return MessageFormat.format(getString(R.string.title_package), "" + examId);
        }
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_show_rule;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);

        switch (type) {
            case DataSource.TYPE_SIM_A:
                textViewRule.setText(getString(R.string.test_rule_sim_a));
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                textViewRule.setText(getString(R.string.test_rule_sim_a_umum));
                break;
            case DataSource.TYPE_SIM_B1:
                textViewRule.setText(getString(R.string.test_rule_sim_b1));
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                textViewRule.setText(getString(R.string.test_rule_sim_b1_umum));
                break;
            case DataSource.TYPE_SIM_B2:
                textViewRule.setText(getString(R.string.test_rule_sim_b2));
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                textViewRule.setText(getString(R.string.test_rule_sim_b2_umum));
                break;
            case DataSource.TYPE_SIM_C:
                textViewRule.setText(getString(R.string.test_rule_sim_c));
                break;
            case DataSource.TYPE_SIM_D:
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case DataSource.TYPE_SIM_A:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_A, !checked);
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_A_UMUM, !checked);
                break;
            case DataSource.TYPE_SIM_B1:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_B1, !checked);
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_B1_UMUM, !checked);
                break;
            case DataSource.TYPE_SIM_B2:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_B2, !checked);
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_B2_UMUM, !checked);
                break;
            case DataSource.TYPE_SIM_C:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_C, !checked);
                break;
            case DataSource.TYPE_SIM_D:
                editor.putBoolean(ListQuestionFragment.PREF_SHOW_RULE_AGAIN_SIM_D, !checked);
                break;
        }
        editor.commit();
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
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
        examId = bundle.getInt("Exam Id", 1);
        isRandom = bundle.getBoolean("Random", false);
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
            bundle.putInt("Type", type);
            bundle.putInt("Exam Id", examId);
            bundle.putBoolean("Random", isRandom);
            fragment.setArguments(bundle);
            replaceFragment(fragment, SHOW_RULE_FRAGMENT_TAG);
        } else if (v == checkBoxShowRule) {
            if (checked) {
                checked = false;
                checkBoxShowRule.setChecked(checked);
            } else {
                checked = true;
                checkBoxShowRule.setChecked(checked);
            }
        } else if (v == checkBoxContainer) {
            if (checked) {
                checked = false;
                checkBoxShowRule.setChecked(checked);
            } else {
                checked = true;
                checkBoxShowRule.setChecked(checked);
            }
        }
    }
}
