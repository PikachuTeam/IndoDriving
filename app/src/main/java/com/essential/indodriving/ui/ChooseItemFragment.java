package com.essential.indodriving.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.Constants;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.ui.learn.LearnAllFragment;
import com.essential.indodriving.ui.test.ListQuestionFragment;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ChooseItemFragment extends MyBaseFragment {

    public final static String TAG_CHOOSE_ITEM_FRAGMENT = "Choose Item Fragment";
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected boolean enableButtonShare() {
        return true;
    }

    @Override
    protected String getTitle() {
        switch (type) {
            case DataSource.TYPE_SIM_A_UMUM:
                return getString(R.string.sim_a_umum);
            case DataSource.TYPE_SIM_B1:
                return getString(R.string.sim_b1);
            case DataSource.TYPE_SIM_B1_UMUM:
                return getString(R.string.sim_b1_umum);
            case DataSource.TYPE_SIM_B2:
                return getString(R.string.sim_b2);
            case DataSource.TYPE_SIM_B2_UMUM:
                return getString(R.string.sim_b2_umum);
            case DataSource.TYPE_SIM_C:
                return getString(R.string.sim_c);
            case DataSource.TYPE_SIM_D:
                return getString(R.string.sim_d);
            default:
                return getString(R.string.sim_a);
        }
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_choose_item;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        rootView.findViewById(R.id.buttonLearn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLearnFragment();
            }
        });
        rootView.findViewById(R.id.buttonTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTestFragment();
            }
        });
        rootView.findViewById(R.id.buttonLearnContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLearnFragment();
            }
        });
        rootView.findViewById(R.id.buttonTestContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTestFragment();
            }
        });
        setFont(rootView, HomeActivity.defaultFont);
    }

    private void moveToLearnFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, type);
        LearnAllFragment learnAllFragment = new LearnAllFragment();
        learnAllFragment.setArguments(bundle);
        replaceFragment(learnAllFragment, TAG_CHOOSE_ITEM_FRAGMENT);
    }

    private void moveToTestFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, type);
        ListQuestionFragment fragment = new ListQuestionFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_CHOOSE_ITEM_FRAGMENT);
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_A);
    }

    private void setFont(View rootView, Typeface font) {
        ((TextView) rootView.findViewById(R.id.textViewLearn)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.textViewTest)).setTypeface(font);
    }
}
