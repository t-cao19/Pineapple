package com.example.androidapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.b07.database.DatabaseSelectHelper;
import com.b07.security.PasswordHelpers;
import com.b07.users.Admin;
import com.b07.users.Employee;
import com.b07.users.Customer;
import com.b07.users.User;

/**
 * Open login activity.
 */
public class LogInActivity extends AppCompatActivity {

    private Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        com.b07.database.DatabaseDriverAndroid driver =
                new com.b07.database.DatabaseDriverAndroid(this);
        if (!driver.checkDatabase(this)) {
            initializeActivity();
        }

        logIn = findViewById(R.id.button_log_in);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    /**
     * Open initialize activity if database does not exist.
     */
    public void initializeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Check user id and password login input. If valid, open the correct view for each role.
     */
    private void validate() {
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);

        EditText userInput = findViewById(R.id.userId);
        int userId = -1;

        try {
            userId = Integer.parseInt(userInput.getText().toString());
        } catch (Exception e) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage("User id must be a number!");
            dlgAlert.setTitle("Error Message");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }

        EditText passwordInput = findViewById(R.id.editPassword);
        String password = passwordInput.getText().toString();

        User userInfo = select.getUserDetailsHelper(userId);

        if (userInfo == null) {
            //print out try again
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage("User id must be not blank!");
            dlgAlert.setTitle("Error Message");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        } else if (!PasswordHelpers.comparePassword(select.getPasswordHelper(userId), password)) {
            //print out wrong password
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage("Wrong password or user id!");
            dlgAlert.setTitle("Error Message");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else {

            if (userInfo instanceof Admin) {
                //launch AdminActivity
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            } else if (userInfo instanceof Employee) {
                //launch employee activity
                Intent intent = new Intent(this, EmployeeActivity.class);
                startActivity(intent);
            } else if (userInfo instanceof Customer) {
                //launch customer Activity
                Intent intent = new Intent(this, CustomerActivity.class);
                intent.putExtra("Username", userId);
                startActivity(intent);
            }
        }
    }
}
