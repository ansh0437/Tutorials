package com.zxc.tutorials.genericAdapter.fourth;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.GenericViewHolder> {

    private List<GenericModel> models = new ArrayList<>();

    public void updateList(List<GenericModel> list) {
        if (!list.isEmpty()) models.clear();
        models.addAll(list);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new GenericViewHolder<>(models.get(viewType).getDataBinding(parent));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        models.get(position).onBind(holder.binding);
    }

    class GenericViewHolder<E extends ViewDataBinding> extends RecyclerView.ViewHolder{
        public E binding;

        public GenericViewHolder(E binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

interface GenericModel<E extends ViewDataBinding> {

    E getDataBinding(ViewGroup parent);

    void onBind(E e);

}