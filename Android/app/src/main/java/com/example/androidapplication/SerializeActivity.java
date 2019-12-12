package com.example.androidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.b07.serialize.SerializeHelper;

public class SerializeActivity extends AppCompatActivity {
    private Button confirm;
    private Button exit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialize);

        confirm = findViewById(R.id.ser_submit_btn);
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmActivity();
            }
        });

        exit = findViewById(R.id.ser_exit_btn);
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
            File file = getFilesDir();
            SerializeHelper ser = new SerializeHelper(this);
            String log = ser.writeDb(file);

            Toast.makeText(SerializeActivity.this, "Successfully write the database",
                    Toast.LENGTH_SHORT).show();
            display.setText(log);
            //display.setText(file.getPath());
        } catch (Exception e) {
            display.setText("Something went wrong" + e.getLocalizedMessage());
        }
    }


    public void exitActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}