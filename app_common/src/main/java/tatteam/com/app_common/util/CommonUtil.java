package tatteam.com.app_common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

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
}
