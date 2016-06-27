package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.Window;

import com.essential.indodriving.R;
import com.essential.indodriving.ui.base.Constants;

/**
 * Created by yue on 27/06/2016.
 */
public class ThanksDialog extends Dialog {

    private AppCompatCheckBox mCheckBox;

    public ThanksDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_thanks);
        setCanceledOnTouchOutside(false);
        findViews();
    }

    private void findViews() {
        mCheckBox = (AppCompatCheckBox) findViewById(R.id.check_box_not_show_again);
        findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences(
                        Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean(Constants.PREF_NOT_SHOW_THANKS_AGAIN, mCheckBox.isChecked());
                editor.commit();
                dismiss();
            }
        });
    }
}
