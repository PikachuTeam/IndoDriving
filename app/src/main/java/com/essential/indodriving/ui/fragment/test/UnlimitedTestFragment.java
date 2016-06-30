package com.essential.indodriving.ui.fragment.test;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.util.ArrayList;

import tatteam.com.app_common.ads.AdsNativeExpressHandler;

/**
 * Created by yue on 07/05/2016.
 */
public class UnlimitedTestFragment extends MyBaseFragment {

    public final static String TAG_WRITTEN_TEST_FRAGMENT = "Written Test Fragment";
    private final int NUMBER_OF_QUESTIONS = 20;
    private ViewPager mQuestionPager;
    private LinearLayout mTestHorizontalScrollView;
    private HorizontalScrollView mTestHorizontalScrollContainer;
    private ViewPagerAdapter mAdapter;
    private ArrayList<Question> mQuestions;
    private ArrayList<QuestionNoItemWrapper> mWrappers;
    private int mCurrentPosition;
    private int mType;
    private Typeface mFont;
    private AdsNativeExpressHandler adsHandler1, adsHandler2;
    private QuestionNoItemWrapper.OnQuestionNoClickListener mOnQuestionNoClickListener =
            new QuestionNoItemWrapper.OnQuestionNoClickListener() {
                @Override
                public void onQuestionNoClick(QuestionNoItemWrapper item) {
                    resetAllWrapper();
                    item.setActive(true);
                    mTestHorizontalScrollView.invalidate();
                    int index = mWrappers.indexOf(item);
                    mQuestionPager.setCurrentItem(index, true);
                    mCurrentPosition = index;
                    scrollToCenter(item);
                    getMyBaseActivity().showBigAdsIfNeeded();
                }
            };
    private OnQuestionPagerItemClickListener mOnQuestionPagerItemClickListener =
            new OnQuestionPagerItemClickListener() {
                @Override
                public void onQuestionPagerItemClick(AnswerChoicesItem item) {
                    mQuestions.get(mCurrentPosition).answer = item.getIndex();
                    QuestionNoItemWrapper wrapper = mWrappers.get(mCurrentPosition);
                    if (!wrapper.isHighlight) {
                        wrapper.setHighlight();
                        mTestHorizontalScrollView.invalidate();
                    }
                    mAdapter.refreshChoicesArea(mCurrentPosition);
                }
            };
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position;
            resetAllWrapper();
            mWrappers.get(mCurrentPosition).setActive(true);
            mTestHorizontalScrollView.invalidate();
            scrollToCenter(mWrappers.get(mCurrentPosition));
            getMyBaseActivity().showBigAdsIfNeeded();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private BaseConfirmDialog.OnConfirmDialogButtonClickListener mOnConfirmDialogButtonClickListener =
            new BaseConfirmDialog.OnConfirmDialogButtonClickListener() {
                @Override
                public void onConfirmDialogButtonClick(BaseConfirmDialog.ConfirmButton button, int type, BaseConfirmDialog dialog) {
                    switch (button) {
                        case OK:
                            dialog.dismiss();
                            switch (type) {
                                case BaseConfirmDialog.TYPE_WARNING_1: // happen when user presses back
                                    getFragmentManager().popBackStack();
                                    break;
                                case BaseConfirmDialog.TYPE_WARNING_2: // happen when user presses Result
                                    moveToNextFragment();
                                    break;
                            }
                            break;
                        case CANCEL:
                            dialog.dismiss();
                            break;
                    }
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        mQuestions = DataSource.getQuestionsByTypeAndExamId(mType, 1, true, NUMBER_OF_QUESTIONS);
        mFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/UTM Caviar.ttf");
        mWrappers = new ArrayList<>();
        for (int i = 0; i < mQuestions.size(); i++) {
            QuestionNoItemWrapper wrapper = new QuestionNoItemWrapper(getActivity());
            wrapper.setText("" + mQuestions.get(i).index, HomeActivity.defaultFont);
            if (i == 0) {
                wrapper.setActive(true);
            } else {
                wrapper.setActive(false);
            }
            wrapper.setOnQuestionNoClickListener(mOnQuestionNoClickListener);
            mWrappers.add(wrapper);
        }
        mCurrentPosition = 0;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        mAdapter = new ViewPagerAdapter(getActivity(), mQuestions);
        mAdapter.setOnQuestionPagerItemClickListener(mOnQuestionPagerItemClickListener);
        mQuestionPager.setAdapter(mAdapter);
        mQuestionPager.addOnPageChangeListener(mOnPageChangeListener);
        for (int i = 0; i < mWrappers.size(); i++) {
            mTestHorizontalScrollView.addView(mWrappers.get(i).getView());
        }
    }

    @Override
    protected boolean enableButtonResult() {
        return true;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_written_test);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_do_test;
    }

