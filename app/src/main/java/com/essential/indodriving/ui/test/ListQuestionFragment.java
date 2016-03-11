package com.essential.indodriving.ui.test;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.essential.indodriving.BuildConfig;
import com.essential.indodriving.R;
import com.essential.indodriving.base.BaseConfirmDialog;
import com.essential.indodriving.base.MyBaseFragment;
import com.essential.indodriving.data.DataSource;
import com.essential.indodriving.data.QuestionPackage;
import com.essential.indodriving.ui.HomeActivity;
import com.essential.indodriving.ui.widget.RatingDialog;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by dongc_000 on 2/24/2016.
 */
public class ListQuestionFragment extends MyBaseFragment implements OnRecyclerViewItemClickListener, BaseConfirmDialog.OnConfirmDialogButtonClickListener {

    private RecyclerView listQuestion;

    private int type;
    private ListQuestionAdapter adapter;
    private ArrayList<QuestionPackage> questionPackages;
    private SpaceItemDecoration spaceItemDecoration;
    private Typeface font;
    private boolean isShowedRuleAgain;
    private boolean isRated;
    private boolean isProVersion;

    public final static String LIST_QUESTION_FRAGMENT_TAG = "List Question Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        spaceItemDecoration = new SpaceItemDecoration(getResources().getInteger(R.integer.grid_layout_item_space));
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Menu Sim.ttf");
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
    }

    @Override
    public void onResume() {
        super.onResume();
        loadState();
        questionPackages = DataSource.getQuestionPackagesByType(type);
        adapter = new ListQuestionAdapter(getActivity(), questionPackages, isRated);
        adapter.setOnRecyclerViewItemClickListener(this);
        setupRecyclerView();
        listQuestion.invalidate();
    }

    private void findViews(View rootView) {
        listQuestion = (RecyclerView) rootView.findViewById(R.id.listQuestion);
    }

    private void loadState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
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
        isRated = sharedPreferences.getBoolean(HomeActivity.PRE_IS_RATE_APP, false);
        isProVersion = sharedPreferences.getBoolean(HomeActivity.PRE_IS_PRO_VERSION, BuildConfig.IS_PRO_VERSION);
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
        listQuestion.addItemDecoration(spaceItemDecoration);
        listQuestion.setAdapter(adapter);
    }

    @Override
    public void onQuestionListItemClick(QuestionPackage questionPackage, boolean isHeader) {
        if (isHeader) {
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
        } else {
            if (questionPackage.index == 1) {
                if (isShowedRuleAgain) {
                    moveToShowRuleFragment(questionPackage.index);
                } else {
                    moveToDoTestFragment(questionPackage.index);
                }
            } else {
                if (!isProVersion) {
                    if (isRated) {
                        if (questionPackage.index < 7) {
                            if (isShowedRuleAgain) {
                                moveToShowRuleFragment(questionPackage.index);
                            } else {
                                moveToDoTestFragment(questionPackage.index);
                            }
                        } else {
                            Snackbar.make(getMyBaseActivity().getMainCoordinatorLayout(), getString(R.string.will_be_updated), Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        RatingDialog ratingDialog = new RatingDialog(getActivity(), font);
                        ratingDialog.show();
                        ratingDialog.addListener(this);
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
        bundle.putInt("Type", type);
        bundle.putInt("Exam Id", index);
        fragment.setArguments(bundle);
        replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
    }

    private void moveToDoTestFragment(int index) {
        DoTestFragment fragment = new DoTestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Type", type);
        bundle.putInt("Exam Id", index);
        fragment.setArguments(bundle);
        replaceFragment(fragment, LIST_QUESTION_FRAGMENT_TAG);
    }

    @Override
    public void onConfirmDialogButtonClick(BaseConfirmDialog.ConfirmButton button, BaseConfirmDialog.Type type, BaseConfirmDialog dialog) {
        dialog.dismiss();
        switch (button) {
            case OK:
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(HomeActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(HomeActivity.PRE_IS_RATE_APP, true);
                editor.commit();
                break;
            case CANCEL:
                // nothing to do here :v
                break;
        }
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space * 2;
                outRect.left = space * 2;
                outRect.right = space * 2;
            } else {
                outRect.top = 0;
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.left = space;
                    outRect.right = space * 2;
                } else {
                    outRect.left = space * 2;
                    outRect.right = space;
                }
            }
            outRect.bottom = space * 2;
        }
    }

    private class ListQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<QuestionPackage> packages;
        private Context context;
        private OnRecyclerViewItemClickListener listener;
        private final static int TYPE_HEADER = 0, TYPE_ITEM = 1;
        private Typeface font;
        private boolean isRated;

        public ListQuestionAdapter(Context context, ArrayList<QuestionPackage> packages, boolean isRated) {
            this.packages = packages;
            this.context = context;
            font = Typeface.createFromAsset(context.getAssets(), "fonts/Menu Sim.ttf");
            this.isRated = isRated;
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
                ((ViewHolderItem) holder).textViewPackage.setText(MessageFormat.format(context.getString(R.string.title_package), "" + questionPackage.index));
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

                if (!ListQuestionFragment.this.isProVersion) {
                    if (position - 1 == 0) {
                        ((ViewHolderItem) holder).lockArea.setVisibility(View.GONE);
                    } else {
                        if (isRated) {
                            ((ViewHolderItem) holder).lockArea.setVisibility(View.GONE);
                        } else {
                            ((ViewHolderItem) holder).lockArea.setVisibility(View.VISIBLE);
                            ((ViewHolderItem) holder).star.setColorFilter(ContextCompat.getColor(context, R.color.yellow_star), PorterDuff.Mode.SRC_ATOP);
                        }
                    }
                } else {
                    ((ViewHolderItem) holder).lockArea.setVisibility(View.GONE);
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
            LinearLayout lockArea;

            public ViewHolderItem(View itemView) {
                super(itemView);
                textViewPackage = (TextView) itemView.findViewById(R.id.textViewPackage);
                textViewLastScore = (TextView) itemView.findViewById(R.id.textViewLastScore);
                buttonPackage = (RelativeLayout) itemView.findViewById(R.id.buttonPackage);
                star = (ImageView) itemView.findViewById(R.id.star);
                lockArea = (LinearLayout) itemView.findViewById(R.id.lockArea);
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
