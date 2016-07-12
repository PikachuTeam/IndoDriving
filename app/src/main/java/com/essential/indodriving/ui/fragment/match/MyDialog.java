package com.essential.indodriving.ui.fragment.match;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.essential.indodriving.R;

/**
 * Created by admin on 6/8/2016.
 */
public class MyDialog extends Dialog implements View.OnClickListener {
    private static MyDialog instance;

    private TextView tvDefinition, btnYes;
    private ImageView ivSign;

    private MyDialog(Context context, String text) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog);
        init();
        setCancelable(false);
        setText(text);
    }

    private MyDialog(Context context, byte[] image) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog);
        init();
        setCancelable(false);
        setImage(image);
    }

    public static MyDialog getInstance(Context context, String text) {
        if (instance != null) {
            instance.setText(text);
            return instance;
        } else {
            instance = new MyDialog(context, text);
            return instance;
        }
    }

    public static MyDialog getInstance(Context context, byte[] image) {

        if (instance != null) {
            instance.setImage(image);
            return instance;
        } else {
            instance = new MyDialog(context, image);
            return instance;
        }
    }

    public void setText(String text) {
        tvDefinition.setVisibility(View.VISIBLE);
        ivSign.setVisibility(View.GONE);
        tvDefinition.setText(text);
    }

    public void setImage(byte[] image) {
        tvDefinition.setVisibility(View.GONE);
        ivSign.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(image).asBitmap().dontAnimate().dontTransform()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivSign.setImageBitmap(resource);
                    }
                });
    }

    private void init() {
        btnYes = (TextView) findViewById(R.id.btn_yes);
        tvDefinition = (TextView) findViewById(R.id.tv_sign_zoom);
        ivSign = (ImageView) findViewById(R.id.iv_sign_zoom);
        ivSign.setOnClickListener(this);
        btnYes.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        MyAnimation.animZoomWhenOnClick(v, MyDialog.this, 1, 1.1f, 1, 1.1f);
        int id = v.getId();
        switch (id) {
            case R.id.btn_yes:
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        instance = null;
    }
}
