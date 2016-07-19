package com.essential.indodriving.ui.fragment.sign.test;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.data.sign.SignDataSource;
import com.essential.indodriving.data.sign.SignQuestion;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.base.SecondBaseActivity;
import com.essential.indodriving.ui.widget.AnswerChoicesItem;
import com.essential.indodriving.ui.widget.QuestionNoItemWrapper;
import com.essential.indodriving.ui.widget.WarningDialog;
import com.essential.indodriving.ui.widget.ZoomInImageDialog;
import com.essential.indodriving.util.OnQuestionPagerItemClickListener;
import com.google.android.gms.ads.AdSize;

import java.util.ArrayList;
import java.util.List;

import tatteam.com.app_common.ads.AdsNativeExpressHandler;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;

/**
 * Created by yue on 08/07/2016.
 */
public class SignUnlimitedTestFragment extends MyBaseFragment implements QuestionNoItemWrapper.OnQuestionNoClickListener, BaseConfirmDialog.OnConfirmDialogButtonClickListener, OnQuestionPagerItemClickListener, ViewPager.OnPageChangeListener {

    public final static String TAG_WRITTEN_TEST_FRAGMENT = "Written Test Fragment";
    private ViewPager questionPager;
    private LinearLayout testHorizontalScrollView;
    private HorizontalScrollView testHorizontalScrollContainer;
    private ViewPagerAdapter adapter;
    private List<SignQuestion> questions;
    private List<QuestionNoItemWrapper> wrappers;
    private Typeface font;
    private TextView headerQuestion;
    private String type;
    private int currentPosition;
    private boolean isProVer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        loadState();
        questions = SignDataSource.getQuestions(type);
        if (!isProVer) {
            addAds(questions);
        }
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/UTM Caviar.ttf");
        wrappers = new ArrayList<>();
        int numberOfQuestions = questions.size();
        for (int i = 0; i < numberOfQuestions; i++) {
            QuestionNoItemWrapper wrapper = new QuestionNoItemWrapper(getActivity());
            SignQuestion question = questions.get(i);
            if (question.isAds) {
                wrapper.setText("", HomeActivity.defaultFont);
            } else {
                wrapper.setText("" + question.index, HomeActivity.defaultFont);
            }
            if (i == 0) {
                wrapper.setActive(true);
            } else {
                wrapper.setActive(false);
            }
            wrapper.setBackgroundResource(R.drawable.sign_question_wrapper);
            wrapper.setOnQuestionNoClickListener(this);
            wrappers.add(wrapper);
        }
        currentPosition = 0;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        adapter = new ViewPagerAdapter(getActivity(), questions);
        adapter.setOnQuestionPagerItemClickListener(this);
        questionPager.setAdapter(adapter);
        questionPager.addOnPageChangeListener(this);
        int size = wrappers.size();
        for (int i = 0; i < size; i++) {
            testHorizontalScrollView.addView(wrappers.get(i).getView());
        }
        headerQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
    }

    @Override
    protected String getTitle() {
        return type;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_do_test;
    }

    @Override
    public void onBackPressed() {
        WarningDialog warningDialog = new WarningDialog(getActivity(), BaseConfirmDialog.TYPE_WARNING_1
                , HomeActivity.defaultFont);
        warningDialog.addListener(this);
        warningDialog.show();
    }

    @Override
    protected boolean enableButtonResult() {
        return true;
    }

    @Override
    protected void onMenuItemClick(int id) {
        if (id == MyBaseFragment.BUTTON_RESULT) {
            WarningDialog warningDialog = new WarningDialog(getActivity(),
                    BaseConfirmDialog.TYPE_WARNING_2, HomeActivity.defaultFont);
            warningDialog.addListener(this);
            warningDialog.show();
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
        getMyBaseActivity().showBigAdsIfNeeded();
    }

    @Override
    public void onConfirmDialogButtonClick(
            BaseConfirmDialog.ConfirmButton button,
            @BaseConfirmDialog.DialogTypeDef int dialogType, BaseConfirmDialog dialog) {
        switch (button) {
            case OK:
                dialog.dismiss();
                switch (dialogType) {
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

    @Override
    public void onQuestionPagerItemClick(AnswerChoicesItem item) {
        questions.get(currentPosition).answer = item.getIndex();
        QuestionNoItemWrapper wrapper = wrappers.get(currentPosition);
        if (!wrapper.isHighlight) {
            wrapper.setHighlight();
            testHorizontalScrollView.invalidate();
        }
        adapter.refreshChoicesArea(currentPosition);
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
        getMyBaseActivity().showBigAdsIfNeeded();

        if (questions.get(currentPosition).isAds) {
            headerQuestion.setVisibility(View.INVISIBLE);
        } else {
            headerQuestion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void findViews(View rootView) {
        rootView.findViewById(R.id.linear_clock).setVisibility(View.GONE);
        questionPager = (ViewPager) rootView.findViewById(R.id.questionPager);
        testHorizontalScrollContainer = (HorizontalScrollView)
                rootView.findViewById(R.id.testHorizontalScrollContainer);
        testHorizontalScrollView = (LinearLayout) rootView.findViewById(R.id.testHorizontalScrollView);
        headerQuestion = (TextView) rootView.findViewById(R.id.headerQuestion);
        rootView.findViewById(R.id.root_layout).setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.default_background_color_sign));
        setFont(rootView);
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle != null ? bundle.getString(Constants.BUNDLE_SIGN_TYPE) :
                SignDataSource.GROUP_PROHIBITION_SIGN;
    }

    private void loadState() {
        isProVer = MySetting.getInstance().isProVersion();
    }

    private void addAds(List<SignQuestion> questions) {
        int count = 0;
        int size = questions.size();
        for (int i = 0; i < size; i++) {
            count++;
            if (count % Constants.WRITTEN_TEST_ADS_BREAK == 0) {
                SignQuestion question = new SignQuestion();
                question.isAds = true;
                questions.add(i, question);
                count++;
                size++;
                i++;
            }
        }
    }

    private void resetAllWrapper() {
        int size = wrappers.size();
        for (int i = 0; i < size; i++) {
            wrappers.get(i).setActive(false);
        }
    }

    private void scrollToCenter(QuestionNoItemWrapper questionNoItemWrapper) {
        int centerX = testHorizontalScrollContainer.getWidth() / 2;
        int[] itemPos = new int[]{0, 0};
        questionNoItemWrapper.getView().getLocationOnScreen(itemPos);
        int x = itemPos[0];
        int offset = x - centerX + questionNoItemWrapper.getView().getWidth() / 2;
        testHorizontalScrollContainer.smoothScrollTo(
                testHorizontalScrollContainer.getScrollX() + offset, 0);
    }

    private void setFont(View rootView) {
        ((TextView) rootView.findViewById(R.id.textViewTwoDots)).setTypeface(HomeActivity.defaultFont);
        headerQuestion.setTypeface(font);
        headerQuestion.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void moveToNextFragment() {
        getMyBaseActivity().showBigAdsIfNeeded();
        SignOverallResultFragment fragment = new SignOverallResultFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.BUNDLE_NEED_SAVING, false);
        bundle.putString(Constants.BUNDLE_FRAGMENT_TYPE, TAG_WRITTEN_TEST_FRAGMENT);
        putHolder(Constants.KEY_HOLDER_QUESTIONS, questions);
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_WRITTEN_TEST_FRAGMENT);
    }

    private class ViewPagerAdapter extends PagerAdapter implements
            AnswerChoicesItem.OnChooseAnswerListener, View.OnClickListener, View.OnTouchListener {

        private Context context;
        private List<SignQuestion> questions;
        private OnQuestionPagerItemClickListener listener;

        public ViewPagerAdapter(Context context, List<SignQuestion> questions) {
            this.context = context;
            this.questions = questions;
        }

        public void setOnQuestionPagerItemClickListener(OnQuestionPagerItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return questions != null ? questions.size() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SignQuestion question = questions.get(position);
            View view = View.inflate(context, R.layout.item_pager_test_sign, null);
            LinearLayout choicesContainer = (LinearLayout) view.findViewById(R.id.choices_container);
            ViewGroup adsContainer = (ViewGroup) view.findViewById(R.id.adsContainer);
            ViewGroup layoutTestRoot = (ViewGroup) view.findViewById(R.id.layout_test_root);
            if (question.isAds) {
                adsContainer.setVisibility(View.VISIBLE);
                layoutTestRoot.setVisibility(View.GONE);
                setupAds(adsContainer);
            } else {
                view.setVisibility(View.VISIBLE);
                ImageView imageSign = (ImageView) view.findViewById(R.id.image_sign);
                ImageView buttonZoomIn = (ImageView) view.findViewById(R.id.button_zoom_in);
                TextView headerChoice = (TextView) view.findViewById(R.id.header_choice);
                headerChoice.setTextColor(ContextCompat.getColor(context, R.color.black));
                headerChoice.setTypeface(HomeActivity.defaultFont);
                headerChoice.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                Glide.with(context).load(question.image).dontAnimate().dontTransform().into(imageSign);
                imageSign.setTag(question);
                imageSign.setOnClickListener(this);
                buttonZoomIn.setTag(question);
                buttonZoomIn.setColorFilter(ContextCompat.getColor(context,
                        R.color.sign_do_test_button_zoom_in_normal_color));
                buttonZoomIn.setOnTouchListener(this);
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
            }
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


        @Override
        public void onChooseAnswer(AnswerChoicesItem item) {
            if (listener != null) {
                listener.onQuestionPagerItemClick(item);
            }
        }

        @Override
        public void onClick(View v) {
            ImageView image = (ImageView) v;
            SignQuestion question = (SignQuestion) image.getTag();
            ZoomInImageDialog dialog = new ZoomInImageDialog(context, question.image);
            dialog.show();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView image = (ImageView) v;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                image.setColorFilter(ContextCompat.getColor(context,
                        R.color.sign_do_test_button_zoom_in_normal_color));
                SignQuestion question = (SignQuestion) image.getTag();
                ZoomInImageDialog dialog = new ZoomInImageDialog(context, question.image);
                dialog.show();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                image.setColorFilter(ContextCompat.getColor(context,
                        R.color.sign_do_test_button_zoom_in_highlight_color));
            }
            return false;
        }

        public void refreshChoicesArea(int position) {
            SignQuestion question = questions.get(position);
            LinearLayout choicesContainer = (LinearLayout) getView().findViewWithTag(position);
            int numberOfChoices = choicesContainer.getChildCount();
            for (int i = 0; i < numberOfChoices; i++) {
                AnswerChoicesItem answerChoicesItem = (AnswerChoicesItem) choicesContainer.getChildAt(i);
                answerChoicesItem.setActive(false);
                answerChoicesItem.hideTextNotify();
            }
            AnswerChoicesItem answerChoicesItem =
                    (AnswerChoicesItem) choicesContainer.getChildAt(question.answer);
            answerChoicesItem.setActive(true);
            answerChoicesItem.showTextNotify(question.answer == question.correctAnswer);
            choicesContainer.invalidate();
        }

        private ArrayList<AnswerChoicesItem> makeChoices(SignQuestion question) {
            ArrayList<AnswerChoicesItem> answerChoicesItems = new ArrayList<>();
            String[] answers = question.answerArray;
            int length = answers.length;
            for (int i = 0; i < length; i++) {
                AnswerChoicesItem answerChoice = new AnswerChoicesItem(context, i);
                answerChoice.setChoice(answers[i]);
                answerChoice.changeCheckboxColor(question.correctAnswer == i);
                answerChoicesItems.add(answerChoice);
            }
            resetAllChoices(answerChoicesItems);
            if (question.answer != DrivingDataSource.ANSWER_NOT_CHOSEN) {
                answerChoicesItems.get(question.answer).setActive(true);
                answerChoicesItems.get(question.answer).
                        showTextNotify(question.correctAnswer == question.answer);
            }
            return answerChoicesItems;
        }

        private void resetAllChoices(ArrayList<AnswerChoicesItem> answerChoicesItems) {
            int size = answerChoicesItems.size();
            for (int i = 0; i < size; i++) {
                AnswerChoicesItem item = answerChoicesItems.get(i);
                item.setActive(false);
                item.hideTextNotify();
            }
        }

        private void setupAds(ViewGroup adsContainer) {
//            AdsNativeExpressHandler adsHandler = new AdsNativeExpressHandler(getActivity(), adsContainer,
//                    SecondBaseActivity.ADS_BIG_NATIVE_EXPRESS, AdsNativeExpressHandler.WIDTH_HEIGHT_RATIO_BIG);
//            adsHandler.setup();

            AdsSmallBannerHandler adsHandler = new AdsSmallBannerHandler(getActivity(), adsContainer, SecondBaseActivity.ADS_SMALL, AdSize.MEDIUM_RECTANGLE);
            adsHandler.setup();
        }
    }
}
