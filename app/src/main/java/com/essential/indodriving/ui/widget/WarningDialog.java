package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/28/2016.
 */
public class WarningDialog extends Dialog {

    private OnWarningDialogButtonClickListener listener;

    public final static int BUTTON_OK = 1, BUTTON_CANCEL = 2;

    public WarningDialog(Context context) {
        super(context);
    }

    public void addListener(OnWarningDialogButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_warning);

        findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onWarningDialogButtonClick(BUTTON_OK);
                }
            }
        });

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onWarningDialogButtonClick(BUTTON_CANCEL);
                }
            }
        });
    }

    public interface OnWarningDialogButtonClickListener {
        void onWarningDialogButtonClick(int buttonId);
    }
}
