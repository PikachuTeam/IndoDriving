package com.essential.indodriving;

import android.content.Context;
import android.content.SharedPreferences;

import com.essential.indodriving.ui.base.Constants;

/**
 * Created by ThanhNH-Mac on 6/23/16.
 */
public class MySetting {
    private final static String PREF_IS_PRO_VERSION = "is_pro_version";
    private final static String PREF_IS_RATE_APP = "is_rate_app";
    private final static String PREF_IS_ENABLE_RATE_TO_UNLOCK = "is_enable_rate_to_unlock";

    private static MySetting instance;
    private SharedPreferences.Editor editor;
    private Context context;
    private SharedPreferences pref;

    private MySetting() {
    }

    public static MySetting getInstance() {
        if (instance == null) {
            instance = new MySetting();
        }
        return instance;
    }

    public void initIfNeeded(Context context) {
        if (this.context == null) {
            this.context = context;
            pref = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }

    public boolean isProVersion() {
        return pref.getBoolean(PREF_IS_PRO_VERSION, false);
    }

    public void setProVersion(boolean isProVersion) {
        editor.putBoolean(PREF_IS_PRO_VERSION, isProVersion);
        editor.commit();
    }

    public boolean isRated() {
        return pref.getBoolean(PREF_IS_RATE_APP, false);
    }

    public void setRated() {
        editor.putBoolean(PREF_IS_RATE_APP, true);
        editor.commit();
    }

    public boolean isEnableRateToUnlock() {
        return pref.getBoolean(PREF_IS_ENABLE_RATE_TO_UNLOCK, false);
    }

    public void setRateToUnlock(boolean isEnabled) {
        editor.putBoolean(PREF_IS_ENABLE_RATE_TO_UNLOCK, isEnabled);
        editor.commit();
    }
}
