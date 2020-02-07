package com.zxc.tutorials.genericAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.zxc.tutorials.R;
import com.zxc.tutorials.databinding.RowItemDemoBinding;
import com.zxc.tutorials.genericAdapter.second.GenericAdapterNew;
import com.zxc.tutorials.genericAdapter.second.GenericModel;
import com.zxc.tutorials.genericAdapter.second.GenericViewHolderNew;

import java.util.ArrayList;
import java.util.List;

public class GenericAdapterDemo extends AppCompatActivity {

    private static final String TAG = "Generic Adapter Demo";

    private GenericAdapterNew genericAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_adapter_demo);
        setTitle(TAG);

        genericAdapter = new GenericAdapterNew();

        List<MyViewHolder> list = new ArrayList<>();

        genericAdapter.setModels(list);

    }

    class User {
        public String name;
    }

    class MyViewHolder implements GenericModel<GenericViewHolderNew<RowItemDemoBinding>> {

        private User user;

        public MyViewHolder(User user) {
            this.user = user;
        }

        @Override
        public GenericViewHolderNew<RowItemDemoBinding> getViewHolder(ViewGroup parent) {
            return new GenericViewHolderNew<>(RowItemDemoBinding.inflate(
                    LayoutInflater.from(parent.getContext())
            ));
        }

        @Override
        public void onBind(int position, GenericViewHolderNew<RowItemDemoBinding> holder) {
            holder.binding.tvTitle.setText(user.name);
        }
    }
}
