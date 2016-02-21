package tatteam.com.app_common.ads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;

/**
 * Created by ThanhNH-Mac on 10/30/15.
 */
public class AdsSmallBannerHandler extends BaseAdsBannerHandler {
    private AdView adView;
    private ViewGroup adsContainer;

    public AdsSmallBannerHandler(Context context, ViewGroup adsContainer, AppConstant.AdsType adsType) {
        super(context, adsType);
        this.adsContainer = adsContainer;
    }

    public AdView getAdView() {
        return this.adView;
    }

    @Override
    protected void buildAds() {
        if (this.adsContainer != null && this.adsType != null) {
            String unitId = AppLocalSharedPreferences.getInstance().getAdsId(this.adsType);
            if (!unitId.trim().isEmpty()) {
                if (this.adView == null) {
                    this.adView = new AdView(this.context);
                    this.adView.setAdSize(AdSize.SMART_BANNER);
                    this.adsContainer.addView(adView);
                }
                this.adView.setAdUnitId(unitId);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.setAdListener(this);
                adView.loadAd(adRequest);
            }
        }
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);
        if (adView != null) {
            adView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        if (adView != null) {
            adView.setVisibility(View.VISIBLE);
        }
    }


}
