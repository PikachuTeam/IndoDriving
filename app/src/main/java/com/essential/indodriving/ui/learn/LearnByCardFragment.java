package com.essential.indodriving.ui.learn;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by dongc_000 on 2/19/2016.
 */
public class LearnByCardFragment extends MyBaseFragment implements View.OnClickListener {

    private boolean isFront = false;
    private CardFrontFragment cardFrontFragment;
    private CardBackFragment cardBackFragment;
    private ArrayList<Question> questions;
    private ArrayList<QuestionNoItemWrapper> numbers;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontalScrollViewContent;
    private int type;
    private int currentPosition;

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
        return R.layout.fragment_learn_by_card;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        if (savedInstanceState == null) {

            findViews(rootView);
            makeData();

            String answer = null;
            String question = questions.get(currentPosition).question;
            Bitmap image = questions.get(currentPosition).image;

            switch (questions.get(currentPosition).correctAnswer) {
                case 1:
                    answer = questions.get(currentPosition).answer1;
                    break;
                case 2:
                    answer = questions.get(currentPosition).answer2;
                    break;
                case 3:
                    answer = questions.get(currentPosition).answer3;
                    break;
                case 4:
                    answer = questions.get(currentPosition).answer4;
                    break;
            }

            cardFrontFragment = new CardFrontFragment(question, image);
            cardBackFragment = new CardBackFragment(answer);
            cardFrontFragment.setOnCardFrontTouchListener(new CardFrontFragment.OnCardFrontTouchListener() {
                @Override
                public void onCardFrontTouch() {
                    getFragmentManager().beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                            .replace(R.id.cardContainer, cardBackFragment)
                            .addToBackStack(null)
                            .commit();
                    isFront = false;
                }
            });

            cardBackFragment.setOnCardFrontTouchListener(new CardBackFragment.OnCardBackTouchListener() {
                @Override
                public void onCardBackTouch() {
                    getFragmentManager().popBackStack();
                    isFront = true;
                }
            });
            getFragmentManager().beginTransaction().add(R.id.cardContainer, cardBackFragment).commit();
            getFragmentManager().beginTransaction().replace(R.id.cardContainer, cardFrontFragment).commit();
            isFront = true;
        }
    }

    private void findViews(View rootView) {
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
        horizontalScrollViewContent = (LinearLayout) rootView.findViewById(R.id.horizontalScrollViewContent);

        rootView.findViewById(R.id.buttonPrevious).setOnClickListener(this);
        rootView.findViewById(R.id.buttonNext).setOnClickListener(this);
    }

    private void setCardData(int position) {
        cardFrontFragment.setImage(questions.get(position).image);
        cardFrontFragment.setText(questions.get(position).question);

        switch (questions.get(position).correctAnswer) {
            case 1:
                cardBackFragment.setText(questions.get(position).answer1);
                break;
            case 2:
                cardBackFragment.setText(questions.get(position).answer2);
                break;
            case 3:
                cardBackFragment.setText(questions.get(position).answer3);
                break;
            case 4:
                cardBackFragment.setText(questions.get(position).answer4);
                break;
        }
    }

    private void makeData() {
        questions = DataSource.getAllQuestionByType(type);
        numbers = new ArrayList<>();
        currentPosition = 0;
        for (int i = 0; i < questions.size(); i++) {
            QuestionNoItemWrapper item = new QuestionNoItemWrapper(getActivity());
            item.setText("" + (i + 1));
            horizontalScrollViewContent.addView(item.getView());
            if (i == 0) {
                item.setActive(true);
            }
            numbers.add(item);
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", 1);
    }

    @Override
    public void onClick(View v) {
        ImageButton imageButton = (ImageButton) v;
        switch (imageButton.getId()) {
            case R.id.buttonPrevious:
                if (currentPosition != 0) {
                    currentPosition--;
                    setCardData(currentPosition);
                }
                break;
            case R.id.buttonNext:
                if (currentPosition != questions.size() - 1) {
                    currentPosition++;
                    setCardData(currentPosition);
                }
                break;
        }
    }

    public static class CardFrontFragment extends Fragment {

        private ImageView imgCardFront;
        private TextView textViewCardFront;
        private String question;
        private Bitmap image;

        private OnCardFrontTouchListener listener;

        public CardFrontFragment() {
        }

        public CardFrontFragment(String question, Bitmap image) {
            this.question = question;
            this.image = image;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.item_card_front, container, false);

//            findViews(view);
//
//            setImage(image);
//            setText(question);

            return null;
        }

        private void findViews(View rootView) {
//            imgCardFront = (ImageView) rootView.findViewById(R.id.imgCardFront);
//            textViewCardFront = (TextView) rootView.findViewById(R.id.textViewCardFront);
//            rootView.findViewById(R.id.cardFront).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        listener.onCardFrontTouch();
//                    }
//                }
//            });
        }

        public void setImage(Bitmap image) {
            if (image == null) {
                imgCardFront.setVisibility(View.GONE);
            } else {
                imgCardFront.setVisibility(View.VISIBLE);
                imgCardFront.setImageBitmap(image);
            }
        }

        public void setText(String question) {
            textViewCardFront.setText(question);
        }

        public void setOnCardFrontTouchListener(OnCardFrontTouchListener listener) {
            this.listener = listener;
        }

        private interface OnCardFrontTouchListener {
            void onCardFrontTouch();
        }
    }

    public static class CardBackFragment extends Fragment {

        private TextView textViewCardBack;
        private String answer;

        private OnCardBackTouchListener listener;

        public CardBackFragment() {
        }

        public CardBackFragment(String answer) {
            this.answer = answer;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.item_card_back, container, false);
//
//            textViewCardBack = (TextView) view.findViewById(R.id.textViewCardBack);
//            view.findViewById(R.id.cardBack).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        listener.onCardBackTouch();
//                    }
//                }
//            });
//
//            setText(answer);
            return null;
        }

        public void setText(String answer) {
            textViewCardBack.setText(answer);
        }

        public void setOnCardFrontTouchListener(OnCardBackTouchListener listener) {
            this.listener = listener;
        }

        private interface OnCardBackTouchListener {
            void onCardBackTouch();
        }
    }
}
