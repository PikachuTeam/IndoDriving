package com.essential.indodriving.ui.fragment.sign.test;

import android.app.FragmentManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.data.sign.SignQuestion;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.fragment.sign.SignChooseItemFragment;
import com.essential.indodriving.ui.fragment.theory.test.UnlimitedTestFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yue on 09/07/2016.
 */
public class SignOverallResultFragment extends MyBaseFragment implements
        View.OnClickListener, View.OnTouchListener {

    public final static String TAG_OVERALL_RESULT_FRAGMENT = "Overall Result Fragment";
    private TextView textViewState;
    private TextView tvCorrectAnswer;
    private TextView tvWrongAnswer;
    private TextView tvNotAnswered;
    private LinearLayout chartContainer;
    private RelativeLayout buttonCorrectAnswer;
    private RelativeLayout buttonWrongAnswer;
    private RelativeLayout buttonNotAnswered;
    private ImageView buttonNext1;
    private ImageView buttonNext2;
    private ImageView buttonNext3;
    private PieChart pieChart;
    private List<SignQuestion> questions;
    private String fragmentType;
    private int totalCorrectAnswer;
    private int totalWrongAnswer;
    private int totalNotAnswered;
    private int numberOfQuestions;
    private float[] yData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        numberOfQuestions = calculateNumberOfQuestions();
        totalCorrectAnswer = calculateCorrectAnswer();
        totalWrongAnswer = calculateWrongAnswer();
        totalNotAnswered = numberOfQuestions - totalCorrectAnswer - totalWrongAnswer;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_overall_result);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_show_result;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        setText();
        setupChart();
        chartContainer.addView(pieChart);
        pieChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        setDefaultColor(rootView);
    }

    private void setDefaultColor(View rootView) {
        ((ImageView) rootView.findViewById(R.id.imgCorrectAnswer)).
                setColorFilter(ContextCompat.getColor(getActivity(),
                        R.color.correct_answer_color), PorterDuff.Mode.SRC_ATOP);
        ((ImageView) rootView.findViewById(R.id.imgWrongAnswer)).
                setColorFilter(ContextCompat.getColor(getActivity(),
                        R.color.wrong_answer_color), PorterDuff.Mode.SRC_ATOP);
        ((ImageView) rootView.findViewById(R.id.imgNotAnswered)).
                setColorFilter(ContextCompat.getColor(getActivity(),
                        R.color.not_answered_color), PorterDuff.Mode.SRC_ATOP);
        buttonNext1.setBackgroundResource(R.drawable.sign_button_next);
        buttonNext1.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.sign_button_normal_color)
                , PorterDuff.Mode.SRC_ATOP);
        buttonNext2.setBackgroundResource(R.drawable.sign_button_next);
        buttonNext2.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.sign_button_normal_color)
                , PorterDuff.Mode.SRC_ATOP);
        buttonNext3.setBackgroundResource(R.drawable.sign_button_next);
        buttonNext3.setColorFilter(ContextCompat.getColor(getActivity()
                , R.color.sign_button_normal_color)
                , PorterDuff.Mode.SRC_ATOP);
    }

    private void getData() {
        if (containHolder(Constants.KEY_HOLDER_QUESTIONS)) {
            questions = (ArrayList<SignQuestion>) getHolder(Constants.KEY_HOLDER_QUESTIONS);
        } else {
            questions = new ArrayList<>();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            fragmentType = bundle.getString(Constants.BUNDLE_FRAGMENT_TYPE,
                    SignUnlimitedTestFragment.TAG_WRITTEN_TEST_FRAGMENT);
        }
    }

    @Override
    protected boolean enableButtonShare() {
        return true;
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack(SignChooseItemFragment.TAG_SIGN_CHOOSE_ITEM_FRAGMENT,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (v == buttonCorrectAnswer) {
            putHolder(Constants.KEY_HOLDER_QUESTIONS, getCorrectAnswers());
            bundle.putInt(Constants.BUNDLE_TYPE, 0);
        } else if (v == buttonWrongAnswer) {
            putHolder(Constants.KEY_HOLDER_QUESTIONS, getWrongAnswers());
            bundle.putInt(Constants.BUNDLE_TYPE, 1);
        } else if (v == buttonNotAnswered) {
            putHolder(Constants.KEY_HOLDER_QUESTIONS, getNotAnsweredAnswers());
            bundle.putInt(Constants.BUNDLE_TYPE, 2);
        }
        SignDetailResultFragment fragment = new SignDetailResultFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_OVERALL_RESULT_FRAGMENT);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == buttonNext1) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonNext1.setColorFilter(ContextCompat.getColor(
                        getActivity(), R.color.sign_button_highlight_color),
                        PorterDuff.Mode.SRC_ATOP);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonNext1.setColorFilter(ContextCompat.getColor(
                        getActivity(), R.color.sign_button_normal_color),
                        PorterDuff.Mode.SRC_ATOP);
                putHolder(Constants.KEY_HOLDER_QUESTIONS, getCorrectAnswers());
                moveToNextFragment();
            }
        } else if (v == buttonNext2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonNext2.setColorFilter(ContextCompat.getColor(
                        getActivity(), R.color.sign_button_highlight_color),
                        PorterDuff.Mode.SRC_ATOP);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonNext2.setColorFilter(ContextCompat.getColor(
                        getActivity(), R.color.sign_button_normal_color),
                        PorterDuff.Mode.SRC_ATOP);
                putHolder(Constants.KEY_HOLDER_QUESTIONS, getWrongAnswers());
                moveToNextFragment();
            }
        } else if (v == buttonNext3) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonNext3.setColorFilter(ContextCompat.getColor(
                        getActivity(), R.color.sign_button_highlight_color),
                        PorterDuff.Mode.SRC_ATOP);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonNext3.setColorFilter(ContextCompat.getColor(
                        getActivity(), R.color.sign_button_normal_color),
                        PorterDuff.Mode.SRC_ATOP);
                putHolder(Constants.KEY_HOLDER_QUESTIONS, getNotAnsweredAnswers());
                moveToNextFragment();
            }
        }
        return false;
    }

    private int calculateCorrectAnswer() {
        int tmp = 0;
        int size = questions.size();
        for (int i = 0; i < size; i++) {
            SignQuestion question = questions.get(i);
            if (!question.isAds) {
                if (question.answer != DrivingDataSource.ANSWER_NOT_CHOSEN
                        && question.answer == question.correctAnswer) {
                    tmp++;
                }
            }
        }
        return tmp;
    }

    private int calculateWrongAnswer() {
        int tmp = 0;
        int size = questions.size();
        for (int i = 0; i < size; i++) {
            SignQuestion question = questions.get(i);
            if (!question.isAds) {
                if (question.answer != DrivingDataSource.ANSWER_NOT_CHOSEN
                        && question.answer != question.correctAnswer) {
                    tmp++;
                }
            }
        }
        return tmp;
    }

    private int calculateNumberOfQuestions() {
        int tmp = 0;
        int size = questions.size();
        for (int i = 0; i < size; i++) {
            if (!questions.get(i).isAds) tmp++;
        }
        return tmp;
    }

    private void setText() {
        switch (fragmentType) {
            case SignDoTestFragment.TAG_SIGN_DO_TEST_FRAGMENT:
                textViewState.setVisibility(View.VISIBLE);
                if (totalCorrectAnswer >= 18) {
                    textViewState.setText(getString(R.string.pass));
                    textViewState.setTextColor(
                            ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                } else {
                    textViewState.setText(getString(R.string.fail));
                    textViewState.setTextColor(
                            ContextCompat.getColor(getActivity(), R.color.wrong_answer_color));
                }
                break;
            case UnlimitedTestFragment.TAG_WRITTEN_TEST_FRAGMENT:
                textViewState.setVisibility(View.GONE);
                break;
        }
        tvCorrectAnswer.setText(MessageFormat.format(getString(R.string.number_of_answers),
                totalCorrectAnswer, numberOfQuestions));
        tvWrongAnswer.setText(MessageFormat.format(getString(R.string.number_of_answers),
                totalWrongAnswer, numberOfQuestions));
        tvNotAnswered.setText(MessageFormat.format(getString(R.string.number_of_answers),
                totalNotAnswered, numberOfQuestions));
    }

    private void findViews(View rootView) {
        tvCorrectAnswer = (TextView) rootView.findViewById(R.id.tvCorrectAnswer);
        tvWrongAnswer = (TextView) rootView.findViewById(R.id.tvWrongAnswer);
        tvNotAnswered = (TextView) rootView.findViewById(R.id.tvNotAnswered);
        textViewState = (TextView) rootView.findViewById(R.id.tvState);
        chartContainer = (LinearLayout) rootView.findViewById(R.id.chartContainer);
        buttonCorrectAnswer = (RelativeLayout) rootView.findViewById(R.id.buttonCorrectAnswer);
        buttonWrongAnswer = (RelativeLayout) rootView.findViewById(R.id.buttonWrongAnswer);
        buttonNotAnswered = (RelativeLayout) rootView.findViewById(R.id.buttonNotAnswered);
        buttonNext1 = (ImageView) rootView.findViewById(R.id.buttonNext1);
        buttonNext2 = (ImageView) rootView.findViewById(R.id.buttonNext2);
        buttonNext3 = (ImageView) rootView.findViewById(R.id.buttonNext3);
        rootView.findViewById(R.id.textViewNotAnswered).setSelected(true);
        rootView.findViewById(R.id.root_layout).setBackgroundColor(ContextCompat.
                getColor(getActivity(), R.color.default_background_color_sign));

        setFont(HomeActivity.defaultFont, rootView);

        buttonCorrectAnswer.setOnClickListener(this);
        buttonWrongAnswer.setOnClickListener(this);
        buttonNotAnswered.setOnClickListener(this);
        buttonNext1.setOnTouchListener(this);
        buttonNext2.setOnTouchListener(this);
        buttonNext3.setOnTouchListener(this);
    }

    private void setFont(Typeface font, View rootView) {
        tvCorrectAnswer.setTypeface(font);
        tvWrongAnswer.setTypeface(font);
        tvNotAnswered.setTypeface(font);
        textViewState.setTypeface(font);
        ((TextView) rootView.findViewById(R.id.textViewCorrectAnswer)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.textViewWrongAnswer)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.textViewNotAnswered)).setTypeface(font);
    }

    private void setupChart() {
        float tmp = 100 / 30f;
        yData = new float[]{totalCorrectAnswer * tmp, totalWrongAnswer * tmp, totalNotAnswered * tmp};
        pieChart = new PieChart(getActivity());
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(getResources().getDimension(R.dimen.pie_chart_hole_radius));
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setRotationEnabled(false);
        pieChart.setTouchEnabled(false);
        addData();
        pieChart.getLegend().setEnabled(false);
        pieChart.getData().setDrawValues(false);
        pieChart.setDescription("");
    }

    private void addData() {
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        if (yData[0] > 0) {
            yVals.add(new Entry(yData[0], 0));
            colors.add(new Integer(ContextCompat.getColor(getActivity(), R.color.correct_answer_color)));
        }
        if (yData[1] > 0) {
            yVals.add(new Entry(yData[1], 1));
            colors.add(new Integer(ContextCompat.getColor(getActivity(), R.color.wrong_answer_color)));
        }
        if (yData[2] > 0) {
            yVals.add(new Entry(yData[2], 2));
            colors.add(new Integer(ContextCompat.getColor(getActivity(), R.color.not_answered_color)));
        }

        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < yVals.size(); i++) {
            xVals.add("");
        }
        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(4);
        dataSet.setSelectionShift(5);
        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(9f);
        data.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private List<SignQuestion> getCorrectAnswers() {
        List<SignQuestion> data = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            SignQuestion question = questions.get(i);
            if (question.answer == question.correctAnswer) data.add(question);
        }
        return data;
    }

    private List<SignQuestion> getWrongAnswers() {
        List<SignQuestion> data = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            SignQuestion question = questions.get(i);
            if (question.answer != DrivingDataSource.ANSWER_NOT_CHOSEN &&
                    question.answer != question.correctAnswer && !question.isAds) {
                data.add(question);
            }
        }
        return data;
    }

    private List<SignQuestion> getNotAnsweredAnswers() {
        List<SignQuestion> data = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            SignQuestion question = questions.get(i);
            if (question.answer == DrivingDataSource.ANSWER_NOT_CHOSEN && !question.isAds) {
                data.add(question);
            }
        }
        return data;
    }

    private void moveToNextFragment() {
        SignDetailResultFragment fragment = new SignDetailResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, 2);
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_OVERALL_RESULT_FRAGMENT);
    }
}
