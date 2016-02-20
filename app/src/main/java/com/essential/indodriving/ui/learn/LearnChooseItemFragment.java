package com.essential.indodriving.ui.learn;

import android.os.Bundle;
import android.view.View;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class LearnChooseItemFragment extends MyBaseFragment {

    public final static String LEARN_CHOOSE_ITEM_FRAGMENT = "Learn Choose Item Fragment";

    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_learn_choose_item);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_choose_item;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        rootView.findViewById(R.id.buttonLearnByCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearnByCardFragment learnByCardFragment = new LearnByCardFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                learnByCardFragment.setArguments(bundle);
                replaceFragment(learnByCardFragment, LEARN_CHOOSE_ITEM_FRAGMENT);
            }
        });
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", 1);
    }
}
