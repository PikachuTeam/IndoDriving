package com.essential.indodriving.ui.fragment.sign.match;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.Sign;
import com.essential.indodriving.data.sign.SignDataSource;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.base.SecondBaseActivity;
import com.google.android.gms.ads.AdSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tatteam.com.app_common.ads.AdsSmallBannerHandler;

/**
 * Created by admin on 7/11/2016.
 */
public class SignMatchFragment extends MyBaseFragment {
    private String type = "";
    private LinearLayout layoutMatching;
    private LinearLayout layoutImages;
    private LinearLayout layoutDefinitions;
    private RelativeLayout btnContinue;
    private TextView tvContinue;
    private List<MatchPage> matchPages;
    private List<ImageWrapper> imageWrappers;
    private List<DefinitionWrapper> definitionWrappers;
    private ImageWrapper selectedImage;
    private DefinitionWrapper selectedDefinition;
    private int pageIndex = 0;
    private ViewGroup adsContainer1, adsContainer2;
    private View layoutContinue;
    private boolean isProVersion;
    private AdsSmallBannerHandler adsHandler1;
    private AdsSmallBannerHandler adsHandler2;

    private Handler refreshAdsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (adsHandler1 != null) {
                adsHandler1.refresh();
            }
            if (adsHandler2 != null) {
                adsHandler2.refresh();
            }
            refreshAdsHandler.sendEmptyMessageDelayed(0, 40000);
            return false;
        }
    });

    @Override
    protected String getTitle() {
        return getString(R.string.matching) + " " + type;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_sign_matching;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isProVersion = MySetting.getInstance().isProVersion();
        type = getArguments().getString(Constants.BUNDLE_SIGN_TYPE);
        List<Sign> signs = SignDataSource.getSigns(type, false, -1);
        Collections.shuffle(signs);
        matchPages = makeData(signs);
    }

    @Override
    public void onDestroyView() {
        refreshAdsHandler.removeCallbacksAndMessages(null);
        if (adsHandler1 != null) {
            adsHandler1.destroy();
        }
        if (adsHandler2 != null) {
            adsHandler2.destroy();
        }
        super.onDestroyView();
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        setupAdsIfNeed();
        renderUI();
    }

    @Override
    protected boolean enableButtonShare() {
        return true;
    }


    private void findViews(View rootView) {
        layoutImages = (LinearLayout) rootView.findViewById(R.id.layout_images);
        layoutDefinitions = (LinearLayout) rootView.findViewById(R.id.layout_definitions);
        btnContinue = (RelativeLayout) rootView.findViewById(R.id.btn_continue);
        layoutMatching = (LinearLayout) rootView.findViewById(R.id.layout_matching);
        tvContinue = (TextView) rootView.findViewById(R.id.tv_continue);
        adsContainer1 = (ViewGroup) rootView.findViewById(R.id.adsContainer1);
        adsContainer2 = (ViewGroup) rootView.findViewById(R.id.adsContainer2);
        layoutContinue = rootView.findViewById(R.id.layout_continue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renderUI();
            }
        });
    }

    private void renderUI() {
        layoutMatching.setVisibility(View.VISIBLE);
        layoutContinue.setVisibility(View.GONE);
        layoutImages.removeAllViews();
        layoutDefinitions.removeAllViews();
        imageWrappers = new ArrayList<>();
        definitionWrappers = new ArrayList<>();
        MatchPage page = matchPages.get(pageIndex);
        for (int i = 0; i < page.signImages.size(); i++) {
            ImageWrapper imageWrapper = new ImageWrapper(getActivity(), layoutImages, page.signImages.get(i));
            DefinitionWrapper definitionWrapper = new DefinitionWrapper(getActivity(), layoutDefinitions, page.signDefinitions.get(i));

            imageWrappers.add(imageWrapper);
            definitionWrappers.add(definitionWrapper);
        }

        pageIndex++;
        if (pageIndex == matchPages.size()) {
            pageIndex = 0;
            tvContinue.setText(R.string.reset_matching);
        } else {
            tvContinue.setText(R.string.continue_matching);
        }
    }

    private void setupAdsIfNeed() {
        if (!isProVersion) {
            adsHandler1 = new AdsSmallBannerHandler(getActivity(), adsContainer1, SecondBaseActivity.ADS_SMALL, AdSize.LARGE_BANNER);
            adsHandler1.setup();

            adsHandler2 = new AdsSmallBannerHandler(getActivity(), adsContainer2, SecondBaseActivity.ADS_SMALL, AdSize.LARGE_BANNER);
            adsHandler2.setup();

            refreshAdsHandler.sendEmptyMessage(0);
        }
    }

    private List<MatchPage> makeData(List<Sign> signs) {
        List<MatchPage> matchPages = new ArrayList<>();
        for (int i = 0; i < signs.size(); i++) {
            if (i % 4 == 0) {
                matchPages.add(new MatchPage());
            }
            MatchPage page = matchPages.get(matchPages.size() - 1);
            page.signDefinitions.add(new SignDefinition(signs.get(i).id, signs.get(i).definition));
            page.signImages.add(new SignImage(signs.get(i).id, signs.get(i).image));
        }

        MatchPage last = matchPages.get(matchPages.size() - 1);
        if (last.signImages.size() == 1) {
            MatchPage beforeLast = matchPages.get(matchPages.size() - 2);
            beforeLast.signImages.add(last.signImages.get(0));
            beforeLast.signDefinitions.add(last.signDefinitions.get(0));
            matchPages.remove(last);
        }

        for (MatchPage page : matchPages) {
            page.shuffle();
        }
        return matchPages;
    }

    private void setCorrectHighLight(List<? extends ItemWrapper> items, ItemWrapper selected) {
        for (ItemWrapper item : items) {
            item.setCorrectHighLight(false);
        }
        if (selected != null) {
            selected.setCorrectHighLight(true);
        }
    }

    private void doMatching() {
        if (selectedImage.signImage.id == selectedDefinition.signDefinition.id) {
            setCorrectHighLight(imageWrappers, selectedImage);
            setCorrectHighLight(definitionWrappers, selectedDefinition);

            selectedImage.matched();
            selectedDefinition.matched();

        } else {
            selectedImage.setIncorrectHighLight();
            selectedDefinition.setIncorrectHighLight();

            selectedImage.notMatched();
            selectedDefinition.notMatched();
        }

        selectedImage = null;
        selectedDefinition = null;
    }

    private void onImageItemClick(ImageWrapper imageWrapper) {
        selectedImage = imageWrapper;
        if (selectedDefinition == null) {
            setCorrectHighLight(imageWrappers, selectedImage);
        } else {
            doMatching();
        }
    }

    private void onDefinitionItemClick(DefinitionWrapper definitionWrapper) {
        selectedDefinition = definitionWrapper;
        if (selectedImage == null) {
            setCorrectHighLight(definitionWrappers, selectedDefinition);
        } else {
            doMatching();
        }
    }

    private void onCorrectMatching() {
        if (layoutImages.getChildCount() == 0 && layoutDefinitions.getChildCount() == 0) {
            layoutContinue.setVisibility(View.VISIBLE);
            layoutMatching.setVisibility(View.GONE);
        }
    }

    private class ImageWrapper extends ItemWrapper {
        public SignImage signImage;

        public ImageWrapper(Context context, ViewGroup parent, SignImage signImage) {
            super(context, R.layout.item_sign_image, parent);
            this.signImage = signImage;
            ImageView sign = (ImageView) item.findViewById(R.id.iv_sign_image);
            Glide.with(context).load(signImage.image).dontAnimate().dontTransform().into(sign);
        }

        @Override
        public void onClick(View view) {
            onImageItemClick(ImageWrapper.this);
        }
    }

    private class DefinitionWrapper extends ItemWrapper {
        public SignDefinition signDefinition;

        public DefinitionWrapper(Context context, ViewGroup parent, SignDefinition signDefinition) {
            super(context, R.layout.item_sign_definition, parent);
            this.signDefinition = signDefinition;
            TextView definition = (TextView) item.findViewById(R.id.tv_sign_definition);
            definition.setText(signDefinition.definition);
        }

        @Override
        public void onClick(View view) {
            onDefinitionItemClick(DefinitionWrapper.this);
        }

    }


    public static class MatchPage {
        List<SignImage> signImages = new ArrayList<>();
        List<SignDefinition> signDefinitions = new ArrayList<>();

        public void shuffle() {
            Collections.shuffle(signImages);
            Collections.shuffle(signDefinitions);
        }
    }

    public static class SignImage {
        int id;
        byte[] image;

        public SignImage(int id, byte[] image) {
            this.id = id;
            this.image = image;
        }
    }

    public static class SignDefinition {
        int id;
        String definition;

        public SignDefinition(int id, String definition) {
            this.id = id;
            this.definition = definition;
        }
    }

    private abstract class ItemWrapper implements View.OnClickListener {
        public View item;
        public RelativeLayout hightlight;
        public ViewGroup parent;

        public ItemWrapper(Context context, int resLayout, ViewGroup parent) {
            this.parent = parent;
            item = LayoutInflater.from(context).inflate(resLayout, parent, false);
            hightlight = (RelativeLayout) item.findViewById(R.id.card_hightlight);
            parent.addView(item);
            item.setOnClickListener(this);
        }

        public void setCorrectHighLight(boolean isHighLight) {
            if (isHighLight) {
                hightlight.setVisibility(View.VISIBLE);
                hightlight.setBackgroundResource(R.drawable.hightlight_sign_match);
            } else {
                hightlight.setVisibility(View.GONE);
            }
        }

        public void setIncorrectHighLight() {
            hightlight.setVisibility(View.VISIBLE);
            hightlight.setBackgroundResource(R.drawable.hightlight_sign_not_match);
        }

        public void matched() {
            item.postDelayed(new Runnable() {
                @Override
                public void run() {
                    parent.removeView(item);
                    onCorrectMatching();
                }
            }, 500);
        }

        public void notMatched() {
            item.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hightlight.setVisibility(View.GONE);
                }
            }, 500);
        }


    }


}
