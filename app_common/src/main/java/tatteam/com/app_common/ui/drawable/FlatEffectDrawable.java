package tatteam.com.app_common.ui.drawable;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import tatteam.com.app_common.R;


/**
 * Created by ThanhNH on 10/14/2015.
 */
public class FlatEffectDrawable extends RelativeLayout {
    private View highlight;

    public FlatEffectDrawable(Context context) {
        super(context);
    }

    public FlatEffectDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public FlatEffectDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        this.onTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            highlight.setBackgroundColor(getResources().getColor(R.color.drawable_flat_effect));
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            highlight.setBackgroundColor(Color.TRANSPARENT);
        }
        return super.onTouchEvent(event);
    }

    private void setup() {
        this.setClickable(true);
        highlight = new FrameLayout(getContext());
        highlight.setBackgroundColor(Color.TRANSPARENT);
        this.post(new Runnable() {
            @Override
            public void run() {
                highlight.setLayoutParams(new LayoutParams(FlatEffectDrawable.this.getWidth(), FlatEffectDrawable.this.getHeight()));
                addView(highlight);
            }
        });
    }

}
