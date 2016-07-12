package com.essential.indodriving.ui.fragment.match;

import android.os.Bundle;
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
        refreshData();

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

    }


    public void refreshData() {
        List<Sign> listSign = SignDataSource.getSigns(type, true, Constants.LIMIT_SIGN_MATCHING);
        adapterImage = new SignImageAdapter(getActivity(), SignDataSource.getSignsImage(listSign));
        adapterDefinition = new SignDefinitionAdapter(getActivity(), SignDataSource.getSignsDefinition(listSign));
        adapterImage.setListener(this);
        adapterDefinition.setListener(this);
        rvSignImage.setAdapter(adapterImage);
        rvSignDefinition.setAdapter(adapterDefinition);
        btnContinue.setVisibility(View.GONE);
    }

    @Override
    public void onDefinitionItemClick(int idSign) {
        if (adapterDefinition.isChecked() && adapterImage.isChecked()) {
            if (adapterDefinition.idChecked() == adapterImage.idChecked()) {
                adapterDefinition.removeItem(adapterDefinition.idChecked());
                adapterImage.removeItem(adapterImage.idChecked());
                if (adapterImage.isEmptyList()) {
                    btnContinue.setVisibility(View.VISIBLE);
                }
            } else {
                adapterDefinition.resetCheck();
                adapterImage.resetCheck();
                adapterDefinition.notifyDataSetChanged();
                adapterImage.notifyDataSetChanged();
            }
        } else {
            if (adapterImage.isChecked()) {
                if (idSign == adapterImage.idChecked()) {
                    adapterImage.setCheck(idSign, true);
                    adapterDefinition.setCheck(idSign, true);
                } else {
                    adapterImage.setCheck(adapterImage.idChecked(), false);
                    adapterDefinition.setCheck(idSign, false);
                }
            } else {
                adapterDefinition.setCheck(idSign, true);
            }
            adapterDefinition.notifyDataSetChanged();
            adapterImage.notifyDataSetChanged();
        }

    }

    @Override
    public void onImageItemClick(int idSign) {
        if (adapterDefinition.isChecked() && adapterImage.isChecked()) {
            if (adapterDefinition.idChecked() == adapterImage.idChecked()) {
                adapterDefinition.removeItem(adapterDefinition.idChecked());
                adapterImage.removeItem(adapterImage.idChecked());
                if (adapterImage.isEmptyList()) {
                    btnContinue.setVisibility(View.VISIBLE);
                }
            } else {
                adapterDefinition.resetCheck();
                adapterImage.resetCheck();
                adapterDefinition.notifyDataSetChanged();
                adapterImage.notifyDataSetChanged();
            }
        } else {
            if (adapterDefinition.isChecked()) {
                if (idSign == adapterDefinition.idChecked()) {
                    adapterDefinition.setCheck(idSign, true);
                    adapterImage.setCheck(idSign, true);
                } else {
                    adapterDefinition.setCheck(adapterDefinition.idChecked(), false);
                    adapterImage.setCheck(idSign, false);
                }
            } else {
                adapterImage.setCheck(idSign, true);
            }
            adapterDefinition.notifyDataSetChanged();
            adapterImage.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_continue) {
            refreshData();
        }
    }
}
