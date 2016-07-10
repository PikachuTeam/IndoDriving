package com.essential.indodriving;

import android.content.Context;
import android.content.SharedPreferences;

import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.ui.base.Constants;

/**
 * Created by ThanhNH-Mac on 6/23/16.
 */
public class MySetting {

    private final static String PREF_IS_SIGN_NEW = "is_sign_new";
    private final static String PREF_IS_PRO_VERSION = "is_pro_version";
    private final static String PREF_IS_RATE_APP = "is_rate_app";
    private final static String PREF_IS_ENABLE_RATE_TO_UNLOCK = "is_enable_rate_to_unlock";
    private static MySetting instance;
    private final String
            PREF_CURRENT_POSITION_SIM_A = "Current Position Sim A",
            PREF_CURRENT_POSITION_SIM_A_UMUM = "Current Position Sim A Umum",
            PREF_CURRENT_POSITION_SIM_B1 = "Current Position Sim B1",
            PREF_CURRENT_POSITION_SIM_B1_UMUM = "Current Position Sim B1 Umum",
            PREF_CURRENT_POSITION_SIM_B2 = "Current Position Sim B2",
            PREF_CURRENT_POSITION_SIM_B2_UMUM = "Current Position Sim B2 Umum",
            PREF_CURRENT_POSITION_SIM_C = "Current Position Sim C",
            PREF_CURRENT_POSITION_SIM_D = "Current Position Sim D";
    private final String
            PREF_SHOW_RULE_AGAIN_SIM_A = "Show Rule Again A",
            PREF_SHOW_RULE_AGAIN_SIM_A_UMUM = "Show Rule Again A Umum",
            PREF_SHOW_RULE_AGAIN_SIM_B1 = "Show Rule Again B1",
            PREF_SHOW_RULE_AGAIN_SIM_B1_UMUM = "Show Rule Again B1 Umum",
            PREF_SHOW_RULE_AGAIN_SIM_B2 = "Show Rule Again B2",
            PREF_SHOW_RULE_AGAIN_SIM_B2_UMUM = "Show Rule Again B2 Umum",
            PREF_SHOW_RULE_AGAIN_SIM_C = "Show Rule Again C",
            PREF_SHOW_RULE_AGAIN_SIM_D = "Show Rule Again D";
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

    public void savePosition(int type, int position) {
        switch (type) {
            case DrivingDataSource.TYPE_SIM_A:
                editor.putInt(PREF_CURRENT_POSITION_SIM_A, position);
                break;
            case DrivingDataSource.TYPE_SIM_A_UMUM:
                editor.putInt(PREF_CURRENT_POSITION_SIM_A_UMUM, position);
                break;
            case DrivingDataSource.TYPE_SIM_B1:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B1, position);
                break;
            case DrivingDataSource.TYPE_SIM_B1_UMUM:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B1_UMUM, position);
                break;
            case DrivingDataSource.TYPE_SIM_B2:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B2, position);
                break;
            case DrivingDataSource.TYPE_SIM_B2_UMUM:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B2_UMUM, position);
                break;
            case DrivingDataSource.TYPE_SIM_C:
                editor.putInt(PREF_CURRENT_POSITION_SIM_C, position);
                break;
            case DrivingDataSource.TYPE_SIM_D:
                editor.putInt(PREF_CURRENT_POSITION_SIM_D, position);
                break;
        }
        editor.commit();
    }

    public int loadPosition(int type) {
        switch (type) {
            case DrivingDataSource.TYPE_SIM_A:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_A, 0);
            case DrivingDataSource.TYPE_SIM_A_UMUM:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_A_UMUM, 0);
            case DrivingDataSource.TYPE_SIM_B1:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_B1, 0);
            case DrivingDataSource.TYPE_SIM_B1_UMUM:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_B1_UMUM, 0);
            case DrivingDataSource.TYPE_SIM_B2:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_B2, 0);
            case DrivingDataSource.TYPE_SIM_B2_UMUM:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_B2_UMUM, 0);
            case DrivingDataSource.TYPE_SIM_C:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_C, 0);
            case DrivingDataSource.TYPE_SIM_D:
                return pref.getInt(PREF_CURRENT_POSITION_SIM_D, 0);
            default:
                return 0;
        }
    }

    public void saveTrialTimes(int times) {
        editor.putInt(Constants.PREF_TRIAL_TIME_LEFT, times);
        editor.commit();
    }

    public int loadTrialTimesLeft() {
        return pref.getInt(Constants.PREF_TRIAL_TIME_LEFT, Constants.NUMBER_OF_TRIALS);
    }

    public void saveStateShowRuleAgain(int type, boolean isChecked) {
        switch (type) {
            case DrivingDataSource.TYPE_SIM_A:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_A, !isChecked);
                break;
            case DrivingDataSource.TYPE_SIM_A_UMUM:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_A_UMUM, !isChecked);
                break;
            case DrivingDataSource.TYPE_SIM_B1:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_B1, !isChecked);
                break;
            case DrivingDataSource.TYPE_SIM_B1_UMUM:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_B1_UMUM, !isChecked);
                break;
            case DrivingDataSource.TYPE_SIM_B2:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_B2, !isChecked);
                break;
            case DrivingDataSource.TYPE_SIM_B2_UMUM:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_B2_UMUM, !isChecked);
                break;
            case DrivingDataSource.TYPE_SIM_C:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_C, !isChecked);
                break;
            case DrivingDataSource.TYPE_SIM_D:
                editor.putBoolean(PREF_SHOW_RULE_AGAIN_SIM_D, !isChecked);
                break;
        }
        editor.commit();
    }

    public boolean isRuleShowedAgain(int type) {
        switch (type) {
            case DrivingDataSource.TYPE_SIM_A:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_A, true);
            case DrivingDataSource.TYPE_SIM_A_UMUM:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_A_UMUM, true);
            case DrivingDataSource.TYPE_SIM_B1:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_B1, true);
            case DrivingDataSource.TYPE_SIM_B1_UMUM:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_B1_UMUM, true);
            case DrivingDataSource.TYPE_SIM_B2:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_B2, true);
            case DrivingDataSource.TYPE_SIM_B2_UMUM:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_B2_UMUM, true);
            case DrivingDataSource.TYPE_SIM_C:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_C, true);
            case DrivingDataSource.TYPE_SIM_D:
                return pref.getBoolean(PREF_SHOW_RULE_AGAIN_SIM_D, true);
            default:
                return true;
        }
    }

    public void saveSignPosition(String type, int position) {
        editor.putInt(type, position);
        editor.commit();
    }

    public int loadSignPosition(String type) {
        return pref.getInt(type, 0);
    }

    public boolean isSignNew() {
        return pref.getBoolean(PREF_IS_SIGN_NEW, true);
    }

    public void setSignNew(boolean isSignNew) {
        editor.putBoolean(PREF_IS_SIGN_NEW, isSignNew);
        editor.commit();
    }
}
