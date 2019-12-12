package com.example.androidapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.b07.database.DatabaseSelectHelper;
import com.b07.users.Employee;
import com.b07.users.User;
import com.b07.security.PasswordHelpers;

/**
 * Open authenticate employee activity.
 */
public class AuthenticateEmployeeActivity extends AppCompatActivity {

    private Button signin;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_employee);

        signin = findViewById(R.id.sign_in_btn);
        signin.setVisibility(View.VISIBLE);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInActivity();
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
     * Sign in new employee based on input id and password.
     */
    public void signInActivity() {
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);
        TextView display = findViewById(R.id.confirmation);

        EditText textid = findViewById(R.id.userId);

        int id = -1;

        try {
            id = Integer.parseInt(textid.getText().toString());
        } catch (Exception e) { // input id is not an int
            display.setText("Invalid Id!");
        }

        EditText textpassword = findViewById(R.id.password);
        String password = textpassword.getText().toString();

        User user = select.getUserDetailsHelper(id);

        if (user == null) { // invalid id
            display.setText("Invalid id!");
        } else if (!(user instanceof Employee)) { // not employee
            display.setText("This is not an employee account!");
        } else if (!PasswordHelpers.comparePassword(select.getPasswordHelper(id), password)) {
            display.setText("Invalid password!");
        } else {
            display.setText("New employee logged on."); // TODO: the interface??
            signin.setVisibility(View.GONE);

            EditText userId = findViewById(R.id.userId);
            userId.setText("");
            EditText pwd = findViewById(R.id.password);
            pwd.setText("");
        }

    }

    /**
     * Exit to employee activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, EmployeeActivity.class);
        startActivity(intent);
    }
}
