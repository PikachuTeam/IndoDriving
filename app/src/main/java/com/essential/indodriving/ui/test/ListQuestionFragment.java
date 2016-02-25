package com.essential.indodriving.ui.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essential.indodriving.R;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.QuestionPackage;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ListQuestionFragment extends MyBaseFragment implements OnRecyclerViewItemClickListener {

    private RecyclerView listQuestion;
    private RelativeLayout buttonRandomQuestion;

    private int type;
    private ListQuestionAdapter adapter;
    private ArrayList<QuestionPackage> questionPackages;

    public final static String LIST_QUESTION_FRAGMENT_TAG = "List Question Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        adapter = new ListQuestionAdapter(getActivity(), questionPackages);
        adapter.setOnRecyclerViewItemClickListener(this);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_test);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_list_question;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        setupRecyclerView();
    }

    private void findViews(View rootView) {
        listQuestion = (RecyclerView) rootView.findViewById(R.id.listQuestion);
        buttonRandomQuestion = (RelativeLayout) rootView.findViewById(R.id.buttonRandomQuestion);

        buttonRandomQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRuleFragment fragment = new ShowRuleFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                bundle.putBoolean("Random", true);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
            }
        });
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
        questionPackages = DataSource.getQuestionPackagesByType(type);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listQuestion.setLayoutManager(layoutManager);
        listQuestion.setAdapter(adapter);
    }

    @Override
    public void onQuestionListItemClick(QuestionPackage questionPackage) {
        ShowRuleFragment fragment = new ShowRuleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Type", type);
        bundle.putString("Exam Id", questionPackage.index);
        fragment.setArguments(bundle);
        replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
    }

    private static class ListQuestionAdapter extends RecyclerView.Adapter<ListQuestionAdapter.ViewHolder> {

        private ArrayList<QuestionPackage> packages;
        private Context context;
        private OnRecyclerViewItemClickListener listener;

        public ListQuestionAdapter(Context context, ArrayList<QuestionPackage> packages) {
            this.packages = packages;
            this.context = context;
        }

        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_question, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final QuestionPackage questionPackage = packages.get(position);
            if (questionPackage.lastScore != 0) {
                holder.scoreArea.setVisibility(View.VISIBLE);
                holder.textViewScore.setText(MessageFormat.format(context.getString(R.string.score), "" + questionPackage.lastScore));
            } else {
                holder.scoreArea.setVisibility(View.GONE);
            }
            holder.textViewQuestionPackage.setText(MessageFormat.format(context.getString(R.string.topic), questionPackage.index));
            holder.testListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onQuestionListItemClick(questionPackage);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return packages.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textViewQuestionPackage;
            RelativeLayout testListItem;
            LinearLayout scoreArea;
            TextView textViewScore;

            public ViewHolder(View itemView) {
                super(itemView);
                textViewQuestionPackage = (TextView) itemView.findViewById(R.id.textViewQuestionPackage);
                testListItem = (RelativeLayout) itemView.findViewById(R.id.testListItem);
                scoreArea = (LinearLayout) itemView.findViewById(R.id.scoreArea);
                textViewScore = (TextView) itemView.findViewById(R.id.textViewScore);
            }
        }
    }
}
