package com.essential.indodriving.ui.learn;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
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
    private CardView learningCardContainer;
    private ImageButton buttonPrevious;
    private ImageButton buttonNext;

    private int type;
    private int currentPosition;
    private boolean isFront;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getAllQuestionByType(type);
        isFront = true;
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
        findViews(rootView);
    }

    private void findViews(View rootView) {
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
        horizontalScrollViewContent = (LinearLayout) rootView.findViewById(R.id.horizontalScrollViewContent);
        learningCard = rootView.findViewById(R.id.learningCard);
        learningCardContainer = (CardView) rootView.findViewById(R.id.learningCardContainer);
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
            Toast.makeText(getActivity(), "test1", Toast.LENGTH_SHORT).show();
            if (currentPosition != questions.size() + 1) {
                if (isFront) {

                } else {

                }
            }
        } else if (v == buttonPrevious) {
            Toast.makeText(getActivity(), "test2", Toast.LENGTH_SHORT).show();
            if (currentPosition != 0) {
                if (isFront) {

                } else {

                }
            }
        } else if (v == learningCard) {
            Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
            ObjectAnimator anim;
            if (isFront) {
                anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.flip);
                isFront = false;
            } else {
                anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.reverse_flip);
                isFront = true;
            }
            anim.setTarget(learningCardContainer);
            anim.setDuration(getResources().getInteger(R.integer.card_flip_time_full));
            anim.start();
        }
    }

    private void setCardData(boolean isFront) {
        if (isFront) {

        } else {

        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", 1);
    }
}
