package com.essential.indodriving.ui.test;

import android.app.FragmentManager;
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
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/28/2016.
 */
public class OverallResultFragment extends MyBaseFragment implements View.OnClickListener, View.OnTouchListener {

    private TextView textViewTotalTime;
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
    private int totalCorrectAnswer;
    private int totalWrongAnswer;
    private int totalNotAnswered;
    private int minute;
    private int second;
    private int timeLeft;
    private int type;
    private int examId;
    private float[] yData;
    private ArrayList<Question> questions;
    private Typeface font;

    public final static String OVERALL_RESULT_FRAGMENT_TAG = "Overall Result Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setTime();
        totalCorrectAnswer = calculateCorrectAnswer();
        totalWrongAnswer = calculateWrongAnswer();
        totalNotAnswered = questions.size() - totalCorrectAnswer - totalWrongAnswer;
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Menu Sim.ttf");
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
        pieChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void getData() {
        if (containHolder(DoTestFragment.KEY_HOLDER_QUESTIONS)) {
            questions = (ArrayList<Question>) getHolder(DoTestFragment.KEY_HOLDER_QUESTIONS);
        } else {
            questions = new ArrayList<>();
        }
        Bundle bundle = getArguments();
        timeLeft = bundle.getInt("Time Left", 0);
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
        examId = bundle.getInt("Exam Id", 1);
    }

    private void setTime() {
        int totalTime = DoTestFragment.TOTAL_TIME - timeLeft;
        totalTime = totalTime / 1000;
        minute = totalTime / 60;
        second = totalTime % 60;
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack(ListQuestionFragment.LIST_QUESTION_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        DataSource.saveScore(examId, "" + type, totalCorrectAnswer);
    }

    private int calculateCorrectAnswer() {
        int tmp = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).answer != -1 && questions.get(i).answer == questions.get(i).correctAnswer) {
                tmp++;
            }
        }
        return tmp;
    }

    private int calculateWrongAnswer() {
        int tmp = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).answer != -1 && questions.get(i).answer != questions.get(i).correctAnswer) {
                tmp++;
            }
        }
        return tmp;
    }

    private void setText() {
        if (totalCorrectAnswer >= 25) {
            textViewState.setText(getString(R.string.pass));
            textViewState.setTextColor(ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
        } else {
            textViewState.setText(getString(R.string.fail));
            textViewState.setTextColor(ContextCompat.getColor(getActivity(), R.color.wrong_answer_color));
        }
        if (minute == 0) {
            textViewTotalTime.setText(MessageFormat.format(getString(R.string.finish_test_2), second));
        } else if (second == 0) {
            textViewTotalTime.setText(MessageFormat.format(getString(R.string.finish_test_3), minute));
        } else {
            textViewTotalTime.setText(MessageFormat.format(getString(R.string.finish_test_1), minute, second));
        }
        tvCorrectAnswer.setText(MessageFormat.format(getString(R.string.total_correct_answers), totalCorrectAnswer));
        tvWrongAnswer.setText(MessageFormat.format(getString(R.string.total_wrong_answers), totalWrongAnswer));
        tvNotAnswered.setText(MessageFormat.format(getString(R.string.total_not_answered), totalNotAnswered));
    }

    private void findViews(View rootView) {
        textViewTotalTime = (TextView) rootView.findViewById(R.id.tvTotalTime);
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

        setFont(font, rootView);

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
        textViewTotalTime.setTypeface(font);
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
        pieChart.setClickable(false);

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

    private ArrayList<Question> getCorrectAnswers() {
        ArrayList<Question> data = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            if (question.answer == question.correctAnswer) {
                data.add(question);
            }
        }
        return data;
    }

    private ArrayList<Question> getWrongAnswers() {
        ArrayList<Question> data = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            if (question.answer != question.correctAnswer && question.answer != DataSource.ANSWER_NOT_CHOSEN) {
                data.add(question);
            }
        }
        return data;
    }

    private ArrayList<Question> getNotAnsweredAnswers() {
        ArrayList<Question> data = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            if (question.answer == DataSource.ANSWER_NOT_CHOSEN) {
                data.add(question);
            }
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        if (v == buttonCorrectAnswer) {
            putHolder(DoTestFragment.KEY_HOLDER_QUESTIONS, getCorrectAnswers());
            bundle.putInt("Type", 0);
        } else if (v == buttonWrongAnswer) {
            putHolder(DoTestFragment.KEY_HOLDER_QUESTIONS, getWrongAnswers());
            bundle.putInt("Type", 1);
        } else if (v == buttonNotAnswered) {
            putHolder(DoTestFragment.KEY_HOLDER_QUESTIONS, getNotAnsweredAnswers());
            bundle.putInt("Type", 2);
        }
        DetailResultFragment fragment = new DetailResultFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment, OVERALL_RESULT_FRAGMENT_TAG);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == buttonNext1) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonNext1.setImageResource(R.drawable.ic_overall_result_highlight_next);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonNext1.setImageResource(R.drawable.ic_overall_result_normal_next);
                putHolder(DoTestFragment.KEY_HOLDER_QUESTIONS, getCorrectAnswers());
                Bundle bundle = new Bundle();
                bundle.putInt("Type", 0);
                DetailResultFragment fragment = new DetailResultFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, OVERALL_RESULT_FRAGMENT_TAG);
            }
        } else if (v == buttonNext2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonNext2.setImageResource(R.drawable.ic_overall_result_highlight_next);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonNext2.setImageResource(R.drawable.ic_overall_result_normal_next);
                putHolder(DoTestFragment.KEY_HOLDER_QUESTIONS, getWrongAnswers());
                Bundle bundle = new Bundle();
                bundle.putInt("Type", 1);
                DetailResultFragment fragment = new DetailResultFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, OVERALL_RESULT_FRAGMENT_TAG);
            }
        } else if (v == buttonNext3) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonNext3.setImageResource(R.drawable.ic_overall_result_highlight_next);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonNext3.setImageResource(R.drawable.ic_overall_result_normal_next);
                putHolder(DoTestFragment.KEY_HOLDER_QUESTIONS, getNotAnsweredAnswers());
                Bundle bundle = new Bundle();
                bundle.putInt("Type", 2);
                DetailResultFragment fragment = new DetailResultFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, OVERALL_RESULT_FRAGMENT_TAG);
            }
        }
        return false;
    }
}
