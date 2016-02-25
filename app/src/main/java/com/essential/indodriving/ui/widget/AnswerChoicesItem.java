package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class AnswerChoicesItem implements View.OnClickListener {

    private View view;
    private Context context;

    private RelativeLayout choiceLayout;
    private RelativeLayout radioButtonContainer;
    private View radioButton;
    private TextView textViewAnswer;

    private OnChooseAnswerListener listener;
    private int index;

    public AnswerChoicesItem(Context context, int index) {
        this.context = context;
        this.index = index;
        view = View.inflate(this.context, R.layout.item_choice, null);
        findViews(view);
    }

    private void findViews(View rootView) {
        choiceLayout = (RelativeLayout) rootView.findViewById(R.id.choiceLayout);
        radioButtonContainer = (RelativeLayout) rootView.findViewById(R.id.radioButtonContainer);
        radioButton = (View) rootView.findViewById(R.id.radioButton);
        textViewAnswer = (TextView) rootView.findViewById(R.id.textViewAnswer);

        radioButtonContainer.setOnClickListener(this);
        choiceLayout.setOnClickListener(this);
    }

    public View getView() {
        return this.view;
    }

    public int getIndex() {
        return index;
    }

    public void setActive(boolean isActive) {
        if (isActive) {
            radioButton.setVisibility(View.VISIBLE);
        } else {
            radioButton.setVisibility(View.GONE);
        }
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
