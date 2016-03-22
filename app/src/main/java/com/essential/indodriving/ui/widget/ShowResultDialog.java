package com.essential.indodriving.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;

/**
 * Created by dongc_000 on 2/29/2016.
 */
public class ShowResultDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private ImageView imgQuestion;
    private TextView tvQuestion;
    private TextView tvChoiceA;
    private TextView tvChoiceB;
    private TextView tvChoiceC;
    private TextView tvChoiceD;
    private TextView tvAnswer;

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

        if (question.image != null) {
            imgQuestion.setVisibility(View.VISIBLE);
            imgQuestion.setImageBitmap(question.image);
        } else {
            imgQuestion.setVisibility(View.GONE);
        }

        tvQuestion.setText(question.question);

        tvChoiceA.setText("A. " + question.answer1);
        tvChoiceB.setText("B. " + question.answer2);
        tvChoiceC.setText("C. " + question.answer3);
        if (question.answer4 != null) {
            tvChoiceD.setText("D. " + question.answer4);
        } else {
            tvChoiceD.setVisibility(View.GONE);
        }

        switch (question.correctAnswer) {
            case 0:
                tvChoiceA.setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
            case 1:
                tvChoiceB.setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
            case 2:
                tvChoiceC.setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
            case 3:
                tvChoiceD.setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
                break;
        }

        if (question.answer == DataSource.ANSWER_NOT_CHOSEN) {
            tvAnswer.setText(getContext().getResources().getText(R.string.not_answered));
            tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.not_answered_color));
        } else if (question.answer == question.correctAnswer) {
            switch (question.answer) {
                case DataSource.ANSWER_A:
                    tvAnswer.setText("A");
                    break;
                case DataSource.ANSWER_B:
                    tvAnswer.setText("B");
                    break;
                case DataSource.ANSWER_C:
                    tvAnswer.setText("C");
                    break;
                case DataSource.ANSWER_D:
                    tvAnswer.setText("D");
                    break;
            }
            tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
        } else {
            switch (question.answer) {
                case DataSource.ANSWER_A:
                    tvAnswer.setText("A");
                    break;
                case DataSource.ANSWER_B:
                    tvAnswer.setText("B");
                    break;
                case DataSource.ANSWER_C:
                    tvAnswer.setText("C");
                    break;
                case DataSource.ANSWER_D:
                    tvAnswer.setText("D");
                    break;
            }
            tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.wrong_answer_color));
        }

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
        imgQuestion = (ImageView) findViewById(R.id.questionImage);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvChoiceA = (TextView) findViewById(R.id.tvChoiceA);
        tvChoiceB = (TextView) findViewById(R.id.tvChoiceB);
        tvChoiceC = (TextView) findViewById(R.id.tvChoiceC);
        tvChoiceD = (TextView) findViewById(R.id.tvChoiceD);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);

        imgQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ZoomInImageDialog dialog = new ZoomInImageDialog(getContext(), question.image);
        dialog.show();
    }
}
