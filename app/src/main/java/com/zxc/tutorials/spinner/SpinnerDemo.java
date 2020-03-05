package com.zxc.tutorials.spinner;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zxc.tutorials.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerDemo extends AppCompatActivity {

    private static final String TAG = "Spinner Demo";

    private Spinner spinner;

    private List<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_demo);
        setTitle(TAG);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(spinnerItemSelectedListener);

        generateList();

        // TODO - For custom view in spinner see customAdapterSpinner
//        customAdapterSpinner();

        // TODO - For normal text in spinner see arrayAdapterSpinner
        arrayAdapterSpinner();
    }

    private void generateList() {
        list.add(new User("1", "First User", "one@gmail.com"));
        list.add(new User("2", "Second User", "two@gmail.com"));
        list.add(new User("3", "Third User", "three@gmail.com"));
        list.add(new User("4", "Fourth User", "four@gmail.com"));
        list.add(new User("5", "Fifth User", "five@gmail.com"));
    }

    private void customAdapterSpinner() {
        SpinnerAdapter adapter = new SpinnerAdapter(this, list);
        spinner.setAdapter(adapter);
    }

    private void arrayAdapterSpinner() {
        // For using array adapter with custom list in spinner
        // override toString() method in your model class
        // in that toString() method return the string you want to display in the spinner
        // For reference see User model class

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);
    }

    private AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            User user = list.get(position);

            Toast.makeText(SpinnerDemo.this, user.getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
