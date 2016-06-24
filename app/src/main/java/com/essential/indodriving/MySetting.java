package com.essential.indodriving;

import android.content.Context;
import android.content.SharedPreferences;

import com.essential.indodriving.ui.base.Constants;

/**
 * Created by ThanhNH-Mac on 6/23/16.
 */
public class MySetting {
    private final static String PREF_IS_PRO_VERSION = "is_pro_version";


    private static MySetting instance;
    private Context context;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;

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
}
