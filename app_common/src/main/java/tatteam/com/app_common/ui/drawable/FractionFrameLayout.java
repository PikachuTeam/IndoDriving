package tatteam.com.app_common.ui.drawable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ThanhNH-Mac on 2/10/16.
 */
public class FractionFrameLayout extends FrameLayout {
    public FractionFrameLayout(Context context) {
        super(context);
    }

    public FractionFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FractionFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getXFraction() {
        final int width = getWidth();
        if (width != 0) return getX() / getWidth();
        else return getX();
    }

    public void setXFraction(float xFraction) {
        final int width = getWidth();
        float newWidth = (width > 0) ? (xFraction * width) : -9999;
        setX(newWidth);
    }

    public float getYFraction() {
        final int height = getHeight();
        if (height != 0) return getY() / getHeight();
        else return getY();
    }

    public void setYFraction(float yFraction) {
        final int height = getHeight();
        float newHeight = (height > 0) ? (yFraction * height) : -9999;
        setY(newHeight);
    }
}