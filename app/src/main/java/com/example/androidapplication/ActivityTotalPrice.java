package com.example.androidapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Open total price activity.
 */
public class ActivityTotalPrice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_price);

        TextView view = findViewById(R.id.total_price);
        view.setText("$" + getIntent().getStringExtra("TOTAL"));
    }
}
