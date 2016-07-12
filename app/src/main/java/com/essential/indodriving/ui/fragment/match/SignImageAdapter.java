package com.essential.indodriving.ui.fragment.match;

import android.content.Context;
import android.graphics.Bitmap;
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

    public void removeItem(int idSign) {
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).id == idSign) {
                listSign.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

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
        holder.btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.getInstance(context, listSign.get(position).image).show();
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
        for (int i = 0; i < listSign.size(); i++) {
            if (listSign.get(i).selected) return true;
        }
        return false;
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


    @Override
    public int getItemCount() {
        return listSign != null ? listSign.size() : 0;
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        public RelativeLayout item, btnZoom, hightlight;
        public ImageView signImage;

        public ItemHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.card_image);
            hightlight = (RelativeLayout) itemView.findViewById(R.id.card_hightlight);
            signImage = (ImageView) itemView.findViewById(R.id.iv_sign_image);
            btnZoom = (RelativeLayout) itemView.findViewById(R.id.btn_zoom);
        }
    }

    interface OnImageItemClick {
        void onImageItemClick(int idSign);
    }

}
