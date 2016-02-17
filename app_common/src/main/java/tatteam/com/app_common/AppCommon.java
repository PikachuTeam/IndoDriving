package tatteam.com.app_common;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import tatteam.com.app_common.ui.dialog.MoreAppsDialog;
import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;


/**
 * Created by ThanhNH-Mac on 10/4/15.
 */
public class AppCommon implements AppConstant {
    public static final String INTENT_UPDATE_ADS_UNIT = "intent_ads_loading";
    public static final String KEY_ADS_TYPE = "key_ads_type";
    public static final String KEY_ADS_ID = "key_ads_id";

    private static AppCommon instance;
    private Context context;

    private AppCommon() {
    }

    public static AppCommon getInstance() {
        if (instance == null) {
            instance = new AppCommon();
        }
        return instance;
    }

    public void initIfNeeded(Context context) {
        if (this.context == null) {
            this.context = context;
            AppLocalSharedPreferences.getInstance().initIfNeeded(this.context);
        }
    }

    public void increaseLaunchTime() {
        AppLocalSharedPreferences.getInstance().increaseAppLaunchTime();
        AppLocalSharedPreferences.getInstance().setSkipRating(false);
    }

    public MoreAppsDialog openMoreAppDialog(Context activity) {
        return openMoreAppDialog(activity, null);
    }

    public MoreAppsDialog openMoreAppDialog(Context activity, String url) {
        MoreAppsDialog moreAppsDialog = new MoreAppsDialog(activity, url);
        moreAppsDialog.show();
        return moreAppsDialog;
    }

    public void syncAdsIfNeeded(final AdsType... adsTypes) {
        if (AppLocalSharedPreferences.getInstance().shouldSyncAds(RE_SYNC_ADS_LAUNCH_TIME_INTERVAL, adsTypes)) {
            Ion.with(context)
                    .load(DEFAULT_ADS_URL)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result != null) {
                                for (AdsType adsType : adsTypes) {
                                    String adsUnitId = result.get(adsType.getType()).getAsString();
                                    if (adsUnitId != null && !adsUnitId.trim().isEmpty()) {
                                        if (!AppLocalSharedPreferences.getInstance().isAdsExist(adsType, adsUnitId)) {
                                            AppLocalSharedPreferences.getInstance().setAdsId(adsType, adsUnitId);
                                            sendBroadCastUpdateAds(adsType, adsUnitId);
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private void sendBroadCastUpdateAds(AdsType adsType, String adsUnitId) {
        Intent intent = new Intent(INTENT_UPDATE_ADS_UNIT);
        intent.putExtra(KEY_ADS_TYPE, adsType.getType());
        intent.putExtra(KEY_ADS_ID, adsUnitId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void destroy() {
        instance = null;
    }

}
