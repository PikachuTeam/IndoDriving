package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;

import java.text.MessageFormat;

/**
 * Created by yue on 12/05/2016.
 */
public class NotificationDialog extends BaseConfirmDialog {

    private TextView mDialogTitle;
    private TextView mDialogContent;
    private TextView mButtonOk;
    private TextView mButtonCancel;
    private int mType;
    private int mTimes;
    private OnConfirmDialogButtonClickListener mOnConfirmDialogButtonClickListener;

    public NotificationDialog(Context context, @DialogTypeDef int type, int times) {
        super(context);
        mType = type;
        mTimes = times;
    }

    public void setOnConfirmDialogButtonClickListener(OnConfirmDialogButtonClickListener onConfirmDialogButtonClickListener) {
        mOnConfirmDialogButtonClickListener = onConfirmDialogButtonClickListener;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_warning;
    }

    @Override
    protected void onCreateContentView() {
        findViews();
        setFont(HomeActivity.defaultFont);
        mDialogTitle.setText(getContext().getString(R.string.dialog_title_trial));
        switch (mType) {
            case TYPE_TRIAL:
                mDialogContent.setText(MessageFormat.format(getContext()
                        .getString(R.string.dialog_content_trial), "" + mTimes));
                break;
            case TYPE_OUT_OF_TIMES:
                mDialogContent.setText(getContext().getString(R.string.dialog_content_out_of_times));
                break;
        }
    }

    public void setContent(String content) {
        mDialogContent.setText(content);
    }

    private void findViews() {
        mDialogContent = (TextView) findViewById(R.id.dialog_content);
        mDialogTitle = (TextView) findViewById(R.id.dialog_title);
        mButtonOk = (TextView) findViewById(R.id.button_ok);
        mButtonCancel = (TextView) findViewById(R.id.button_cancel);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmDialogButtonClickListener != null) {
                    mOnConfirmDialogButtonClickListener.onConfirmDialogButtonClick(ConfirmButton.OK
                            , mType, NotificationDialog.this);
                }
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmDialogButtonClickListener != null) {
                    mOnConfirmDialogButtonClickListener.onConfirmDialogButtonClick(ConfirmButton.CANCEL
                            , mType, NotificationDialog.this);
                }
            }
        });
    }

    private void setFont(Typeface font) {
        mDialogTitle.setTypeface(font);
        mButtonOk.setTypeface(font);
        mButtonCancel.setTypeface(font);
    }
}
