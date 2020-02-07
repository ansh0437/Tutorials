package com.zxc.tutorials.genericAdapter.second;

import android.view.ViewGroup;

public interface GenericModel<T extends GenericViewHolderNew> {

    T getViewHolder(ViewGroup parent);

    void onBind(int position, T holder);

}
