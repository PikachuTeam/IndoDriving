package com.essential.indodriving.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.essential.indodriving.R;

/**
 * Created by yue on 14/05/2016.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridItemDecoration(Context context) {
        mSpace = context.getResources().getDimensionPixelSize(R.dimen.common_size_5);
    }

    public GridItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position == 0) {
            outRect.top = mSpace;
            outRect.left = mSpace;
            outRect.right = mSpace;
        } else {
            outRect.top = 0;
            if (position % 2 == 0) {
                outRect.left = mSpace / 2;
                outRect.right = mSpace;
            } else {
                outRect.left = mSpace;
                outRect.right = mSpace / 2;
            }
        }
        outRect.bottom = mSpace;
    }
}
