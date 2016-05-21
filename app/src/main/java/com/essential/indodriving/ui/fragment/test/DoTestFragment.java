package com.essential.indodriving.ui.fragment.test;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.widget.AnswerChoicesItem;
import com.essential.indodriving.ui.widget.QuestionNoItemWrapper;
import com.essential.indodriving.ui.widget.WarningDialog;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;
import com.essential.indodriving.util.OnQuestionPagerItemClickListener;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class DoTestFragment extends MyBaseFragment implements ViewPager.OnPageChangeListener,
        OnQuestionPagerItemClickListener, QuestionNoItemWrapper.OnQuestionNoClickListener,
        BaseConfirmDialog.OnConfirmDialogButtonClickListener {

    public final static String TAG_DO_TEST_FRAGMENT = "Do Test Fragment";
    public final static String BUNDLE_IS_RANDOM = "is random";
    public final static int INTERVAL = 1000, TOTAL_TIME = 1801000;
    private final int NUMBER_OF_QUESTIONS = 30;
    private ViewPager questionPager;
    private LinearLayout testHorizontalScrollView;
    private HorizontalScrollView testHorizontalScrollContainer;
    private TextView textViewMinute1;
    private TextView textViewMinute2;
    private TextView textViewSecond1;
    private TextView textViewSecond2;
    private ViewPagerAdapter adapter;
    private ArrayList<Question> questions;
    private ArrayList<QuestionNoItemWrapper> wrappers;
    private int type;
    private int examId;
    private int currentPosition;
    private int minute1, minute2, second1, second2;
    private CountDownTimer timer;
    private boolean mIsRandom;
    private int mTimeLeft;
    private Typeface font;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getQuestionsByTypeAndExamId(type, examId, mIsRandom, NUMBER_OF_QUESTIONS);
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/UTM Caviar.ttf");
        wrappers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            QuestionNoItemWrapper wrapper = new QuestionNoItemWrapper(getActivity());
            wrapper.setText("" + questions.get(i).index, HomeActivity.defaultFont);
            if (i == 0) {
                wrapper.setActive(true);
            } else {
                wrapper.setActive(false);
            }
            wrapper.setOnQuestionNoClickListener(this);
            wrappers.add(wrapper);
        }
        minute1 = 3;
        minute2 = 0;
        second1 = 0;
        second2 = 0;
        mTimeLeft = 0;
        currentPosition = 0;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        adapter = new ViewPagerAdapter(getActivity(), questions);
        adapter.setOnQuestionPagerItemClickListener(this);
        questionPager.setAdapter(adapter);
        questionPager.addOnPageChangeListener(this);
        for (int i = 0; i < wrappers.size(); i++) {
            testHorizontalScrollView.addView(wrappers.get(i).getView());
        }
        testHorizontalScrollView.invalidate();
        textViewMinute1.setText("" + minute1);
        textViewMinute2.setText("" + minute2);
        textViewSecond1.setText("" + second1);
        textViewSecond2.setText("" + second2);
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new CountDownTimer(TOTAL_TIME - INTERVAL * mTimeLeft, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                makeTime();
            }

            @Override
            public void onFinish() {
                moveToNextFragment();
            }
        };
        timer.start();
    }

    @Override
    protected String getTitle() {
        if (mIsRandom) {
            return getString(R.string.title_test);
        } else {
            return MessageFormat.format(getString(R.string.title_package), "" + examId);
        }
    }

    @Override
    protected boolean enableButtonResult() {
        return true;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_do_test;
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        WarningDialog warningDialog = new WarningDialog(getActivity()
                , BaseConfirmDialog.TYPE_WARNING_1, HomeActivity.defaultFont);
        warningDialog.addListener(this);
        warningDialog.show();
    }

    @Override
    protected void onMenuItemClick(int id) {
        if (id == MyBaseFragment.BUTTON_RESULT) {
            timer.cancel();
            WarningDialog warningDialog = new WarningDialog(getActivity()
                    , BaseConfirmDialog.TYPE_WARNING_2, HomeActivity.defaultFont);
            warningDialog.addListener(this);
            warningDialog.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        resetAllWrapper();
        wrappers.get(currentPosition).setActive(true);
        testHorizontalScrollView.invalidate();
        scrollToCenter(wrappers.get(currentPosition));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onQuestionPagerItemClick(AnswerChoicesItem item) {
        if (questions.get(currentPosition).answer == DataSource.ANSWER_NOT_CHOSEN) {
            questions.get(currentPosition).answer = item.getIndex();
            QuestionNoItemWrapper wrapper = wrappers.get(currentPosition);
            if (!wrapper.isHighlight) {
                wrapper.setHighlight();
                testHorizontalScrollView.invalidate();
            }
            if (currentPosition < 29) {
                currentPosition++;
                questionPager.setCurrentItem(currentPosition, true);
                scrollToCenter(wrappers.get(currentPosition));
            }
        } else {
            questions.get(currentPosition).answer = item.getIndex();
            QuestionNoItemWrapper wrapper = wrappers.get(currentPosition);
            if (!wrapper.isHighlight) {
                wrapper.setHighlight();
                testHorizontalScrollView.invalidate();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void findViews(View rootView) {
        questionPager = (ViewPager) rootView.findViewById(R.id.questionPager);
        testHorizontalScrollContainer = (HorizontalScrollView) rootView.findViewById(R.id.testHorizontalScrollContainer);
        testHorizontalScrollView = (LinearLayout) rootView.findViewById(R.id.testHorizontalScrollView);
        textViewMinute1 = (TextView) rootView.findViewById(R.id.textViewMinute1);
        textViewMinute2 = (TextView) rootView.findViewById(R.id.textViewMinute2);
        textViewSecond1 = (TextView) rootView.findViewById(R.id.textViewSecond1);
        textViewSecond2 = (TextView) rootView.findViewById(R.id.textViewSecond2);
        setFont(rootView);
    }

    private void setFont(View rootView) {
        textViewMinute1.setTypeface(HomeActivity.defaultFont);
        textViewMinute2.setTypeface(HomeActivity.defaultFont);
        textViewSecond1.setTypeface(HomeActivity.defaultFont);
        textViewSecond2.setTypeface(HomeActivity.defaultFont);
        ((TextView) rootView.findViewById(R.id.textViewTwoDots)).setTypeface(HomeActivity.defaultFont);
        ((TextView) rootView.findViewById(R.id.headerQuestion)).setTypeface(font);
        ((TextView) rootView.findViewById(R.id.headerQuestion)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constants.BUNDLE_TYPE, DataSource.TYPE_SIM_A);
        examId = bundle.getInt(Constants.BUNDLE_EXAM_ID, 1);
        mIsRandom = bundle.getBoolean(BUNDLE_IS_RANDOM, false);
    }

    private void resetAllWrapper() {
        for (int i = 0; i < wrappers.size(); i++) {
            wrappers.get(i).setActive(false);
        }
    }

    private void makeTime() {
        second2--;
        if (second2 == -1) {
            second2 = 9;
            second1--;
            if (second1 == -1) {
                second1 = 5;
                minute2--;
                if (minute2 == -1) {
                    minute2 = 9;
                    minute1--;
                }
            }
        }
        textViewMinute1.setText("" + minute1);
        textViewMinute2.setText("" + minute2);
        textViewSecond1.setText("" + second1);
        textViewSecond2.setText("" + second2);
        mTimeLeft++;
    }

    @Override
    public void onQuestionNoClick(QuestionNoItemWrapper item) {
        resetAllWrapper();
        item.setActive(true);
        testHorizontalScrollView.invalidate();
        int index = wrappers.indexOf(item);
        questionPager.setCurrentItem(index, true);
        currentPosition = index;
        scrollToCenter(item);
    }

    private void scrollToCenter(QuestionNoItemWrapper questionNoItemWrapper) {
        int centerX = testHorizontalScrollContainer.getWidth() / 2;
        int[] itemPos = new int[]{0, 0};
        questionNoItemWrapper.getView().getLocationOnScreen(itemPos);
        int x = itemPos[0];
        int offset = x - centerX + questionNoItemWrapper.getView().getWidth() / 2;
        testHorizontalScrollContainer.smoothScrollTo(testHorizontalScrollContainer.getScrollX() + offset, 0);
    }

    private void moveToNextFragment() {
        getMyBaseActivity().showBigAdsIfNeeded();
        OverallResultFragment fragment = new OverallResultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, this.type);
        bundle.putInt(Constants.BUNDLE_EXAM_ID, examId);
        bundle.putString(Constants.BUNDLE_FRAGMENT_TYPE, TAG_DO_TEST_FRAGMENT);
        bundle.putBoolean(Constants.BUNDLE_NEED_SAVING, true);
        putHolder(Constants.KEY_HOLDER_QUESTIONS, questions);
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_DO_TEST_FRAGMENT);
    }

    @Override
    public void onConfirmDialogButtonClick(BaseConfirmDialog.ConfirmButton button, int type, BaseConfirmDialog dialog) {
        switch (button) {
            case OK:
                dialog.dismiss();
                switch (type) {
                    case BaseConfirmDialog.TYPE_WARNING_1: // happen when user presses back
                        getFragmentManager().popBackStack(ListQuestionFragment.LIST_QUESTION_FRAGMENT_TAG
                                , FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        break;
                    case BaseConfirmDialog.TYPE_WARNING_2: // happen when user presses Result
                        moveToNextFragment();
                        break;
                }
                break;
            case CANCEL:
                dialog.dismiss();
                timer = new CountDownTimer(TOTAL_TIME - INTERVAL * mTimeLeft, INTERVAL) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        makeTime();
                    }

                    @Override
                    public void onFinish() {
                        moveToNextFragment();
                    }
                };
                timer.start();
                break;
        }
    }

    private class ViewPagerAdapter extends PagerAdapter implements
            AnswerChoicesItem.OnChooseAnswerListener, View.OnClickListener, View.OnTouchListener {

        private ArrayList<Question> questions;
        private Context context;
        private OnQuestionPagerItemClickListener listener;

        public ViewPagerAdapter(Context context, ArrayList<Question> questions) {
            this.context = context;
            this.questions = questions;
        }

        public void setOnQuestionPagerItemClickListener(OnQuestionPagerItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Question question = questions.get(position);
            View view = View.inflate(context, R.layout.item_pager_question, null);
            ImageView questionImage = (ImageView) view.findViewById(R.id.questionImage);
            TextView textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
            LinearLayout choicesContainer = (LinearLayout) view.findViewById(R.id.choicesContainer);
            RelativeLayout imageArea = (RelativeLayout) view.findViewById(R.id.imageArea);
            ImageView buttonZoomIn = (ImageView) view.findViewById(R.id.buttonZoomIn);
            ((TextView) view.findViewById(R.id.headerChoice)).setTypeface(font);
            ((TextView) view.findViewById(R.id.headerChoice)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            if (question.imageData == null) {
                imageArea.setVisibility(View.GONE);
            } else {
                imageArea.setVisibility(View.VISIBLE);
//                questionImage.setImageBitmap(question.image);
                Glide.with(DoTestFragment.this).load(question.imageData).dontAnimate().dontTransform().into(questionImage);
                questionImage.setTag(question);
                questionImage.setOnClickListener(this);
                buttonZoomIn.setTag(question);
                buttonZoomIn.setOnTouchListener(this);
            }
            textViewQuestion.setText(question.question);

            ArrayList<AnswerChoicesItem> answerChoicesItems = makeChoices(question);
            for (int i = 0; i < answerChoicesItems.size(); i++) {
                choicesContainer.addView(answerChoicesItems.get(i).getView());
                LinearLayout.MarginLayoutParams marginParams =
                        (LinearLayout.MarginLayoutParams) answerChoicesItems.get(i).getView().getLayoutParams();
                marginParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.common_size_5));
                answerChoicesItems.get(i).getView().requestLayout();
                answerChoicesItems.get(i).setOnChooseAnswerListener(this);
            }
            choicesContainer.invalidate();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private ArrayList<AnswerChoicesItem> makeChoices(Question question) {
            ArrayList<AnswerChoicesItem> answerChoicesItems = new ArrayList<>();
            if (question.answer1 != null) {
                AnswerChoicesItem answer1 = new AnswerChoicesItem(context, DataSource.ANSWER_A);
                answer1.setChoice(question.answer1);
                answerChoicesItems.add(answer1);
            }
            if (question.answer2 != null) {
                AnswerChoicesItem answer2 = new AnswerChoicesItem(context, DataSource.ANSWER_B);
                answer2.setChoice(question.answer2);
                answerChoicesItems.add(answer2);
            }
            if (question.answer3 != null) {
                AnswerChoicesItem answer3 = new AnswerChoicesItem(context, DataSource.ANSWER_C);
                answer3.setChoice(question.answer3);
                answerChoicesItems.add(answer3);
            }
            if (question.answer4 != null) {
                AnswerChoicesItem answer4 = new AnswerChoicesItem(context, DataSource.ANSWER_D);
                answer4.setChoice(question.answer4);
                answerChoicesItems.add(answer4);
            }
            resetAllChoices(answerChoicesItems);
            if (question.answer != DataSource.ANSWER_NOT_CHOSEN) {
                answerChoicesItems.get(question.answer).setActive(true);
            }
            return answerChoicesItems;
        }

        private void resetAllChoices(ArrayList<AnswerChoicesItem> answerChoicesItems) {
            for (int i = 0; i < answerChoicesItems.size(); i++) {
                answerChoicesItems.get(i).setActive(false);
            }
        }

        @Override
        public void onChooseAnswer(AnswerChoicesItem item) {
            if (listener != null) {
                listener.onQuestionPagerItemClick(item);
            }
        }

        @Override
        public void onClick(View v) {
            ImageView image = (ImageView) v;
            Question question = (Question) image.getTag();
            ZoomInImageDialog dialog = new ZoomInImageDialog(context, question.imageData);
            dialog.show();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView image = (ImageView) v;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                image.setImageResource(R.drawable.ic_zoom_in_normal);
                Question question = (Question) image.getTag();
                ZoomInImageDialog dialog = new ZoomInImageDialog(context, question.imageData);
                dialog.show();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                image.setImageResource(R.drawable.ic_zoom_in_highlight);
            }
            return false;
        }
    }
}
