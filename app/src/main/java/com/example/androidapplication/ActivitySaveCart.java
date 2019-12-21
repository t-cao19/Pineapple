package com.example.androidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Open save cart activity.
 */
public class ActivitySaveCart extends AppCompatActivity {

    private Button saveCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_cart);

        Intent intent = getIntent();
        List<Integer> accounts = intent.getIntegerArrayListExtra("ACCOUNTS");
        displayAccounts(accounts);

        saveCart = findViewById(R.id.action_submit_account);
        saveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemText = (EditText) findViewById(R.id.save_accountId);
                int accountId = -1;
                if (!itemText.getText().toString().equals("")) {
                    accountId = Integer.parseInt(itemText.getText().toString());
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("SAVEACCOUNTID", accountId);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    /**
     * Get all active accounts of the user.
     *
     * @param accounts the accounts of the user
     */
    public void displayAccounts(List<Integer> accounts) {
        TextView view = (TextView) findViewById(R.id.display_accounts);
        EditText text = (EditText) findViewById(R.id.save_accountId);
        if (accounts.isEmpty()) {
            view.setText("No Current Active Accounts");
            text.setVisibility(View.GONE);
        } else {
            view.setText("");
            for (int i = 0; i < accounts.size(); i++) {
                view.setText(view.getText() + "Account ID: " + accounts.get(i) + "\n");
            }
        }
    }
}
