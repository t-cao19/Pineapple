package com.example.androidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.b07.database.DatabaseDriver;
import com.b07.database.DatabaseDriverAndroid;
import com.b07.serialize.DeserializeHelper;

import java.io.File;


public class DeserializeActivity extends AppCompatActivity {
    private Button confirm;
    private Button exit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deserialize);

        confirm = findViewById(R.id.deser_submit_btn);
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmActivity();
            }
        });

        exit = findViewById(R.id.deser_exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

    }


    public void confirmActivity() {

        TextView display = findViewById(R.id.result);

        try {
            DatabaseDriverAndroid driver = new DatabaseDriverAndroid(this);
            driver.reInitializeHelper();

            File file = getFilesDir();
            DeserializeHelper deser = new DeserializeHelper(this);
            String log = deser.readDB(file);

            Toast.makeText(DeserializeActivity.this, "Successfully read the database",
                    Toast.LENGTH_SHORT).show();
            display.setText(log);
        } catch (Exception e) {
            display.setText("Something went wrong" + e.getLocalizedMessage());
        }

    }


    public void exitActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}