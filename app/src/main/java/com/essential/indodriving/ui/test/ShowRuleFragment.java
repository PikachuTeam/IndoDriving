package com.essential.indodriving.ui.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ShowRuleFragment extends MyBaseFragment implements View.OnClickListener {

    private ImageView checkBoxShowRule;
    private Button buttonStart;
    private TextView textViewRule;

    private int type;
    private boolean checked;
    private int examId;
    private boolean isRandom;

    public final static String SHOW_RULE_FRAGMENT_TAG = "Show Rule Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_test);
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
        checkBoxShowRule = (ImageView) rootView.findViewById(R.id.checkBoxShowRule);
        buttonStart = (Button) rootView.findViewById(R.id.buttonStart);
        textViewRule = (TextView) rootView.findViewById(R.id.textViewRule);

        buttonStart.setOnClickListener(this);
        checkBoxShowRule.setOnClickListener(this);
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case DataSource.TYPE_SIM_A:
                editor.putBoolean("Show Rule Again A", !checked);
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                editor.putBoolean("Show Rule Again A Umum", !checked);
                break;
            case DataSource.TYPE_SIM_B1:
                editor.putBoolean("Show Rule Again B1", !checked);
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                editor.putBoolean("Show Rule Again B1 Umum", !checked);
                break;
            case DataSource.TYPE_SIM_B2:
                editor.putBoolean("Show Rule Again B2", !checked);
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                editor.putBoolean("Show Rule Again B2 Umum", !checked);
                break;
            case DataSource.TYPE_SIM_C:
                editor.putBoolean("Show Rule Again C", !checked);
                break;
            case DataSource.TYPE_SIM_D:
                editor.putBoolean("Show Rule Again D", !checked);
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
                checkBoxShowRule.setImageResource(R.drawable.ic_check_box_outline_blank);
            } else {
                checked = true;
                checkBoxShowRule.setImageResource(R.drawable.ic_check_box);
            }
        }
    }
}
