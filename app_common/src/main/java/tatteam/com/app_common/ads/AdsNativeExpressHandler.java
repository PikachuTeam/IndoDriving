package tatteam.com.app_common.ads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;
import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by ThanhNH-Mac on 6/26/16.
 */
public class AdsNativeExpressHandler extends BaseAdsBannerHandler {
    public static final float WIDTH_HEIGHT_RATIO_SMALL = 2.0f;
    public static final float WIDTH_HEIGHT_RATIO_BIG = 1.125f;

    private NativeExpressAdView adView;
    private ViewGroup adsContainer;
    private float widthHeightRatio;


    public AdsNativeExpressHandler(Context context, ViewGroup adsContainer, AppConstant.AdsType adsType, float widthHeightRatio) {
        super(context, adsType);
        this.adsContainer = adsContainer;
        this.widthHeightRatio = widthHeightRatio;
    }

    @Override
    protected void buildAds() {
        if (this.adsContainer != null && this.adsType != null) {
            int[] adsSize = AppLocalSharedPreferences.getInstance().getAdsSize(adsType);
            if (adsSize[0] == 0 || adsSize[1] == 0) {
                adsContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        int widthPx = adsContainer.getWidth();
                        int adsWidth = (int) CommonUtil.dpFromPx(context, widthPx);
                        int adsHeight = (int) (adsWidth / widthHeightRatio);
                        AppLocalSharedPreferences.getInstance().setAdsSize(adsType, adsWidth, adsHeight);
                        setAds(adsWidth, adsHeight);
                    }
                });
            } else {
                setAds(adsSize[0], adsSize[1]);
            }
        }
    }

    private void setAds(int adsWidth, int adsHeight) {
        String unitId = AppLocalSharedPreferences.getInstance().getAdsId(adsType);
        if (!unitId.trim().isEmpty()) {
            if (adView == null) {
                adView = new NativeExpressAdView(context);
                adView.setAdSize(new AdSize(adsWidth, adsHeight));
                adsContainer.addView(adView);
            }
            adView.setAdUnitId(unitId);
            requestAds(adView);
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
            try {
                adView.loadAd(adRequest);
            }catch (Exception ex){
                ex.printStackTrace();
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

    @Override
    public void destroy() {
        super.destroy();
    }
}
