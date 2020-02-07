package com.zxc.tutorials.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zxc.tutorials.R;
import com.zxc.tutorials.dagger.DaggerDemo;
import com.zxc.tutorials.genericAdapter.GenericAdapterDemo;
import com.zxc.tutorials.imageselector.ImageSelectorDemo;
import com.zxc.tutorials.location.LocationDemo;
import com.zxc.tutorials.permission.PermissionDemo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MenuDTO> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuList.add(new MenuDTO("Permission", PermissionDemo.class));
        menuList.add(new MenuDTO("Location", LocationDemo.class));
        menuList.add(new MenuDTO("Image Selector", ImageSelectorDemo.class));
        menuList.add(new MenuDTO("Dagger 2", DaggerDemo.class));
        menuList.add(new MenuDTO("Generic Adapter", GenericAdapterDemo.class));

        RecyclerView mRecyclerView = findViewById(R.id.rvMenu);
        mRecyclerView.setAdapter(new MenuAdapter());
    }

    private void openDemo(Class aClass) {
        startActivity(new Intent(this, aClass));
    }

    class MenuDTO implements Serializable {
        String title;
        Class cls;

        public MenuDTO(String title, Class cls) {
            this.title = title;
            this.cls = cls;
        }
    }

    class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

        @NonNull
        @Override
        public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MenuHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item_menu, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return menuList.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder {
            private CardView cardView;
            private TextView title;

            public MenuHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.cvMenu);
                title = itemView.findViewById(R.id.tvTitle);
            }

            public void bind(int position) {
                MenuDTO menu = menuList.get(position);
                title.setText(menu.title);
                cardView.setOnClickListener(v -> openDemo(menu.cls));
            }
        }
    }
}
