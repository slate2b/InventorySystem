package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddProductActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "com.cs360.inventorysystem.question_id";

    private EditText mProductNameText;
    private EditText mProductNumberText;
    private EditText mProductQuantityText;
    private TextView mMessage;

    private InventoryDatabase mInventoryDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mProductNameText = findViewById(R.id.editTextProductName);
        mProductNumberText = findViewById(R.id.editTextProductNumber);
        mProductQuantityText = findViewById(R.id.editTextProductQuantity);
        mMessage = findViewById(R.id.textViewAddProductMessage);

        mInventoryDb = InventoryDatabase.getInstance(getApplicationContext());

        Button mButtonCancel = findViewById(R.id.cancelAddProductButton);
        mButtonCancel.setOnClickListener(listener -> handleCancel());

        Button mButtonSave = findViewById(R.id.saveAddProductButton);
        mButtonSave.setOnClickListener(listener -> handleSave());
    }

    /**
     * Adds new product to db, retrieves the productId, and sends the productId to the
     * inventory activity.
     */
    public void handleSave() {

        String mName = mProductNameText.getText().toString();
        String mNumber = mProductNumberText.getText().toString();
        String mQuantity = mProductQuantityText.getText().toString();

        if (mName.isEmpty()) {
            mMessage.setText("Please enter the name of the product.");
        }
        else if (mNumber.isEmpty()) {
            mMessage.setText("Please enter the product number.");
        }
        else if (mQuantity.isEmpty()) {
            mMessage.setText("Please enter the quantity to be added to inventory.");
        }
        else {
            // Create new product
            Product mProduct = new Product(
                    mName,
                    mNumber,
                    Long.parseLong(mQuantity));

            // Pass user input to inventory db to create new product record
            mInventoryDb.addProduct(mProduct);

            // Send back the product ID
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PRODUCT_ID, mProduct.getProductId());
            setResult(RESULT_OK, intent);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Handles app flow when user clicks the cancel button
     */
    private void handleCancel() {
        // Navigate back to the inventory activity
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }
}