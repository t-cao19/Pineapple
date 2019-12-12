package com.example.androidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.users.Employee;
import com.b07.users.Roles;
import com.b07.users.User;
import java.util.List;

/**
 * Open promote employee activity.
 */
public class PromoteEmployeeActivity extends AppCompatActivity {

    private Button promote;
    private Button exit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_employee);

        promote = findViewById(R.id.promote_btn);
        promote.setVisibility(View.VISIBLE);
        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoteActivity();
            }
        });

        exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

        TextView employeeIdList = findViewById(R.id.employee_id_list);

        com.b07.database.DatabaseSelectHelper select = new com.b07.database.DatabaseSelectHelper(
                this);
        String idList = "";

        List<Integer> list = select
                .getUsersByRoleHelper(select.getRoleIdHelper(Roles.EMPLOYEE.name()));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != -1) {
                idList = idList + " " + list.get(i);
            }
        }
        if (idList.equals("")) {
            employeeIdList.setText("No available employee to promote");
        } else {
            employeeIdList.setText("All available employee id(s): " + idList);
        }
    }

    /**
     * Promote an employee to an admin.
     */
    public void promoteActivity() {
        com.b07.database.DatabaseSelectHelper select = new com.b07.database.DatabaseSelectHelper(
                this);

        TextView display = findViewById(R.id.confirmation);

        EditText textid = findViewById(R.id.userId);

        try {
            User emUser = select.getUserDetailsHelper(
                    Integer.parseInt(textid.getText().toString()));
            display.setText("emUser" + emUser.getName());

            if (emUser == null) {
                display.setText("Could not find this user!");
            } else if (!(emUser instanceof Employee)) {
                display.setText("This is not an employee Id");
            } else {
                int employeeId = emUser.getId();
                int adminRoleId = select.getRoleIdHelper(Roles.ADMIN.name());
                com.b07.database.DatabaseUpdateHelper update = new com.b07.database.DatabaseUpdateHelper(
                        this);

                update.updateUserRoleHelper(adminRoleId, employeeId);
                display.setText("Employee promoted! \n Employee # " + employeeId
                        + " has been promoted to Admin (" + adminRoleId + ").");
            }

        } catch (Exception e) {

            display.setText("Please input a valid ID!");

        }
    }

    /**
     * Exit to admin activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}