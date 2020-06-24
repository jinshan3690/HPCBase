package com.hpcang.base.common.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import com.hpcang.base.common.OnAntiShakeClickListener;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class BaseRecyclerAdapter<Y extends RecyclerView.ViewHolder, T extends Object> extends RecyclerView.Adapter<Y>{

    protected int lastPosition = -1;
    public int currentPosition = 0;
    public int itemType = 0;

    protected List<T> data = new ArrayList<>();
    protected Context context;
    protected Integer layout;
    protected OnItemClickListener itemClickListener;

    public BaseRecyclerAdapter(Context context, List<T> data, Integer layout) {
        if (data != null)
            this.data.addAll(data);
        this.context = context;
        this.layout = layout;
    }

    public BaseRecyclerAdapter(Context context, Integer layout) {
        this.context = context;
        this.layout = layout;
    }

    public BaseRecyclerAdapter(Context context, List<T> list) {
        if (data != null)
            this.data.addAll(data);
        this.context = context;
    }

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Y onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (layout != null)
            view = LayoutInflater.from(context).inflate(layout, parent, false);
        return onCreateViewHolder(parent, view, viewType);
    }

    protected Y onCreateViewHolder(ViewGroup parent, View view, int viewType){
        return (Y) new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Y holder, int position) {
        currentPosition = position;
        onBindViewHolder(holder, position, data.size() == 0 ? null : data.get(position));
    }

    public abstract void onBindViewHolder(Y holder, int position, T item);

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);

        void OnItemLongClick(View v, int position);
    }

    protected void setOnClickListener(final View view, final int position) {
        setOnClickListener(view, position, true);
    }

    protected void setOnClickListener(final View view, final int position, boolean hasAntiShake) {
        if(hasAntiShake) {
            view.setOnClickListener(new OnAntiShakeClickListener() {
                @Override
                public void antiShakeOnClick(@Nullable View v) {
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(view, position);
                }
            });
        }else{
            view.setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.OnItemClick(view, position);
            });
        }
    }

    protected void setOnLongClickListener(final View view, final int position) {
        view.setOnLongClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.OnItemLongClick(view, position);
                return true;
            }
            return false;
        });
    }

    public View inflate(int layout) {
        return LayoutInflater.from(context).inflate(layout, null);
    }

    public View inflate(int layout, ViewGroup view) {
        return LayoutInflater.from(context).inflate(layout, view, false);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data.addAll(data);

        notifyDataSetChanged();
    }

    public void addData(T data) {
        this.data.add(data);

        notifyItemInserted(this.data.size());
    }

    public void addDataAll(List<T> data) {
        int start = this.data.size();
        this.data.addAll(data);

        notifyItemRangeInserted(start, data.size());
    }

    public void removeData(int position) {
        getData().remove(position);
        notifyItemRemoved(position);
    }

    public void removeAllData() {
        notifyItemRangeRemoved(0, data.size());
    }

    public void clearData() {
        lastPosition = -1;
        this.data.clear();
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    protected void startAnimators(RecyclerView.ViewHolder holder) {
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition > lastPosition) {
            for (ObjectAnimator anim : getAnimators(holder.itemView)) {
                anim.setInterpolator(new LinearInterpolator());
                anim.setDuration(300).start();
            }
            lastPosition = adapterPosition;
        } else {
//            ViewHelper.clear(holder.itemView);
        }
    }

    private List<ObjectAnimator> getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);
        return Arrays.asList(scaleX, scaleY);
    }

}
