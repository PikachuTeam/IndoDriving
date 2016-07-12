package com.essential.indodriving.ui.fragment.match;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by admin on 5/18/2016.
 */
public class MyAnimation {
    private static ObjectAnimator scaleX, scaleY, scaleReturnX, scaleReturnY;
    private static AnimatorSet scaleView, scaleReturnView;
    public static int DURATION_SCALE = 50;

    public static void animZoomWhenOnClick(final View view, final View.OnClickListener listener, float scaleFromX, float scaleToX, float scaleFromY, float scaleToY) {
        scaleX = ObjectAnimator.ofFloat(view, "scaleX", scaleFromX, scaleToX);
        scaleY = ObjectAnimator.ofFloat(view, "scaleY", scaleFromY, scaleToY);
        scaleReturnX = ObjectAnimator.ofFloat(view, "scaleX", scaleToX, 1f);
        scaleReturnY = ObjectAnimator.ofFloat(view, "scaleY", scaleToY, 1f);
        scaleX.setDuration(DURATION_SCALE);
        scaleY.setDuration(DURATION_SCALE);
        scaleReturnX.setDuration(DURATION_SCALE);
        scaleReturnY.setDuration(DURATION_SCALE);
        scaleView = new AnimatorSet();
        scaleView.play(scaleX).with(scaleY);
        scaleView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (listener != null)
                    view.setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        //--
        scaleView.start();
        scaleReturnView = new AnimatorSet();
        scaleReturnView.play(scaleReturnX).with(scaleReturnY);
        scaleReturnView.setStartDelay(DURATION_SCALE);
        scaleReturnView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null)
                    view.setOnClickListener(listener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        scaleReturnView.start();
    }

}
