package com.example.androidapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import com.b07.database.DatabaseDriverAndroid;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        com.b07.database.DatabaseDriverAndroid driver =
                new com.b07.database.DatabaseDriverAndroid(this);
        final boolean dbExists = driver.checkDatabase(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent;
                if (dbExists){
                    intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                }
                else{
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
            }, 5000);
    }
}
