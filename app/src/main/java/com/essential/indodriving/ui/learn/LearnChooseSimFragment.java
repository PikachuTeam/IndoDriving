package com.essential.indodriving.ui.learn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class LearnChooseSimFragment extends MyBaseFragment implements View.OnClickListener{

    private Button buttonSimA;
    private Button buttonSimAUmum;
    private Button buttonSimB1;
    private Button buttonSimB1Umum;
    private Button buttonSimB2;
    private Button buttonSimB2Umum;
    private Button buttonSimC;
    private Button buttonSimD;
    public final static String LEARN_CHOOSE_SIM_FRAGMENT_TAG="Learn Choose Sim";

    private void findViews(View rootView) {
        buttonSimA = (Button)rootView.findViewById( R.id.buttonSimA );
        buttonSimAUmum = (Button)rootView.findViewById( R.id.buttonSimAUmum );
        buttonSimB1 = (Button)rootView.findViewById( R.id.buttonSimB1 );
        buttonSimB1Umum = (Button)rootView.findViewById( R.id.buttonSimB1Umum );
        buttonSimB2 = (Button)rootView.findViewById( R.id.buttonSimB2 );
        buttonSimB2Umum = (Button)rootView.findViewById( R.id.buttonSimB2Umum );
        buttonSimC = (Button)rootView.findViewById( R.id.buttonSimC );
        buttonSimD = (Button)rootView.findViewById( R.id.buttonSimD );

        buttonSimA.setOnClickListener( this );
        buttonSimAUmum.setOnClickListener( this );
        buttonSimB1.setOnClickListener( this );
        buttonSimB1Umum.setOnClickListener( this );
        buttonSimB2.setOnClickListener( this );
        buttonSimB2Umum.setOnClickListener( this );
        buttonSimC.setOnClickListener( this );
        buttonSimD.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == buttonSimA ) {
            replaceFragment(new LearnChooseItemFragment(),LEARN_CHOOSE_SIM_FRAGMENT_TAG);
        } else if ( v == buttonSimAUmum ) {
            // Handle clicks for buttonSimAUmum
        } else if ( v == buttonSimB1 ) {
            // Handle clicks for buttonSimB1
        } else if ( v == buttonSimB1Umum ) {
            // Handle clicks for buttonSimB1Umum
        } else if ( v == buttonSimB2 ) {
            // Handle clicks for buttonSimB2
        } else if ( v == buttonSimB2Umum ) {
            // Handle clicks for buttonSimB2Umum
        } else if ( v == buttonSimC ) {
            // Handle clicks for buttonSimC
        } else if ( v == buttonSimD ) {
            // Handle clicks for buttonSimD
        }
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
