package com.essential.indodriving.ui.learn;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.widget.QuestionNoItemWrapper;

import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class LearnByCardFragment extends MyBaseFragment implements View.OnClickListener, QuestionNoItemWrapper.OnQuestionNoClickListener {
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
    private int textRotation;
    private boolean isFront;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getAllQuestionByType(type);
        isFront = true;
        currentPosition = getCurrentPosition();
        getNumberOfQuestions();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_learn_by_card);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_by_card;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        setCardData(isFront, currentPosition);
        addItemToQuestionNoWrapper();
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
            if (currentPosition != questions.size() + 1) {
                currentPosition++;
                setCardData(isFront, currentPosition);
            }
        } else if (v == buttonPrevious) {
            if (currentPosition != 0) {
                currentPosition--;
                setCardData(isFront, currentPosition);
            }
        } else if (v == learningCard) {
            ObjectAnimator anim;
            if (isFront) {
                anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.flip);
                textRotation = -180;
                isFront = false;
            } else {
                anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.reverse_flip);
                textRotation = 0;
                isFront = true;
            }
            anim.setTarget(learningCardContainer);
            anim.setDuration(getResources().getInteger(R.integer.card_flip_time_full));
            anim.start();
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setCardData(isFront, currentPosition);
                    textViewCard.setRotationY(textRotation);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    private void setCardData(boolean isFront, int position) {
        Question question = questions.get(position);
        if (isFront) {
            if (question.image != null) {
                imgCard.setVisibility(View.VISIBLE);
                imgCard.setImageBitmap(question.image);
            } else {
                imgCard.setVisibility(View.GONE);
            }
            textViewCard.setText(question.question);

        } else {
            if (imgCard.getVisibility() != View.GONE) {
                imgCard.setVisibility(View.GONE);
            }
            switch (question.correctAnswer) {
                case 1:
                    textViewCard.setText(question.answer1);
                    break;
                case 2:
                    textViewCard.setText(question.answer2);
                    break;
                case 3:
                    textViewCard.setText(question.answer3);
                    break;
                case 4:
                    textViewCard.setText(question.answer4);
                    break;
            }
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", 1);
    }

    private int getCurrentPosition() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Current Position", 0);
    }

    private void getNumberOfQuestions() {
        numbers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            QuestionNoItemWrapper item = new QuestionNoItemWrapper(getActivity());
            item.setText("" + (i + 1));
            if (i == currentPosition) {
                item.setActive(true);
            }
            item.setOnQuestionNoClickListener(this);
            numbers.add(item);
        }
    }

    private void resetAllQuestionNumber() {
        for (int i = 0; i < numbers.size(); i++) {
            numbers.get(i).setActive(false);
        }
    }

    private void addItemToQuestionNoWrapper() {
        for (int i = 0; i < numbers.size(); i++) {
            horizontalScrollViewContent.addView(numbers.get(i).getView());
        }
    }

    @Override
    public void onQuestionNoClick(QuestionNoItemWrapper item) {
        resetAllQuestionNumber();
        item.setActive(true);
        horizontalScrollViewContent.invalidate();

        currentPosition = item.getQuestionNo();
        setCardData(isFront, currentPosition);
    }
}
