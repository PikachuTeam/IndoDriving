package com.essential.indodriving.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.ui.learn.LearnChooseItemFragment;
import com.essential.indodriving.ui.test.ListQuestionFragment;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ChooseItemFragment extends MyBaseFragment {

    private int type;

    public final static String CHOOSE_ITEM_FRAGMENT_TAG = "Choose Item Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_choose_item;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        rootView.findViewById(R.id.buttonLearn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                LearnChooseItemFragment fragment = new LearnChooseItemFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, CHOOSE_ITEM_FRAGMENT_TAG);
            }
        });

        rootView.findViewById(R.id.buttonTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                ListQuestionFragment fragment = new ListQuestionFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, CHOOSE_ITEM_FRAGMENT_TAG);
            }
        });
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
    }
}
