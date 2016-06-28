package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class AnswerChoicesItem extends CardView implements View.OnClickListener {

    private AppCompatCheckBox mCheckBox;
    private TextView mTextAnswer;
    private TextView mTextNotify;
    private OnChooseAnswerListener mListener;
    private int mIndex;

    public AnswerChoicesItem(Context context, int index) {
        this(context, index, true);
    }

    public AnswerChoicesItem(Context context, int index, boolean showCheckbox) {
        super(context);
        View.inflate(context, R.layout.item_choice, this);
        mIndex = index;
        setRadius(getResources().getDimensionPixelSize(R.dimen.common_size_3));
        setPreventCornerOverlap(false);
        findViews();
        mCheckBox.setVisibility(showCheckbox ? VISIBLE : INVISIBLE);
    }

    public void changeTextColor() {
        mTextAnswer.setTextColor(
                ContextCompat.getColor(getContext(), R.color.dialog_not_chosen_text_color));
    }

    private void findViews() {
        mCheckBox = (AppCompatCheckBox) findViewById(R.id.checkBox);
        mTextAnswer = (TextView) findViewById(R.id.textViewAnswer);
        mTextNotify = (TextView) findViewById(R.id.text_notify);
        mCheckBox.setOnClickListener(this);
        findViewById(R.id.choiceLayout).setOnClickListener(this);
    }

    public int getIndex() {
        return mIndex;
    }

    public void setActive(boolean isActive) {
        mCheckBox.setChecked(isActive);
    }

    public void showTextNotify(boolean isCorrect) {
        mTextNotify.setVisibility(View.VISIBLE);
        if (isCorrect) {
            mTextNotify.setText(getContext().getResources().getString(R.string.correct_answer));
            mTextNotify.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.correct_answer_color));
        } else {
            mTextNotify.setText(getContext().getResources().getString(R.string.wrong_answer));
            mTextNotify.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.wrong_answer_color));
        }
    }

    public void hideTextNotify() {
        mTextNotify.setVisibility(View.GONE);
    }

    public void setOnChooseAnswerListener(OnChooseAnswerListener listener) {
        this.mListener = listener;
    }

    public void setChoice(String choice) {
        mTextAnswer.setText(choice);
    }

    public void changeCheckboxColor(boolean isTrue) {
        ColorStateList colorStateList;
        if (isTrue) {
            colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked},
                    },
                    new int[]{
                            ContextCompat.getColor(getContext(), R.color.not_answered_color),
                            ContextCompat.getColor(getContext(), R.color.correct_answer_color)
                    }
            );
        } else {
            colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked},
                    },
                    new int[]{
                            ContextCompat.getColor(getContext(), R.color.not_answered_color),
                            ContextCompat.getColor(getContext(), R.color.wrong_answer_color)
                    }
            );
        }
        mCheckBox.setSupportButtonTintList(colorStateList);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onChooseAnswer(this);
        }
    }

    public interface OnChooseAnswerListener {
        void onChooseAnswer(AnswerChoicesItem item);
    }
}
