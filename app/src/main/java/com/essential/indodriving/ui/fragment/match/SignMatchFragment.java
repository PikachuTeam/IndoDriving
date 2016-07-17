package com.essential.indodriving.ui.fragment.match;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.Sign;
import com.essential.indodriving.data.sign.SignDataSource;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.base.SecondBaseActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by admin on 7/11/2016.
 */
public class SignMatchFragment extends MyBaseFragment implements SignDefinitionAdapter.OnDefinitionItemClick, SignImageAdapter.OnImageItemClick, View.OnClickListener {
    private RecyclerView rvSignImage, rvSignDefinition;
    private SignImageAdapter adapterImage;
    private SignDefinitionAdapter adapterDefinition;
    private RelativeLayout btnContinue;
    private String type = "";
    private List<Sign> listSign;
    private Handler mHandler = new Handler();
    private boolean isWaiting;

    @Override

    protected String getTitle() {
        return getString(R.string.matching) + " " + type;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_sign_matching;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        type = getArguments().getString(Constants.BUNDLE_SIGN_TYPE);
        init(rootView);
        updateToolbar();
        refreshData();

    }

    public void updateToolbar() {
        ((SecondBaseActivity) getActivity()).enableButtonShare(true);
    }

    public void init(View rootView) {
        rvSignImage = (RecyclerView) rootView.findViewById(R.id.rv_images);
        rvSignDefinition = (RecyclerView) rootView.findViewById(R.id.rv_definition);
        btnContinue = (RelativeLayout) rootView.findViewById(R.id.btn_continue);
        rvSignDefinition.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSignImage.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSignImage.setHasFixedSize(true);
        rvSignDefinition.setHasFixedSize(true);
        btnContinue.setOnClickListener(this);

        rvSignImage.setItemAnimator(new FadeInAnimator(new OvershootInterpolator(1f)));
        rvSignImage.getItemAnimator().setRemoveDuration(250);
        rvSignDefinition.setItemAnimator(new FadeInAnimator(new OvershootInterpolator(1f)));
        rvSignDefinition.getItemAnimator().setRemoveDuration(250);

        listSign = SignDataSource.getSigns(type, true, 0);
    }


    public void refreshData() {
        ArrayList<Sign> listMatch = getListMatch();
        adapterImage = new SignImageAdapter(getActivity(), SignDataSource.getSignsImage(listMatch));
        adapterDefinition = new SignDefinitionAdapter(getActivity(), SignDataSource.getSignsDefinition(listMatch));
        adapterImage.setListener(this);
        adapterDefinition.setListener(this);
        rvSignImage.setAdapter(adapterImage);
        rvSignDefinition.setAdapter(adapterDefinition);
        btnContinue.setVisibility(View.GONE);
    }

    public ArrayList<Sign> getListMatch() {
        ArrayList<Sign> listResult = new ArrayList<>();
        int listSize = listSign.size();
        if (listSize >= 4) {
            if (listSize == 5) {
                for (int i = 0; i < Constants.LIMIT_SIGN_MATCHING + 1; i++) {
                    listResult.add(listSign.get(0));
                    listSign.remove(0);
                }
            } else {
                for (int i = 0; i < Constants.LIMIT_SIGN_MATCHING; i++) {
                    listResult.add(listSign.get(0));
                    listSign.remove(0);
                }
            }
        } else {
            if (listSize == 3 || listSize == 2) {
                for (int i = 0; i < listSize; i++) {
                    listResult.add(listSign.get(0));
                    listSign.remove(0);
                }
            }
        }
        return listResult;
    }

    private Runnable removeItem = new Runnable() {
        @Override
        public void run() {
            adapterDefinition.removeItem(adapterDefinition.idChecked());
            adapterImage.removeItem(adapterImage.idChecked());
            if (adapterImage.isEmptyList()) {
                if (listSign.size() > 0) {
                    refreshData();
                } else {
                    btnContinue.setVisibility(View.VISIBLE);
                }
            }
            isWaiting = false;
        }
    };


    private Runnable resetCheck = new Runnable() {
        @Override
        public void run() {
            adapterDefinition.resetCheck();
            adapterImage.resetCheck();
            isWaiting = false;
        }
    };

    @Override
    public void onDefinitionItemClick(int idSign) {
        if (!isWaiting) {
            if (adapterImage.isChecked()) {
                if (idSign == adapterImage.idChecked()) {
                    adapterDefinition.setCheck(idSign, true);
                    mHandler = new Handler();
                    mHandler.postDelayed(removeItem, 500);
                } else {
                    adapterImage.setCheck(adapterImage.idChecked(), false);
                    adapterDefinition.setCheck(idSign, false);
                    mHandler = new Handler();
                    mHandler.postDelayed(resetCheck, 500);
                }
                isWaiting = true;
            } else {
                adapterDefinition.setCheck(idSign, true);
            }
        }

    }

    @Override
    public void onImageItemClick(int idSign) {
        if (!isWaiting) {
            if (adapterDefinition.isChecked()) {
                if (idSign == adapterDefinition.idChecked()) {
                    adapterImage.setCheck(idSign, true);
                    mHandler = new Handler();
                    mHandler.postDelayed(removeItem, 500);
                } else {
                    adapterImage.setCheck(idSign, false);
                    adapterDefinition.setCheck(adapterDefinition.idChecked(), false);
                    mHandler = new Handler();
                    mHandler.postDelayed(resetCheck, 500);
                }
                isWaiting = true;
            } else {
                adapterImage.setCheck(idSign, true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_continue: {
                listSign = SignDataSource.getSigns(type, true, 0);
                refreshData();
            }
        }
    }
}
