package com.example.androidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLogImpl;
import java.util.HashMap;
import java.util.List;

/**
 * Open view book activity.
 */
public class ViewBookActivity extends AppCompatActivity {

    private Button exit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        exit = findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

        TextView display = findViewById(R.id.books);
        String book = "";
        com.b07.database.DatabaseSelectHelper select = new com.b07.database.DatabaseSelectHelper(
                this);

        SalesLogImpl salesLog = (SalesLogImpl) select.consolidateSalesLog();
        List<Sale> sales = salesLog.getSales();

        for (Sale sale : sales) {
            book += "Customer: " + sale.getUser().getName() + "\n";
            book += "Purchase Number: " + sale.getId() + "\n";
            book += "Total Purchase Price: " + sale.getTotalPrice() + "\n";
            book += "Itemized Breakdown: " + "\n";

            HashMap<Item, Integer> itemMap = sale.getItemMap();
            for (Item item : itemMap.keySet()) {
                book += item.getName() + ": " + itemMap.get(item) + "\n";
            }

            book += "---------------------------------------\n";
        }

        HashMap<Integer, Integer> itemMaps = salesLog.getItemMaps();
        for (int itemId : itemMaps.keySet()) {
            Item item = select.getItemHelper(itemId);
            book += "Number of " + item.getName() + " sold: " + itemMaps.get(itemId) + "\n";
        }
        book += "TOTAL SALES: " + salesLog.getGrandTotal() + "\n";

        display.setText(book);
    }

    /**
     * Exit to admin activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}
