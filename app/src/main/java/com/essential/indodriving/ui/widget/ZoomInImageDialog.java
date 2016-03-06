package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/28/2016.
 */
public class ZoomInImageDialog extends Dialog {

    private Context context;

    private ImageView zoomedInImage;

    private Bitmap image;

    public ZoomInImageDialog(Context context, Bitmap image) {
        super(context);
        this.context = context;
        this.image = image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_zoom_in_picture);

        zoomedInImage = (ImageView) findViewById(R.id.zoomedInImage);

        zoomedInImage.setImageBitmap(image);

        zoomedInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
