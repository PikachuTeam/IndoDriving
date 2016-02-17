package tatteam.com.app_common.util;

import android.util.Log;

/**
 * Created by ThanhNH on 10/16/2015.
 */
public class AppLog {
    private static final String LOG_TAG = "App Log";

    public static void i(String message) {
        Log.i(LOG_TAG, message);
    }

    public static void e(String message) {
        Log.e(LOG_TAG, message);
    }
}
