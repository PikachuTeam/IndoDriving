package tatteam.com.app_common.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import tatteam.com.app_common.util.AppConstant;


/**
 * Created by ThanhNH-Mac on 2/10/16.
 */
public abstract class BaseSplashActivity extends Activity {

    private Thread commonThread;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResIdContentView());
        onCreateContentView();

        initAppCommon();

        waitUntilFinishInitAppCommon();
    }

    private void initAppCommon() {
        commonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                onInitAppCommon();
            }
        });
        commonThread.start();
    }

    private void waitUntilFinishInitAppCommon() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (commonThread != null) {
                    try {
                        commonThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                onFinishInitAppCommon();
            }
        }, AppConstant.SPLASH_DURATION);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    protected abstract int getLayoutResIdContentView();

    protected abstract void onCreateContentView();

    protected abstract void onInitAppCommon();

    protected abstract void onFinishInitAppCommon();

}
