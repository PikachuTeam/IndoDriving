package com.essential.indodriving.ui.fragment.sign.learn;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.Sign;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.activity.SignMainActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.base.SecondBaseActivity;
import com.essential.indodriving.ui.widget.LearningCardSeekbar;
import com.essential.indodriving.ui.widget.RatingDialog;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;
import com.essential.indodriving.util.ImageHelper;
import com.google.android.gms.ads.AdSize;

import java.util.ArrayList;
import java.util.List;

import tatteam.com.app_common.ads.AdsNativeExpressHandler;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;

/**
 * Created by yue on 08/07/2016.
 */
public class LearnSignByCardFragment extends MyBaseFragment implements
        LearningCardSeekbar.OnProgressChangedListener, View.OnTouchListener, View.OnClickListener {

    private final long ALPHA_ANIM_DURATION = 600;
    private View actionLearningSignByList;
    private ScrollView cardArea;
    private ImageView imageSign;
    private ImageView buttonNext;
    private ImageView buttonPrevious;
    private ImageView buttonZoomIn;
    private TextView textDefinition;
    private LearningCardSeekbar learningCardSeekbar;
    private View lockedArea;
    private View layoutAnswerRoot, layoutQuestionRoot;
    private ViewGroup adsContainer1, adsContainer2, adsContainer3;
    private ImageView blurryImage;
    private List<Sign> signs;
    private String type;
    private int currentPosition;
    private boolean isBlurred;
    private boolean isProVersion;
    private boolean isEnableRateToUnlock;
    private AlphaAnimation alphaAnimation;

    private AdsNativeExpressHandler adsHandler1, adsHandler2;
    private AdsSmallBannerHandler adsHandler3;

    private Handler refreshAdsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (adsHandler1 != null) {
                adsHandler1.refresh();
            }
            if (adsHandler2 != null) {
                adsHandler2.refresh();
            }
            if (adsHandler3 != null) {
                adsHandler3.refresh();
            }
            refreshAdsHandler.sendEmptyMessageDelayed(0, 40000);
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadState();
        getData();
        alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(ALPHA_ANIM_DURATION);
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        setListeners();
        setupToolbar();

        buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.learn_all_button_zoom_in_normal_color)
                , PorterDuff.Mode.SRC_ATOP);

        learningCardSeekbar.setMaxProgress(signs.size());
        Sign sign = signs.get(currentPosition);
        setCardData(sign);

        if (currentPosition == 0) {
            disableButton(buttonPrevious, R.drawable.ic_previous);
            buttonNext.setColorFilter(ContextCompat.getColor(getActivity()
                    , R.color.learn_all_button_normal_color)
                    , PorterDuff.Mode.SRC_ATOP);
        } else if (currentPosition == signs.size() - 1) {
            disableButton(buttonNext, R.drawable.ic_next);
            buttonPrevious.setColorFilter(ContextCompat.getColor(getActivity()
                    , R.color.learn_all_button_normal_color)
                    , PorterDuff.Mode.SRC_ATOP);
        } else {
            buttonPrevious.setColorFilter(ContextCompat.getColor(getActivity()
                    , R.color.learn_all_button_normal_color)
                    , PorterDuff.Mode.SRC_ATOP);
            buttonNext.setColorFilter(ContextCompat.getColor(getActivity()
                    , R.color.learn_all_button_normal_color)
                    , PorterDuff.Mode.SRC_ATOP);
        }

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (getActivity() != null && !isBlurred) {
                            makeBlurEffectIfNeed();
                        }
                    }
                });
        if (!isProVersion) {
            setupADSIfNeeded();
            refreshAdsHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    public void refreshUI() {
        super.refreshUI();
        loadState();
        if (!isEnableRateToUnlock || isRated || isProVersion) {
            lockedArea.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isEnableRateToUnlock || isRated || isProVersion) {
            lockedArea.setVisibility(View.GONE);
        }
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
        if (adsHandler3 != null) {
            adsHandler3.destroy();
        }
        super.onDestroyView();
    }

    @Override
    protected String getTitle() {
        return type;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_sign_by_card;
    }

    @Override
    public void onProgressChanged(int progress) {
        currentPosition = progress;
        setCardData(signs.get(currentPosition));
        if (progress == 0) {
            disableButton(buttonPrevious, R.drawable.ic_previous);
            if (!buttonNext.isEnabled()) enableButton(buttonNext, R.drawable.ic_next);
        } else if (progress == signs.size() - 1) {
            disableButton(buttonNext, R.drawable.ic_next);
            if (!buttonPrevious.isEnabled()) enableButton(buttonPrevious, R.drawable.ic_previous);
        } else {
            if (!buttonNext.isEnabled()) enableButton(buttonNext, R.drawable.ic_next);
            if (!buttonPrevious.isEnabled()) enableButton(buttonPrevious, R.drawable.ic_previous);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == buttonZoomIn) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
                buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity()
                        , R.color.learn_all_button_zoom_in_highlight_color)
                        , PorterDuff.Mode.SRC_ATOP);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
                buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity()
                        , R.color.learn_all_button_zoom_in_normal_color)
                        , PorterDuff.Mode.SRC_ATOP);
                Sign sign = signs.get(currentPosition);
                ZoomInImageDialog dialog = new ZoomInImageDialog(getActivity(), sign.image);
                dialog.show();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNext) {
            if (currentPosition < signs.size() - 1) {
                currentPosition++;
                if (currentPosition == signs.size() - 1) {
                    disableButton(buttonNext, R.drawable.ic_next);
                }
                if (currentPosition != 0) {
                    if (!buttonPrevious.isEnabled()) {
                        enableButton(buttonPrevious, R.drawable.ic_previous);
                    }
                }
                setCardData(signs.get(currentPosition));
                getMyBaseActivity().showBigAdsIfNeeded();
            }
        } else if (v == buttonPrevious) {
            if (currentPosition > 0) {
                currentPosition--;
                if (currentPosition == 0) {
                    disableButton(buttonPrevious, R.drawable.ic_previous);
                }
                if (currentPosition != signs.size()) {
                    if (!buttonNext.isEnabled()) {
                        enableButton(buttonNext, R.drawable.ic_next);
                    }
                }
                setCardData(signs.get(currentPosition));
                getMyBaseActivity().showBigAdsIfNeeded();
            }
        } else if (v == imageSign) {
            ZoomInImageDialog dialog = new ZoomInImageDialog(
                    getActivity(), signs.get(currentPosition).image);
            dialog.show();
        } else if (v == lockedArea) {
            RatingDialog ratingDialog = new RatingDialog(getActivity(), HomeActivity.defaultFont);
            ratingDialog.show();
            ratingDialog.addListener(new BaseConfirmDialog.OnConfirmDialogButtonClickListener() {
                @Override
                public void onConfirmDialogButtonClick(BaseConfirmDialog.ConfirmButton button
                        , int type, BaseConfirmDialog dialog) {
                    dialog.dismiss();
                    switch (button) {
                        case OK:
                            rateApp();
//                            shareFacebook();
                            break;
                        case CANCEL:
                            break;
                    }
                }
            });
        }
    }

    private void findViews(View rootView) {
        learningCardSeekbar = (LearningCardSeekbar) rootView.findViewById(R.id.learning_card_seekbar);
        cardArea = (ScrollView) rootView.findViewById(R.id.card_area);
        buttonPrevious = (ImageView) rootView.findViewById(R.id.button_previous);
        buttonNext = (ImageView) rootView.findViewById(R.id.button_next);
        buttonZoomIn = (ImageView) rootView.findViewById(R.id.button_zoom_in);
        lockedArea = rootView.findViewById(R.id.locked_area);
        blurryImage = (ImageView) rootView.findViewById(R.id.image_blur);
        layoutAnswerRoot = rootView.findViewById(R.id.layout_answer_root);
        layoutQuestionRoot = rootView.findViewById(R.id.layout_question_root);
        adsContainer1 = (ViewGroup) rootView.findViewById(R.id.adsContainer1);
        adsContainer2 = (ViewGroup) rootView.findViewById(R.id.adsContainer2);
        adsContainer3 = (ViewGroup) rootView.findViewById(R.id.adsContainer3);
        imageSign = (ImageView) rootView.findViewById(R.id.image_sign);
        textDefinition = (TextView) rootView.findViewById(R.id.text_definition);
        ((TextView) rootView.findViewById(R.id.text_press_to_unlock)).
                setTypeface(HomeActivity.defaultFont);
    }

    private void getData() {
        if (containHolder(LearnSignByListFragment.HOLDER_SIGNS_LIST)) {
            signs = (List<Sign>) getHolder(LearnSignByListFragment.HOLDER_SIGNS_LIST);
            if (!isProVersion) {
                List<Sign> temp = new ArrayList<>();
                temp.addAll(signs);
                addAds(temp);
                signs = temp;
            }
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString(Constants.BUNDLE_SIGN_TYPE);
            currentPosition = bundle.getInt(Constants.BUNDLE_CURRENT_POSITION);
        }
    }

    private void setListeners() {
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnTouchListener(this);
        buttonPrevious.setOnTouchListener(this);
        buttonZoomIn.setOnTouchListener(this);
        lockedArea.setOnClickListener(this);
        learningCardSeekbar.setOnProgressChangedListener(this);
        imageSign.setOnClickListener(this);
    }

    private void setupToolbar() {
        actionLearningSignByList =
                ((SignMainActivity) getMyBaseActivity()).getActionLearningSignByList();
        if (actionLearningSignByList != null) {
            actionLearningSignByList.setVisibility(View.VISIBLE);
            actionLearningSignByList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
        }
        ((SignMainActivity) getMyBaseActivity()).getActionLearningSignByCard().setVisibility(View.GONE);
    }

    private void setCardData(Sign sign) {
        layoutAnswerRoot.setVisibility(!sign.isAds ? View.VISIBLE : View.GONE);
        layoutQuestionRoot.setVisibility(!sign.isAds ? View.VISIBLE : View.GONE);
//        adsContainer1.setVisibility(sign.isAds ? View.VISIBLE : View.INVISIBLE);
//        adsContainer2.setVisibility(sign.isAds ? View.VISIBLE : View.INVISIBLE);

        cardArea.setVisibility(!sign.isAds ? View.VISIBLE : View.GONE);
        adsContainer3.setVisibility(sign.isAds ? View.VISIBLE : View.INVISIBLE);

        cardArea.scrollTo(0, 0);
        if (sign.isAds) {
            setupADSIfNeeded();
            learningCardSeekbar.setProgress(currentPosition + 1, false);
        } else {
            Glide.with(getActivity()).load(sign.image).dontAnimate().dontTransform().into(imageSign);
            textDefinition.setText(sign.definition);
            if (!isProVersion) {
                int tmp = currentPosition + 1;
                learningCardSeekbar.setProgress((tmp - tmp / Constants.LEARN_ALL_ADS_BREAK), tmp);
            } else {
                learningCardSeekbar.setProgress(currentPosition + 1, true);
            }
            isBlurred = false;
        }
    }

    private void setupADSIfNeeded() {
        if (adsHandler1 == null && false) {
            adsHandler1 = new AdsNativeExpressHandler(getActivity(),
                    adsContainer1, SecondBaseActivity.ADS_NATIVE_EXPRESS_CONTENT,
                    AdsNativeExpressHandler.WIDTH_HEIGHT_RATIO_SMALL);
            adsHandler1.setup();
        }

        if (adsHandler2 == null && false) {
            adsHandler2 = new AdsNativeExpressHandler(getActivity(),
                    adsContainer2, SecondBaseActivity.ADS_NATIVE_EXPRESS_INSTALL,
                    AdsNativeExpressHandler.WIDTH_HEIGHT_RATIO_SMALL);
            adsHandler2.setup();
        }
        if (adsHandler3 == null) {
            adsHandler3 = new AdsSmallBannerHandler(getActivity(), adsContainer3, SecondBaseActivity.ADS_SMALL, AdSize.MEDIUM_RECTANGLE);
            adsHandler3.setup();
        }
    }

    private void makeBlurEffectIfNeed() {
        if (isEnableRateToUnlock && !isProVersion && !isRated) {
            if (currentPosition >= Constants.LOCK_START_INDEX) {
                if (lockedArea.getVisibility() == View.GONE) {
                    lockedArea.setVisibility(View.VISIBLE);
                }
                try {
                    Bitmap blurBitmap = ImageHelper.blur(getActivity(),
                            ImageHelper.captureView(textDefinition));
                    blurryImage.setImageBitmap(blurBitmap);
                    lockedArea.startAnimation(alphaAnimation);
                } catch (Exception e) {
                    blurryImage.setBackgroundResource(R.drawable.default_blur_background);
                }
            } else {
                if (lockedArea.getVisibility() == View.VISIBLE) {
                    lockedArea.setVisibility(View.GONE);
                }
            }
        }
        isBlurred = true;
    }

    private void enableButton(ImageView button, int image) {
        button.setEnabled(true);
        button.setImageResource(image);
        button.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.learn_all_button_normal_color)
                , PorterDuff.Mode.SRC_ATOP);
    }

    private void disableButton(ImageView button, int image) {
        button.setEnabled(false);
        button.setImageResource(image);
        button.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.learn_all_button_disabled_color)
                , PorterDuff.Mode.SRC_ATOP);
    }

    private void saveState() {
        MySetting.getInstance().saveSignPosition(type, currentPosition);
    }

    private void loadState() {
        isProVersion = MySetting.getInstance().isProVersion();
        isRated = isProVersion ? true : MySetting.getInstance().isRated();
        isEnableRateToUnlock = MySetting.getInstance().isEnableRateToUnlock();
    }

    private void addAds(List<Sign> signs) {
        int count = 0;
        int size = signs.size();
        for (int i = 0; i < size; i++) {
            count++;
            if (count % Constants.LEARN_ALL_ADS_BREAK == 0) {
                Sign sign = new Sign();
                sign.isAds = true;
                signs.add(i, sign);
                count++;
                size++;
                i++;
            }
        }
    }
}
