package com.zxc.tutorials.genericAdapter.fourth;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.zxc.tutorials.databinding.RowItemDemoBinding;

import java.util.ArrayList;
import java.util.List;

public class Demo4 {

    private RecyclerView recyclerView;
    private GenericAdapter genericAdapter;

    public void start() {

        List<User> users = new ArrayList<>();
        users.add(new User("Full Name 1"));
        users.add(new User("Full Name 2"));
        users.add(new User("Full Name 3"));
        users.add(new User("Full Name 4"));
        users.add(new User("Full Name 5"));

        List<GenericModel> modelList = new ArrayList<>();

        for (User user : users) {
            modelList.add(new UserModel(user));
        }

        genericAdapter = new GenericAdapter();
        genericAdapter.updateList(modelList);
        recyclerView.setAdapter(genericAdapter);
    }

    class UserModel implements GenericModel<RowItemDemoBinding> {

        private User user;

        public UserModel(User user) {
            this.user = user;
        }

        @Override
        public RowItemDemoBinding getDataBinding(ViewGroup parent) {
            return RowItemDemoBinding.inflate(LayoutInflater.from(parent.getContext()));
        }

        @Override
        public void onBind(RowItemDemoBinding binding) {
            binding.tvTitle.setText(user.name);
        }
    }

    class User {
        String name;

        public User(String name) {
            this.name = name;
        }
    }

}
