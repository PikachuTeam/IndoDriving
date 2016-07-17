package com.essential.indodriving.ui.fragment.match;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.essential.indodriving.R;
import com.essential.indodriving.data.sign.SignImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 7/11/2016.
 */
public class SignImageAdapter extends RecyclerView.Adapter<SignImageAdapter.ItemHolder> {

    private Context context;
    private List<SignImage> listSign = new ArrayList<>();
    private OnImageItemClick listener;
    private int idSelected = -1;

    public void setListener(OnImageItemClick listener) {
        this.listener = listener;
    }

    public SignImageAdapter(Context context, List<SignImage> signs) {
        this.context = context;
        this.listSign = listSignMixed(signs);
    }

    public List<SignImage> listSignMixed(List<SignImage> listSignStock) {

        List<SignImage> listSignMixed = new ArrayList<>();
        for (int i = 0; i < listSignStock.size(); i++) {
            listSignMixed.add(listSignStock.get(i));
        }

        Collections.shuffle(listSignMixed);
        return listSignMixed;
    }

    public boolean isEmptyList() {
        if (listSign.size() == 0) return true;
        else return false;
    }

    public int idChecked() {
        return idSelected;
    }

    public void resetCheck() {
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).id == idSelected) {
                listSign.get(i).selected = false;
                listSign.get(i).match = false;
                break;
            }
        }
        idSelected = -1;
        notifyDataSetChanged();
    }


    public void setCheck(int idSign, boolean match) {
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).id == idSign) {
                listSign.get(i).selected = true;
                listSign.get(i).match = match;
                idSelected = idSign;
            } else {
                listSign.get(i).selected = false;
                listSign.get(i).match = false;
            }
        }
        notifyDataSetChanged();
    }

    public void removeItem(int idSign) {
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).id == idSign) {
                listSign.remove(i);
                idSelected = -1;
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

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_sign_image, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
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
        Glide.with(context).load(listSign.get(position).image).asBitmap().dontAnimate().dontTransform()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.signImage.setImageBitmap(resource);
                    }
                });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageItemClick(listSign.get(position).id);
            }
        });

    }

    public boolean isChecked() {
        if (idSelected >= 0) return true;
        else
            return false;
    }


    @Override
    public int getItemCount() {
        return listSign != null ? listSign.size() : 0;
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        public RelativeLayout item, hightlight;
        public ImageView signImage;

        public ItemHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.card_image);
            hightlight = (RelativeLayout) itemView.findViewById(R.id.card_hightlight);
            signImage = (ImageView) itemView.findViewById(R.id.iv_sign_image);
        }
    }

    interface OnImageItemClick {
        void onImageItemClick(int idSign);
    }

}
