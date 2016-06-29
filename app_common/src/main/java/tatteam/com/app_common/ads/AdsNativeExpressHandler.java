package tatteam.com.app_common.ads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;

/**
 * Created by ThanhNH-Mac on 6/26/16.
 */
public class AdsNativeExpressHandler extends BaseAdsBannerHandler {
    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 150;

    private NativeExpressAdView adView;
    private AdSize adSize;
    private ViewGroup adsContainer;

    public AdsNativeExpressHandler(Context context, ViewGroup adsContainer, AppConstant.AdsType adsType) {
        this(context, adsContainer, adsType, new AdSize(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public AdsNativeExpressHandler(Context context, ViewGroup adsContainer, AppConstant.AdsType adsType, AdSize adSize) {
        super(context, adsType);
        this.adsContainer = adsContainer;
        this.adSize = adSize;
    }

    @Override
    protected void buildAds() {
        if (this.adsContainer != null && this.adsType != null) {
            String unitId = AppLocalSharedPreferences.getInstance().getAdsId(this.adsType);
            if (!unitId.trim().isEmpty()) {
                if (this.adView == null) {
                    this.adView = new NativeExpressAdView(this.context);
                    this.adView.setAdSize(adSize);
                    this.adsContainer.addView(adView);
                }
                this.adView.setAdUnitId(unitId);
                requestAds(this.adView);
            }
        }
    }

    public void refresh() {
        if (adView != null && adView.getAdUnitId() != null && !adView.getAdUnitId().isEmpty()) {
            requestAds(adView);
        }
    }


    private void requestAds(NativeExpressAdView adView) {
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setAdListener(this);
            adView.loadAd(adRequest);
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

    @Override
    public void destroy() {
        super.destroy();
    }
}