    @Override
    public void onBackPressed() {
        WarningDialog warningDialog = new WarningDialog(getActivity(), BaseConfirmDialog.TYPE_WARNING_1
                , HomeActivity.defaultFont);
        warningDialog.addListener(mOnConfirmDialogButtonClickListener);
        warningDialog.show();
    }

    @Override
    protected void onMenuItemClick(int id) {
        if (id == MyBaseFragment.BUTTON_RESULT) {
            WarningDialog warningDialog = new WarningDialog(getActivity(), BaseConfirmDialog.TYPE_WARNING_2
                    , HomeActivity.defaultFont);
            warningDialog.addListener(mOnConfirmDialogButtonClickListener);
            warningDialog.show();
        }
    }

    private void findViews(View rootView) {
        rootView.findViewById(R.id.linear_clock).setVisibility(View.GONE);
        mQuestionPager = (ViewPager) rootView.findViewById(R.id.questionPager);
        mTestHorizontalScrollContainer = (HorizontalScrollView) rootView.findViewById(R.id.testHorizontalScrollContainer);
        mTestHorizontalScrollView = (LinearLayout) rootView.findViewById(R.id.testHorizontalScrollView);
        setFont(rootView);
    }

    private void setFont(View rootView) {
        ((TextView) rootView.findViewById(R.id.textViewTwoDots)).setTypeface(HomeActivity.defaultFont);
        ((TextView) rootView.findViewById(R.id.headerQuestion)).setTypeface(mFont);
        ((TextView) rootView.findViewById(R.id.headerQuestion)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void getData() {
        Bundle bundle = getArguments();
        if (bundle != null) mType = bundle.getInt(Constants.BUNDLE_TYPE);
    }

    private void resetAllWrapper() {
        for (int i = 0; i < mWrappers.size(); i++) {
            mWrappers.get(i).setActive(false);
        }
    }

    private void scrollToCenter(QuestionNoItemWrapper questionNoItemWrapper) {
        int centerX = mTestHorizontalScrollContainer.getWidth() / 2;
        int[] itemPos = new int[]{0, 0};
        questionNoItemWrapper.getView().getLocationOnScreen(itemPos);
        int x = itemPos[0];
        int offset = x - centerX + questionNoItemWrapper.getView().getWidth() / 2;
        mTestHorizontalScrollContainer.smoothScrollTo(mTestHorizontalScrollContainer.getScrollX() + offset, 0);
    }

    private void moveToNextFragment() {
        getMyBaseActivity().showBigAdsIfNeeded();
        OverallResultFragment fragment = new OverallResultFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.BUNDLE_NEED_SAVING, false);
        bundle.putString(Constants.BUNDLE_FRAGMENT_TYPE, TAG_WRITTEN_TEST_FRAGMENT);
        putHolder(Constants.KEY_HOLDER_QUESTIONS, mQuestions);
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_WRITTEN_TEST_FRAGMENT);
    }

    private class ViewPagerAdapter extends PagerAdapter implements AnswerChoicesItem.OnChooseAnswerListener, View.OnClickListener, View.OnTouchListener {

        private ArrayList<Question> mQuestions;
        private Context mContext;
        private OnQuestionPagerItemClickListener mListener;

        public ViewPagerAdapter(Context context, ArrayList<Question> questions) {
            this.mContext = context;
            this.mQuestions = questions;
        }

