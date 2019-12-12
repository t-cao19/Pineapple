package com.example.androidapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.b07.database.DatabaseSelectHelper;
import com.b07.database.DatabaseUpdateHelper;
import com.b07.inventory.Item;
import java.util.HashMap;

/**
 * Open restock inventory activity.
 */
public class RestockInventoryActivity extends AppCompatActivity {

    int quantity = 0;
    private Button restock;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_inventory);

        listInventory();

        restock = findViewById(R.id.action_restock);
        restock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restockActivity();
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
     * List all inventory items.
     */
    public void listInventory() {
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);
        HashMap<Item, Integer> invMap = select.getInventoryHelper().getItemMap();

        TextView list = findViewById(R.id.item_list);
        list.setText("");

        for (Item item : invMap.keySet()) {
            list.setText(list.getText() + "  ID: " + item.getId() + "  NAME: " + item.getName() +
                    " QTY: " + invMap.get(item) + "\n");
        }
    }

    /**
     * Exit to employee activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, EmployeeActivity.class);
        startActivity(intent);
    }

    /**
     * Restock an item in inventory.
     */
    public void restockActivity() {
        DatabaseSelectHelper select = new DatabaseSelectHelper(this);
        DatabaseUpdateHelper update = new DatabaseUpdateHelper(this);
        TextView display = findViewById(R.id.confirmation);

        EditText textQuan = findViewById(R.id.quantity);
        try {
            quantity = Integer.parseInt(textQuan.getText().toString());
        } catch (Exception e) {
            display.setText("Invalid quantity!");
            return;
        }

        EditText id = findViewById(R.id.itemId);
        int itemId = -1;
        try {
            itemId = Integer.parseInt(id.getText().toString());
        } catch (Exception e) {
            display.setText("Invalid item id!");
            return;
        }

        Item item = select.getItemHelper(itemId);

        if (item == null) {
            display.setText("No such item!");
        } else if (quantity <= 0) {
            display.setText("Can't restock this amount!");
        } else {
            boolean restocked = update.updateInventoryQuantityHelper(
                    quantity + select.getInventoryQuantityHelper(itemId), itemId);
            if (restocked) {
                display.setText("Successfully restocked!");
                id.setText("");
                textQuan.setText("0");
                listInventory(); //update the list
            } else {
                display.setText("Restock unsuccessful!");
            }
        }
    }

    /**
     * Increase quantity to add.
     *
     * @param view the view
     */
    public void increaseInteger(View view) {
        quantity = quantity + 1;
        display(quantity);

    }

    /**
     * Decrease the quantity to add.
     *
     * @param view the view
     */
    public void decreaseInteger(View view) {
        if (quantity != 0) {
            quantity = quantity - 1;
            display(quantity);
        }
    }

    /**
     * Display the quantity to add.
     *
     * @param number the quantity to add
     */
    private void display(int number) {
        EditText displayInteger = findViewById(R.id.quantity);
        displayInteger.setText("" + number);
    }
}
