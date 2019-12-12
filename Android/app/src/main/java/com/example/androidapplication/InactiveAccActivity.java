package com.example.androidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.users.Roles;
import java.util.ArrayList;
import java.util.List;

/**
 * Open inactive account activity.
 */
public class InactiveAccActivity extends AppCompatActivity {

    private Button submit;
    private Button exit;
    private List<Integer> customerIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inactive_acc);

        submit = findViewById(R.id.submit_btn);
        submit.setVisibility(View.VISIBLE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitActivity();
            }
        });

        exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

        TextView customerIdList = findViewById(R.id.customer_id_list);

        com.b07.database.DatabaseSelectHelper select = new com.b07.database.DatabaseSelectHelper(
                this);
        String idList = "";

        customerIds = select
                .getUsersByRoleHelper(select.getRoleIdHelper(Roles.CUSTOMER.name()));
        for (int i = 0; i < customerIds.size(); i++) {
            if (customerIds.get(i) != -1) {
                idList += " " + customerIds.get(i);
            }
        }
        customerIdList.setText("All available customer id(s): " + idList);
    }

    /**
     * Check inactive accounts of the user.
     */
    public void submitActivity() {
        com.b07.database.DatabaseSelectHelper select = new com.b07.database.DatabaseSelectHelper(
                this);

        TextView display = findViewById(R.id.result);
        EditText textid = findViewById(R.id.input_userId);
        display.setText(textid.getText().toString());
        int id = 0;
        try {
            id = Integer.parseInt(textid.getText().toString());
        } catch (Exception e) {
            display.setText("Numbers only!");
        }

        if (customerIds.contains(id)) {

            List<Integer> inactive = select.getUserInactiveAccountsHelper(id);

            if (inactive.isEmpty()) {
                display.setText("No inactive accounts of this customer.");
            } else {
                String s = "All inactive account ids are :";
                for (int inactiveId : inactive) {
                    s += " " + inactiveId;
                }
                display.setText(s);
            }
        } else {
            display.setText("Please input a customer id!");

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