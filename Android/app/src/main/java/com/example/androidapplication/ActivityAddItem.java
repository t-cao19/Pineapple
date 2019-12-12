package com.example.androidapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;

/**
 * Open add item activity.
 */
public class ActivityAddItem extends AppCompatActivity {

    private Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        TextView items = findViewById(R.id.list_all_items);

        Intent intent = getIntent();
        List<Integer> itemIds = intent.getIntegerArrayListExtra("ITEMIDS");
        List<String> itemNames = intent.getStringArrayListExtra("ITEMNAMES");
        List<String> itemPrices = intent.getStringArrayListExtra("ITEMPRICE");

        items.setText("");

        for (int i = 0; i < itemIds.size(); i++) {
            items.setText(items.getText() + "Item ID: " + itemIds.get(i) + "\n" + "Item Name: " +
                    itemNames.get(i) + "\n" + "Item Price: " + itemPrices.get(i) +
                    "\n-----------------------------\n");
        }

        addItem = findViewById(R.id.action_add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText itemText = (EditText) findViewById(R.id.add_itemId);
                int itemId = -1;
                if (!itemText.getText().toString().equals("")) {
                    itemId = Integer.parseInt(itemText.getText().toString());
                }
                EditText quanText = (EditText) findViewById(R.id.add_itemQuantity);
                int itemQuantity = -1;
                if (!quanText.getText().toString().equals("")) {
                    itemQuantity = Integer.parseInt(quanText.getText().toString());
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ADDITEMID", itemId);
                resultIntent.putExtra("ADDQUANTITY", itemQuantity);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

}
