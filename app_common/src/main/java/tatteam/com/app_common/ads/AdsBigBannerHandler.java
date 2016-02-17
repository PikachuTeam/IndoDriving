package tatteam.com.app_common.ads;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;

/**
 * Created by ThanhNH-Mac on 11/5/15.
 */
public class AdsBigBannerHandler extends BaseAdsBannerHandler {
    private InterstitialAd interstitialAd;

    public AdsBigBannerHandler(Context context, AppConstant.AdsType adsType) {
        super(context, adsType);
    }

    @Override
    protected void buildAds() {
        if (this.adsType != null) {
            String unitId = AppLocalSharedPreferences.getInstance().getAdsId(this.adsType);
            if (!unitId.trim().isEmpty()) {
                if (interstitialAd == null) {
                    interstitialAd = new InterstitialAd(this.context);
                }
                interstitialAd.setAdUnitId(AppLocalSharedPreferences.getInstance().getAdsId(this.adsType));
                requestNewInterstitial();
                interstitialAd.setAdListener(this);
            }
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        requestNewInterstitial();
    }

    public void show() {
        if (this.interstitialAd != null) {
            this.interstitialAd.show();
        }
    }


    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }
}
