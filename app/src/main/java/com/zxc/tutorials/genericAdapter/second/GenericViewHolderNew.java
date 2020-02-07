package com.zxc.tutorials.genericAdapter.second;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class GenericViewHolderNew<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public T binding;

    public GenericViewHolderNew(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
