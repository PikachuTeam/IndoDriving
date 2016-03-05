package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/28/2016.
 */
public class WarningDialog extends Dialog {

    private int type;
    private Context context;
    private TextView textViewWarning;
    private TextView textViewDialogTitle;
    private TextView buttonOk;
    private TextView buttonCancel;
    private Typeface font;

    private OnWarningDialogButtonClickListener listener;

    public final static int BUTTON_OK = 1, BUTTON_CANCEL = 2;

    public WarningDialog(Context context, int type, Typeface font) {
        super(context);
        this.context = context;
        this.type = type;
        this.font = font;
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

        findViews();
        setFont(font);

        switch (type) {
            case 0:
                textViewWarning.setText(context.getString(R.string.dialog_warning_text_exit));
                break;
            case 1:
                textViewWarning.setText(context.getString(R.string.dialog_warning_text));
                break;
        }

        findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onWarningDialogButtonClick(BUTTON_OK, type, WarningDialog.this);
                }
            }
        });

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onWarningDialogButtonClick(BUTTON_CANCEL, type, WarningDialog.this);
                }
            }
        });
    }

    private void findViews(){
        textViewWarning = (TextView) findViewById(R.id.tvWarning);
        buttonOk= (TextView) findViewById(R.id.buttonOk);
        buttonCancel= (TextView) findViewById(R.id.buttonCancel);
        textViewDialogTitle = (TextView) findViewById(R.id.textViewDialogTitle);
    }

    private void setFont(Typeface font){
        textViewDialogTitle.setTypeface(font);
        buttonOk.setTypeface(font);
        buttonCancel.setTypeface(font);
    }

    public interface OnWarningDialogButtonClickListener {
        void onWarningDialogButtonClick(int buttonId, int type, WarningDialog dialog);
    }
}
