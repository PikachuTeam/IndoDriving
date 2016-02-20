package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class QuestionNoItemWrapper {

    private Context context;
    private View view;
    private OnQuestionNoClickListener listener;

    private TextView textViewNumber;
    private LinearLayout questionNoWrapper;
    private View wall;

    public QuestionNoItemWrapper(Context context) {
        this.context = context;
        view = View.inflate(this.context, R.layout.item_question_no, null);
        findViews(view);
    }

    private void findViews(View rootView) {
        textViewNumber = (TextView) rootView.findViewById(R.id.textViewNumber);
        wall = rootView.findViewById(R.id.wall);
        questionNoWrapper = (LinearLayout) rootView.findViewById(R.id.questionNoWrapper);

        questionNoWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onQuestionNoClick(QuestionNoItemWrapper.this);
                }
            }
        });
    }

    public int getQuestionNo() {
        return Integer.parseInt(textViewNumber.getText().toString());
    }

    public void setOnQuestionNoClickListener(OnQuestionNoClickListener listener) {
        this.listener = listener;
    }

    public void setActive(boolean isActive) {
        if (isActive) {
            textViewNumber.setAlpha(1);
        } else {
            textViewNumber.setAlpha(0);
        }
    }

    public void setText(String number) {
        textViewNumber.setText(number);
    }

    public View getView() {
        return this.view;
    }

    public interface OnQuestionNoClickListener {
        void onQuestionNoClick(QuestionNoItemWrapper item);
    }
}
