package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_NAME = "com.cs360.inventorysystem.product_name";
    public static final String EXTRA_PRODUCT_NUMBER = "com.cs360.inventorysystem.product_number";
    public static final String EXTRA_PRODUCT_QUANTITY = "com.cs360.inventorysystem.product_quantity";


    private EditText mProductNameText;
    private EditText mProductNumberText;
    private EditText mProductQuantityText;
    private TextView mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setTitle("Add New Product");

        mProductNameText = findViewById(R.id.editTextProductName);
        mProductNumberText = findViewById(R.id.editTextProductNumber);
        mProductQuantityText = findViewById(R.id.editTextProductQuantity);
        mMessage = findViewById(R.id.textViewAddProductMessage);

        Button mButtonCancel = findViewById(R.id.cancelAddProductButton);
        mButtonCancel.setOnClickListener(listener -> handleCancel());

        Button mButtonSave = findViewById(R.id.saveAddProductButton);
        mButtonSave.setOnClickListener(listener -> handleSave());
    }

    /**
     * Collects user input and sends the data back to the inventory activity so product can be
     * created.
     */
    public void handleSave() {

        String name = mProductNameText.getText().toString();
        String number = mProductNumberText.getText().toString();
        String quantity = mProductQuantityText.getText().toString();
        Long quantityLong = Long.parseLong(quantity);

        if (name.isEmpty()) {
            mMessage.setText(R.string.enter_product_name);
            Toast.makeText(this, "Please enter the name of the product", Toast.LENGTH_SHORT).show();
        }
        else if (number.isEmpty()) {
            mMessage.setText(R.string.enter_product_number);
            Toast.makeText(this, "Please enter the product number", Toast.LENGTH_SHORT).show();
        }
        else if (quantity.isEmpty()) {
            mMessage.setText(R.string.enter_quantity);
            Toast.makeText(this, "Please enter the product quantity", Toast.LENGTH_SHORT).show();
        }
        else {

            // Send back the product ID
            Intent intent = new Intent(this, InventoryActivity.class);
            intent.putExtra(EXTRA_PRODUCT_NAME, name);
            intent.putExtra(EXTRA_PRODUCT_NUMBER, number);
            intent.putExtra(EXTRA_PRODUCT_QUANTITY, quantityLong);
            setResult(RESULT_OK, intent);
            //startActivity(intent);
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