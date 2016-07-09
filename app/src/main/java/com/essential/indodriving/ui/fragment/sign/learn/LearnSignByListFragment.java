package com.essential.indodriving.ui.fragment.sign.learn;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essential.indodriving.MySetting;
import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.Sign;
import com.essential.indodriving.data.sign.SignDataSource;
import com.essential.indodriving.ui.activity.HomeActivity;
import com.essential.indodriving.ui.activity.SignMainActivity;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;
import com.essential.indodriving.ui.widget.ShowSignDialog;
import com.essential.indodriving.util.ImageHelper;
import com.essential.indodriving.util.LinearItemDecoration;
import com.essential.indodriving.util.OnSignRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by yue on 07/07/2016.
 */
public class LearnSignByListFragment extends MyBaseFragment implements OnSignRecyclerViewItemClickListener {

    public final static String TAG_LEARN_SIGN_BY_LIST_FRAGMENT = "Fragment Learn Sign By List";
    private View actionLearningSignByCard;
    private RecyclerView recyclerViewSign;
    private List<Sign> signs;
    private ListSignsAdapter adapter;
    private String type;
    private int currentPosition;
    private boolean isFirst;
    private boolean isRated;
    private boolean isProVersion;
    private boolean isEnableRateToUnlock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        signs = SignDataSource.getSigns(type);
        isFirst = true;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        adapter = new ListSignsAdapter(getActivity(), signs);
        recyclerViewSign.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSign.addItemDecoration(new LinearItemDecoration(getActivity()));
        recyclerViewSign.setAdapter(adapter);
        adapter.setListener(this);
        actionLearningSignByCard =
                ((SignMainActivity) getMyBaseActivity()).getActionLearningSignByCard();
        if (actionLearningSignByCard != null) {
            actionLearningSignByCard.setVisibility(View.VISIBLE);
            actionLearningSignByCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFirst = false;
                    moveToLearningSignByCardFragment();
                }
            });
        }
        ((SignMainActivity) getMyBaseActivity()).getActionLearningSignByList().setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadState();
    }

    @Override
    protected String getTitle() {
        return type;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_learn_sign_by_list;
    }

    @Override
    public void onSignItemClick(int position) {
        currentPosition = position;
        if (!isFirst) {
            ShowSignDialog dialog = new ShowSignDialog(getActivity(), signs.get(position));
            dialog.show();
        } else {
            isFirst = false;
            moveToLearningSignByCardFragment();
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle != null ? bundle.getString(Constants.BUNDLE_SIGN_TYPE) :
                SignDataSource.GROUP_PROHIBITION_SIGN;
    }

    private void findViews(View rootView) {
        recyclerViewSign = (RecyclerView) rootView.findViewById(R.id.recycler_view_sign);
    }

    private void loadState() {
        currentPosition = MySetting.getInstance().loadSignPosition(type);
        if (currentPosition > signs.size() - 1) currentPosition = signs.size() - 1;
        isProVersion = MySetting.getInstance().isProVersion();
        isRated = isProVersion ? true : MySetting.getInstance().isRated();
        isEnableRateToUnlock = MySetting.getInstance().isEnableRateToUnlock();
    }

    private void moveToLearningSignByCardFragment() {
        LearnSignByCardFragment fragment = new LearnSignByCardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_SIGN_TYPE, type);
        bundle.putInt(Constants.BUNDLE_CURRENT_POSITION, currentPosition);
        fragment.setArguments(bundle);
        replaceFragment(fragment, TAG_LEARN_SIGN_BY_LIST_FRAGMENT,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out,
                R.animator.card_flip_right_in, R.animator.card_flip_right_out);
    }

    private class ListSignsAdapter extends RecyclerView.Adapter<ListSignsAdapter.ItemHolder> {

        private Context context;
        private List<Sign> signs;
        private OnSignRecyclerViewItemClickListener listener;

        public ListSignsAdapter(Context context, List<Sign> signs) {
            this.context = context;
            this.signs = signs;
        }

        public void setListener(OnSignRecyclerViewItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_detail, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Sign sign = signs.get(position);
            Glide.with(context).load(sign.image).
                    dontAnimate().dontTransform().into(holder.imageQuestion);
            holder.textDetail.setText(sign.definition);
            holder.buttonDetail.setTag(position);
            holder.buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    if (listener != null) {
                        listener.onSignItemClick(position);
                    }
                }
            });
            if (isEnableRateToUnlock && !isProVersion && !isRated) {
                if (position >= Constants.LOCK_START_INDEX) {
                    Bitmap blurImage = ImageHelper.
                            blur(context, ImageHelper.captureView(holder.buttonDetail));
                    if (holder.lockedArea.getVisibility() == View.GONE)
                        holder.lockedArea.setVisibility(View.VISIBLE);
                    holder.blurryImage.setImageBitmap(blurImage);
                } else {
                    if (holder.lockedArea.getVisibility() == View.VISIBLE)
                        holder.lockedArea.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return signs != null ? signs.size() : 0;
        }

        class ItemHolder extends RecyclerView.ViewHolder {

            ImageView imageQuestion;
            TextView textDetail;
            View buttonDetail;
            View lockedArea;
            ImageView blurryImage;

            public ItemHolder(View itemView) {
                super(itemView);
                imageQuestion = (ImageView) itemView.findViewById(R.id.image_sign);
                textDetail = (TextView) itemView.findViewById(R.id.text_detail);
                buttonDetail = itemView.findViewById(R.id.button_detail);
                lockedArea = itemView.findViewById(R.id.locked_area);
                blurryImage = (ImageView) itemView.findViewById(R.id.image_blur);
                ((TextView) itemView.findViewById(R.id.text_press_to_unlock)).
                        setTypeface(HomeActivity.defaultFont);
            }
        }
    }
}
