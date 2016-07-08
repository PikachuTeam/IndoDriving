package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.Sign;

/**
 * Created by yue on 08/07/2016.
 */
public class ShowSignDialog extends Dialog {

    private TextView textDefinition;
    private ImageView imageSign;
    private Sign sign;

    public ShowSignDialog(Context context, Sign sign) {
        super(context);
        this.sign = sign;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_show_sign);
        findViews();
        if (sign != null) {
            Glide.with(getContext()).load(sign.image).into(imageSign);
            textDefinition.setText(sign.definition);
        }
    }

    private void findViews() {
        textDefinition = (TextView) findViewById(R.id.text_definition);
        imageSign = (ImageView) findViewById(R.id.image_sign);

        findViewById(R.id.dialog_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
