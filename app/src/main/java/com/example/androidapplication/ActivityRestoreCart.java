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
 * Open restore cart activity.
 */
public class ActivityRestoreCart extends AppCompatActivity {

    private Button restoreCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_cart);

        Intent intent = getIntent();
        List<Integer> accoundIds = intent.getIntegerArrayListExtra("ACCOUNTS");
        displayAccounts(accoundIds);

        restoreCart = findViewById(R.id.action_submit_account_restore);
        restoreCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText itemText = (EditText) findViewById(R.id.save_accountId_restore);
                int accountId = -1;
                if (!itemText.getText().toString().equals("")) {
                    accountId = Integer.parseInt(itemText.getText().toString());
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("RESTOREACCOUNTID", accountId);
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
        TextView view = (TextView) findViewById(R.id.display_accounts_restore);
        EditText text = (EditText) findViewById(R.id.save_accountId_restore);
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
