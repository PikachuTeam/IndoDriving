package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;

/**
 * Created by yue on 05/06/2016.
 */
public class UpgradeToProVerDialog extends BaseConfirmDialog {

    private TextView mButtonOk;
    private TextView mButtonCancel;
    private int mType;
    private OnConfirmDialogButtonClickListener mListener;

    public UpgradeToProVerDialog(Context context) {
        super(context);
        mType = TYPE_UPGRADE;
    }

    public void setOnConfirmDialogButtonClickListener(OnConfirmDialogButtonClickListener listener) {
        mListener = listener;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_upgrade_to_pro_ver;
    }

    @Override
    protected void onCreateContentView() {
        setCanceledOnTouchOutside(false);
        findViews();
        setFont();
    }

    private void findViews() {
        mButtonOk = (TextView) findViewById(R.id.button_ok);
        mButtonCancel = (TextView) findViewById(R.id.button_cancel);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onConfirmDialogButtonClick(ConfirmButton.OK, mType, UpgradeToProVerDialog.this);
                }
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onConfirmDialogButtonClick(ConfirmButton.CANCEL, mType, UpgradeToProVerDialog.this);
                }
            }
        });
    }

    private void setFont() {
        mButtonOk.setTypeface(HomeActivity.defaultFont);
        mButtonCancel.setTypeface(HomeActivity.defaultFont);
        ((TextView) findViewById(R.id.dialog_title)).setTypeface(HomeActivity.defaultFont);
    }
}
