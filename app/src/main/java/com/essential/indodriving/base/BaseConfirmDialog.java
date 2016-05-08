package com.essential.indodriving.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by dongc_000 on 3/11/2016.
 */
public abstract class BaseConfirmDialog extends Dialog {

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

    public enum Type {
        WARNING1,
        WARNING2,
        RATING
    }

    public enum ConfirmButton {
        OK,
        CANCEL
    }

    public interface OnConfirmDialogButtonClickListener {
        void onConfirmDialogButtonClick(ConfirmButton button, Type type, BaseConfirmDialog dialog);
    }
}
