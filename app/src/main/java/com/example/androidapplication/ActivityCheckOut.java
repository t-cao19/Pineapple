package com.example.androidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Open check out activity.
 */
public class ActivityCheckOut extends AppCompatActivity {

    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Intent intent = getIntent();
        TextView price = findViewById(R.id.checkout_total);
        price.setText(intent.getStringExtra("TOTAL"));

        List<String> itemNames = intent.getStringArrayListExtra("ITEMS");
        List<Integer> itemQuan = intent.getIntegerArrayListExtra("QUANTITY");

        TextView checkOutItems = findViewById(R.id.checkout_items);
        checkOutItems.setText("");
        if (itemNames.isEmpty()) {
            checkOutItems.setText("Cart Is Empty!");
        } else {
            int i;
            for (i = 0; i < itemNames.size(); i++) {
                checkOutItems.setText(checkOutItems.getText() + "Item: " + itemNames.get(i) + "\n" +
                        "Quantity: " + itemQuan.get(i) + "\n" +
                        "-----------------------------\n");
            }
        }

        confirm = findViewById(R.id.action_confirm_checkout);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
