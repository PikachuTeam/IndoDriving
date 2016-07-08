package com.essential.indodriving.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.essential.indodriving.R;

/**
 * Created by yue on 14/05/2016.
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public LinearItemDecoration(Context context) {
        mSpace = context.getResources().getDimensionPixelSize(R.dimen.common_size_5);
    }

    public LinearItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position == 0) {
            outRect.top = mSpace;
        } else {
            outRect.top = 0;
        }
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
    }
}
