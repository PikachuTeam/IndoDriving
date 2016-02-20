package com.essential.indodriving.ui.learn;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DatabaseHelper;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.widget.QuestionNoItemWrapper;

import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class LearnByCardFragment1 extends MyBaseFragment implements View.OnClickListener {
    private View learningCard;
    private ImageView imgCard;
    private TextView textViewCard;
    private ArrayList<Question> questions;
    private ArrayList<QuestionNoItemWrapper> numbers;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontalScrollViewContent;
    private ImageButton buttonPrevious;
    private ImageButton buttonNext;

    private int type;
    private int currentPosition;
    private boolean isFront;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_learn_by_card);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_by_card_1;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        questions = DatabaseHelper.getInstance().getAllQuestionByType(type);

        isFront = true;
    }

    private void findViews(View rootView) {
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
        horizontalScrollViewContent = (LinearLayout) rootView.findViewById(R.id.horizontalScrollViewContent);
        learningCard = rootView.findViewById(R.id.learningCard);
        imgCard = (ImageView) rootView.findViewById(R.id.imgCard);
        textViewCard = (TextView) rootView.findViewById(R.id.textViewCard);
        buttonPrevious = (ImageButton) rootView.findViewById(R.id.buttonPrevious);
        buttonNext = (ImageButton) rootView.findViewById(R.id.buttonNext);

        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        learningCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNext) {

        } else if (v == buttonPrevious) {
            if (currentPosition != 0) {
                if (isFront) {

                } else {

                }
            }
        } else {
            if (currentPosition != questions.size() + 1) {
                if (isFront) {

                } else {

                }
            }
        }
    }

    private void setCardData() {

    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", 1);
    }
}
