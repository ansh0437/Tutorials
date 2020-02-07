package com.zxc.tutorials.genericAdapter.first;

public interface OnItemClickListener<T> extends GenericRecyclerListener {

    void onItemClicked(int position, T item);

}