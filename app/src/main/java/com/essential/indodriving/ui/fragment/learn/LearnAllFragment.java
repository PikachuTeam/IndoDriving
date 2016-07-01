package com.essential.indodriving.ui.fragment.learn;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseActivity;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.widget.ModifyAnswerDialog;
import com.essential.indodriving.ui.widget.RatingDialog;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;

import java.text.MessageFormat;
import java.util.ArrayList;

import tatteam.com.app_common.ads.AdsNativeExpressHandler;

/**
 * Created by dongc_000 on 2/27/2016.
 */
public class LearnAllFragment extends MyBaseFragment implements
        View.OnClickListener, View.OnTouchListener, ModifyAnswerDialog.OnAnswerModifiedListener {

    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7f;
    private static final long ALPHA_ANIM_DURATION = 600;
    private ImageView cardQuestionImage;
    private TextView cardTextViewQuestion;
    private TextView textViewProgress;
    private ProgressBar readingProgress;
    private TextView textViewAnswerA;
    private TextView textViewAnswerB;
    private TextView textViewAnswerC;
    private TextView textViewAnswerD;
    private ImageView buttonPrevious;
    private ImageView buttonNext;
    private ImageView buttonZoomIn;
    private RelativeLayout imageArea;
    private RelativeLayout indicator;
    private RelativeLayout progressBarContainer;
    private RelativeLayout lockedArea;
    private ImageView blurryImage;
    private LinearLayout answerArea;
    private View layoutAnswerRoot, layoutQuestionRoot;
    private ViewGroup adsContainer1, adsContainer2;
    private ArrayList<Question> questions;
    private int type;
    private int currentPosition;
    private int mTrialTimesLeft;
    private float indicatorPosition;
    private float indicatorPositionOffset;
    private boolean isFirst;
    private boolean isRated;
    private boolean isProVersion;
    private boolean isEnableRateToUnlock;
    private AlphaAnimation alphaAnimation;

    private AdsNativeExpressHandler adsHandler1, adsHandler2;

    private View.OnTouchListener mIndicatorTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float realX = event.getRawX() - getResources().getDimension(R.dimen.common_size_20);
                    float ratio = realX / readingProgress.getWidth();
                    int numberOfQuestions = questions.size();
                    int tmp = (int) (ratio * numberOfQuestions);
                    if (tmp > 0 && tmp < questions.size()) {
                        currentPosition = tmp;
                        setCardData(questions.get(currentPosition));
                        if (currentPosition == 0) {
                            disableButton(buttonPrevious, R.drawable.ic_previous);
                            if (!buttonNext.isEnabled()) {
                                enableButton(buttonNext, R.drawable.ic_next);
                            }
                        } else if (currentPosition == numberOfQuestions - 1) {
                            disableButton(buttonNext, R.drawable.ic_next);
                            if (!buttonPrevious.isEnabled()) {
                                enableButton(buttonPrevious, R.drawable.ic_previous);
                            }
                        } else if (currentPosition > 0 && currentPosition < numberOfQuestions) {
                            if (!buttonNext.isEnabled()) {
                                enableButton(buttonNext, R.drawable.ic_next);
                            }
                            if (!buttonPrevious.isEnabled()) {
                                enableButton(buttonPrevious, R.drawable.ic_previous);
                            }
                        }
                        modifyToolbar();
                    }
                    break;
            }
            return false;
        }
    };

    private Handler refreshAdsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (adsHandler1 != null) {
                adsHandler1.refresh();
            }
            if (adsHandler2 != null) {
                adsHandler2.refresh();
            }
            refreshAdsHandler.sendEmptyMessageDelayed(0, 30000);
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getAllQuestionByType(type);
        loadState();
        if (!isProVersion) {
            addADS(questions);
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        isFirst = true;
        alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(ALPHA_ANIM_DURATION);
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.learn_all_button_zoom_in_normal_color)
                , PorterDuff.Mode.SRC_ATOP);
        if (currentPosition >= questions.size()) {
            currentPosition = 0;
        }
        Question question = questions.get(currentPosition);
        setCardData(question);
        modifyToolbar();
        readingProgress.setMax(questions.size());
        readingProgress.setProgress(currentPosition + 1);
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
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirst) {
                    isFirst = false;
                    indicatorPositionOffset = (float) readingProgress.getWidth() / questions.size();
                }
                indicatorPosition = progressBarContainer.getX() - indicator.getWidth() / 2
                        + indicatorPositionOffset * (currentPosition + 1);
                indicator.setX(indicatorPosition);
                if (getActivity() != null) {
                    if (isEnableRateToUnlock && !isProVersion && !isRated) {
                        if (currentPosition >= 10) {
                            if (lockedArea.getVisibility() == View.GONE) {
                                lockedArea.setVisibility(View.VISIBLE);
                            }
                            try {
                                Bitmap blurBitmap = getBlurredBackground(getScreenshot(answerArea));
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
                }
            }
        });
        progressBarContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float rate = event.getX() / readingProgress.getWidth();
                    float tmp = questions.size() * rate;
                    currentPosition = (int) tmp;
                    setCardData(questions.get(currentPosition));
                    if (currentPosition == 0) {
                        disableButton(buttonPrevious, R.drawable.ic_previous);
                        if (!buttonNext.isEnabled()) {
                            enableButton(buttonNext, R.drawable.ic_next);
                        }
                    } else if (currentPosition == questions.size() - 1) {
                        disableButton(buttonNext, R.drawable.ic_next);
                        if (!buttonPrevious.isEnabled()) {
                            enableButton(buttonPrevious, R.drawable.ic_previous);
                        }
                    } else if (currentPosition > 0 && currentPosition < questions.size()) {
                        if (!buttonNext.isEnabled()) {
                            enableButton(buttonNext, R.drawable.ic_next);
                        }
                        if (!buttonPrevious.isEnabled()) {
                            enableButton(buttonPrevious, R.drawable.ic_previous);
                        }
                    }
                    modifyToolbar();
                }
                return false;
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
        super.onDestroyView();
    }

    @Override
    protected String getTitle() {
        switch (type) {
            case DataSource.TYPE_SIM_A:
                return MessageFormat.format(getString(R.string.learn_sim_a), "");
            case DataSource.TYPE_SIM_A_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_a_umum), "");
            case DataSource.TYPE_SIM_B1:
                return MessageFormat.format(getString(R.string.learn_sim_b1), "");
            case DataSource.TYPE_SIM_B1_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_b1_umum), "");
            case DataSource.TYPE_SIM_B2:
                return MessageFormat.format(getString(R.string.learn_sim_b2), "");
            case DataSource.TYPE_SIM_B2_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_b2_umum), "");
            case DataSource.TYPE_SIM_C:
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
                indicatorPosition += indicatorPositionOffset;
                indicator.setX(indicatorPosition);
                getMyBaseActivity().showBigAdsIfNeeded();
            }
        } else if (v == buttonPrevious) {
            if (currentPosition > 0) {
                currentPosition--;
                if (currentPosition == 0) {
                    disableButton(buttonPrevious, R.drawable.ic_previous);
                }
                if (currentPosition != questions.size()) {
                    if (!buttonNext.isEnabled()) {
                        enableButton(buttonNext, R.drawable.ic_next);
                    }
                }
                modifyToolbar();
                setCardData(questions.get(currentPosition));
                indicatorPosition -= indicatorPositionOffset;
                indicator.setX(indicatorPosition);
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
                            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            try {
                                startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id="
                                                + getActivity().getPackageName())));
                            }
                            isRated = true;
                            MySetting.getInstance().setRated();
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

    private void findViews(View rootView) {
        cardQuestionImage = (ImageView) rootView.findViewById(R.id.cardQuestionImage);
        cardTextViewQuestion = (TextView) rootView.findViewById(R.id.cardTextViewQuestion);
        textViewProgress = (TextView) rootView.findViewById(R.id.textViewProgress);
        readingProgress = (ProgressBar) rootView.findViewById(R.id.readingProgress);
        textViewAnswerA = (TextView) rootView.findViewById(R.id.textViewAnswerA);
        textViewAnswerB = (TextView) rootView.findViewById(R.id.textViewAnswerB);
        textViewAnswerC = (TextView) rootView.findViewById(R.id.textViewAnswerC);
        textViewAnswerD = (TextView) rootView.findViewById(R.id.textViewAnswerD);
        buttonPrevious = (ImageView) rootView.findViewById(R.id.buttonPrevious);
        buttonNext = (ImageView) rootView.findViewById(R.id.buttonNext);
        buttonZoomIn = (ImageView) rootView.findViewById(R.id.buttonZoomIn);
        imageArea = (RelativeLayout) rootView.findViewById(R.id.imageArea);
        indicator = (RelativeLayout) rootView.findViewById(R.id.position);
        progressBarContainer = (RelativeLayout) rootView.findViewById(R.id.progressBarContainer);
        lockedArea = (RelativeLayout) rootView.findViewById(R.id.lockedArea);
        answerArea = (LinearLayout) rootView.findViewById(R.id.answerArea);
        blurryImage = (ImageView) rootView.findViewById(R.id.blurryImage);
        ((TextView) rootView.findViewById(R.id.textViewPressToUnlock)).
                setTypeface(HomeActivity.defaultFont);

        layoutAnswerRoot = rootView.findViewById(R.id.layout_answer_root);
        layoutQuestionRoot = rootView.findViewById(R.id.layout_question_root);
        adsContainer1 = (ViewGroup) rootView.findViewById(R.id.adsContainer1);
        adsContainer2 = (ViewGroup) rootView.findViewById(R.id.adsContainer2);

        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnTouchListener(this);
        buttonPrevious.setOnTouchListener(this);
        cardQuestionImage.setOnClickListener(this);
        buttonZoomIn.setOnTouchListener(this);
        lockedArea.setOnClickListener(this);
        indicator.setOnTouchListener(mIndicatorTouchListener);
    }

    private void saveState() {
        MySetting.getInstance().savePosition(type, currentPosition);
        MySetting.getInstance().saveTrialTimes(mTrialTimesLeft);
    }

    private void loadState() {
        currentPosition = MySetting.getInstance().loadPosition(type);
        isProVersion = MySetting.getInstance().isProVersion();
        isRated = isProVersion ? true : MySetting.getInstance().isRated();
        isEnableRateToUnlock = MySetting.getInstance().isEnableRateToUnlock();
        mTrialTimesLeft = MySetting.getInstance().loadTrialTimesLeft();
    }

    private void setCardData(Question question) {
        layoutAnswerRoot.setVisibility(!question.isAds ? View.VISIBLE : View.GONE);
        layoutQuestionRoot.setVisibility(!question.isAds ? View.VISIBLE : View.GONE);
        adsContainer1.setVisibility(question.isAds ? View.VISIBLE : View.GONE);
        adsContainer2.setVisibility(question.isAds ? View.VISIBLE : View.GONE);

        if (question.isAds) {
            setupADSIfNeeded();
            textViewProgress.setText("Ads");
            readingProgress.setProgress(currentPosition + 1);
        } else {
            if (question.imageData == null) {
                imageArea.setVisibility(View.GONE);
            } else {
                imageArea.setVisibility(View.VISIBLE);
                Glide.with(LearnAllFragment.this).load(question.imageData).
                        dontAnimate().dontTransform().into(cardQuestionImage);
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
            int tmp = currentPosition + 1;
            textViewProgress.setText("" + (tmp - tmp / Constants.ADS_BREAK));
            readingProgress.setProgress(tmp);
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
            case DataSource.ANSWER_A:
                textViewAnswerA.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DataSource.ANSWER_B:
                textViewAnswerB.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DataSource.ANSWER_C:
                textViewAnswerC.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DataSource.ANSWER_D:
                textViewAnswerD.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_A);
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

    private Bitmap getRoundedCornerBitmap(Bitmap src) {
        Bitmap output = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = getResources().getDimension(tatteam.com.app_common.R.dimen.common_size_10);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        return output;
    }

    private Bitmap getScreenshot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private Bitmap getBlurredBackground(Bitmap image) throws Exception {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(getActivity());
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        rs.destroy();
        return getRoundedCornerBitmap(Bitmap.createScaledBitmap(
                outputBitmap, image.getWidth(), image.getHeight(), false));
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
            if (count % Constants.ADS_BREAK == 0) {
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
        if (adsHandler1 == null) {
            adsHandler1 = new AdsNativeExpressHandler(getActivity(), adsContainer1, MyBaseActivity.ADS_NATIVE_EXPRESS);
            adsHandler1.setup();
        }

        if (adsHandler2 == null) {
            adsHandler2 = new AdsNativeExpressHandler(getActivity(), adsContainer2, MyBaseActivity.ADS_NATIVE_EXPRESS);
            adsHandler2.setup();
        }
    }
}
