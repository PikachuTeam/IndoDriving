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
    private LinearLayout checkBoxContainer;

    private int type;
    private boolean checked;
    private String examId;
    private boolean isRandom;

    public final static String SHOW_RULE_FRAGMENT_TAG = "Show Rule Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadState();
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

        if (checked) {
            textViewRule.setVisibility(View.GONE);
            checkBoxContainer.setVisibility(View.GONE);
        } else {
            textViewRule.setVisibility(View.VISIBLE);
            checkBoxContainer.setVisibility(View.VISIBLE);
        }
    }

    private void findViews(View rootView) {
        checkBoxContainer = (LinearLayout) rootView.findViewById(R.id.checkBoxContainer);
        checkBoxShowRule = (ImageView) rootView.findViewById(R.id.checkBoxShowRule);
        buttonStart = (Button) rootView.findViewById(R.id.buttonStart);
        textViewRule = (TextView) rootView.findViewById(R.id.textViewRule);

        buttonStart.setOnClickListener(this);
        checkBoxShowRule.setOnClickListener(this);
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Show Rule Again", checked);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveState();
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
        examId = bundle.getString("Exam Id", "1         ");
        isRandom = bundle.getBoolean("Random", false);
    }

    private void loadState() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        checked = sharedPreferences.getBoolean("Show Rule Again", false);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonStart) {
            saveState();
            DoTestFragment fragment = new DoTestFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("Type", type);
            bundle.putString("Exam Id", examId);
            bundle.putBoolean("Random", isRandom);
            fragment.setArguments(bundle);
            replaceFragment(fragment, SHOW_RULE_FRAGMENT_TAG);
        } else if (v == checkBoxShowRule) {
            if (checked) {
                checked = false;
                checkBoxShowRule.setImageResource(R.drawable.ic_check_box_outline_blank_black);
            } else {
                checked = true;
                checkBoxShowRule.setImageResource(R.drawable.ic_check_box_black);
            }
        }
    }
}
