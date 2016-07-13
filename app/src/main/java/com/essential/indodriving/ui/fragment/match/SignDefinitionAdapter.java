package com.essential.indodriving.ui.fragment.match;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.SignDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 7/11/2016.
 */
public class SignDefinitionAdapter extends RecyclerView.Adapter<SignDefinitionAdapter.ItemHolder> {

    private Context context;
    private List<SignDefinition> listSign = new ArrayList<>();
    private OnDefinitionItemClick listener;

    public void setListener(OnDefinitionItemClick listener) {
        this.listener = listener;
    }

    public SignDefinitionAdapter(Context context, List<SignDefinition> signs) {
        this.context = context;
        this.listSign = listSignMixed(signs);
    }

    public List<SignDefinition> listSignMixed(List<SignDefinition> listSignStock) {

        List<SignDefinition> listSignMixed = new ArrayList<>();
        for (int i = 0; i < listSignStock.size(); i++) {
            listSignMixed.add(listSignStock.get(i));
        }
        Collections.shuffle(listSignMixed);
        return listSignMixed;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_sign_definition, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        holder.signDefinition.setText(listSign.get(position).definition);
        if (listSign.get(position).selected) {
            holder.hightlight.setVisibility(View.VISIBLE);
            if (listSign.get(position).match) {
                holder.hightlight.setBackgroundResource(R.drawable.hightlight_sign_match);
            } else {
                holder.hightlight.setBackgroundResource(R.drawable.hightlight_sign_not_match);
            }
        } else {
            holder.hightlight.setVisibility(View.GONE);
        }
        holder.btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.getInstance(context, listSign.get(position).definition).show();
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDefinitionItemClick(listSign.get(position).id);
            }
        });

    }


    public int idChecked() {
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).selected) return listSign.get(i).id;
        }
        return 0;
    }

    public void resetCheck() {
        for (int i = 0; i < listSign.size(); i++) {
            listSign.get(i).selected = false;
            listSign.get(i).match = false;
        }
    }

    public void setCheck(int idSign, boolean match) {
        resetCheck();
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).id == idSign) {
                listSign.get(i).selected = true;
                listSign.get(i).match = match;
            }
        }
    }

    public boolean isEmptyList() {
        if (listSign.size() == 0) return true;
        else return false;
    }

    public void removeItem(int idSign) {
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).id == idSign) {
                listSign.remove(i);
                notifyItemRemoved(i);
                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnable, 450);
                break;
            }
        }
    }

    private Runnable mMyRunnable = new Runnable() {
        @Override
        public void run() {
            notifyDataSetChanged();
        }
    };

    public boolean isChecked() {
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).selected == true) return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return listSign != null ? listSign.size() : 0;
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        public RelativeLayout item, btnZoom, hightlight;
        public TextView signDefinition;

        public ItemHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.card_image);
            hightlight = (RelativeLayout) itemView.findViewById(R.id.card_hightlight);
            signDefinition = (TextView) itemView.findViewById(R.id.tv_sign_definition);
            btnZoom = (RelativeLayout) itemView.findViewById(R.id.btn_zoom);
        }
    }

    interface OnDefinitionItemClick {
        void onDefinitionItemClick(int idSign);
    }
}
