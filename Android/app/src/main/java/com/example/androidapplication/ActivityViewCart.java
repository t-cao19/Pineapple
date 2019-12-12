package com.example.androidapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Open view cart activity.
 */
public class ActivityViewCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        TextView view = findViewById(R.id.cart_items);
        List<String> itemNames = getIntent().getStringArrayListExtra("ITEMS");
        List<Integer> itemQuan = getIntent().getIntegerArrayListExtra("QUANTITY");
        List<String> itemPrices = getIntent().getStringArrayListExtra("ITEMPRICE");

        if (itemNames.isEmpty()) {
            view.setText("Current Shopping Cart is Empty");
        } else {
            view.setText("");
            for (int i = 0; i < itemNames.size(); i++) {
                view.setText(view.getText() + "Name: " + itemNames.get(i) + "\n  Quantity: " +
                        itemQuan.get(i) + "\n Price: " + itemPrices.get(i) +
                        "\n-----------------------------\n");
            }
        }
    }
}
