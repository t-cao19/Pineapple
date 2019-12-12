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
 * Open new admin activity.
 */
public class NewAdminActivity extends AppCompatActivity {

    private Button signup;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_admin);

        signup = findViewById(R.id.sign_up_btn);
        signup.setVisibility(View.VISIBLE);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAdmin();
                signup.setVisibility(View.GONE);
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
     * Exit to main activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Create a new admin.
     */
    public void addAdmin() {
        DatabaseInsertHelper insert = new DatabaseInsertHelper(this);
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);

        EditText textname = findViewById(R.id.admin_name);
        String name = textname.getText().toString();

        EditText textage = findViewById(R.id.age);
        int age = Integer.parseInt(textage.getText().toString());

        EditText textaddress = findViewById(R.id.address);
        String address = textaddress.getText().toString();

        EditText textpassword = findViewById(R.id.password);
        String password = textpassword.getText().toString();

        int id = (int) (insert.insertNewUserHelper(name, age, address, password));

        HashMap<Integer, String> roles = select.getRolesHelper();
        int roleId = 0;
        for (int key : roles.keySet()) {
            if (roles.get(key).equals(Roles.ADMIN.name())) {
                roleId = key;
            }
        }

        insert.insertUserRoleHelper(id, roleId);

        TextView display = findViewById(R.id.id_display);
        display.setText("New Id: " + id);
    }
}
