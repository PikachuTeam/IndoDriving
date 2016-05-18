package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/28/2016.
 */
public class ZoomInImageDialog extends Dialog {

    private ImageView zoomedInImage;
    private byte[] imageData;

    public ZoomInImageDialog(Context context, byte[] imageData) {
        super(context);
        this.imageData = imageData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_zoom_in_picture);
        zoomedInImage = (ImageView) findViewById(R.id.zoomedInImage);
        Glide.with(getContext()).load(this.imageData).dontAnimate().dontTransform().into(zoomedInImage);
        zoomedInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
