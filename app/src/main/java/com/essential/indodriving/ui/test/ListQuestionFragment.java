package com.essential.indodriving.ui.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private int type;
    private ListQuestionAdapter adapter;
    private ArrayList<QuestionPackage> questionPackages;
    private boolean isShowedRuleAgain;

    public final static String LIST_QUESTION_FRAGMENT_TAG = "List Question Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_choose_package);
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_list_question;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        loadState();
        questionPackages = DataSource.getQuestionPackagesByType(type);
        adapter = new ListQuestionAdapter(getActivity(), questionPackages);
        adapter.setOnRecyclerViewItemClickListener(this);
        findViews(rootView);
        setupRecyclerView();
        listQuestion.invalidate();
    }

    private void findViews(View rootView) {
        listQuestion = (RecyclerView) rootView.findViewById(R.id.listQuestion);
    }

    private void loadState() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        switch (type) {
            case DataSource.TYPE_SIM_A:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again A", true);
                break;
            case DataSource.TYPE_SIM_A_UMUM:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again A Umum", true);
                break;
            case DataSource.TYPE_SIM_B1:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again B1", true);
                break;
            case DataSource.TYPE_SIM_B1_UMUM:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again B1 Umum", true);
                break;
            case DataSource.TYPE_SIM_B2:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again B2", true);
                break;
            case DataSource.TYPE_SIM_B2_UMUM:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again B2 Umum", true);
                break;
            case DataSource.TYPE_SIM_C:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again C", true);
                break;
            case DataSource.TYPE_SIM_D:
                isShowedRuleAgain = sharedPreferences.getBoolean("Show Rule Again D", true);
                break;
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("Type", DataSource.TYPE_SIM_A);
    }

    private void setupRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        listQuestion.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isPositionHeader(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        listQuestion.setAdapter(adapter);
    }

    @Override
    public void onQuestionListItemClick(QuestionPackage questionPackage, boolean isHeader) {
        if (!isHeader) {
            if (isShowedRuleAgain) {
                ShowRuleFragment fragment = new ShowRuleFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                bundle.putInt("Exam Id", questionPackage.index);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
            } else {
                DoTestFragment fragment = new DoTestFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                bundle.putInt("Exam Id", questionPackage.index);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
            }
        } else {
            if (isShowedRuleAgain) {
                ShowRuleFragment fragment = new ShowRuleFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                bundle.putBoolean("Random", true);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
            } else {
                DoTestFragment fragment = new DoTestFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                bundle.putBoolean("Random", true);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
            }
        }
    }

    private class ListQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<QuestionPackage> packages;
        private Context context;
        private OnRecyclerViewItemClickListener listener;
        private final static int TYPE_HEADER = 0, TYPE_ITEM = 1;
        private Typeface font;

        public ListQuestionAdapter(Context context, ArrayList<QuestionPackage> packages) {
            this.packages = packages;
            this.context = context;
            font = Typeface.createFromAsset(context.getAssets(), "fonts/Menu Sim.ttf");
        }

        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View view = View.inflate(context, R.layout.header_list_question, null);
                return new ViewHolderHeader(view);
            } else {
                View view = View.inflate(context, R.layout.item_list_question, null);
                return new ViewHolderItem(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolderItem) {
                final QuestionPackage questionPackage = packages.get(position - 1);
                ((ViewHolderItem) holder).textViewPackage.setText(MessageFormat.format(context.getString(R.string.topic), "" + questionPackage.index));
                ((ViewHolderItem) holder).textViewPackage.setTypeface(font);
                if (questionPackage.lastScore == 0) {
                    ((ViewHolderItem) holder).textViewLastScore.setVisibility(View.GONE);
                } else {
                    ((ViewHolderItem) holder).textViewLastScore.setVisibility(View.VISIBLE);
                    if (questionPackage.lastScore >= 21) {
                        ((ViewHolderItem) holder).textViewLastScore.setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
                    } else {
                        ((ViewHolderItem) holder).textViewLastScore.setTextColor(ContextCompat.getColor(context, R.color.wrong_answer_color));
                    }
                    ((ViewHolderItem) holder).textViewLastScore.setText(MessageFormat.format(context.getString(R.string.score), "" + questionPackage.lastScore));
                    ((ViewHolderItem) holder).textViewLastScore.setTypeface(font);
                }
                ((ViewHolderItem) holder).buttonPackage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onQuestionListItemClick(questionPackage, false);
                        }
                    }
                });
            } else if (holder instanceof ViewHolderHeader) {
                ((ViewHolderHeader) holder).buttonRandomQuestion.setTypeface(font);
                ((ViewHolderHeader) holder).buttonRandomQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onQuestionListItemClick(questionPackages.get(position), true);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return packages.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        class ViewHolderItem extends RecyclerView.ViewHolder {

            TextView textViewPackage;
            TextView textViewLastScore;
            RelativeLayout buttonPackage;

            public ViewHolderItem(View itemView) {
                super(itemView);
                textViewPackage = (TextView) itemView.findViewById(R.id.textViewPackage);
                textViewLastScore = (TextView) itemView.findViewById(R.id.textViewLastScore);
                buttonPackage = (RelativeLayout) itemView.findViewById(R.id.buttonPackage);
            }
        }

        class ViewHolderHeader extends RecyclerView.ViewHolder {

            TextView buttonRandomQuestion;

            public ViewHolderHeader(View itemView) {
                super(itemView);
                buttonRandomQuestion = (TextView) itemView.findViewById(R.id.buttonRandomQuestion);
            }
        }
    }
}
