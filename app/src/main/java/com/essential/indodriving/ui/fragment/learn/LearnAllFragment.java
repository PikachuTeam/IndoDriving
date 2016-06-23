package com.essential.indodriving.ui.fragment.learn;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.R;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.MySetting;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.fragment.test.UnlimitedTestFragment;
import com.essential.indodriving.ui.widget.RatingDialog;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/27/2016.
 */
public class LearnAllFragment extends MyBaseFragment implements View.OnClickListener, View.OnTouchListener {

    public final static String
            TAG_LEARN_ALL_FRAGMENT = "Learn All Fragment",
            PREF_CURRENT_POSITION_SIM_A = "Current Position Sim A",
            PREF_CURRENT_POSITION_SIM_A_UMUM = "Current Position Sim A Umum",
            PREF_CURRENT_POSITION_SIM_B1 = "Current Position Sim B1",
            PREF_CURRENT_POSITION_SIM_B1_UMUM = "Current Position Sim B1 Umum",
            PREF_CURRENT_POSITION_SIM_B2 = "Current Position Sim B2",
            PREF_CURRENT_POSITION_SIM_B2_UMUM = "Current Position Sim B2 Umum",
            PREF_CURRENT_POSITION_SIM_C = "Current Position Sim C",
            PREF_CURRENT_POSITION_SIM_D = "Current Position Sim D";
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
    private View.OnTouchListener mIndicatorTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float realX = event.getRawX() - getResources().getDimension(R.dimen.common_size_20);
                    float ratio = realX / readingProgress.getWidth();
                    int tmp = (int) (ratio * questions.size());
                    if (tmp > 0 && tmp < questions.size()) {
                        currentPosition = tmp;
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
                    }
                    break;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getAllQuestionByType(type);
        loadState();
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
        Question question = questions.get(currentPosition);
        setCardData(question);
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
                }
                return false;
            }
        });
    }

    @Override
    protected String getTitle() {
        switch (type) {
            case DataSource.TYPE_SIM_A:
                return MessageFormat.format(getString(R.string.learn_sim_a), "" + questions.size());
            case DataSource.TYPE_SIM_A_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_a_umum), "" + questions.size());
            case DataSource.TYPE_SIM_B1:
                return MessageFormat.format(getString(R.string.learn_sim_b1), "" + questions.size());
            case DataSource.TYPE_SIM_B1_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_b1_umum), "" + questions.size());
            case DataSource.TYPE_SIM_B2:
                return MessageFormat.format(getString(R.string.learn_sim_b2), "" + questions.size());
            case DataSource.TYPE_SIM_B2_UMUM:
                return MessageFormat.format(getString(R.string.learn_sim_b2_umum), "" + questions.size());
            case DataSource.TYPE_SIM_C:
                return MessageFormat.format(getString(R.string.learn_sim_c), "" + questions.size());
            default:
                return MessageFormat.format(getString(R.string.learn_sim_d), "" + questions.size());
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
    protected boolean enableButtonShare() {
        return true;
    }

    @Override
    protected void onMenuItemClick(int id) {
        sharingEvent();
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case DataSource.TYPE_SIM_A:
                editor.putInt(PREF_CURRENT_POSITION_SIM_A, currentPosition);
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                editor.putInt(PREF_CURRENT_POSITION_SIM_A_UMUM, currentPosition);
                break;
            case DataSource.TYPE_SIM_B1:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B1, currentPosition);
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B1_UMUM, currentPosition);
                break;
            case DataSource.TYPE_SIM_B2:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B2, currentPosition);
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                editor.putInt(PREF_CURRENT_POSITION_SIM_B2_UMUM, currentPosition);
                break;
            case DataSource.TYPE_SIM_C:
                editor.putInt(PREF_CURRENT_POSITION_SIM_C, currentPosition);
                break;
            case DataSource.TYPE_SIM_D:
                editor.putInt(PREF_CURRENT_POSITION_SIM_D, currentPosition);
                break;
        }
        editor.putInt(Constants.PREF_TRIAL_TIME_LEFT, mTrialTimesLeft);
        editor.commit();
    }

    private void loadState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        switch (type) {
            case DataSource.TYPE_SIM_A:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_A, 0);
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_A_UMUM, 0);
                break;
            case DataSource.TYPE_SIM_B1:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_B1, 0);
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_B1_UMUM, 0);
                break;
            case DataSource.TYPE_SIM_B2:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_B2, 0);
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_B2_UMUM, 0);
                break;
            case DataSource.TYPE_SIM_C:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_C, 0);
                break;
            case DataSource.TYPE_SIM_D:
                currentPosition = sharedPreferences.getInt(PREF_CURRENT_POSITION_SIM_D, 0);
                break;
            default:
                currentPosition = 0;
        }
        isProVersion = MySetting.getInstance().isProVersion();
        isRated = sharedPreferences.getBoolean(Constants.PREF_IS_RATE_APP, false);
        isEnableRateToUnlock = sharedPreferences.getBoolean(Constants.PREF_RATE_TO_UNLOCK, false);
        mTrialTimesLeft = sharedPreferences.getInt(Constants.PREF_TRIAL_TIME_LEFT, Constants.NUMBER_OF_TRIALS);
    }

    private void moveToNextFragment() {
        UnlimitedTestFragment fragment = new UnlimitedTestFragment();
        putHolder(Constants.KEY_HOLDER_QUESTIONS, questions);
        replaceFragment(fragment, TAG_LEARN_ALL_FRAGMENT);
        saveState();
    }

    private void setCardData(Question question) {
        if (question.imageData == null) {
            imageArea.setVisibility(View.GONE);
        } else {
            imageArea.setVisibility(View.VISIBLE);
//            cardQuestionImage.setImageBitmap(question.image);
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
        switch (question.correctAnswer) {
            case DataSource.ANSWER_A:
                textViewAnswerA.setTextColor(ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DataSource.ANSWER_B:
                textViewAnswerC.setTextColor(ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DataSource.ANSWER_C:
                textViewAnswerB.setTextColor(ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
            case DataSource.ANSWER_D:
                textViewAnswerD.setTextColor(ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                break;
        }
        textViewProgress.setText("" + (currentPosition + 1));
        readingProgress.setProgress(currentPosition + 1);
    }

    private void resetAllAnswers() {
        textViewAnswerA.setTextColor(ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
        textViewAnswerB.setTextColor(ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
        textViewAnswerC.setTextColor(ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
        textViewAnswerD.setTextColor(ContextCompat.getColor(getActivity(), R.color.learn_all_text_color));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isEnableRateToUnlock || isRated || isProVersion) {
            lockedArea.setVisibility(View.GONE);
        }
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
                setCardData(questions.get(currentPosition));
                indicatorPosition -= indicatorPositionOffset;
                indicator.setX(indicatorPosition);
                getMyBaseActivity().showBigAdsIfNeeded();
            }
        } else if (v == cardQuestionImage) {
            ZoomInImageDialog dialog = new ZoomInImageDialog(getActivity(), questions.get(currentPosition).imageData);
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
                            SharedPreferences sharedPreferences = getActivity()
                                    .getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.PREF_IS_RATE_APP, isRated);
                            editor.commit();
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
        } else if (v == buttonNext) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getEventTime() == 200) {
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
                        setCardData(questions.get(currentPosition));
                        indicatorPosition += indicatorPositionOffset;
                        indicator.setX(indicatorPosition);
                    }
                }
            }
        } else if (v == buttonPrevious) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getEventTime() == 200) {
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
                        setCardData(questions.get(currentPosition));
                        indicatorPosition -= indicatorPositionOffset;
                        indicator.setX(indicatorPosition);
                    }
                }
            }
        }
        return false;
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
        ((TextView) rootView.findViewById(R.id.textViewPressToUnlock)).setTypeface(HomeActivity.defaultFont);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnTouchListener(this);
        buttonPrevious.setOnTouchListener(this);
        cardQuestionImage.setOnClickListener(this);
        buttonZoomIn.setOnTouchListener(this);
        lockedArea.setOnClickListener(this);
        indicator.setOnTouchListener(mIndicatorTouchListener);
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
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
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
        return getRoundedCornerBitmap(Bitmap.createScaledBitmap(outputBitmap, image.getWidth(), image.getHeight(), false));
    }
}
