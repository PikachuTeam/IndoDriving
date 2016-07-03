package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.data.driving.Question;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;

import java.util.ArrayList;

/**
 * Created by yue on 26/06/2016.
 */
public class ModifyAnswerDialog extends BaseConfirmDialog implements View.OnClickListener,
        View.OnTouchListener, AnswerChoicesItem.OnChooseAnswerListener {

    private final int TYPE_TEXT = 1, TYPE_RADIO = 2;
    private LinearLayout mAnswerGroup;
    private ImageView mImageQuestion;
    private TextView mTextQuestion;
    private ImageView mButtonZoomIn;
    private RelativeLayout mImageArea;
    private Question mQuestion;
    private ArrayList<AnswerChoicesItem> items = new ArrayList<>();
    private int mCurrentAnswer;
    private boolean mIsNotShowedAgain;
    private OnAnswerModifiedListener mOnAnswerModifiedListener;

    public ModifyAnswerDialog(Context context, Question question) {
        super(context);
        mQuestion = question;
        mCurrentAnswer = mQuestion.fixedAnswer != -1 ?
                mQuestion.fixedAnswer : mQuestion.correctAnswer;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mIsNotShowedAgain = sharedPreferences.getBoolean(Constants.PREF_NOT_SHOW_THANKS_AGAIN, false);
    }

    public void setOnAnswerModifiedListener(OnAnswerModifiedListener onAnswerModifiedListener) {
        mOnAnswerModifiedListener = onAnswerModifiedListener;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_modify_answer;
    }

    @Override
    protected void onCreateContentView() {
        setCanceledOnTouchOutside(false);
        findViews();
        if (mQuestion.imageData != null) {
            mImageArea.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mQuestion.imageData).dontAnimate().
                    dontTransform().dontAnimate().dontTransform().into(mImageQuestion);
            mButtonZoomIn.setColorFilter(ContextCompat.getColor(getContext()
                    , R.color.learn_all_button_zoom_in_normal_color), PorterDuff.Mode.SRC_ATOP);
        } else {
            mImageArea.setVisibility(View.GONE);
        }
        makeAnswerGroup(
                mQuestion.fixedAnswer != -1 ? mQuestion.fixedAnswer : mQuestion.correctAnswer);
        mTextQuestion.setText(mQuestion.question);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_image:
                ZoomInImageDialog dialog = new ZoomInImageDialog(getContext(), mQuestion.imageData);
                dialog.show();
                break;
            case R.id.button_ok:
                if (mQuestion.fixedAnswer != -1 && mCurrentAnswer != mQuestion.fixedAnswer) {
                    saveAnswer();
                } else if (mQuestion.fixedAnswer == -1 && mCurrentAnswer != mQuestion.correctAnswer) {
                    saveAnswer();
                }
                dismiss();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mButtonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
            mButtonZoomIn.setColorFilter(ContextCompat.getColor(getContext()
                    , R.color.learn_all_button_zoom_in_highlight_color), PorterDuff.Mode.SRC_ATOP);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mButtonZoomIn.setImageResource(R.drawable.ic_zoom_in_normal);
            mButtonZoomIn.setColorFilter(ContextCompat.getColor(getContext()
                    , R.color.learn_all_button_zoom_in_normal_color), PorterDuff.Mode.SRC_ATOP);
            ZoomInImageDialog dialog = new ZoomInImageDialog(getContext(), mQuestion.imageData);
            dialog.show();
        }
        return false;
    }

    @Override
    public void onChooseAnswer(AnswerChoicesItem item) {
        int size = items.size();
        for (int i = 0; i < size; i++) {
            items.get(i).setActive(false);
        }
        item.setActive(true);
        mCurrentAnswer = item.getIndex();
    }

    private void findViews() {
        mAnswerGroup = (LinearLayout) findViewById(R.id.answer_group);
        mImageArea = (RelativeLayout) findViewById(R.id.image_area);
        mImageQuestion = (ImageView) findViewById(R.id.question_image);
        mTextQuestion = (TextView) findViewById(R.id.text_question);
        mButtonZoomIn = (ImageView) findViewById(R.id.button_zoom_in);
        mButtonZoomIn.setOnTouchListener(this);
        mImageQuestion.setOnClickListener(this);
        findViewById(R.id.button_ok).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);
    }

    private void makeAnswerGroup(int correctAnswer) {
        if (correctAnswer == DrivingDataSource.ANSWER_A) {
            makeChoice(mQuestion.answer1, DrivingDataSource.ANSWER_A, TYPE_TEXT);
            makeChoice(mQuestion.answer2, DrivingDataSource.ANSWER_B, TYPE_RADIO);
            makeChoice(mQuestion.answer3, DrivingDataSource.ANSWER_C, TYPE_RADIO);
            if (!TextUtils.isEmpty(mQuestion.answer4)) {
                makeChoice(mQuestion.answer4, DrivingDataSource.ANSWER_D, TYPE_RADIO);
            }
        } else if (correctAnswer == DrivingDataSource.ANSWER_B) {
            makeChoice(mQuestion.answer1, DrivingDataSource.ANSWER_A, TYPE_RADIO);
            makeChoice(mQuestion.answer2, DrivingDataSource.ANSWER_B, TYPE_TEXT);
            makeChoice(mQuestion.answer3, DrivingDataSource.ANSWER_C, TYPE_RADIO);
            if (!TextUtils.isEmpty(mQuestion.answer4)) {
                makeChoice(mQuestion.answer4, DrivingDataSource.ANSWER_D, TYPE_RADIO);
            }
        } else if (correctAnswer == DrivingDataSource.ANSWER_C) {
            makeChoice(mQuestion.answer1, DrivingDataSource.ANSWER_A, TYPE_RADIO);
            makeChoice(mQuestion.answer2, DrivingDataSource.ANSWER_B, TYPE_RADIO);
            makeChoice(mQuestion.answer3, DrivingDataSource.ANSWER_C, TYPE_TEXT);
            if (!TextUtils.isEmpty(mQuestion.answer4)) {
                makeChoice(mQuestion.answer4, DrivingDataSource.ANSWER_D, TYPE_RADIO);
            }
        } else {
            makeChoice(mQuestion.answer1, DrivingDataSource.ANSWER_A, TYPE_RADIO);
            makeChoice(mQuestion.answer2, DrivingDataSource.ANSWER_B, TYPE_RADIO);
            makeChoice(mQuestion.answer3, DrivingDataSource.ANSWER_C, TYPE_RADIO);
            makeChoice(mQuestion.answer4, DrivingDataSource.ANSWER_D, TYPE_TEXT);
        }
    }

    private void makeChoice(String answer, int index, int type) {
        switch (type) {
            case TYPE_TEXT:
                AnswerChoicesItem item = new AnswerChoicesItem(getContext(), index, false);
                item.changeTextColor();
                item.setChoice(answer);
                mAnswerGroup.addView(item);
                LinearLayout.MarginLayoutParams marginParams =
                        (LinearLayout.MarginLayoutParams) item.getLayoutParams();
                marginParams.setMargins(0, 0, 0,
                        getContext().getResources().getDimensionPixelSize(R.dimen.common_size_5));
                items.add(item);
                break;
            case TYPE_RADIO:
                item = new AnswerChoicesItem(getContext(), index);
                item.setOnChooseAnswerListener(this);
                item.setChoice(answer);
                mAnswerGroup.addView(item);
                marginParams =
                        (LinearLayout.MarginLayoutParams) item.getLayoutParams();
                marginParams.setMargins(0, 0, 0,
                        getContext().getResources().getDimensionPixelSize(R.dimen.common_size_5));
                items.add(item);
                break;
        }
    }

    private void saveAnswer() {
        DrivingDataSource.modifyAnswer(mQuestion.id, mCurrentAnswer);
        if (mOnAnswerModifiedListener != null) {
            mOnAnswerModifiedListener.onAnswerModified(mCurrentAnswer);
        }
        if (!mIsNotShowedAgain) {
            ThanksDialog thanksDialog = new ThanksDialog(getContext());
            thanksDialog.show();
        }
    }

    public interface OnAnswerModifiedListener {
        void onAnswerModified(int correctAnswer);
    }
}

