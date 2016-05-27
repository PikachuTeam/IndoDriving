package tatteam.com.app_common.ads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
    private static final long RETRY = 60 * 1000;

    private AdView adView;
    private ViewGroup adsContainer;
    private Handler retryHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            requestAds(adView);
            return false;
        }
    });

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
                requestAds(this.adView);
            }
        }
    }

    private void requestAds(AdView adView) {
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
            retryHandler.sendEmptyMessageDelayed(0, RETRY);
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
        if (retryHandler != null) {
            retryHandler.removeCallbacksAndMessages(null);
        }
    }
}
