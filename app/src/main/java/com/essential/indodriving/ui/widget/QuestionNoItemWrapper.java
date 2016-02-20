package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class QuestionNoItemWrapper {

    private Context context;
    private View view;

    private TextView textViewNumber;
    private View wall;

    public QuestionNoItemWrapper(Context context) {
        this.context = context;
        view = View.inflate(this.context, R.layout.item_question_no, null);
        findViews(view);
    }

    private void findViews(View rootView) {
        textViewNumber = (TextView) rootView.findViewById(R.id.textViewNumber);
        wall = rootView.findViewById(R.id.wall);
    }

    public void setActive(boolean isActive) {
        if (isActive) {
            textViewNumber.setAlpha(1);
        } else {
            textViewNumber.setAlpha(0);
        }
    }

    public void setText(String number){
        textViewNumber.setText(number);
    }

    public View getView(){
        return this.view;
    }
}
