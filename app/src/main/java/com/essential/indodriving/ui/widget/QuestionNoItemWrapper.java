package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class QuestionNoItemWrapper {

    public boolean isHighlight;
    private Context context;
    private View view;
    private OnQuestionNoClickListener listener;
    private TextView textViewNumber;
    private LinearLayout questionNoWrapper;
    private View highlightView;

    public QuestionNoItemWrapper(Context context) {
        this.context = context;
        view = View.inflate(this.context, R.layout.item_question_no, null);
        findViews(view);
    }

    private void findViews(View rootView) {
        textViewNumber = (TextView) rootView.findViewById(R.id.textViewNumber);
        highlightView = rootView.findViewById(R.id.highlightView);
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
        return Integer.parseInt(textViewNumber.getText().toString()) - 1;
    }

    public void setOnQuestionNoClickListener(OnQuestionNoClickListener listener) {
        this.listener = listener;
    }

    public void setActive(boolean isActive) {
        if (isActive) {
            highlightView.setVisibility(View.VISIBLE);
        } else {
            highlightView.setVisibility(View.GONE);
        }
    }

    public void setHighlight() {
        isHighlight = true;
        textViewNumber.setTextColor(ContextCompat.getColor(context, R.color.question_wrapper_text_highlight_color));
    }

    public void setText(String number, Typeface font) {
        textViewNumber.setText(number);
        textViewNumber.setTypeface(font);
    }

    public void setBackgroundResource(int drawable) {
        questionNoWrapper.setBackgroundResource(drawable);
    }

    public View getView() {
        return this.view;
    }

    public interface OnQuestionNoClickListener {
        void onQuestionNoClick(QuestionNoItemWrapper item);
    }
}
