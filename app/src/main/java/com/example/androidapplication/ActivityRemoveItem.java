package com.example.androidapplication;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;

/**
 * Open remove item activity.
 */
public class ActivityRemoveItem extends AppCompatActivity {

    private Button removeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        TextView items = findViewById(R.id.remove_list_all_items);

        Intent intent = getIntent();
        List<String> itemNames = intent.getStringArrayListExtra("ITEMS");
        List<Integer> itemQuan = intent.getIntegerArrayListExtra("QUANTITY");
        List<Integer> itemIds = intent.getIntegerArrayListExtra("ITEMIDS");
        List<String> itemPrices = intent.getStringArrayListExtra("ITEMPRICE");
        if (itemNames.isEmpty()) {
            items.setText("Current Shopping Cart is Empty");
        } else {
            items.setText("");
            for (int i = 0; i < itemNames.size(); i++) {
                items.setText(items.getText() + "Name: " + itemNames.get(i) + "\n Item ID: "
                        + itemIds.get(i) + "\n  Quantity: " +
                        itemQuan.get(i) + "\n Price: " + itemPrices.get(i) +
                        "\n-----------------------------\n");
            }
        }

        removeItem = findViewById(R.id.action_remove_item);
        removeItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemText = (EditText) findViewById(R.id.remove_itemId);
                int itemId = -1;
                if (!itemText.getText().toString().equals("")) {
                    itemId = Integer.parseInt(itemText.getText().toString());
                }

                EditText quanText = (EditText) findViewById(R.id.remove_itemQuantity);
                int itemQuantity = -1;
                if (!quanText.getText().toString().equals("")) {
                    itemQuantity = Integer.parseInt(quanText.getText().toString());
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("REMOVEITEMID", itemId);
                resultIntent.putExtra("REMOVEQUANTITY", itemQuantity);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
