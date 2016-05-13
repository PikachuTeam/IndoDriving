package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.BaseConfirmDialog;

/**
 * Created by dongc_000 on 3/11/2016.
 */
public class WarningDialog extends BaseConfirmDialog {

    private Context context;
    private TextView textViewWarning;
    private TextView textViewDialogTitle;
    private TextView buttonOk;
    private TextView buttonCancel;
    private Typeface font;
    private int mType;
    private OnConfirmDialogButtonClickListener listener;

    public WarningDialog(Context context, @DialogTypeDef int type, Typeface font) {
        super(context);
        this.context = context;
        this.font = font;
        this.mType = type;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_warning;
    }

    @Override
    protected void onCreateContentView() {
        setCanceledOnTouchOutside(false);
        findViews();
        setFont(font);
        switch (mType) {
            case TYPE_WARNING_1:
                textViewWarning.setText(context.getString(R.string.dialog_warning_text_exit));
                break;
            case TYPE_WARNING_2:
                textViewWarning.setText(context.getString(R.string.dialog_warning_text));
                break;
        }
    }

    public void addListener(OnConfirmDialogButtonClickListener listener) {
        this.listener = listener;
    }

    private void findViews() {
        textViewWarning = (TextView) findViewById(R.id.dialog_content);
        buttonOk = (TextView) findViewById(R.id.button_ok);
        buttonCancel = (TextView) findViewById(R.id.button_cancel);
        textViewDialogTitle = (TextView) findViewById(R.id.dialog_title);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirmDialogButtonClick(ConfirmButton.OK, mType, WarningDialog.this);
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirmDialogButtonClick(ConfirmButton.CANCEL, mType, WarningDialog.this);
                }
            }
        });
    }

    private void setFont(Typeface font) {
        textViewDialogTitle.setTypeface(font);
        buttonOk.setTypeface(font);
        buttonCancel.setTypeface(font);
    }
}
