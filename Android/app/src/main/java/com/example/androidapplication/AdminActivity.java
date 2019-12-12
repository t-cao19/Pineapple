package com.example.androidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Open admin activity.
 */
public class AdminActivity extends AppCompatActivity {

    private Button promote;
    private Button viewBooks;
    private Button viewInactiveAcc;
    private Button viewActiveAcct;
    private Button ser;
    private Button deser;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        promote = findViewById(R.id.button_promote_employee);
        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPromoteEmployeeActivity();
            }
        });

        viewBooks = findViewById(R.id.button_view_books);
        viewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewBooksActivity();
            }
        });

        viewInactiveAcc = findViewById(R.id.button_view_inactive);
        viewInactiveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInactiveAccActivity();
            }
        });

        viewActiveAcct = findViewById(R.id.button_view_active);
        viewActiveAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActiveAccActivity();
            }
        });

        ser = findViewById(R.id.button_serialize);
        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSerializeActivity();
            }
        });

        deser = findViewById(R.id.button_deserialize);
        deser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeserializeActivity();
            }
        });

        exit = findViewById(R.id.button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });
    }

    /**
     * Open promote employee activity.
     */
    public void openPromoteEmployeeActivity() {
        Intent intent = new Intent(this, PromoteEmployeeActivity.class);
        startActivity(intent);
    }

    /**
     * Open view books activity.
     */
    public void openViewBooksActivity() {
        Intent intent = new Intent(this, ViewBookActivity.class);
        startActivity(intent);
    }

    /**
     * Open inactive accounts activity.
     */
    public void openInactiveAccActivity() {
        Intent intent = new Intent(this, InactiveAccActivity.class);
        startActivity(intent);
    }

    /**
     * Open active accounts activity.
     */
    public void openActiveAccActivity() {
        Intent intent = new Intent(this, ActiveAccActivity.class);
        startActivity(intent);
    }

    /**
     * Open serialize activity.
     */
    public void openSerializeActivity() {
        Intent intent = new Intent(this, SerializeActivity.class);
        startActivity(intent);
    }

    /**
     * Open deserialize activity.
     */
    public void openDeserializeActivity() {
        Intent intent = new Intent(this, DeserializeActivity.class);
        startActivity(intent);
    }

    /**
     * Exit to login activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
