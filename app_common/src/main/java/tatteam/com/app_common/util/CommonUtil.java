package tatteam.com.app_common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ThanhNH on 10/17/2015.
 */
public class CommonUtil {
    public static void openApplicationOnGooglePlay(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void openDeveloperPageOnGooglePlay(Context context, String pubName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://search?q=pub:" + pubName));
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isPackageInstalled(String packageName, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void sharePlainText(Context context, String message) {
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent2, "Share via"));
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void closeSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void closeSoftKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void openSoftKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public static boolean isAnmationSupported(Context context) {
        boolean isAnmationSupport = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                isAnmationSupport = Settings.Global.getFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE) != 0;
            } else {
                isAnmationSupport = Settings.System.getFloat(context.getContentResolver(), Settings.System.ANIMATOR_DURATION_SCALE) != 0;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return isAnmationSupport;
    }
}
