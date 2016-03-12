package com.essential.indodriving.ui.learn;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.HomeActivity;
import com.essential.indodriving.ui.MainActivity;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;

import java.text.MessageFormat;
import java.util.ArrayList;


/**
 * Created by dongc_000 on 2/27/2016.
 */
public class LearnAllFragment extends MyBaseFragment implements View.OnClickListener, View.OnTouchListener {

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
    private ViewGroup blurArea;

    private ArrayList<Question> questions;
    private int type;
    private int currentPosition;
    private float indicatorPosition;
    private float indicatorPositionOffset;
    private boolean isFirst;
    private boolean isRated;

    public final static String LEARN_ALL_FRAGMENT_TAG = "Learn All Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getAllQuestionByType(type);
        currentPosition = loadState();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        isFirst = true;
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
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);

        buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_zoom_in_normal_color), PorterDuff.Mode.SRC_ATOP);

        Question question = questions.get(currentPosition);
        setCardData(question);

        readingProgress.setMax(questions.size());
        readingProgress.setProgress(currentPosition + 1);

        if (currentPosition == 0) {
            disableButton(buttonPrevious, R.drawable.ic_previous);
            buttonNext.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_normal_color), PorterDuff.Mode.SRC_ATOP);
        } else if (currentPosition == questions.size() - 1) {
            disableButton(buttonNext, R.drawable.ic_next);
            buttonPrevious.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_normal_color), PorterDuff.Mode.SRC_ATOP);
        } else {
            buttonPrevious.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_normal_color), PorterDuff.Mode.SRC_ATOP);
            buttonNext.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_normal_color), PorterDuff.Mode.SRC_ATOP);
        }

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirst) {
                    isFirst = false;
                    indicatorPositionOffset = (float) readingProgress.getWidth() / questions.size();
                }
                indicatorPosition = progressBarContainer.getX() - indicator.getWidth() / 2 + indicatorPositionOffset * (currentPosition + 1);
                indicator.setX(indicatorPosition);
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
        blurArea = (ViewGroup) rootView.findViewById(R.id.blurArea);

        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnTouchListener(this);
        buttonPrevious.setOnTouchListener(this);
        cardQuestionImage.setOnClickListener(this);
        buttonZoomIn.setOnTouchListener(this);
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected boolean enableButtonTutorial() {
        switch (type) {
            case DataSource.TYPE_SIM_A:
            case DataSource.TYPE_SIM_C:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onMenuItemClick(int id) {
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Type", type);
        tutorialFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_silde_bot_enter, 0, 0, R.anim.fragment_silde_bot_exit);
        transaction.replace(R.id.fragmentContainer, tutorialFragment, LEARN_ALL_FRAGMENT_TAG);
        transaction.addToBackStack(LEARN_ALL_FRAGMENT_TAG);
        transaction.commit();
        saveState();
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case DataSource.TYPE_SIM_A:
                editor.putInt("Current Position A", currentPosition);
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                editor.putInt("Current Position A Umum", currentPosition);
                break;
            case DataSource.TYPE_SIM_B1:
                editor.putInt("Current Position B1", currentPosition);
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                editor.putInt("Current Position B1 Umum", currentPosition);
                break;
            case DataSource.TYPE_SIM_B2:
                editor.putInt("Current Position B2", currentPosition);
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                editor.putInt("Current Position B2 Umum", currentPosition);
                break;
            case DataSource.TYPE_SIM_C:
                editor.putInt("Current Position C", currentPosition);
                break;
            case DataSource.TYPE_SIM_D:
                editor.putInt("Current Position D", currentPosition);
                break;
        }
        editor.commit();
    }

    private int loadState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        switch (type) {
            case DataSource.TYPE_SIM_A:
                return sharedPreferences.getInt("Current Position A", 0);
            case DataSource.TYPE_SIM_A_UMUM:
                return sharedPreferences.getInt("Current Position A Umum", 0);
            case DataSource.TYPE_SIM_B1:
                return sharedPreferences.getInt("Current Position B1", 0);
            case DataSource.TYPE_SIM_B1_UMUM:
                return sharedPreferences.getInt("Current Position B1 Umum", 0);
            case DataSource.TYPE_SIM_B2:
                return sharedPreferences.getInt("Current Position B2", 0);
            case DataSource.TYPE_SIM_B2_UMUM:
                return sharedPreferences.getInt("Current Position B2 Umum", 0);
            case DataSource.TYPE_SIM_C:
                return sharedPreferences.getInt("Current Position C", 0);
            case DataSource.TYPE_SIM_D:
                return sharedPreferences.getInt("Current Position D", 0);
            default:
                return 0;
        }
    }

    private void setCardData(Question question) {
        if (question.image == null) {
            imageArea.setVisibility(View.GONE);
        } else {
            imageArea.setVisibility(View.VISIBLE);
            cardQuestionImage.setImageBitmap(question.image);
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
            ZoomInImageDialog dialog = new ZoomInImageDialog(getActivity(), questions.get(currentPosition).image);
            dialog.show();
        }
    }

    private void enableButton(ImageView button, int image) {
        button.setEnabled(true);
        button.setImageResource(image);
        button.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_normal_color), PorterDuff.Mode.SRC_ATOP);
    }

    private void disableButton(ImageView button, int image) {
        button.setEnabled(false);
        button.setImageResource(image);
        button.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_disabled_color), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == buttonZoomIn) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
                buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_zoom_in_highlight_color), PorterDuff.Mode.SRC_ATOP);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
                buttonZoomIn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.learn_all_button_zoom_in_normal_color), PorterDuff.Mode.SRC_ATOP);
                Question question = questions.get(currentPosition);
                ZoomInImageDialog dialog = new ZoomInImageDialog(getActivity(), question.image);
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
}
