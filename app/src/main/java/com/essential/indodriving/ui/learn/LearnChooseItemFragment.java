package com.essential.indodriving.ui.learn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;

/**
 * Created by dongc_000 on 2/17/2016.
 */
public class LearnChooseItemFragment extends MyBaseFragment {

    private CardView buttonTutorialContainer;

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
        buttonTutorialContainer = (CardView) rootView.findViewById(R.id.buttonTutorialContainer);

        switch (type) {
            case LearnChooseSimFragment.TYPE_SIM_A:
            case LearnChooseSimFragment.TYPE_SIM_C:
                buttonTutorialContainer.setVisibility(View.VISIBLE);
                break;
            default:
                buttonTutorialContainer.setVisibility(View.GONE);
                break;
        }

        rootView.findViewById(R.id.buttonLearnByCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearnByCardFragment1 learnByCardFragment = new LearnByCardFragment1();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                learnByCardFragment.setArguments(bundle);
                replaceFragment(learnByCardFragment, LEARN_CHOOSE_ITEM_FRAGMENT);
            }
        });

        rootView.findViewById(R.id.buttonTutorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case LearnChooseSimFragment.TYPE_SIM_A:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=Ujian+Praktek+SIM+A")));
                        break;
                    case LearnChooseSimFragment.TYPE_SIM_C:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=Ujian+Praktek+SIM+C")));
                        break;
                }
            }
        });
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", 1);
    }
}
