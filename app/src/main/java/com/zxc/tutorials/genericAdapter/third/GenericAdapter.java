package com.zxc.tutorials.genericAdapter.third;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericAdapter<T, E extends ViewDataBinding> extends RecyclerView.Adapter<MyViewHolder<E>> {

    private int layoutId;
    private List<T> mItems;

    public GenericAdapter(int layoutId, List<T> mItems) {
        this.layoutId = layoutId;
        this.mItems = mItems;
    }

    public void submitList(List<T> list) {
        if (!list.isEmpty()) mItems.clear();
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public MyViewHolder<E> onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new MyViewHolder<>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder<E> holder, int position) {
        onBind(holder.binding, mItems.get(position));
    }

    public abstract void onBind(E binding, T t);
}

class MyViewHolder<E extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public E binding;

    public MyViewHolder(E binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
