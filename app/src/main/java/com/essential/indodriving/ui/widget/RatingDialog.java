package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.BaseConfirmDialog;

/**
 * Created by dongc_000 on 3/11/2016.
 */
public class RatingDialog extends BaseConfirmDialog implements View.OnClickListener {

    private TextView buttonOk;
    private TextView buttonCancel;
    private Typeface font;

    private OnConfirmDialogButtonClickListener listener;

    public RatingDialog(Context context, Typeface font) {
        super(context);
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
        buttonOk = (TextView) findViewById(R.id.button_ok);
        buttonCancel = (TextView) findViewById(R.id.button_cancel);

        ((ImageView) findViewById(R.id.image_star_1))
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.dialog_rating_star_color));
        ((ImageView) findViewById(R.id.image_star_2))
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.dialog_rating_star_color));
        ((ImageView) findViewById(R.id.image_star_3))
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.dialog_rating_star_color));
        ((ImageView) findViewById(R.id.image_star_4))
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.dialog_rating_star_color));
        ((ImageView) findViewById(R.id.image_star_5))
                .setColorFilter(ContextCompat.getColor(getContext(), R.color.dialog_rating_star_color));

        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonOk) {
            if (listener != null) {
                listener.onConfirmDialogButtonClick(ConfirmButton.OK, TYPE_RATING, this);
            }
        } else if (v == buttonCancel) {
            if (listener != null) {
                listener.onConfirmDialogButtonClick(ConfirmButton.CANCEL, TYPE_RATING, this);
            }
        }
    }
}
