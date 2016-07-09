package com.essential.indodriving.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.essential.indodriving.R;

/**
 * Created by yue on 09/07/2016.
 */
public class SearchView extends RelativeLayout implements TextWatcher, View.OnClickListener {

    private EditText textSearch;
    private ImageView buttonCancel;
    private OnSearchViewInteractListener listener;

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.search_view, this);
        setBackgroundResource(R.drawable.rounded_edit_text);
        findViews();
        textSearch.addTextChangedListener(this);
        buttonCancel.setOnClickListener(this);
    }

    public void setOnSearchViewInteractListener(OnSearchViewInteractListener listener) {
        this.listener = listener;
    }

    private void findViews() {
        textSearch = (EditText) findViewById(R.id.text_search);
        buttonCancel = (ImageView) findViewById(R.id.button_cancel);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0) {
            buttonCancel.setVisibility(VISIBLE);
            textSearch.setTypeface(null, Typeface.NORMAL);
        }
        if (listener != null) {
            listener.onSearch(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {
        textSearch.setText("");
        textSearch.setTypeface(null, Typeface.ITALIC);
        buttonCancel.setVisibility(GONE);
        if (listener != null) {
            listener.onCancel();
        }
    }

    public interface OnSearchViewInteractListener {
        void onSearch(CharSequence s);

        void onCancel();
    }
}
