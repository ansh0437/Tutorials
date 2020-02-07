package com.zxc.tutorials.genericAdapter.first;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.zxc.tutorials.genericAdapter.first.GenericRecyclerListener;

public abstract class GenericViewHolder<T, L extends GenericRecyclerListener> extends RecyclerView.ViewHolder {

    public T data;
    private L listener;

    public GenericViewHolder(View itemView) {
        super(itemView);
    }

    public GenericViewHolder(View itemView, L listener) {
        super(itemView);
        this.listener = listener;
    }

    public abstract void onBind(T data);

    protected L getListener() {
        return listener;
    }
}