        public void setOnQuestionPagerItemClickListener(OnQuestionPagerItemClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public int getCount() {
            return mQuestions.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Question question = mQuestions.get(position);
            View view = View.inflate(mContext, R.layout.item_pager_question, null);
            ImageView questionImage = (ImageView) view.findViewById(R.id.questionImage);
            TextView textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
            LinearLayout choicesContainer = (LinearLayout) view.findViewById(R.id.choicesContainer);
            RelativeLayout imageArea = (RelativeLayout) view.findViewById(R.id.imageArea);
            ImageView buttonZoomIn = (ImageView) view.findViewById(R.id.buttonZoomIn);
            ((TextView) view.findViewById(R.id.headerChoice)).setTypeface(mFont);
            ((TextView) view.findViewById(R.id.headerChoice)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            if (question.imageData == null) {
                imageArea.setVisibility(View.GONE);
            } else {
                imageArea.setVisibility(View.VISIBLE);
                Glide.with(UnlimitedTestFragment.this).load(question.imageData).dontAnimate().dontTransform().into(questionImage);
                questionImage.setTag(question);
                questionImage.setOnClickListener(this);
                buttonZoomIn.setTag(question);
                buttonZoomIn.setOnTouchListener(this);
            }
            textViewQuestion.setText(question.question);
            ArrayList<AnswerChoicesItem> answerChoicesItems = makeChoices(question);
            for (int i = 0; i < answerChoicesItems.size(); i++) {
                choicesContainer.addView(answerChoicesItems.get(i));
                LinearLayout.MarginLayoutParams marginParams =
                        (LinearLayout.MarginLayoutParams) answerChoicesItems.get(i).getLayoutParams();
                marginParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.common_size_5));
                answerChoicesItems.get(i).requestLayout();
                answerChoicesItems.get(i).setOnChooseAnswerListener(this);
            }
            choicesContainer.invalidate();
            choicesContainer.setTag(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void refreshChoicesArea(int position) {
            Question question = mQuestions.get(position);
            LinearLayout choicesContainer = (LinearLayout) getView().findViewWithTag(position);
            int numberOfChoices = choicesContainer.getChildCount();
            for (int i = 0; i < numberOfChoices; i++) {
                AnswerChoicesItem answerChoicesItem = (AnswerChoicesItem) choicesContainer.getChildAt(i);
                answerChoicesItem.setActive(false);
                answerChoicesItem.hideTextNotify();
            }
            AnswerChoicesItem answerChoicesItem = (AnswerChoicesItem) choicesContainer.getChildAt(question.answer);
            answerChoicesItem.setActive(true);
            answerChoicesItem.showTextNotify(question.answer == question.correctAnswer);
            choicesContainer.invalidate();
        }

        private ArrayList<AnswerChoicesItem> makeChoices(Question question) {
            ArrayList<AnswerChoicesItem> answerChoicesItems = new ArrayList<>();
            if (question.answer1 != null) {
                AnswerChoicesItem answer1 = new AnswerChoicesItem(mContext, DataSource.ANSWER_A);
                answer1.setChoice(question.answer1);
                answer1.changeCheckboxColor(question.correctAnswer == 0);
                answerChoicesItems.add(answer1);
            }
            if (question.answer2 != null) {
                AnswerChoicesItem answer2 = new AnswerChoicesItem(mContext, DataSource.ANSWER_B);
                answer2.setChoice(question.answer2);
                answer2.changeCheckboxColor(question.correctAnswer == 1);
                answerChoicesItems.add(answer2);
            }
            if (question.answer3 != null) {
                AnswerChoicesItem answer3 = new AnswerChoicesItem(mContext, DataSource.ANSWER_C);
                answer3.setChoice(question.answer3);
                answer3.changeCheckboxColor(question.correctAnswer == 2);
                answerChoicesItems.add(answer3);
            }
            if (question.answer4 != null) {
                AnswerChoicesItem answer4 = new AnswerChoicesItem(mContext, DataSource.ANSWER_D);
                answer4.setChoice(question.answer4);
                answer4.changeCheckboxColor(question.correctAnswer == 3);
                answerChoicesItems.add(answer4);
            }
            resetAllChoices(answerChoicesItems);
            if (question.answer != DataSource.ANSWER_NOT_CHOSEN) {
                answerChoicesItems.get(question.answer).setActive(true);
                if (question.fixedAnswer == -1) {
                    answerChoicesItems.get(question.answer).
                            showTextNotify(question.correctAnswer == question.answer);
                } else {
                    answerChoicesItems.get(question.answer).
                            showTextNotify(question.fixedAnswer == question.answer);
                }
            }
            return answerChoicesItems;
        }

        private void resetAllChoices(ArrayList<AnswerChoicesItem> answerChoicesItems) {
            for (int i = 0; i < answerChoicesItems.size(); i++) {
                answerChoicesItems.get(i).setActive(false);
                answerChoicesItems.get(i).hideTextNotify();
            }
        }

        @Override
        public void onChooseAnswer(AnswerChoicesItem item) {
            if (mListener != null) {
                mListener.onQuestionPagerItemClick(item);
            }
        }

        @Override
        public void onClick(View v) {
            ImageView image = (ImageView) v;
            Question question = (Question) image.getTag();
            ZoomInImageDialog dialog = new ZoomInImageDialog(mContext, question.imageData);
            dialog.show();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView image = (ImageView) v;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                image.setImageResource(R.drawable.ic_zoom_in_normal);
                Question question = (Question) image.getTag();
                ZoomInImageDialog dialog = new ZoomInImageDialog(mContext, question.imageData);
                dialog.show();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                image.setImageResource(R.drawable.ic_zoom_in_highlight);
            }
            return false;
        }
    }
}
