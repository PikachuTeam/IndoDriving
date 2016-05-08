package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class AnswerChoicesItem implements View.OnClickListener {

    private View mView;
    private Context mContext;
    private AppCompatCheckBox checkBox;
    private TextView textViewAnswer;
    private TextView mTextNotify;
    private OnChooseAnswerListener listener;
    private int index;

    public AnswerChoicesItem(Context context, int index) {
        this.mContext = context;
        this.index = index;
        mView = View.inflate(this.mContext, R.layout.item_choice, null);
        findViews(mView);
    }

    private void findViews(View rootView) {
        checkBox = (AppCompatCheckBox) rootView.findViewById(R.id.checkBox);
        textViewAnswer = (TextView) rootView.findViewById(R.id.textViewAnswer);
        mTextNotify = (TextView) rootView.findViewById(R.id.text_notify);
        checkBox.setOnClickListener(this);
        rootView.findViewById(R.id.choiceLayout).setOnClickListener(this);
    }

    public View getView() {
        return this.mView;
    }

    public int getIndex() {
        return index;
    }

    public void setActive(boolean isActive) {
        checkBox.setChecked(isActive);
    }

    public void showTextNotify(boolean isCorrect) {
        mTextNotify.setVisibility(View.VISIBLE);
        if (isCorrect) {
            mTextNotify.setText(mContext.getResources().getString(R.string.correct_answer));
            mTextNotify.setBackgroundColor(ContextCompat.getColor(mContext, R.color.correct_answer_color));
        } else {
            mTextNotify.setText(mContext.getResources().getString(R.string.wrong_answer));
            mTextNotify.setBackgroundColor(ContextCompat.getColor(mContext, R.color.wrong_answer_color));
        }
    }

    public void hideTextNotify() {
        mTextNotify.setVisibility(View.GONE);
    }

    public void setOnChooseAnswerListener(OnChooseAnswerListener listener) {
        this.listener = listener;
    }

    public void setChoice(String choice) {
        textViewAnswer.setText(choice);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onChooseAnswer(this);
        }
    }

    public interface OnChooseAnswerListener {
        void onChooseAnswer(AnswerChoicesItem item);
    }
}
