package tatteam.com.app_common;

import android.app.Application;

import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by ThanhNH-Mac on 7/16/16.
 */
public class BaseApplication extends Application {

    private boolean isAnimationSupported;

    @Override
    public void onCreate() {
        super.onCreate();
        isAnimationSupported = CommonUtil.isAnmationSupported(getApplicationContext());
    }

    public boolean isAnimationSupported() {
        return isAnimationSupported;
    }
}
