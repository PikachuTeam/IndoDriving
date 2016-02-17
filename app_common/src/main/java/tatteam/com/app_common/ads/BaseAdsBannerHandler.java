package tatteam.com.app_common.ads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.ads.AdListener;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;

/**
 * Created by ThanhNH on 2/15/2016.
 */
public abstract class BaseAdsBannerHandler extends AdListener {

    protected Context context;
    protected AppConstant.AdsType adsType;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(AppCommon.KEY_ADS_TYPE);
            if (type.equals(BaseAdsBannerHandler.this.adsType.getType())) {
                String adsUnitId = intent.getStringExtra(AppCommon.KEY_ADS_ID);
                AppLocalSharedPreferences.getInstance().setAdsId(adsType, adsUnitId);

                buildAds();
            }
        }
    };

    public BaseAdsBannerHandler(Context context, AppConstant.AdsType adsType) {
        this.context = context;
        this.adsType = adsType;
    }

    public void setup() {
        LocalBroadcastManager.getInstance(this.context).registerReceiver(receiver, new IntentFilter(AppCommon.INTENT_UPDATE_ADS_UNIT));
        buildAds();
    }

    public void destroy() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(receiver);
    }

    protected abstract void buildAds();
}
