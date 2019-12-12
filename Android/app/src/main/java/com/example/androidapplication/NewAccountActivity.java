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
import com.b07.users.Customer;
import com.b07.users.Roles;
import com.b07.users.User;
import java.util.HashMap;
import java.util.List;

/**
 * Open new account activity.
 */
public class NewAccountActivity extends AppCompatActivity {

    private Button makeAccount;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        listUsers();

        makeAccount = findViewById(R.id.action_make_account);
        makeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAccount();
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
     * List all customers able to create an account.
     */
    public void listUsers() {
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);
        TextView list = findViewById(R.id.user_id_list);
        list.setText("");

        HashMap<Integer, String> roles = select.getRolesHelper();
        int roleId = 0;
        for (int key : roles.keySet()) {
            if (roles.get(key).equals(Roles.CUSTOMER.name())) { //get customer id
                roleId = key;
            }
        }

        List<Integer> users = select.getUsersByRoleHelper(roleId);
        if (users == null) {
            list.setText("No customers to display!");
            return;
        }

        for (int id : users) {
            if (id != -1) {
                User user = select.getUserDetailsHelper(id);
                list.setText(list.getText() + "  ID: " + id + "  NAME: " + user.getName() + "\n");
            }
        }
    }

    /**
     * Make an account for a customer.
     */
    public void makeAccount() {
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);
        DatabaseInsertHelper insert = new DatabaseInsertHelper(this);
        long accountId = -1;
        TextView display = findViewById(R.id.confirmation);

        EditText id = findViewById(R.id.userId);

        int userId = -1;
        try {
            userId = Integer.parseInt(id.getText().toString());
        } catch (Exception e) { // input id invalid
            display.setText("Invalid Id!");
        }

        User user = select.getUserDetailsHelper(userId);

        if (user != null && user instanceof Customer) {
            accountId = insert.insertAccountHelper(userId, true);

            if (accountId != -1) { //successfully created
                display.setText("Account Id: " + accountId);

                EditText inputId = findViewById(R.id.userId);
                inputId.setText("");
            } else {
                display.setText("Could not create account for this customer.");
            }
        } else {
            display.setText("Invalid customer id!");
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
