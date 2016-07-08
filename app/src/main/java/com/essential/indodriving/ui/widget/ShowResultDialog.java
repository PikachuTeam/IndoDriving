package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.data.driving.Question;

/**
 * Created by dongc_000 on 2/29/2016.
 */
public class ShowResultDialog extends Dialog implements View.OnClickListener, View.OnTouchListener {

    private Context context;
    private ImageView imgQuestion;
    private TextView tvQuestion;
    private TextView tvChoiceA;
    private TextView tvChoiceB;
    private TextView tvChoiceC;
    private TextView tvChoiceD;
    private TextView tvAnswer;
    private ImageView buttonZoomIn;
    private RelativeLayout imageArea;
    private Question question;

    public ShowResultDialog(Context context, Question question) {
        super(context);
        this.context = context;
        this.question = question;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_show_result);
        findViews();
        if (question.imageData != null) {
            imageArea.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(question.imageData).dontAnimate().
                    dontTransform().dontAnimate().dontTransform().into(imgQuestion);
            buttonZoomIn.setColorFilter(ContextCompat.getColor(context
                    , R.color.learn_all_button_zoom_in_normal_color), PorterDuff.Mode.SRC_ATOP);
        } else {
            imageArea.setVisibility(View.GONE);
        }
        tvQuestion.setText(question.question);
        tvChoiceA.setText("A. " + question.answer1);
        tvChoiceB.setText("B. " + question.answer2);
        tvChoiceC.setText("C. " + question.answer3);
        if (!TextUtils.isEmpty(question.answer4)) tvChoiceD.setText("D. " + question.answer4);
        else tvChoiceD.setVisibility(View.GONE);
        makeCorrectAnswer(question.fixedAnswer != -1 ? question.fixedAnswer : question.correctAnswer);
        makeAnswerColor(question.fixedAnswer != -1 ? question.fixedAnswer : question.correctAnswer);
        findViewById(R.id.dialogShowRuleContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.scrollArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.questionArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void findViews() {
        imgQuestion = (ImageView) findViewById(R.id.image_sign);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvChoiceA = (TextView) findViewById(R.id.tvChoiceA);
        tvChoiceB = (TextView) findViewById(R.id.tvChoiceB);
        tvChoiceC = (TextView) findViewById(R.id.tvChoiceC);
        tvChoiceD = (TextView) findViewById(R.id.tvChoiceD);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);
        buttonZoomIn = (ImageView) findViewById(R.id.buttonZoomIn);
        imageArea = (RelativeLayout) findViewById(R.id.imageArea);
        imgQuestion.setOnClickListener(this);
        buttonZoomIn.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        ZoomInImageDialog dialog = new ZoomInImageDialog(getContext(), question.imageData);
        dialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            buttonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
            buttonZoomIn.setColorFilter(ContextCompat.getColor(context
                    , R.color.learn_all_button_zoom_in_highlight_color), PorterDuff.Mode.SRC_ATOP);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            buttonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
            buttonZoomIn.setColorFilter(ContextCompat.getColor(context
                    , R.color.learn_all_button_zoom_in_normal_color), PorterDuff.Mode.SRC_ATOP);
            ZoomInImageDialog dialog = new ZoomInImageDialog(context, question.imageData);
            dialog.show();
        }
        return false;
    }

    private void makeAnswerColor(int correctAnswer) {
        if (question.answer == DrivingDataSource.ANSWER_NOT_CHOSEN) {
            tvAnswer.setText(getContext().getResources().getText(R.string.not_answered));
            tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.not_answered_color));
        } else if (question.answer == correctAnswer) {
            switch (question.answer) {
                case DrivingDataSource.ANSWER_A:
                    tvAnswer.setText("A");
                    break;
                case DrivingDataSource.ANSWER_B:
                    tvAnswer.setText("B");
                    break;
                case DrivingDataSource.ANSWER_C:
                    tvAnswer.setText("C");
                    break;
                case DrivingDataSource.ANSWER_D:
                    tvAnswer.setText("D");
                    break;
            }
            tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
        } else {
            switch (question.answer) {
                case DrivingDataSource.ANSWER_A:
                    tvAnswer.setText("A");
                    break;
                case DrivingDataSource.ANSWER_B:
                    tvAnswer.setText("B");
                    break;
                case DrivingDataSource.ANSWER_C:
                    tvAnswer.setText("C");
                    break;
                case DrivingDataSource.ANSWER_D:
                    tvAnswer.setText("D");
                    break;
            }
            tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.wrong_answer_color));
        }
    }

    private void makeCorrectAnswer(int correctAnswer) {
        switch (correctAnswer) {
            case 0:
                tvChoiceA.setTextColor(
                        ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
            case 1:
                tvChoiceB.setTextColor(
                        ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
            case 2:
                tvChoiceC.setTextColor(
                        ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
            case 3:
                tvChoiceD.setTextColor(
                        ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
        }
    }
}
