package com.essential.indodriving.ui.learn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;

import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/27/2016.
 */
public class LearnAllFragment extends MyBaseFragment implements View.OnClickListener {

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

    private ArrayList<Question> questions;
    private int type;
    private int currentPosition;
    private float offset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getAllQuestionByType(type);
        currentPosition = loadState();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_learn_all);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_all;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);

        Question question = questions.get(currentPosition);
        setCardData(question);

        readingProgress.setMax(questions.size());
        readingProgress.setProgress(currentPosition + 1);

        readingProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float rate = event.getX() / readingProgress.getWidth();
                    float tmp = questions.size() * rate;
                    currentPosition = (int) tmp;
                    Toast.makeText(getActivity(), "" + currentPosition + " " + questions.size(), Toast.LENGTH_SHORT).show();
                    setCardData(questions.get(currentPosition));
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

        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        cardQuestionImage.setOnClickListener(this);
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveState();
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
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
            cardQuestionImage.setVisibility(View.GONE);
        } else {
            cardQuestionImage.setVisibility(View.VISIBLE);
            cardQuestionImage.setImageBitmap(question.image);
        }
        cardTextViewQuestion.setText(question.question);

        textViewAnswerA.setText("A. " + question.answer1);
        textViewAnswerB.setText("B. " + question.answer2);
        textViewAnswerC.setText("C. " + question.answer3);
        textViewAnswerD.setText("D. " + question.answer4);

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

        textViewProgress.setText("" + (currentPosition + 1) + "/" + questions.size());

        readingProgress.setProgress(currentPosition + 1);
    }

    private void resetAllAnswers() {
        textViewAnswerA.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        textViewAnswerB.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        textViewAnswerC.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        textViewAnswerD.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNext) {
            if (currentPosition < questions.size()) {
                currentPosition++;
                if (currentPosition == questions.size() - 1) {
                    buttonNext.setEnabled(false);
                }
                if (currentPosition != 0) {
                    if (!buttonPrevious.isEnabled()) {
                        buttonPrevious.setEnabled(true);
                    }
                }
                setCardData(questions.get(currentPosition));
            }
        } else if (v == buttonPrevious) {
            if (currentPosition > 0) {
                currentPosition--;
                if (currentPosition == 0) {
                    buttonPrevious.setEnabled(false);
                }
                if (currentPosition != questions.size()) {
                    if (!buttonNext.isEnabled()) {
                        buttonNext.setEnabled(true);
                    }
                }
                setCardData(questions.get(currentPosition));
            }
        } else if (v == cardQuestionImage) {
            ZoomInImageDialog dialog = new ZoomInImageDialog(getActivity(), questions.get(currentPosition).image);
            dialog.show();
        }
    }
}
