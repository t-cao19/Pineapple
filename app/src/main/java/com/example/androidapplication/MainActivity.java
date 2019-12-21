package com.example.androidapplication;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import com.b07.database.DatabaseInsertHelper;
import com.b07.database.DatabaseSelectHelper;
import com.b07.inventory.ItemTypes;
import java.util.HashMap;
import java.math.BigDecimal;

/**
 * Open main activity.
 */
public class MainActivity extends AppCompatActivity {

    private Button newAdmin;
    private Button newEmp;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDatabase();

        exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

        newAdmin = findViewById(R.id.button_admin_create);
        newAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewAdminActivity();
            }
        });

        newEmp = findViewById(R.id.button_employee_create);
        newEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewEmployeeActivity();
            }
        });
    }

    /**
     * Create the database if database does not exist.
     */
    public void initializeDatabase() {
        DatabaseInsertHelper insert = new DatabaseInsertHelper(this);
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);

        HashMap<Integer, String> roles = select.getRolesHelper();
        if (roles.isEmpty()) { // has not been initialized before
            insert.insertRoleHelper("ADMIN");
            insert.insertRoleHelper("EMPLOYEE");
            insert.insertRoleHelper("CUSTOMER");

            long reg = insert.insertItemHelper(ItemTypes.REGULAR_PINEAPPLE.name(),
                    new BigDecimal("5.99"));
            long red = insert.insertItemHelper(ItemTypes.RED_HUED_PINEAPPLE.name(),
                    new BigDecimal("49.99"));
            long blueMini = insert.insertItemHelper(ItemTypes.BLUE_PINEAPPLE_MINI.name(),
                    new BigDecimal("24.99"));
            long blueReg = insert.insertItemHelper(ItemTypes.BLUE_PINEAPPLE_REGULAR.name(),
                    new BigDecimal("39.99"));
            long nuc = insert.insertItemHelper(ItemTypes.NUCLEAR_PINEAPPLE.name(),
                    new BigDecimal("66.66"));

            insert.insertInventoryHelper((int) reg, 123);
            insert.insertInventoryHelper((int) red, 20);
            insert.insertInventoryHelper((int) blueMini, 75);
            insert.insertInventoryHelper((int) blueReg, 32);
            insert.insertInventoryHelper((int) nuc, 5);
        }
    }

    /**
     * Open new admin activity to create new admin.
     */
    public void openNewAdminActivity() {
        Intent intent = new Intent(this, NewAdminActivity.class);
        startActivity(intent);
    }

    /**
     * Open new employee activity to create new employee.
     */
    public void openNewEmployeeActivity() {
        Intent intent = new Intent(this, NewEmployeeActivity.class);
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
