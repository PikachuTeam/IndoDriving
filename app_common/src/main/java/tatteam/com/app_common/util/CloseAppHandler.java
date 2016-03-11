package tatteam.com.app_common.util;

import android.app.Activity;
import android.content.Context;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import tatteam.com.app_common.R;

/**
 * Created by ThanhNH on 10/8/2015.
 */
public class CloseAppHandler {
    private static final long KEY_BACK_INTERVAL = 2000l;

    private Context context;
    private AppRate appRate;
    private OnCloseAppListener listener;

    private int rateAppOverLaunchTime = 3;
    private int rateAppOverDate = 1;
    private long backPressedPeriod;
    private boolean withRateApp;

    public CloseAppHandler(Activity activity) {
        this(activity, true);
    }

    public CloseAppHandler(Activity activity, boolean withRateApp) {
        this.context = activity;
        this.withRateApp = withRateApp;
        if (this.withRateApp) {
            setupRateApp();
        }
    }

    private void setupRateApp() {
        appRate = AppRate.with(context)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(0) // default 10
                .setRemindInterval(0) // default 1
                .setShowLaterButton(false)
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        if (which == -1) {//rate
                            AppLocalSharedPreferences.getInstance().setIsRateApp(true);
                        } else {//close
                            appRate.clearAgreeShowDialog();
                            AppLocalSharedPreferences.getInstance().setRateAppRemindInterval();
//                            LocalSharedPreferManager.getInstance().resetRateAppLaunchTime();
                            if (listener != null) {
                                listener.onRateAppDialogClose();
                            }
                        }
                    }
                });
    }

    public void setKeyBackPress(Activity activity) {
        if (!withRateApp || !showRatingDialogIfNeeded(activity)) {
            handleDoubleBackToExit();
        }
    }

    private boolean showRatingDialogIfNeeded(Activity activity) {
        boolean isSkipRating = AppLocalSharedPreferences.getInstance().isSkipRating();
        boolean isRateAlready = AppLocalSharedPreferences.getInstance().isRatedApp();
        boolean isRateOverLaunchTime = AppLocalSharedPreferences.getInstance().isRateAppOverLaunchTime(rateAppOverLaunchTime);
        boolean isRateOverDate = (AppLocalSharedPreferences.getInstance().isRateAppOverDate(rateAppOverDate)
                || AppLocalSharedPreferences.getInstance().getAppLaunchTime() % rateAppOverLaunchTime == 0);
        if (!isRateAlready && !isSkipRating && isRateOverLaunchTime && isRateOverDate) {
            appRate.showRateDialogIfMeetsConditions(activity);
            AppLocalSharedPreferences.getInstance().setSkipRating(true);
            return true;
        }
        return false;
    }

    private void handleDoubleBackToExit() {
        if (backPressedPeriod + KEY_BACK_INTERVAL > System.currentTimeMillis()) {
            if (listener != null) {
                listener.onReallyWantToCloseApp();
            }
        } else {
            backPressedPeriod = System.currentTimeMillis();
            if (listener != null) {
                listener.onTryToCloseApp();
            }
        }
    }

    public String getDefaultExitMessage() {
        return context.getString(R.string.message_exit);
    }

    public int getRateAppOverDate() {
        return rateAppOverDate;
    }

    public void setRateAppOverDate(int rateAppOverDate) {
        this.rateAppOverDate = rateAppOverDate;
    }

    public int getRateAppOverLaunchTime() {
        return rateAppOverLaunchTime;
    }

    public void setRateAppOverLaunchTime(int rateAppOverLaunchTime) {
        this.rateAppOverLaunchTime = rateAppOverLaunchTime;
    }

    public void setListener(OnCloseAppListener listener) {
        this.listener = listener;
    }

    public static interface OnCloseAppListener {
        public void onRateAppDialogClose();

        public void onTryToCloseApp();

        public void onReallyWantToCloseApp();
    }
}
