package com.essential.indodriving.util;

import com.essential.indodriving.data.QuestionPackage;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public interface OnRecyclerViewItemClickListener {
    void onQuestionListItemClick(QuestionPackage questionPackage, boolean isHeader);
}
