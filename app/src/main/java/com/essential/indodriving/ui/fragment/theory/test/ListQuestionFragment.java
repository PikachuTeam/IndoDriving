package com.essential.indodriving.ui.fragment.theory.test;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.data.driving.QuestionPackage;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.activity.TheoryMainActivity;
import com.essential.indodriving.ui.base.BaseConfirmDialog;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.widget.RatingDialog;
import com.essential.indodriving.ui.widget.UpgradeToProVerDialog;
import com.essential.indodriving.util.GridItemDecoration;
import com.essential.indodriving.util.OnRecyclerViewItemClickListener;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ListQuestionFragment extends MyBaseFragment implements
        OnRecyclerViewItemClickListener, BaseConfirmDialog.OnConfirmDialogButtonClickListener {

    public final static String LIST_QUESTION_FRAGMENT_TAG = "List Question Fragment";
    private RecyclerView listQuestion;
    private int type;
    private ListQuestionAdapter adapter;
    private ArrayList<QuestionPackage> questionPackages;
    private boolean isShowedRuleAgain;
    private boolean isProVersion;
    private boolean isEnableRateToUnlock;
    private BaseConfirmDialog.OnConfirmDialogButtonClickListener mOnConfirmDialogButtonClickListener =
            new BaseConfirmDialog.OnConfirmDialogButtonClickListener() {
                @Override
                public void onConfirmDialogButtonClick(BaseConfirmDialog.ConfirmButton button, @BaseConfirmDialog.DialogTypeDef int dialogType, BaseConfirmDialog dialog) {
                    switch (button) {
                        case OK:
                            dialog.dismiss();
                            ((TheoryMainActivity) getActivity()).handlePurchasing();
                            break;
                        case CANCEL:
                            dialog.dismiss();
                            break;
                    }
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected boolean enableButtonShare() {
        return true;
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
        findViews(rootView);
        loadState();
        questionPackages = DrivingDataSource.getQuestionPackagesByType(type);
        adapter = new ListQuestionAdapter(getActivity(), questionPackages, HomeActivity.defaultFont);
        adapter.setOnRecyclerViewItemClickListener(this);
        setupRecyclerView();
        listQuestion.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshUI() {
        loadState();
        adapter.notifyDataSetChanged();
    }

    private void findViews(View rootView) {
        listQuestion = (RecyclerView) rootView.findViewById(R.id.listQuestion);
    }

    private void loadState() {
        isShowedRuleAgain = MySetting.getInstance().isRuleShowedAgain(type);
        isProVersion = MySetting.getInstance().isProVersion();
        isRated = isProVersion ? true : MySetting.getInstance().isRated();
        isEnableRateToUnlock = MySetting.getInstance().isEnableRateToUnlock();
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_A);
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
        listQuestion.addItemDecoration(new GridItemDecoration(getActivity()));
        listQuestion.setAdapter(adapter);
    }

    @Override
    public void onQuestionListItemClick(QuestionPackage questionPackage, boolean isHeader) {
        if (isHeader) {
            if (isShowedRuleAgain) {
                ShowRuleFragment fragment = new ShowRuleFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_TYPE, type);
                bundle.putBoolean(DoTestFragment.BUNDLE_IS_RANDOM, true);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
            } else {
                DoTestFragment fragment = new DoTestFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_TYPE, type);
                bundle.putBoolean(DoTestFragment.BUNDLE_IS_RANDOM, true);
                fragment.setArguments(bundle);
                replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
            }
        } else {
            if (questionPackage.index == 1) {
                if (isShowedRuleAgain) {
                    moveToShowRuleFragment(questionPackage.index);
                } else {
                    moveToDoTestFragment(questionPackage.index);
                }
            } else {
                if (!isProVersion) {
                    if (isRated || !isEnableRateToUnlock) {
                        if (questionPackage.index <= 6) {
                            if (isShowedRuleAgain) {
                                moveToShowRuleFragment(questionPackage.index);
                            } else {
                                moveToDoTestFragment(questionPackage.index);
                            }
                        } else {
                            UpgradeToProVerDialog dialog = new UpgradeToProVerDialog(getActivity());
                            dialog.setOnConfirmDialogButtonClickListener(mOnConfirmDialogButtonClickListener);
                            dialog.show();
                        }
                    } else {
                        if (questionPackage.index <= 6) {
                            RatingDialog ratingDialog = new RatingDialog(getActivity(), HomeActivity.defaultFont);
                            ratingDialog.show();
                            ratingDialog.addListener(this);
                        } else {
                            UpgradeToProVerDialog dialog = new UpgradeToProVerDialog(getActivity());
                            dialog.setOnConfirmDialogButtonClickListener(mOnConfirmDialogButtonClickListener);
                            dialog.show();
                        }
                    }
                } else {
                    if (isShowedRuleAgain) {
                        moveToShowRuleFragment(questionPackage.index);
                    } else {
                        moveToDoTestFragment(questionPackage.index);
                    }
                }
            }
        }
    }

    private void moveToShowRuleFragment(int index) {
        ShowRuleFragment fragment = new ShowRuleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, type);
        bundle.putInt(Constants.BUNDLE_EXAM_ID, index);
        fragment.setArguments(bundle);
        replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
    }

    private void moveToDoTestFragment(int index) {
        DoTestFragment fragment = new DoTestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_TYPE, type);
        bundle.putInt(Constants.BUNDLE_EXAM_ID, index);
        fragment.setArguments(bundle);
        replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
    }

    @Override
    public void onConfirmDialogButtonClick(BaseConfirmDialog.ConfirmButton button, int type, BaseConfirmDialog dialog) {
        dialog.dismiss();
        switch (button) {
            case OK:
                rateApp();
//                shareFacebook();
                break;
            case CANCEL:
                break;
        }
    }

    private class ListQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final static int TYPE_HEADER = 0, TYPE_ITEM = 1;
        private ArrayList<QuestionPackage> packages;
        private Context context;
        private OnRecyclerViewItemClickListener listener;
        private Typeface font;

        public ListQuestionAdapter(Context context, ArrayList<QuestionPackage> packages, Typeface font) {
            this.packages = packages;
            this.context = context;
            this.font = font;
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
                ((ViewHolderItem) holder).textViewPackage.setText(MessageFormat.
                        format(context.getString(R.string.title_package), "" + questionPackage.index));
                ((ViewHolderItem) holder).textViewPackage.setTypeface(font);
                if (questionPackage.lastScore == 0) {
                    ((ViewHolderItem) holder).textViewLastScore.setVisibility(View.GONE);
                } else {
                    ((ViewHolderItem) holder).textViewLastScore.setVisibility(View.VISIBLE);
                    if (questionPackage.lastScore >= 21) {
                        ((ViewHolderItem) holder).textViewLastScore.
                                setTextColor(ContextCompat.getColor(context, R.color.correct_answer_color));
                    } else {
                        ((ViewHolderItem) holder).textViewLastScore.
                                setTextColor(ContextCompat.getColor(context, R.color.wrong_answer_color));
                    }
                    ((ViewHolderItem) holder).textViewLastScore.setText(
                            MessageFormat.format(context.getString(R.string.score), "" + questionPackage.lastScore));
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
                if (!ListQuestionFragment.this.isProVersion) {
                    if (position - 1 == 0) {
                        ((ViewHolderItem) holder).lockArea.setVisibility(View.GONE);
                        ((ViewHolderItem) holder).buttonPackage.
                                setBackgroundResource(tatteam.com.app_common.R.drawable.raised_button);
                    } else {
                        if (ListQuestionFragment.this.isRated || !ListQuestionFragment.this.isEnableRateToUnlock) {
                            if (position - 1 <= 5) {
                                ((ViewHolderItem) holder).lockArea.setVisibility(View.GONE);
                                ((ViewHolderItem) holder).buttonPackage.setBackgroundResource(
                                        tatteam.com.app_common.R.drawable.raised_button);
                            } else {
                                ((ViewHolderItem) holder).lockArea.setVisibility(View.VISIBLE);
                                ((ViewHolderItem) holder).star.setVisibility(View.GONE);
                                ((ViewHolderItem) holder).buttonPackage.setBackgroundResource(
                                        R.drawable.list_question_locked_item);
                            }
                        } else {
                            if (position - 1 <= 5) {
                                ((ViewHolderItem) holder).lockArea.setVisibility(View.VISIBLE);
                                ((ViewHolderItem) holder).star.setVisibility(View.VISIBLE);
                                ((ViewHolderItem) holder).lock.setVisibility(View.VISIBLE);
                                ((ViewHolderItem) holder).star.setColorFilter(ContextCompat.
                                        getColor(context, R.color.yellow_star), PorterDuff.Mode.SRC_ATOP);
                                ((ViewHolderItem) holder).buttonPackage.setBackgroundResource(
                                        tatteam.com.app_common.R.drawable.raised_button);
                            } else {
                                ((ViewHolderItem) holder).lockArea.setVisibility(View.VISIBLE);
                                ((ViewHolderItem) holder).star.setVisibility(View.GONE);
                                ((ViewHolderItem) holder).buttonPackage.setBackgroundResource(
                                        R.drawable.list_question_locked_item);
                            }
                        }
                    }
                } else {
                    ((ViewHolderItem) holder).lockArea.setVisibility(View.GONE);
                    ((ViewHolderItem) holder).buttonPackage.setBackgroundResource(
                            tatteam.com.app_common.R.drawable.raised_button);
                }
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
            ImageView star;
            ImageView lock;
            LinearLayout lockArea;

            public ViewHolderItem(View itemView) {
                super(itemView);
                textViewPackage = (TextView) itemView.findViewById(R.id.textViewPackage);
                textViewLastScore = (TextView) itemView.findViewById(R.id.textViewLastScore);
                buttonPackage = (RelativeLayout) itemView.findViewById(R.id.buttonPackage);
                star = (ImageView) itemView.findViewById(R.id.star);
                lockArea = (LinearLayout) itemView.findViewById(R.id.lockArea);
                lock = (ImageView) itemView.findViewById(R.id.lock);
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
