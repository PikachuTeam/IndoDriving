package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essential.indodriving.R;

/**
 * Created by yue on 07/07/2016.
 */
public class LearningCardSeekbar extends RelativeLayout implements View.OnTouchListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    public final static int DEFAULT_MAX_PROGRESS = 100;
    public final static int DEFAULT_PROGRESS = 0;
    private View indicator;
    private ImageView thumb;
    private ProgressBar readingProgressBar;
    private TextView textProgress;
    private Drawable progressDrawable;
    private OnProgressChangedListener onProgressChangedListener;
    private int maxProgress;
    private int progress;
    private int thumbColor;
    private int textColor;
    private boolean isFirst;

    public LearningCardSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LearningCardSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.learning_card_seekbar, this);
        isFirst = true;
        findViews();
        setListeners();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LearningCardSeekbar, 0, 0);
        thumbColor = array.getColor(R.styleable.LearningCardSeekbar_thumbColor,
                ContextCompat.getColor(context, R.color.black));
        textColor = array.getColor(R.styleable.LearningCardSeekbar_textColor,
                ContextCompat.getColor(context, R.color.black));
        maxProgress = array.getInt(R.styleable.LearningCardSeekbar_maxProgress, DEFAULT_MAX_PROGRESS);
        progress = array.getInt(R.styleable.LearningCardSeekbar_progress, DEFAULT_PROGRESS);
        progressDrawable = array.getDrawable(R.styleable.LearningCardSeekbar_progressDrawable);
        init();
        array.recycle();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        readingProgressBar.setMax(maxProgress);
    }

    public void setProgress(int progress, boolean isDisplayed) {
        this.progress = progress;
        readingProgressBar.setProgress(progress);
        textProgress.setText(isDisplayed ? "" + progress : "");
        updateIndicatorLocation();
    }

    public void setProgress(int progressToDisplay, int realProgress) {
        progress = realProgress;
        readingProgressBar.setProgress(realProgress);
        textProgress.setText("" + progressToDisplay);
        updateIndicatorLocation();
    }

    public void updateIndicatorLocation() {
        float ratio = (float) progress / maxProgress;
        indicator.setX(getResources().getDimension(R.dimen.common_size_20) +
                readingProgressBar.getWidth() * ratio - indicator.getWidth() / 2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.progress_bar_container:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float ratio = event.getX() / readingProgressBar.getWidth();
                    float tmp = ratio * (float) maxProgress;
                    progress = (int) tmp;
                    if (onProgressChangedListener != null) {
                        onProgressChangedListener.onProgressChanged(progress);
                    }
                }
                break;
            case R.id.indicator:
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float ratio = event.getRawX() / readingProgressBar.getWidth();
                    float tmp = ratio * (float) maxProgress;
                    progress = (int) tmp;
                    if (onProgressChangedListener != null && progress >= 0 && progress < maxProgress) {
                        onProgressChangedListener.onProgressChanged(progress);
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onGlobalLayout() {
        if (isFirst) {
            updateIndicatorLocation();
            isFirst = false;
        }
    }

    private void findViews() {
        thumb = (ImageView) findViewById(R.id.thumb);
        readingProgressBar = (ProgressBar) findViewById(R.id.reading_progress);
        textProgress = (TextView) findViewById(R.id.text_progress);
        indicator = findViewById(R.id.indicator);
    }

    private void init() {
        thumb.setColorFilter(thumbColor);
        textProgress.setTextColor(textColor);
        if (progressDrawable != null) readingProgressBar.setProgressDrawable(progressDrawable);
        readingProgressBar.setMax(maxProgress);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) indicator.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.common_size_20));
        readingProgressBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void setListeners() {
        findViewById(R.id.progress_bar_container).setOnTouchListener(this);
        indicator.setOnTouchListener(this);
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int progress);
    }
}
