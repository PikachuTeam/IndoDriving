package com.essential.indodriving.ui.fragment.theory.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.Question;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.widget.ShowResultDialog;
import com.essential.indodriving.util.LinearItemDecoration;

import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/29/2016.
 */
public class DetailResultFragment extends MyBaseFragment {

    private TextView textType;
    private TextView textReport;
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
                textType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.correct_answer_color));
                textType.setText(getString(R.string.title_correct_answer));
                if (questions.size() == 0) {
                    textReport.setVisibility(View.VISIBLE);
                    textReport.setText(getString(R.string.no_correct_answer));
                } else {
                    textReport.setVisibility(View.GONE);
                }
                break;
            case 1:
                textType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.wrong_answer_color));
                textType.setText(getString(R.string.title_wrong_answer));
                if (questions.size() == 0) {
                    textReport.setVisibility(View.VISIBLE);
                    textReport.setText(getString(R.string.no_wrong_answer));
                } else {
                    textReport.setVisibility(View.GONE);
                }
                break;
            case 2:
                textType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.not_answered_color));
                textType.setText(getString(R.string.title_not_answered));
                if (questions.size() == 0) {
                    textReport.setVisibility(View.VISIBLE);
                    textReport.setText(getString(R.string.answer_all));
                } else {
                    textReport.setVisibility(View.GONE);
                }
                break;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listDetailResult.setLayoutManager(layoutManager);
        listDetailResult.addItemDecoration(new LinearItemDecoration(getActivity()));
        listDetailResult.setAdapter(adapter);
        getMyBaseActivity().showBigAdsIfNeeded();
    }

    private void findViews(View rootView) {
        textType = (TextView) rootView.findViewById(R.id.text_type);
        listDetailResult = (RecyclerView) rootView.findViewById(R.id.listDetailResult);
        textReport = (TextView) rootView.findViewById(R.id.text_report);
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
                    inflate(R.layout.item_detail, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Question question = questions.get(position);
            if (question.imageData != null) {
                holder.questionImage.setVisibility(View.VISIBLE);
                Glide.with(DetailResultFragment.this).load(question.imageData).dontAnimate().dontTransform().into(holder.questionImage);
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

            public FrameLayout buttonDetailResult;
            public ImageView questionImage;
            public TextView textViewQuestion;

            public ViewHolder(View itemView) {
                super(itemView);
                buttonDetailResult = (FrameLayout) itemView.findViewById(R.id.button_detail);
                questionImage = (ImageView) itemView.findViewById(R.id.image_sign);
                textViewQuestion = (TextView) itemView.findViewById(R.id.text_detail);
            }
        }
    }
}
