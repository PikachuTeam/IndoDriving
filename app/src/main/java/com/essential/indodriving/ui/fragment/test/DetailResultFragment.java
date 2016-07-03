package com.essential.indodriving.ui.fragment.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.Question;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.widget.ShowResultDialog;
import com.essential.indodriving.util.ListItemDecoration;

import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/29/2016.
 */
public class DetailResultFragment extends MyBaseFragment {

    private TextView mTextType;
    private TextView mTextReport;
    private RecyclerView listDetailResult;
    private ArrayList<Question> questions;
    private DetailResultAdapter adapter;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        adapter = new DetailResultAdapter(getActivity(), questions);
    }

    @Override
    protected boolean enableButtonShare() {
        return true;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_detail_result);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_show_detail_result;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        switch (type) {
            case 0:
                mTextType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                mTextType.setText(getString(R.string.title_correct_answer));
                if (questions.size() == 0) {
                    mTextReport.setVisibility(View.VISIBLE);
                    mTextReport.setText(getString(R.string.no_correct_answer));
                } else {
                    mTextReport.setVisibility(View.GONE);
                }
                break;
            case 1:
                mTextType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.wrong_answer_color));
                mTextType.setText(getString(R.string.title_wrong_answer));
                if (questions.size() == 0) {
                    mTextReport.setVisibility(View.VISIBLE);
                    mTextReport.setText(getString(R.string.no_wrong_answer));
                } else {
                    mTextReport.setVisibility(View.GONE);
                }
                break;
            case 2:
                mTextType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.not_answered_color));
                mTextType.setText(getString(R.string.title_not_answered));
                if (questions.size() == 0) {
                    mTextReport.setVisibility(View.VISIBLE);
                    mTextReport.setText(getString(R.string.answer_all));
                } else {
                    mTextReport.setVisibility(View.GONE);
                }
                break;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listDetailResult.setLayoutManager(layoutManager);
        listDetailResult.addItemDecoration(new ListItemDecoration(getActivity()));
        listDetailResult.setAdapter(adapter);
        getMyBaseActivity().showBigAdsIfNeeded();
    }

    private void findViews(View rootView) {
        mTextType = (TextView) rootView.findViewById(R.id.text_type);
        listDetailResult = (RecyclerView) rootView.findViewById(R.id.listDetailResult);
        mTextReport = (TextView) rootView.findViewById(R.id.text_report);
    }

    private void getData() {
        if (containHolder(Constants.KEY_HOLDER_QUESTIONS)) {
            questions = (ArrayList<Question>) getHolder(Constants.KEY_HOLDER_QUESTIONS);
        } else {
            questions = new ArrayList<>();
        }
        type = getArguments().getInt(Constants.BUNDLE_TYPE, 0);
    }

    private class DetailResultAdapter extends RecyclerView.Adapter<DetailResultAdapter.ViewHolder> {

        private ArrayList<Question> questions;
        private Context context;

        public DetailResultAdapter(Context context, ArrayList<Question> questions) {
            this.context = context;
            this.questions = questions;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_list_detail_result, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Question question = questions.get(position);
            if (question.imageData != null) {
                holder.questionImage.setVisibility(View.VISIBLE);
                Glide.with(DetailResultFragment.this).load(question.imageData).
                        dontAnimate().dontTransform().into(holder.questionImage);
            } else {
                holder.questionImage.setVisibility(View.GONE);
            }
            holder.textViewQuestion.setText(question.question);
            holder.buttonDetailResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowResultDialog dialog = new ShowResultDialog(context, question);
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout buttonDetailResult;
            public ImageView questionImage;
            public TextView textViewQuestion;

            public ViewHolder(View itemView) {
                super(itemView);
                buttonDetailResult = (LinearLayout) itemView.findViewById(R.id.buttonDetailResult);
                questionImage = (ImageView) itemView.findViewById(R.id.questionImage);
                textViewQuestion = (TextView) itemView.findViewById(R.id.textViewQuestion);
            }
        }
    }
}
