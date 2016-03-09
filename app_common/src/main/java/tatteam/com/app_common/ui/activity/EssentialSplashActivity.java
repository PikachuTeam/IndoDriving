package tatteam.com.app_common.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import tatteam.com.app_common.R;

/**
 * Created by ThanhNH-Mac on 3/9/16.
 * author: Hoa
 */
public abstract class EssentialSplashActivity extends BaseSplashActivity {

    private RelativeLayout background, keybroad, rain, text;
    private ObjectAnimator keyScaleX, keyScaleY, backgroundScaleX, backgroundScaleY;
    private AnimatorSet animationZoomBackground;
    private Animation animationDownBackground;

    @Override
    protected final int getLayoutResIdContentView() {
        return R.layout.activity_essential_splash;
    }

    @Override
    protected void onCreateContentView() {
        findView();
        animBackground();
        animLogo();
    }

    @Override
    protected long getSplashDuration() {
        return 3200l;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationZoomBackground.cancel();
        animationDownBackground.cancel();
    }

    private void animBackground() {
        backgroundScaleX = ObjectAnimator.ofFloat(background, "scaleX", 1.2f, 1f);
        backgroundScaleY = ObjectAnimator.ofFloat(background, "scaleY", 1.2f, 1f);
        backgroundScaleX.setDuration(3000);
        backgroundScaleY.setDuration(3000);
        animationZoomBackground = new AnimatorSet();
        animationZoomBackground.play(backgroundScaleX).with(backgroundScaleY);
        animationZoomBackground.start();
        animationDownBackground = AnimationUtils.loadAnimation(this, R.anim.translate_background_center_to_bot);
        background.startAnimation(animationDownBackground);
    }

    private void animText() {
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.translate_text_bot_to_center);
        hyperspaceJump.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animRain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        text.startAnimation(hyperspaceJump);

    }

    private void animRain() {
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.translate_rain_top_to_center);
        rain.startAnimation(hyperspaceJump);
        hyperspaceJump.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void findView() {
        background = (RelativeLayout) findViewById(R.id.background);
        keybroad = (RelativeLayout) findViewById(R.id.key_broad);
        rain = (RelativeLayout) findViewById(R.id.rain);
        text = (RelativeLayout) findViewById(R.id.essential);
    }

    public void animLogo() {
        keyScaleX = ObjectAnimator.ofFloat(keybroad, "scaleX", 2f, 1f);
        keyScaleY = ObjectAnimator.ofFloat(keybroad, "scaleY", 2f, 1f);
        keyScaleX.setDuration(1200);
        keyScaleY.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(keyScaleX).with(keyScaleY);
        animatorSet.setStartDelay(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            Thread.sleep(300);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        keybroad.setVisibility(View.VISIBLE);
                    }
                }.execute("");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animText();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();
    }
}
