package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.BaseConfirmDialog;

/**
 * Created by dongc_000 on 3/11/2016.
 */
public class RatingDialog extends BaseConfirmDialog implements View.OnClickListener {

    private Context context;
    private TextView buttonOk;
    private TextView buttonCancel;
    private Typeface font;

    private OnConfirmDialogButtonClickListener listener;

    public RatingDialog(Context context, Typeface font) {
        super(context);
        this.context = context;
        this.font = font;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_rating;
    }

    @Override
    protected void onCreateContentView() {
        setCanceledOnTouchOutside(false);
        findViews();
        buttonOk.setTypeface(font);
        buttonCancel.setTypeface(font);
    }

    public void addListener(OnConfirmDialogButtonClickListener listener) {
        this.listener = listener;
    }

    private void findViews() {
        buttonOk = (TextView) findViewById(R.id.buttonOk);
        buttonCancel = (TextView) findViewById(R.id.buttonCancel);

        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonOk) {
            if (listener != null) {
                listener.onConfirmDialogButtonClick(ConfirmButton.OK, Type.RATING, this);
            }
        } else if (v == buttonCancel) {
            if (listener != null) {
                listener.onConfirmDialogButtonClick(ConfirmButton.CANCEL, Type.RATING, this);
            }
        }
    }
}
