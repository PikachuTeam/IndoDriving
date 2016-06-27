package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.base.BaseConfirmDialog;

/**
 * Created by yue on 26/06/2016.
 */
public class ModifyAnswerDialog extends BaseConfirmDialog implements View.OnClickListener {

    private final int TYPE_TEXT = 1, TYPE_RADIO = 2;
    private final int SUB_STRING_START_INDEX = 3;
    private RadioGroup mAnswerGroup;
    private ImageView mImageQuestion;
    private TextView mTextQuestion;
    private ImageView mButtonZoomIn;
    private RelativeLayout mImageArea;
    private Question mQuestion;
    private OnAnswerModifiedListener mOnAnswerModifiedListener;

    public ModifyAnswerDialog(Context context, Question question) {
        super(context);
        mQuestion = question;
    }

    public void setOnAnswerModifiedListener(OnAnswerModifiedListener onAnswerModifiedListener) {
        mOnAnswerModifiedListener = onAnswerModifiedListener;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_modify_answer;
    }

    @Override
    protected void onCreateContentView() {
        setCanceledOnTouchOutside(false);
        findViews();
        if (mQuestion.imageData != null) {
            mImageArea.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mQuestion.imageData).dontAnimate().
                    dontTransform().dontAnimate().dontTransform().into(mImageQuestion);
            mButtonZoomIn.setColorFilter(ContextCompat.getColor(getContext()
                    , R.color.learn_all_button_zoom_in_normal_color), PorterDuff.Mode.SRC_ATOP);
        } else {
            mImageArea.setVisibility(View.GONE);
        }
        makeAnswerGroup(
                mQuestion.fixedAnswer != -1 ? mQuestion.fixedAnswer : mQuestion.correctAnswer);
        mTextQuestion.setText(mQuestion.question);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_area:
                ZoomInImageDialog dialog = new ZoomInImageDialog(getContext(), mQuestion.imageData);
                dialog.show();
                break;
            case R.id.button_ok:
                RadioButton tmp = (RadioButton) findViewById(mAnswerGroup.getCheckedRadioButtonId());
                int fixedAnswer = getModifiedAnswer(
                        tmp.getText().toString().substring(SUB_STRING_START_INDEX));
                DataSource.modifyAnswer(mQuestion.id, fixedAnswer);
                if (mOnAnswerModifiedListener != null) {
                    mOnAnswerModifiedListener.onAnswerModified(fixedAnswer);
                }
                dismiss();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
        }
    }

    private void findViews() {
        mAnswerGroup = (RadioGroup) findViewById(R.id.answer_group);
        mImageArea = (RelativeLayout) findViewById(R.id.image_area);
        mImageQuestion = (ImageView) findViewById(R.id.question_image);
        mTextQuestion = (TextView) findViewById(R.id.text_question);
        mButtonZoomIn = (ImageView) findViewById(R.id.button_zoom_in);
        mButtonZoomIn.setOnClickListener(this);
        findViewById(R.id.button_ok).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);
        findViewById(R.id.question_area).setOnClickListener(this);
    }

    private void makeAnswerGroup(int correctAnswer) {
        if (correctAnswer == DataSource.ANSWER_A) {
            makeChoice("A. " + mQuestion.answer1, TYPE_TEXT);
            makeChoice("B. " + mQuestion.answer2, TYPE_RADIO);
            makeChoice("C. " + mQuestion.answer3, TYPE_RADIO);
            if (!TextUtils.isEmpty(mQuestion.answer4)) {
                makeChoice("D. " + mQuestion.answer4, TYPE_RADIO);
            }
        } else if (correctAnswer == DataSource.ANSWER_B) {
            makeChoice("A. " + mQuestion.answer1, TYPE_RADIO);
            makeChoice("B. " + mQuestion.answer2, TYPE_TEXT);
            makeChoice("C. " + mQuestion.answer3, TYPE_RADIO);
            if (!TextUtils.isEmpty(mQuestion.answer4)) {
                makeChoice("D. " + mQuestion.answer4, TYPE_RADIO);
            }
        } else if (correctAnswer == DataSource.ANSWER_C) {
            makeChoice("A. " + mQuestion.answer1, TYPE_RADIO);
            makeChoice("B. " + mQuestion.answer2, TYPE_RADIO);
            makeChoice("C. " + mQuestion.answer3, TYPE_TEXT);
            if (!TextUtils.isEmpty(mQuestion.answer4)) {
                makeChoice("D. " + mQuestion.answer4, TYPE_RADIO);
            }
        } else {
            makeChoice("A. " + mQuestion.answer1, TYPE_RADIO);
            makeChoice("B. " + mQuestion.answer2, TYPE_RADIO);
            makeChoice("C. " + mQuestion.answer3, TYPE_RADIO);
            makeChoice("D. " + mQuestion.answer4, TYPE_TEXT);
        }
    }

    private int getModifiedAnswer(String answer) {
        if (answer.equals(mQuestion.answer1)) return DataSource.ANSWER_A;
        else if (answer.equals(mQuestion.answer2)) return DataSource.ANSWER_B;
        else if (answer.equals(mQuestion.answer3)) return DataSource.ANSWER_C;
        else if (answer.equals(mQuestion.answer4)) return DataSource.ANSWER_D;
        return -1;
    }

    private void makeChoice(String answer, int type) {
        switch (type) {
            case TYPE_TEXT:
                TextView textView = new TextView(getContext());
                textView.setText(answer);
                textView.setTextSize(getContext().getResources().
                        getDimensionPixelSize(R.dimen.common_text_size_10));
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                mAnswerGroup.addView(textView);
                break;
            case TYPE_RADIO:
                AppCompatRadioButton radioButton = new AppCompatRadioButton(getContext());
                radioButton.setText(answer);
                radioButton.setTextSize(getContext().getResources().
                        getDimensionPixelSize(R.dimen.common_text_size_10));
                radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                mAnswerGroup.addView(radioButton);
                break;
        }
    }

    public interface OnAnswerModifiedListener {
        void onAnswerModified(int correctAnswer);
    }
}
