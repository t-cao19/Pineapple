package com.example.androidapplication;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;

/**
 * Open employee activity.
 */
public class EmployeeActivity extends AppCompatActivity {

    private Button authenticateEmp;
    private Button newUser;
    private Button newEmp;
    private Button newAccount;
    private Button restock;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        authenticateEmp = findViewById(R.id.button_authenticate_new_user);
        authenticateEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAuthenticateEmployeeActivity();
            }
        });

        newUser = findViewById(R.id.button_make_new_user);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewUserActivity();
            }
        });

        newEmp = findViewById(R.id.button_employee_create);
        newEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewEmployeeActivity();
            }
        });

        newAccount = findViewById(R.id.button_make_new_account);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAccountActivity();
            }
        });

        restock = findViewById(R.id.button_restock_inventory);
        restock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRestockInventoryActivity();
            }
        });

        exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });
    }

    /**
     * Exit to login activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    /**
     * Open authenticate employee activity.
     */
    public void openAuthenticateEmployeeActivity() {
        Intent intent = new Intent(this, AuthenticateEmployeeActivity.class);
        startActivity(intent);
    }

    /**
     * Open new user activity.
     */
    public void openNewUserActivity() {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    /**
     * Open new employee activity.
     */
    public void openNewEmployeeActivity() {
        Intent intent = new Intent(this, NewEmployeeActivity.class);
        startActivity(intent);
    }

    /**
     * Open new account activity.
     */
    public void openNewAccountActivity() {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Open restock inventory activity.
     */
    public void openRestockInventoryActivity() {
        Intent intent = new Intent(this, RestockInventoryActivity.class);
        startActivity(intent);
    }
}
