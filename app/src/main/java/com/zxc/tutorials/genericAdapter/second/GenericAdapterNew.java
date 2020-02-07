package com.zxc.tutorials.genericAdapter.second;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenericAdapterNew<T extends GenericModel<GenericViewHolderNew>> extends RecyclerView.Adapter<GenericViewHolderNew> {

    private List<T> models = new ArrayList<>();

    public void setModels(List<T> viewHolders) {
        if (!viewHolders.isEmpty()) this.models.clear();
        this.models.addAll(viewHolders);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public GenericViewHolderNew onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return models.get(viewType).getViewHolder(parent);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public void onBindViewHolder(@NotNull GenericViewHolderNew holder, int position) {
        models.get(position).onBind(position, holder);
    }
}

