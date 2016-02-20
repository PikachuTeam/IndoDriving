package com.essential.indodriving.ui.learn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class LearnChooseSimFragment extends MyBaseFragment implements View.OnClickListener {

    private Button buttonSimA;
    private Button buttonSimAUmum;
    private Button buttonSimB1;
    private Button buttonSimB1Umum;
    private Button buttonSimB2;
    private Button buttonSimB2Umum;
    private Button buttonSimC;
    private Button buttonSimD;

    public final static String LEARN_CHOOSE_SIM_FRAGMENT_TAG = "Learn Choose Sim";
    public final static int TYPE_SIM_A = 1, TYPE_SIM_A_UMUM = 2, TYPE_SIM_B1 = 3, TYPE_SIM_B1_UMUM = 4, TYPE_SIM_B2 = 5, TYPE_SIM_B2_UMUM = 6, TYPE_SIM_C = 7, TYPE_SIM_D = 8;

    private void findViews(View rootView) {
        buttonSimA = (Button) rootView.findViewById(R.id.buttonSimA);
        buttonSimAUmum = (Button) rootView.findViewById(R.id.buttonSimAUmum);
        buttonSimB1 = (Button) rootView.findViewById(R.id.buttonSimB1);
        buttonSimB1Umum = (Button) rootView.findViewById(R.id.buttonSimB1Umum);
        buttonSimB2 = (Button) rootView.findViewById(R.id.buttonSimB2);
        buttonSimB2Umum = (Button) rootView.findViewById(R.id.buttonSimB2Umum);
        buttonSimC = (Button) rootView.findViewById(R.id.buttonSimC);
        buttonSimD = (Button) rootView.findViewById(R.id.buttonSimD);

        buttonSimA.setOnClickListener(this);
        buttonSimAUmum.setOnClickListener(this);
        buttonSimB1.setOnClickListener(this);
        buttonSimB1Umum.setOnClickListener(this);
        buttonSimB2.setOnClickListener(this);
        buttonSimB2Umum.setOnClickListener(this);
        buttonSimC.setOnClickListener(this);
        buttonSimD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LearnChooseItemFragment learnChooseItemFragment = new LearnChooseItemFragment();
        Bundle bundle = new Bundle();
        if (v == buttonSimA) {
            bundle.putInt("Type", TYPE_SIM_A);
        } else if (v == buttonSimAUmum) {
            bundle.putInt("Type", TYPE_SIM_A_UMUM);
        } else if (v == buttonSimB1) {
            bundle.putInt("Type", TYPE_SIM_B1);
        } else if (v == buttonSimB1Umum) {
            bundle.putInt("Type", TYPE_SIM_B1_UMUM);
        } else if (v == buttonSimB2) {
            bundle.putInt("Type", TYPE_SIM_B2);
        } else if (v == buttonSimB2Umum) {
            bundle.putInt("Type", TYPE_SIM_B2_UMUM);
        } else if (v == buttonSimC) {
            bundle.putInt("Type", TYPE_SIM_C);
        } else if (v == buttonSimD) {
            bundle.putInt("Type", TYPE_SIM_D);
        }
        learnChooseItemFragment.setArguments(bundle);
        replaceFragment(learnChooseItemFragment, LEARN_CHOOSE_SIM_FRAGMENT_TAG);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_learn_choose_sim);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_choose_sim;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
    }
}
