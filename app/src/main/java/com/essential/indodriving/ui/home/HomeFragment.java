package com.essential.indodriving.ui.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.ui.learn.LearnByCardFragment;

import java.util.ArrayList;

import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by dongc_000 on 2/23/2016.
 */
public class HomeFragment extends MyBaseFragment implements View.OnClickListener {

    private RelativeLayout buttonLearnSimA;
    private RelativeLayout buttonLearnSimAUmum;
    private RelativeLayout buttonLearnSimB1;
    private RelativeLayout buttonLearnSimB1Umum;
    private RelativeLayout buttonLearnSimB2;
    private RelativeLayout buttonLearnSimB2Umum;
    private RelativeLayout buttonLearnSimC;
    private RelativeLayout buttonLearnSimD;

    public final static String CHOOSE_SIM_FRAGMENT_TAG = "Choose Sim";

    @Override
    protected String getTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
    }

    @Override
    protected boolean enableIndicator() {
        return false;
    }


    private void findViews(View rootView) {
        buttonLearnSimA = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimA);
        buttonLearnSimAUmum = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimAUmum);
        buttonLearnSimB1 = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimB1);
        buttonLearnSimB1Umum = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimB1Umum);
        buttonLearnSimB2 = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimB2);
        buttonLearnSimB2Umum = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimB2Umum);
        buttonLearnSimC = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimC);
        buttonLearnSimD = (RelativeLayout) rootView.findViewById(R.id.buttonLearnSimD);

        buttonLearnSimA.setOnClickListener(this);
        buttonLearnSimAUmum.setOnClickListener(this);
        buttonLearnSimB1.setOnClickListener(this);
        buttonLearnSimB1Umum.setOnClickListener(this);
        buttonLearnSimB2.setOnClickListener(this);
        buttonLearnSimB2Umum.setOnClickListener(this);
        buttonLearnSimC.setOnClickListener(this);
        buttonLearnSimD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLearnSimA) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_A);
            ChooseItemFragment fragment = new ChooseItemFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        } else if (v == buttonLearnSimAUmum) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_A_UMUM);
            LearnByCardFragment fragment = new LearnByCardFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        } else if (v == buttonLearnSimB1) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_B1);
            LearnByCardFragment fragment = new LearnByCardFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        } else if (v == buttonLearnSimB1Umum) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_B1_UMUM);
            LearnByCardFragment fragment = new LearnByCardFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        } else if (v == buttonLearnSimB2) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_B2);
            LearnByCardFragment fragment = new LearnByCardFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        } else if (v == buttonLearnSimB2Umum) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_B2_UMUM);
            LearnByCardFragment fragment = new LearnByCardFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        } else if (v == buttonLearnSimC) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_C);
            ChooseItemFragment fragment = new ChooseItemFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        } else if (v == buttonLearnSimD) {
            Bundle bundle = new Bundle();
            bundle.putInt("Type", DataSource.TYPE_SIM_D);
            LearnByCardFragment fragment = new LearnByCardFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, CHOOSE_SIM_FRAGMENT_TAG);
        }
    }
}
