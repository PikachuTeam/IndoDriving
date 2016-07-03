package tatteam.com.app_common.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by ThanhNH on 5/2/2015.
 */
public class AppLocalSharedPreferences {
    private static final String PREF_NAME = "app_setting_prefer";
    private static final String PREF_LAUNCH_TIME = "app_launch_time";
    private static final String PREF_RATE_APP = "app_rate";
    private static final String PREF_RATE_INTERVAL = "app_rate_interval";
    private static final String PREF_RATE_SKIP = "app_rate_skip";
    private static final String PREF_MY_EXTRA_APPS = "my_extra_apps";
    private static final String PREF_LOCAL_DATABASE_NAME = "database_name";

    private static AppLocalSharedPreferences instance;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private AppLocalSharedPreferences() {
    }

    public static AppLocalSharedPreferences getInstance() {
        if (instance == null) {
            instance = new AppLocalSharedPreferences();
        }
        return instance;
    }

    public void initIfNeeded(Context context) {
        if (pref == null) {
            pref = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }


    public String getMyExtraAppsString() {
        return pref.getString(PREF_MY_EXTRA_APPS, "");
    }

    public void setMyExtraApps(String jSon) {
        editor.putString(PREF_MY_EXTRA_APPS, jSon);
        editor.commit();
    }

    //launch app
    public void increaseAppLaunchTime() {
        long launchTime = getAppLaunchTime() + 1;
        editor.putLong(PREF_LAUNCH_TIME, (launchTime));
        editor.commit();
    }

    public void setDatabaseName(String databaseName){
        editor.putString(PREF_LOCAL_DATABASE_NAME, databaseName);
        editor.commit();
    }

    public String getDatabaseName(){
        return pref.getString(PREF_LOCAL_DATABASE_NAME, "");
    }

    public long getAppLaunchTime() {
        return pref.getLong(PREF_LAUNCH_TIME, 0L);
    }

    public void resetAppLaunchTime() {
        editor.putLong(PREF_LAUNCH_TIME, (0));
        editor.commit();
    }


    //rate app
    public void setIsRateApp(boolean isRateApp) {
        editor.putBoolean(PREF_RATE_APP, isRateApp);
        editor.commit();
    }

    public boolean isRatedApp() {
        return pref.getBoolean(PREF_RATE_APP, false);
    }

    public void setSkipRating(boolean isSkip) {
        editor.putBoolean(PREF_RATE_SKIP, isSkip);
        editor.commit();
    }

    public boolean isSkipRating() {
        return pref.getBoolean(PREF_RATE_SKIP, false);
    }

    public void setRateAppRemindInterval() {
        editor.putLong(PREF_RATE_INTERVAL, (new Date()).getTime());
        editor.commit();
    }

    public long getRateAppRemindInterval() {
        return pref.getLong(PREF_RATE_INTERVAL, 0L);
    }

    public boolean isRateAppOverDate(int threshold) {
        return (new Date()).getTime() - getRateAppRemindInterval() >= (long) (threshold * 24 * 60 * 60 * 1000);
    }

    public boolean isRateAppOverLaunchTime(int threshold) {
        return getAppLaunchTime() >= threshold;
    }


    // ads
    public boolean shouldSyncAds(int afterEachLaunchTime, AppConstant.AdsType... adsTypes) {
        if (getAppLaunchTime() % afterEachLaunchTime == 0) {
            return true;
        }
        for (AppConstant.AdsType adsType : adsTypes) {
            if (getAdsId(adsType).trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }


    public void setAdsId(AppConstant.AdsType adsType, String adsId) {
        editor.putString(adsType.getType(), adsId);
        editor.commit();
    }

    public String getAdsId(AppConstant.AdsType adsType) {
        return pref.getString(adsType.getType(), "");
    }

    public boolean isAdsExist(AppConstant.AdsType adsType, String adsId){
        String currentAdsId = getAdsId(adsType);
        return currentAdsId.equals(adsId);
    }

    public int[] getAdsSize(AppConstant.AdsType adsType) {
        int width = pref.getInt(adsType.getType() + "_WIDTH", 0);
        int height = pref.getInt(adsType.getType() + "_HEIGHT", 0);
        return new int[]{width, height};
    }

    public void setAdsSize(AppConstant.AdsType adsType, int width, int height) {
        editor.putInt(adsType.getType() + "_WIDTH", width);
        editor.putInt(adsType.getType() + "_HEIGHT", height);
        editor.commit();
    }

    public void removeAdsId(AppConstant.AdsType adsType) {
        setAdsId(adsType, "");
    }

    ///////
    public void destroy() {
        instance = null;
    }

}
