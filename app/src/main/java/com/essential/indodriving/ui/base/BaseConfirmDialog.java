package com.essential.indodriving.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.Window;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dongc_000 on 3/11/2016.
 */
public abstract class BaseConfirmDialog extends Dialog {

    public final static int TYPE_WARNING_1 = 1;
    public final static int TYPE_WARNING_2 = 2;
    public final static int TYPE_RATING = 3;
    public final static int TYPE_TRIAL = 4;
    public final static int TYPE_OUT_OF_TIMES = 5;

    public BaseConfirmDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResourceId());
        onCreateContentView();
    }

    protected abstract int getLayoutResourceId();

    protected abstract void onCreateContentView();

    public enum ConfirmButton {
        OK,
        CANCEL
    }

    public interface OnConfirmDialogButtonClickListener {
        void onConfirmDialogButtonClick(ConfirmButton button, @DialogTypeDef int dialogType, BaseConfirmDialog dialog);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_TRIAL, TYPE_OUT_OF_TIMES, TYPE_WARNING_1, TYPE_WARNING_2, TYPE_RATING
    })
    public @interface DialogTypeDef {
    }
}
