package com.essential.indodriving.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.ui.activity.ChooseSimActivity;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.fragment.learn.LearnAllFragment;
import com.essential.indodriving.ui.fragment.test.ListQuestionFragment;
import com.essential.indodriving.ui.fragment.test.UnlimitedTestFragment;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ChooseItemFragment extends MyBaseFragment {

    public final static String TAG_CHOOSE_ITEM_FRAGMENT = "Choose Item Fragment";
    private int mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected String getTitle() {
        switch (mType) {
            case DrivingDataSource.TYPE_SIM_A_UMUM:
                return getString(R.string.sim_a_umum);
            case DrivingDataSource.TYPE_SIM_B1:
                return getString(R.string.sim_b1);
            case DrivingDataSource.TYPE_SIM_B1_UMUM:
                return getString(R.string.sim_b1_umum);
            case DrivingDataSource.TYPE_SIM_B2:
                return getString(R.string.sim_b2);
            case DrivingDataSource.TYPE_SIM_B2_UMUM:
                return getString(R.string.sim_b2_umum);
            case DrivingDataSource.TYPE_SIM_C:
                return getString(R.string.sim_c);
            case DrivingDataSource.TYPE_SIM_D:
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
        rootView.findViewById(R.id.linear_learn_theory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLearnFragment();
            }
        });
        rootView.findViewById(R.id.button_learn_theory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLearnFragment();
            }
        });
        rootView.findViewById(R.id.linear_simulation_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToChooseTestFragment();
            }
        });
        rootView.findViewById(R.id.button_simulation_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToChooseTestFragment();
            }
        });
        rootView.findViewById(R.id.linear_unlimited_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToUnlimitedTestFragment();
            }
        });
        rootView.findViewById(R.id.button_unlimited_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToUnlimitedTestFragment();
            }
        });
        setFont(rootView, HomeActivity.defaultFont);
    }

    @Override
    protected boolean enableButtonTutorial() {
        switch (mType) {
            case DrivingDataSource.TYPE_SIM_A:
            case DrivingDataSource.TYPE_SIM_C:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onMenuItemClick(int id) {
        String url = "";
        switch (mType) {
            case DrivingDataSource.TYPE_SIM_A:
                url = "https://www.youtube.com/results?search_query=Ujian+Praktek+SIM+A";
                break;
            case DrivingDataSource.TYPE_SIM_C:
                url = "https://www.youtube.com/results?search_query=Ujian+Praktek+SIM+C";
                break;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void moveToLearnFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, mType);
        LearnAllFragment learnAllFragment = new LearnAllFragment();
        learnAllFragment.setArguments(bundle);
        replaceFragment(learnAllFragment, TAG_CHOOSE_ITEM_FRAGMENT);

        sendItemChosenLog(getTitle(), "LEARNING CARD");
    }

    private void moveToChooseTestFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, mType);
        ListQuestionFragment fragment = new ListQuestionFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_CHOOSE_ITEM_FRAGMENT);

        sendItemChosenLog(getTitle(), "WRITTEN TEST");
    }

    private void moveToUnlimitedTestFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, mType);
        UnlimitedTestFragment fragment = new UnlimitedTestFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_CHOOSE_ITEM_FRAGMENT);

        sendItemChosenLog(getTitle(), "SIMULATION");
    }

    private void getData() {
        Bundle bundle = getArguments();
        mType = bundle.getInt(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_A);
    }

    private void setFont(View rootView, Typeface font) {
        ((TextView) rootView.findViewById(R.id.text_learn_theory)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.text_simulation_test)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.text_unlimited_test)).setTypeface(font);
    }
}
