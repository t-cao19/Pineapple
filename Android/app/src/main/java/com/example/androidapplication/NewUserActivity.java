package com.example.androidapplication;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import com.b07.database.DatabaseInsertHelper;
import com.b07.database.DatabaseSelectHelper;
import com.b07.users.Roles;
import java.util.HashMap;

/**
 * Open new user activity.
 */
public class NewUserActivity extends AppCompatActivity {

    private Button signup;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        signup = findViewById(R.id.sign_up_btn);
        signup.setVisibility(View.VISIBLE);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
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
     * Exit to employee activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, EmployeeActivity.class);
        startActivity(intent);
    }

    /**
     * Create a new customer.
     */
    public void addCustomer() {
        DatabaseInsertHelper insert = new DatabaseInsertHelper(this);
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);
        TextView display = findViewById(R.id.id_display);

        EditText textname = findViewById(R.id.user_name);
        String name = textname.getText().toString();
        if (!name.contains(" ") || name.equals("")) {
            display.setText("Invalid name!");
            return;
        }

        EditText textage = findViewById(R.id.age);
        int age = -1;
        try {
            age = Integer.parseInt(textage.getText().toString());
        } catch (Exception e) { //not a number
            display.setText("Invalid age!");
        }

        EditText textaddress = findViewById(R.id.address);
        String address = textaddress.getText().toString();
        if (address.equals("")) {
            display.setText("Invalid address!");
            return;
        }

        EditText textpassword = findViewById(R.id.password);
        String password = textpassword.getText().toString();
        if (password.equals("")) {
            display.setText("Enter a password!");
            return;
        }

        int id = (int) (insert.insertNewUserHelper(name, age, address, password));

        if (id == -1) {
            display.setText("Sorry, something went wrong!");
            return;
        }

        HashMap<Integer, String> roles = select.getRolesHelper();
        int roleId = 0;
        for (int key : roles.keySet()) {
            if (roles.get(key).equals(Roles.CUSTOMER.name())) {
                roleId = key;
            }
        }

        insert.insertUserRoleHelper(id, roleId);

        display.setText("New Id: " + id);
        signup.setVisibility(View.GONE);
    }
}
