package com.zxc.tutorials.genericAdapter.first;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenericAdapter<VH extends GenericViewHolder> extends RecyclerView.Adapter<VH> {

    private List<VH> viewHolders = new ArrayList<>();

    public void setViewHolders(List<VH> viewHolders) {
        if (!viewHolders.isEmpty()) this.viewHolders.clear();
        this.viewHolders.addAll(viewHolders);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return viewHolders.get(viewType);
    }

    @Override
    public int getItemCount() {
        return viewHolders.size();
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        holder.onBind(viewHolders.get(position).data);
    }
}

