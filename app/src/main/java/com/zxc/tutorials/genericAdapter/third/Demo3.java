package com.zxc.tutorials.genericAdapter.third;

import com.zxc.tutorials.R;
import com.zxc.tutorials.databinding.RowItemDemoBinding;

import java.util.ArrayList;
import java.util.List;

public class Demo3 {

    public void start() {

        List<User> list = new ArrayList<>();

        MyAdapter myAdapter = new MyAdapter(R.layout.row_item_demo, list);

    }

    class MyAdapter extends GenericAdapter<User, RowItemDemoBinding> {

        public MyAdapter(int layoutId, List<User> mItems) {
            super(layoutId, mItems);
        }

        @Override
        public void onBind(RowItemDemoBinding binding, User user) {

        }
    }

    class User {
        String name;
    }

}
