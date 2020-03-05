package com.zxc.tutorials.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxc.tutorials.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<User> mList = new ArrayList<>();

    public SpinnerAdapter(Context mContext, List<User> mList) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView = null;
        try {
            ViewHolder holder;
            myView = convertView;
            if (myView == null) {
                myView = mLayoutInflater.inflate(R.layout.row_item_spinner, null);
                holder = new ViewHolder();
                holder.tvTitle = myView.findViewById(R.id.tvTitle);
                myView.setTag(holder);
            } else {
                holder = (ViewHolder) myView.getTag();
            }

            User user = mList.get(position);
            holder.tvTitle.setText(user.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }

    private class ViewHolder {
        private TextView tvTitle;
    }
}
