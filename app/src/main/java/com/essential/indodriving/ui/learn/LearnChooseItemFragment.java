package com.essential.indodriving.ui.learn;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;

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
                LearnAllFragment fragment = new LearnAllFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LEARN_CHOOSE_ITEM_FRAGMENT);
            }
        });

        rootView.findViewById(R.id.buttonTutorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialFragment tutorialFragment = new TutorialFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                tutorialFragment.setArguments(bundle);
                replaceFragment(tutorialFragment, LEARN_CHOOSE_ITEM_FRAGMENT);
            }
        });
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
    }
}
