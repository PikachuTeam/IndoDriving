package com.essential.indodriving.ui.fragment.theory.learn;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.data.driving.Question;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.base.SecondBaseActivity;
import com.essential.indodriving.ui.widget.LearningCardSeekbar;
import com.essential.indodriving.ui.widget.ModifyAnswerDialog;
import com.essential.indodriving.ui.widget.RatingDialog;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;
import com.essential.indodriving.util.ImageHelper;
import com.google.android.gms.ads.AdSize;

import java.text.MessageFormat;
import java.util.ArrayList;

import tatteam.com.app_common.ads.AdsNativeExpressHandler;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;

/**
 * Created by dongc_000 on 2/27/2016.
 */
public class LearnAllFragment extends MyBaseFragment implements
        View.OnClickListener, View.OnTouchListener, ModifyAnswerDialog.OnAnswerModifiedListener,
        LearningCardSeekbar.OnProgressChangedListener {

    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7f;
    private static final long ALPHA_ANIM_DURATION = 600;
    private ImageView cardQuestionImage;
    private TextView cardTextViewQuestion;
    private ScrollView cardArea;
    private TextView textViewAnswerA;
    private TextView textViewAnswerB;
    private TextView textViewAnswerC;
    private TextView textViewAnswerD;
    private ImageView buttonPrevious;
    private ImageView buttonNext;
    private ImageView buttonZoomIn;
    private RelativeLayout imageArea;
    private LearningCardSeekbar learningCardSeekbar;
    private RelativeLayout lockedArea;
    private ImageView blurryImage;
    private LinearLayout answerArea;
    private View layoutAnswerRoot, layoutQuestionRoot;
    private ViewGroup adsContainer1, adsContainer2, adsContainer3;
    private ArrayList<Question> questions;
    private int type;
    private int currentPosition;
    private int mTrialTimesLeft;
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
        getData();
        questions = DrivingDataSource.getAllQuestionByType(type);
        loadState();
        if (!isProVersion) {
            addADS(questions);
        }
        alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(ALPHA_ANIM_DURATION);
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        setListeners();
        buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.learn_all_button_zoom_in_normal_color)
                , PorterDuff.Mode.SRC_ATOP);
        if (currentPosition >= questions.size()) {
            currentPosition = 0;
        }
        learningCardSeekbar.setMaxProgress(questions.size());
        Question question = questions.get(currentPosition);
        setCardData(question);
        modifyToolbar();
        if (currentPosition == 0) {
            disableButton(buttonPrevious, R.drawable.ic_previous);
            buttonNext.setColorFilter(ContextCompat.getColor(getActivity()
                    , R.color.learn_all_button_normal_color)
                    , PorterDuff.Mode.SRC_ATOP);
        } else if (currentPosition == questions.size() - 1) {
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
        switch (type) {
            case DrivingDataSource.TYPE_SIM_A:
                return MessageFormat.format(getString(R.string.learn_sim_a), "");
            case DrivingDataSource.TYPE_SIM_A_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_a_umum), "");
            case DrivingDataSource.TYPE_SIM_B1:
                return MessageFormat.format(getString(R.string.learn_sim_b1), "");
            case DrivingDataSource.TYPE_SIM_B1_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_b1_umum), "");
            case DrivingDataSource.TYPE_SIM_B2:
                return MessageFormat.format(getString(R.string.learn_sim_b2), "");
            case DrivingDataSource.TYPE_SIM_B2_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_b2_umum), "");
            case DrivingDataSource.TYPE_SIM_C:
                return MessageFormat.format(getString(R.string.learn_sim_c), "");
            default:
                return MessageFormat.format(getString(R.string.learn_sim_d), "");
        }
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_all;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected boolean enableButtonModifyAnswer() {
        return currentPosition >= 10 ? isRated : true;
    }

    @Override
    protected void onMenuItemClick(int id) {
        ModifyAnswerDialog dialog = new ModifyAnswerDialog(getActivity(),
                questions.get(currentPosition));
        dialog.setOnAnswerModifiedListener(this);
        dialog.show();
    }

    @Override
    public void refreshUI() {
        super.refreshUI();
        loadState();
        if (!isEnableRateToUnlock || isRated || isProVersion) {
            lockedArea.setVisibility(View.GONE);
            getButtonModifyAnswer().setVisibility(View.VISIBLE);
        }
        modifyToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isEnableRateToUnlock || isRated || isProVersion) {
            lockedArea.setVisibility(View.GONE);
            getButtonModifyAnswer().setVisibility(View.VISIBLE);
        }
        modifyToolbar();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNext) {
            if (currentPosition < questions.size() - 1) {
                currentPosition++;
                if (currentPosition == questions.size() - 1) {
                    disableButton(buttonNext, R.drawable.ic_next);
                }
                if (currentPosition != 0) {
                    if (!buttonPrevious.isEnabled()) {
                        enableButton(buttonPrevious, R.drawable.ic_previous);
                    }
                }
                modifyToolbar();
                setCardData(questions.get(currentPosition));
                getMyBaseActivity().showBigAdsIfNeeded();
            }
        } else if (v == buttonPrevious) {
            if (currentPosition > 0) {
                currentPosition--;
                if (currentPosition == 0) {
                    disableButton(buttonPrevious, R.drawable.ic_previous);
                } else if (currentPosition != questions.size()) {
                    if (!buttonNext.isEnabled()) {
                        enableButton(buttonNext, R.drawable.ic_next);
                    }
                }
                modifyToolbar();
                setCardData(questions.get(currentPosition));
                getMyBaseActivity().showBigAdsIfNeeded();
            }
        } else if (v == cardQuestionImage) {
            ZoomInImageDialog dialog = new ZoomInImageDialog(
                    getActivity(), questions.get(currentPosition).imageData);
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
                Question question = questions.get(currentPosition);
                ZoomInImageDialog dialog = new ZoomInImageDialog(getActivity(), question.imageData);
                dialog.show();
            }
        }
        return false;
    }

    @Override
    public void onAnswerModified(int correctAnswer) {
        resetAllAnswers();
        makeCorrectAnswer(correctAnswer);
        questions.get(currentPosition).fixedAnswer = correctAnswer;
    }

    @Override
    public void onProgressChanged(int progress) {
        currentPosition = progress;
        setCardData(questions.get(currentPosition));
        modifyToolbar();
        if (progress == 0) {
            disableButton(buttonPrevious, R.drawable.ic_previous);
            if (!buttonNext.isEnabled()) enableButton(buttonNext, R.drawable.ic_next);
        } else if (progress == questions.size() - 1) {
            disableButton(buttonNext, R.drawable.ic_next);
            if (!buttonPrevious.isEnabled()) enableButton(buttonPrevious, R.drawable.ic_previous);
        } else {
            if (!buttonNext.isEnabled()) enableButton(buttonNext, R.drawable.ic_next);
            if (!buttonPrevious.isEnabled()) enableButton(buttonPrevious, R.drawable.ic_previous);
        }
    }

    private void findViews(View rootView) {
        learningCardSeekbar = (LearningCardSeekbar) rootView.findViewById(R.id.learning_card_seekbar);
        cardQuestionImage = (ImageView) rootView.findViewById(R.id.cardQuestionImage);
        cardArea = (ScrollView) rootView.findViewById(R.id.cardArea);
        cardTextViewQuestion = (TextView) rootView.findViewById(R.id.cardTextViewQuestion);
        textViewAnswerA = (TextView) rootView.findViewById(R.id.textViewAnswerA);
        textViewAnswerB = (TextView) rootView.findViewById(R.id.textViewAnswerB);
        textViewAnswerC = (TextView) rootView.findViewById(R.id.textViewAnswerC);
        textViewAnswerD = (TextView) rootView.findViewById(R.id.textViewAnswerD);
        buttonPrevious = (ImageView) rootView.findViewById(R.id.buttonPrevious);
        buttonNext = (ImageView) rootView.findViewById(R.id.buttonNext);
        buttonZoomIn = (ImageView) rootView.findViewById(R.id.buttonZoomIn);
        imageArea = (RelativeLayout) rootView.findViewById(R.id.imageArea);
        lockedArea = (RelativeLayout) rootView.findViewById(R.id.lockedArea);
        answerArea = (LinearLayout) rootView.findViewById(R.id.answerArea);
        blurryImage = (ImageView) rootView.findViewById(R.id.blurryImage);
        ((TextView) rootView.findViewById(R.id.textViewPressToUnlock)).
                setTypeface(HomeActivity.defaultFont);
        layoutAnswerRoot = rootView.findViewById(R.id.layout_answer_root);
        layoutQuestionRoot = rootView.findViewById(R.id.layout_question_root);
        adsContainer1 = (ViewGroup) rootView.findViewById(R.id.adsContainer1);
        adsContainer2 = (ViewGroup) rootView.findViewById(R.id.adsContainer2);
        adsContainer3 = (ViewGroup) rootView.findViewById(R.id.adsContainer3);
    }

    private void setListeners() {
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnTouchListener(this);
        buttonPrevious.setOnTouchListener(this);
        cardQuestionImage.setOnClickListener(this);
        buttonZoomIn.setOnTouchListener(this);
        lockedArea.setOnClickListener(this);
        learningCardSeekbar.setOnProgressChangedListener(this);
    }

    private void saveState() {
        MySetting.getInstance().savePosition(type, currentPosition);
        MySetting.getInstance().saveTrialTimes(mTrialTimesLeft);
    }

    private void loadState() {
        currentPosition = MySetting.getInstance().loadPosition(type);
        if (currentPosition > questions.size() - 1) currentPosition = questions.size() - 1;
        isProVersion = MySetting.getInstance().isProVersion();
        isRated = isProVersion ? true : MySetting.getInstance().isRated();
        isEnableRateToUnlock = MySetting.getInstance().isEnableRateToUnlock();
        mTrialTimesLeft = MySetting.getInstance().loadTrialTimesLeft();
    }

    private void setCardData(Question question) {
        layoutAnswerRoot.setVisibility(!question.isAds ? View.VISIBLE : View.GONE);
        layoutQuestionRoot.setVisibility(!question.isAds ? View.VISIBLE : View.GONE);
//        adsContainer1.setVisibility(question.isAds ? View.VISIBLE : View.INVISIBLE);
//        adsContainer2.setVisibility(question.isAds ? View.VISIBLE : View.INVISIBLE);

        cardArea.setVisibility(!question.isAds ? View.VISIBLE : View.GONE);
        adsContainer3.setVisibility(question.isAds ? View.VISIBLE : View.INVISIBLE);

        cardArea.scrollTo(0, 0);
        if (question.isAds) {
            setupADSIfNeeded();
            learningCardSeekbar.setProgress(currentPosition + 1, false);
        } else {
            if (question.imageData == null) {
                imageArea.setVisibility(View.GONE);
            } else {
                imageArea.setVisibility(View.VISIBLE);
                Glide.with(LearnAllFragment.this).load(question.imageData).dontAnimate().dontTransform().into(cardQuestionImage);
            }
            cardTextViewQuestion.setText(question.question);
            if (question.answer1 != null) {
                textViewAnswerA.setVisibility(View.VISIBLE);
                textViewAnswerA.setText("A. " + question.answer1);
            } else {
                textViewAnswerA.setVisibility(View.GONE);
            }
            if (question.answer2 != null) {
                textViewAnswerB.setVisibility(View.VISIBLE);
                textViewAnswerB.setText("B. " + question.answer2);
            } else {
                textViewAnswerB.setVisibility(View.GONE);
            }
            if (question.answer3 != null) {
                textViewAnswerC.setVisibility(View.VISIBLE);
                textViewAnswerC.setText("C. " + question.answer3);
            } else {
                textViewAnswerC.setVisibility(View.GONE);
            }
            if (question.answer4 != null) {
                textViewAnswerD.setVisibility(View.VISIBLE);
                textViewAnswerD.setText("D. " + question.answer4);
            } else {
                textViewAnswerD.setVisibility(View.GONE);
            }
            resetAllAnswers();
            makeCorrectAnswer(
                    question.fixedAnswer != -1 ? question.fixedAnswer : question.correctAnswer);
            if (!isProVersion) {
                int tmp = currentPosition + 1;
                learningCardSeekbar.setProgress((tmp - tmp / Constants.LEARN_ALL_ADS_BREAK), tmp);
            } else {
                learningCardSeekbar.setProgress(currentPosition + 1, true);
            }
            isBlurred = false;
        }
    }

    private void resetAllAnswers() {
        textViewAnswerA.setTextColor(
                ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
        textViewAnswerB.setTextColor(
                ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
        textViewAnswerC.setTextColor(
                ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
        textViewAnswerD.setTextColor(
                ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
    }

    private void makeCorrectAnswer(int correctAnswer) {
        switch (correctAnswer) {
            case DrivingDataSource.ANSWER_A:
                textViewAnswerA.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DrivingDataSource.ANSWER_B:
                textViewAnswerB.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DrivingDataSource.ANSWER_C:
                textViewAnswerC.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DrivingDataSource.ANSWER_D:
                textViewAnswerD.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_A);
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

    private void modifyToolbar() {
        if (!isRated && !isProVersion) {
            if (currentPosition >= 10 &&
                    getButtonModifyAnswer().getVisibility() == View.VISIBLE) {
                getButtonModifyAnswer().setVisibility(View.GONE);
            } else if (currentPosition < 10 &&
                    getButtonModifyAnswer().getVisibility() == View.GONE) {
                getButtonModifyAnswer().setVisibility(View.VISIBLE);
            }
        } else {
            getButtonModifyAnswer().setVisibility(View.VISIBLE);
        }

        if (questions.get(currentPosition).isAds) {
            getButtonModifyAnswer().setVisibility(View.GONE);
        }
    }

    private void addADS(ArrayList<Question> questions) {
        int count = 0;
        int size = questions.size();
        for (int i = 0; i < size; i++) {
            count++;
            if (count % Constants.LEARN_ALL_ADS_BREAK == 0) {
                Question question = new Question();
                question.isAds = true;
                questions.add(i, question);
                count++;
                size++;
                i++;
            }
        }
    }

    private void setupADSIfNeeded() {
        if (adsHandler1 == null && false) {
            adsHandler1 = new AdsNativeExpressHandler(getActivity(), adsContainer1, SecondBaseActivity.ADS_NATIVE_EXPRESS_CONTENT, AdsNativeExpressHandler.WIDTH_HEIGHT_RATIO_SMALL);
            adsHandler1.setup();
        }

        if (adsHandler2 == null && false) {
            adsHandler2 = new AdsNativeExpressHandler(getActivity(), adsContainer2, SecondBaseActivity.ADS_NATIVE_EXPRESS_INSTALL, AdsNativeExpressHandler.WIDTH_HEIGHT_RATIO_SMALL);
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
                            ImageHelper.captureView(answerArea));
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
}
