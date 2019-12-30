package com.example.androidapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // THIS LINE CAUSING EITHER TO START UP THE CREATE EMPLOYEE/ADMIN SCREEN OR LOGIN NOT BOTH
                Intent intent = new Intent(splash_screen.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
            }, 5000);
    }
}
