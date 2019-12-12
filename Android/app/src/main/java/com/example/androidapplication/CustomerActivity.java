package com.example.androidapplication;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.os.Bundle;
import com.b07.database.DatabaseInsertHelper;
import com.b07.database.DatabaseSelectHelper;
import com.b07.database.DatabaseUpdateHelper;
import com.b07.inventory.InventoryImpl;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;
import com.b07.inventory.Item;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Open customer activity.
 */
public class CustomerActivity extends AppCompatActivity {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.pineapple_1, R.drawable.pineapple_2, R.drawable.pineapple_3,
            R.drawable.pineapple_4};
    ImageListener imageListener = new ImageListener() {

        @Override

        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }

    };

    private Button viewCart;
    private Button addItem;
    private Button viewPrice;
    private Button removeItem;
    private Button checkOut;
    private Button restoreCart;
    private Button saveCart;
    private Button exit;
    private ShoppingCart shopCart;
    private int customerId;
    private List<Integer> restored = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        Bundle extras = getIntent().getExtras();
        int value = -1;
        if (extras != null) {
            value = extras.getInt("Username");
            this.customerId = value;
        }

        DatabaseSelectHelper selectDb = new DatabaseSelectHelper(this);
        this.shopCart = new ShoppingCart((Customer) selectDb.getUserDetailsHelper(this.customerId),
                this);

        viewCart = findViewById(R.id.button_items_in_cart);
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewCart();
            }
        });

        addItem = findViewById(R.id.button_add_item_to_cart);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItem();
            }
        });

        viewPrice = findViewById(R.id.button_total_price);
        viewPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewTotal();
            }
        });

        removeItem = findViewById(R.id.button_remove_item_from_cart);
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemoveItem();
            }
        });

        checkOut = findViewById(R.id.button_check_out);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCheckOut();
            }
        });

        restoreCart = findViewById(R.id.button_restore_shopping_cart);
        restoreCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRestoreCart();
            }
        });

        saveCart = findViewById(R.id.button_save_shopping_cart);
        saveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSaveCart();
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
     * Open view cart activity.
     */
    public void openViewCart() {
        Intent intent = new Intent(this, ActivityViewCart.class);
        List<Item> items = this.shopCart.getItems();
        List<String> itemPrice = new ArrayList<String>();
        List<Integer> itemQuan = new ArrayList<Integer>();
        List<String> itemNames = new ArrayList<String>();
        for (int i = 0; i < items.size(); i++) {
            itemNames.add(items.get(i).getName());
            itemQuan.add(this.shopCart.getItemQty(items.get(i)));
            itemPrice.add(items.get(i).getPrice().multiply(
                    new BigDecimal(this.shopCart.getItemQty(items.get(i)))).toString());
        }
        intent.putStringArrayListExtra("ITEMS", (ArrayList) itemNames);
        intent.putIntegerArrayListExtra("QUANTITY", (ArrayList) itemQuan);
        intent.putStringArrayListExtra("ITEMPRICE", (ArrayList) itemPrice);
        startActivity(intent);
    }

    /**
     * Open add item to cart activity.
     */
    public void openAddItem() {
        Intent intent = new Intent(this, ActivityAddItem.class);
        DatabaseSelectHelper selectDb = new DatabaseSelectHelper(this);
        InventoryImpl inventory = (InventoryImpl) selectDb.getInventoryHelper();
        HashMap<Item, Integer> itemMap = inventory.getItemMap();
        List<Integer> itemIds = new ArrayList<Integer>();
        List<String> itemNames = new ArrayList<String>();
        List<String> prices = new ArrayList<String>();
        for (Map.Entry<Item, Integer> entry : itemMap.entrySet()) {
            itemNames.add(entry.getKey().getName());
            itemIds.add(entry.getKey().getId());
            prices.add(entry.getKey().getPrice().toString());
        }
        intent.putIntegerArrayListExtra("ITEMIDS", (ArrayList) itemIds);
        intent.putStringArrayListExtra("ITEMNAMES", (ArrayList) itemNames);
        intent.putStringArrayListExtra("ITEMPRICE", (ArrayList) prices);
        startActivityForResult(intent, 1);
    }

    /**
     * Open view total activity.
     */
    public void openViewTotal() {
        Intent intent = new Intent(this, ActivityTotalPrice.class);
        BigDecimal total = this.shopCart.getTotal();
        intent.putExtra("TOTAL", total.toString());
        startActivity(intent);
    }

    /**
     * Open remove item from cart activity.
     */
    public void openRemoveItem() {
        Intent intent = new Intent(this, ActivityRemoveItem.class);
        List<Item> items = this.shopCart.getItems();
        List<Integer> itemQuan = new ArrayList<Integer>();
        List<String> itemNames = new ArrayList<String>();
        List<Integer> itemIds = new ArrayList<Integer>();
        List<String> itemPrice = new ArrayList<String>();

        for (int i = 0; i < items.size(); i++) {
            itemNames.add(items.get(i).getName());
            itemQuan.add(this.shopCart.getItemQty(items.get(i)));
            itemIds.add(items.get(i).getId());
            itemPrice.add(items.get(i).getPrice().multiply(
                    new BigDecimal(this.shopCart.getItemQty(items.get(i)))).toString());
        }
        intent.putStringArrayListExtra("ITEMS", (ArrayList) itemNames);
        intent.putIntegerArrayListExtra("QUANTITY", (ArrayList) itemQuan);
        intent.putIntegerArrayListExtra("ITEMIDS", (ArrayList) itemIds);
        intent.putStringArrayListExtra("ITEMPRICE", (ArrayList) itemPrice);
        startActivityForResult(intent, 2);
    }

    /**
     * Open checkout activity.
     */
    public void openCheckOut() {
        Intent intent = new Intent(this, ActivityCheckOut.class);
        intent.putExtra("TOTAL", this.shopCart.getTotal().toString());
        List<Item> items = this.shopCart.getItems();
        List<Integer> itemQuan = new ArrayList<Integer>();
        List<String> itemNames = new ArrayList<String>();
        for (int i = 0; i < items.size(); i++) {
            itemNames.add(items.get(i).getName());
            itemQuan.add(this.shopCart.getItemQty(items.get(i)));
        }
        intent.putStringArrayListExtra("ITEMS", (ArrayList) itemNames);
        intent.putIntegerArrayListExtra("QUANTITY", (ArrayList) itemQuan);
        startActivityForResult(intent, 3);
    }

    /**
     * Open restore cart activity.
     */
    public void openRestoreCart() {
        DatabaseSelectHelper selectDb = new DatabaseSelectHelper(this);
        List<Integer> accounts = selectDb.getUserActiveAccountsHelper(this.customerId);
        Intent intent = new Intent(this, ActivityRestoreCart.class);
        intent.putIntegerArrayListExtra("ACCOUNTS", (ArrayList) accounts);
        startActivityForResult(intent, 4);
    }

    /**
     * Open save cart activity.
     */
    public void openSaveCart() {
        DatabaseSelectHelper selectDb = new DatabaseSelectHelper(this);
        List<Integer> accounts = selectDb.getUserActiveAccountsHelper(this.customerId);
        Intent intent = new Intent(this, ActivitySaveCart.class);
        intent.putIntegerArrayListExtra("ACCOUNTS", (ArrayList) accounts);
        startActivityForResult(intent, 5);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        DatabaseInsertHelper insertDb = new DatabaseInsertHelper(this);
        DatabaseSelectHelper selectDb = new DatabaseSelectHelper(this);
        DatabaseUpdateHelper updateDb = new DatabaseUpdateHelper(this);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int itemId = result.getIntExtra("ADDITEMID", 0);
                int itemQuan = result.getIntExtra("ADDQUANTITY", 0);
                selectDb = new DatabaseSelectHelper(this);
                if(selectDb.getItemHelper(itemId) != null){
                    this.shopCart.addItem(selectDb.getItemHelper(itemId), itemQuan);
                    Toast.makeText(this, "Successfully added item!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Invalid item!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                int itemId = result.getIntExtra("REMOVEITEMID", 0);
                int itemQuan = result.getIntExtra("REMOVEQUANTITY", 0);
                selectDb = new DatabaseSelectHelper(this);
                if(this.shopCart.getItems().contains(selectDb.getItemHelper(itemId))){
                    if(this.shopCart.getItemQty(selectDb.getItemHelper(itemId)) >= itemQuan){
                        this.shopCart.removeItem(selectDb.getItemHelper(itemId), itemQuan);
                        Toast.makeText(this, "Successfully removed item!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Cannot remove more than present!", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(this, "Invalid item!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                boolean success = this.shopCart.checkout();
                if (success) {
                    for (int i = 0; i < this.restored.size(); i++) {
                        updateDb.updateAccountStatusHelper(this.restored.get(i), false, this);
                    }
                    this.restored.clear(); // Remove all restored carts
                    Toast.makeText(this, "Successfully checked out!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Insufficient inventory cannot check out!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                int accountId = result.getIntExtra("RESTOREACCOUNTID", 0);
                if (selectDb.getUserActiveAccountsHelper(this.customerId).contains(accountId)) {
                    // VALID ACCOUNT ID CHOSEN
                    this.restored.add(accountId);
                    Item oneItem;
                    HashMap<Integer, Integer> itemsToQuan = selectDb
                            .getAccountDetailsHelper(accountId);
                    for (Map.Entry<Integer, Integer> entry : itemsToQuan.entrySet()) {
                        oneItem = selectDb.getItemHelper(entry.getKey());
                        this.shopCart.addItem(oneItem, entry.getValue());
                    }
                    Toast.makeText(this, "Successfully restored cart!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid account ID!!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                int accountId = result.getIntExtra("SAVEACCOUNTID", 0);
                if (selectDb.getUserActiveAccountsHelper(this.customerId).contains(accountId)) {
                    // VALID ACCOUNT ID CHOSEN
                    Item oneItem;
                    HashMap<Integer, Integer> itemToQuantity =
                            selectDb.getAccountDetailsHelper(accountId);
                    List<Integer> accountItems = new ArrayList<Integer>(itemToQuantity.keySet());
                    List<Item> cartItems = this.shopCart.getItems();
                    for (int i = 0; i < cartItems.size(); i++) {
                        oneItem = cartItems.get(i);
                        if (!accountItems.contains(oneItem.getId())) {
                            insertDb.insertAccountLineHelper(accountId, oneItem.getId(),
                                    this.shopCart.getItemQty(oneItem));
                        }
                    }
                    Toast.makeText(this, "Successfully saved cart!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid account ID!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Exit to login activity.
     */
    public void exitActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}


