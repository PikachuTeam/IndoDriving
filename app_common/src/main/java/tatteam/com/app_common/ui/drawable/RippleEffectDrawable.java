package tatteam.com.app_common.ui.drawable;

import android.content.Context;
import android.util.AttributeSet;

import tatteam.com.app_common.R;

/**
 * Created by ThanhNH on 10/12/2015.
 */
public class RippleEffectDrawable extends RippleView {
    public RippleEffectDrawable(Context context) {
        super(context);
    }

    public RippleEffectDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRippleEffect();
    }

    public RippleEffectDrawable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setRippleEffect();
    }

    private void setRippleEffect() {
        setRippleColor(R.color.drawable_ripple_effect);
        setRippleAlpha(100);
        setRippleDuration(200);
//        setRippleType(RippleType.DOUBLE);
//        setZooming(true);
//        setZoomScale(1.5f);
    }
}
