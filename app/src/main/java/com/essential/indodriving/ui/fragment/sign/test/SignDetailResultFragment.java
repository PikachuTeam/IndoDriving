package com.essential.indodriving.ui.fragment.sign.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.SignQuestion;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.widget.SignShowResultDialog;
import com.essential.indodriving.util.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yue on 09/07/2016.
 */
public class SignDetailResultFragment extends MyBaseFragment {

    private TextView textType;
    private TextView textReport;
    private RecyclerView detailResultRecyclerView;
    private List<SignQuestion> questions;
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
                    detailResultRecyclerView.setVisibility(View.GONE);
                    textReport.setText(getString(R.string.no_correct_answer));
                } else {
                    textReport.setVisibility(View.GONE);
                    detailResultRecyclerView.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                textType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.wrong_answer_color));
                textType.setText(getString(R.string.title_wrong_answer));
                if (questions.size() == 0) {
                    textReport.setVisibility(View.VISIBLE);
                    textReport.setText(getString(R.string.no_wrong_answer));
                    detailResultRecyclerView.setVisibility(View.GONE);
                } else {
                    textReport.setVisibility(View.GONE);
                    detailResultRecyclerView.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                textType.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.not_answered_color));
                textType.setText(getString(R.string.title_not_answered));
                if (questions.size() == 0) {
                    textReport.setVisibility(View.VISIBLE);
                    textReport.setText(getString(R.string.answer_all));
                    detailResultRecyclerView.setVisibility(View.GONE);
                } else {
                    textReport.setVisibility(View.GONE);
                    detailResultRecyclerView.setVisibility(View.VISIBLE);
                }
                break;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        detailResultRecyclerView.setLayoutManager(layoutManager);
        detailResultRecyclerView.addItemDecoration(new LinearItemDecoration(getActivity()));
        detailResultRecyclerView.setAdapter(adapter);
        getMyBaseActivity().showBigAdsIfNeeded();
    }

    private void findViews(View rootView) {
        textType = (TextView) rootView.findViewById(R.id.text_type);
        detailResultRecyclerView = (RecyclerView) rootView.findViewById(R.id.listDetailResult);
        textReport = (TextView) rootView.findViewById(R.id.text_report);
        textReport.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
    }

    private void getData() {
        if (containHolder(Constants.KEY_HOLDER_QUESTIONS)) {
            questions = (List<SignQuestion>) getHolder(Constants.KEY_HOLDER_QUESTIONS);
        } else {
            questions = new ArrayList<>();
        }
        type = getArguments().getInt(Constants.BUNDLE_TYPE, 0);
    }

    private class DetailResultAdapter extends RecyclerView.Adapter<DetailResultAdapter.ViewHolder> {

        private List<SignQuestion> questions;
        private Context context;

        public DetailResultAdapter(Context context, List<SignQuestion> questions) {
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
            final SignQuestion question = questions.get(position);
            Glide.with(context).load(question.image).dontAnimate().dontTransform().into(holder.imageSign);
            holder.textDefinition.setText(question.answerArray[question.correctAnswer]);
            holder.buttonDetailResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignShowResultDialog dialog = new SignShowResultDialog(context, question);
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public View buttonDetailResult;
            public ImageView imageSign;
            public TextView textDefinition;

            public ViewHolder(View itemView) {
                super(itemView);
                buttonDetailResult = itemView.findViewById(R.id.button_detail);
                imageSign = (ImageView) itemView.findViewById(R.id.image_sign);
                textDefinition = (TextView) itemView.findViewById(R.id.text_detail);
            }
        }
    }
}
