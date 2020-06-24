package com.hpcang.base.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hpcang.base.common.OnAntiShakeClickListener;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Js on 2016/5/13.
 */
public abstract class BaseListViewAdapter<T> extends android.widget.BaseAdapter {

    protected Context context;
    protected List<T> data = new ArrayList<>();
    protected int layoutId;
    protected OnItemClickListener itemClickListener;

    public BaseListViewAdapter(Context context, List<T> data, int layoutId) {
        this.context = context;
        if (data != null)
            this.data.addAll(data);
        this.layoutId = layoutId;
    }

    public BaseListViewAdapter(Context context, List<T> data) {
        this.context = context;
        if (data != null)
            this.data.addAll(data);
    }

    public BaseListViewAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
    }

    public BaseListViewAdapter(Context context) {
        this.context = context;
    }

    private BaseListViewAdapter() {
    }

    @Override
    public int getCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (data != null)
            return data.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        if (contentView == null && getViewTypeCount() == 1) {
            contentView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        }
        return getView(position, contentView, parent, data.get(position));
    }

    protected abstract View getView(int position, View contentView, ViewGroup parent, T item);

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.data.clear();
    }

    public View inflate(int layout) {
        return LayoutInflater.from(context).inflate(layout, null);
    }

    public View inflate(int layout, ViewGroup view) {
        return LayoutInflater.from(context).inflate(layout, view, false);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);

        void OnItemLongClick(View v, int position);
    }

    protected void setOnClickListener(final View view, final int position) {
        view.setOnClickListener(new OnAntiShakeClickListener() {
            @Override
            public void antiShakeOnClick(@Nullable View v) {
                if (itemClickListener != null)
                    itemClickListener.OnItemClick(view, position);
            }
        });
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

}
