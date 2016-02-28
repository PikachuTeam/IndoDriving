package com.essential.indodriving.ui.test;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.Question;
import com.essential.indodriving.tools.AnimatorHelper;
import com.essential.indodriving.ui.widget.AnswerChoicesItem;
import com.essential.indodriving.ui.widget.QuestionNoItemWrapper;

import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class DoTestFragment extends MyBaseFragment implements ViewPager.OnPageChangeListener, OnQuestionPagerItemClickListener, QuestionNoItemWrapper.OnQuestionNoClickListener {

    private ViewPager questionPager;
    private LinearLayout testHorizontalScrollView;
    private HorizontalScrollView testHorizontalScrollContainer;

    private ViewPagerAdapter adapter;
    private ArrayList<Question> questions;
    private ArrayList<QuestionNoItemWrapper> wrappers;
    private int type;
    private String examId;
    private int shortAnimationDuration;
    private int currentPosition;
    private boolean isRandom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        questions = DataSource.getQuestionsByTypeAndExamId(type, examId, isRandom);
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        wrappers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            QuestionNoItemWrapper wrapper = new QuestionNoItemWrapper(getActivity());
            wrapper.setText("" + questions.get(i).index);
            if (i == 0) {
                wrapper.setActive(true);
            } else {
                wrapper.setActive(false);
            }
            wrapper.setOnQuestionNoClickListener(this);
            wrappers.add(wrapper);
        }

        currentPosition = 0;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_test);
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
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);

        adapter = new ViewPagerAdapter(getActivity(), questions, shortAnimationDuration);
        adapter.setOnQuestionPagerItemClickListener(this);

        questionPager.setAdapter(adapter);
        questionPager.addOnPageChangeListener(this);

        for (int i = 0; i < wrappers.size(); i++) {
            testHorizontalScrollView.addView(wrappers.get(i).getView());
        }
        testHorizontalScrollView.invalidate();
    }

    private void findViews(View rootView) {
        questionPager = (ViewPager) rootView.findViewById(R.id.questionPager);
        testHorizontalScrollContainer = (HorizontalScrollView) rootView.findViewById(R.id.testHorizontalScrollContainer);
        testHorizontalScrollView = (LinearLayout) rootView.findViewById(R.id.testHorizontalScrollView);
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack(ListQuestionFragment.LIST_QUESTION_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
        examId = bundle.getString("Exam Id", "1         ");
        isRandom = bundle.getBoolean("Random", false);
    }

    @Override
    protected void onMenuItemClick(int id) {
        if (id == MyBaseFragment.BUTTON_RESULT_ID) {
        }
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
        questions.get(currentPosition).answer = item.getIndex();
        adapter.notifyDataSetChanged();
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
    }

    private void resetAllWrapper() {
        for (int i = 0; i < wrappers.size(); i++) {
            wrappers.get(i).setActive(false);
        }
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

    private class ViewPagerAdapter extends PagerAdapter implements AnswerChoicesItem.OnChooseAnswerListener, View.OnClickListener {

        private ArrayList<Question> questions;
        private Context context;
        private OnQuestionPagerItemClickListener listener;
        private int currentPosition;
        private int shortAnimationDuration;

        public ViewPagerAdapter(Context context, ArrayList<Question> questions, int shortAnimationDuration) {
            this.context = context;
            this.questions = questions;
            this.shortAnimationDuration = shortAnimationDuration;
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
            if (question.image == null) {
                questionImage.setVisibility(View.GONE);
            } else {
                questionImage.setVisibility(View.VISIBLE);
                questionImage.setImageBitmap(question.image);
            }
            questionImage.setOnClickListener(this);
            textViewQuestion.setText(question.question);

            ArrayList<AnswerChoicesItem> answerChoicesItems = makeChoices(question);
            for (int i = 0; i < answerChoicesItems.size(); i++) {
                choicesContainer.addView(answerChoicesItems.get(i).getView());
                answerChoicesItems.get(i).setOnChooseAnswerListener(this);
            }
            currentPosition = position;
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
            if (question.answer1 != "") {
                AnswerChoicesItem answer1 = new AnswerChoicesItem(context, DataSource.ANSWER_A);
                answer1.setChoice(question.answer1);
                answerChoicesItems.add(answer1);
            }
            if (question.answer2 != "") {
                AnswerChoicesItem answer2 = new AnswerChoicesItem(context, DataSource.ANSWER_B);
                answer2.setChoice(question.answer2);
                answerChoicesItems.add(answer2);
            }
            if (question.answer3 != "") {
                AnswerChoicesItem answer3 = new AnswerChoicesItem(context, DataSource.ANSWER_C);
                answer3.setChoice(question.answer3);
                answerChoicesItems.add(answer3);
            }
            if (question.answer4 != "") {
                AnswerChoicesItem answer4 = new AnswerChoicesItem(context, DataSource.ANSWER_D);
                answer4.setChoice(question.answer4);
                answerChoicesItems.add(answer4);
            }
            resetAllChoices(answerChoicesItems);
            switch (question.answer) {
                case DataSource.ANSWER_A:
                    answerChoicesItems.get(0).setActive(true);
                    break;
                case DataSource.ANSWER_B:
                    answerChoicesItems.get(1).setActive(true);
                    break;
                case DataSource.ANSWER_C:
                    answerChoicesItems.get(2).setActive(true);
                    break;
                case DataSource.ANSWER_D:
                    answerChoicesItems.get(3).setActive(true);
                    break;
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
            AnimatorHelper.zoomImageFromThumb(v, questions.get(currentPosition - 1).image, DoTestFragment.this.getView(), shortAnimationDuration);
        }
    }
}
