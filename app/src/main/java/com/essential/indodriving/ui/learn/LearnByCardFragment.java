package com.essential.indodriving.ui.learn;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
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
import java.util.Set;

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
    private LinearLayout layoutTutorial;
    private CardView learningCardContainer;
    private ImageButton buttonPrevious;
    private ImageButton buttonNext;
    private ImageButton buttonNotShowAgain;

    private int type;
    private int currentPosition;
    private int textRotation;
    private Animator zoomInAnimator;
    private int shortAnimationDuration;
    private boolean isFront;
    private boolean isCheck;

    private final static float TUTORIAL_LAYOUT_ALPHA = 0.35f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getAllQuestionByType(type);
        isFront = true;
        currentPosition = getCurrentPosition();
        getNumberOfQuestions();
        isCheck = false;

        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
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
        moveHorizontalScrollBar(currentPosition);
        if (!isShowAgain()) {
            layoutTutorial.setVisibility(View.GONE);
        }
        layoutTutorial.setAlpha(TUTORIAL_LAYOUT_ALPHA);
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
        layoutTutorial = (LinearLayout) rootView.findViewById(R.id.layoutTutorial);
        buttonNotShowAgain = (ImageButton) rootView.findViewById(R.id.buttonNotShowAgain);

        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        learningCard.setOnClickListener(this);
        layoutTutorial.setOnClickListener(this);
        imgCard.setOnClickListener(this);
        buttonNotShowAgain.setOnClickListener(this);
    }

    private void moveHorizontalScrollBar(int itemIndex) {
        scrollToCenter(numbers.get(itemIndex));
        resetAllQuestionNumber();
        numbers.get(itemIndex).setActive(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveState();
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case DataSource.TYPE_SIM_A:
                editor.putInt("Current Position A", currentPosition);
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                editor.putInt("Current Position A Umum", currentPosition);
                break;
            case DataSource.TYPE_SIM_B1:
                editor.putInt("Current Position B1", currentPosition);
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                editor.putInt("Current Position B1 Umum", currentPosition);
                break;
            case DataSource.TYPE_SIM_B2:
                editor.putInt("Current Position B2", currentPosition);
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                editor.putInt("Current Position B2 Umum", currentPosition);
                break;
            case DataSource.TYPE_SIM_C:
                editor.putInt("Current Position C", currentPosition);
                break;
            case DataSource.TYPE_SIM_D:
                editor.putInt("Current Position D", currentPosition);
                break;
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNext) {
            if (currentPosition != questions.size() + 1) {
                currentPosition++;
                setCardData(isFront, currentPosition);
                moveHorizontalScrollBar(currentPosition);
            }
        } else if (v == buttonPrevious) {
            if (currentPosition != 0) {
                currentPosition--;
                setCardData(isFront, currentPosition);
                moveHorizontalScrollBar(currentPosition);
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
        } else if (v == imgCard) {
            zoomImageFromThumb(imgCard, questions.get(currentPosition).image, this.getView());
        } else if (v == layoutTutorial) {
            layoutTutorial.setVisibility(View.GONE);
            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Show Tutorial Again", !isCheck);
            editor.commit();
        } else if (v == buttonNotShowAgain) {
            if (isCheck) {
                buttonNotShowAgain.setImageResource(R.drawable.ic_check_box_outline);
                isCheck = false;
            } else {
                buttonNotShowAgain.setImageResource(R.drawable.ic_check_box);
                isCheck = true;
            }
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
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
    }

    private int getCurrentPosition() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        switch (type) {
            case DataSource.TYPE_SIM_A:
                return sharedPreferences.getInt("Current Position A", 0);
            case DataSource.TYPE_SIM_A_UMUM:
                return sharedPreferences.getInt("Current Position A Umum", 0);
            case DataSource.TYPE_SIM_B1:
                return sharedPreferences.getInt("Current Position B1", 0);
            case DataSource.TYPE_SIM_B1_UMUM:
                return sharedPreferences.getInt("Current Position B1 Umum", 0);
            case DataSource.TYPE_SIM_B2:
                return sharedPreferences.getInt("Current Position B2", 0);
            case DataSource.TYPE_SIM_B2_UMUM:
                return sharedPreferences.getInt("Current Position B2 Umum", 0);
            case DataSource.TYPE_SIM_C:
                return sharedPreferences.getInt("Current Position C", 0);
            case DataSource.TYPE_SIM_D:
                return sharedPreferences.getInt("Current Position D", 0);
            default:
                return 0;
        }
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

    private void scrollToCenter(QuestionNoItemWrapper item) {
        int centerX = horizontalScrollView.getWidth() / 2;
        int[] itemPos = new int[]{0, 0};
        item.getView().getLocationOnScreen(itemPos);
        int x = itemPos[0];
        int offset = x - centerX + item.getView().getWidth() / 2;
        horizontalScrollView.smoothScrollTo(horizontalScrollView.getScrollX() + offset, 0);
    }

    private boolean isShowAgain() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("Show Tutorial Again", true);
    }

    @Override
    public void onQuestionNoClick(QuestionNoItemWrapper item) {
        currentPosition = item.getQuestionNo();
        setCardData(isFront, currentPosition);
        moveHorizontalScrollBar(currentPosition);
    }

    private void zoomImageFromThumb(final View thumbView, Bitmap image, View rootView) {
        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) rootView.findViewById(
                R.id.expandedImage);
        expandedImageView.setImageBitmap(image);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        rootView.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                zoomInAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                zoomInAnimator = null;
            }
        });
        set.start();
        zoomInAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zoomInAnimator != null) {
                    zoomInAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        zoomInAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        zoomInAnimator = null;
                    }
                });
                set.start();
                zoomInAnimator = set;
            }
        });
    }
}
