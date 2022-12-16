package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateQuantityActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "com.cs360.inventorysystem.product_id";
    public static final String EXTRA_PRODUCT_QUANTITY = "com.cs360.inventorysystem.product_quantity";


    private TextView mProductNameText;
    private TextView mProductNumberText;
    private TextView mCurrentQuantityText;
    private EditText mQuantityEditText;

    private InventoryDatabase mInventoryDb;

    private Product mProduct;
    private long mProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_quantity);

        setTitle("Update Product Quantity");

        mInventoryDb = InventoryDatabase.getInstance(getApplicationContext());

        // Get the product id from the hosting activity
        Intent intent = getIntent();
        mProductId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1);

        mProduct = mInventoryDb.getProduct(mProductId);

        mProductNameText = findViewById(R.id.textViewQuantityProductName);
        mProductNumberText = findViewById(R.id.textViewQuantityProductNumber);
        mCurrentQuantityText = findViewById(R.id.textViewQuantityCurrent);

        mQuantityEditText = findViewById(R.id.editTextUpdateQuantity);

        Button mButtonCancel = findViewById(R.id.cancelQuantityButton);
        mButtonCancel.setOnClickListener(listener -> handleCancel());

        Button mButtonSave = findViewById(R.id.saveQuantityButton);
        mButtonSave.setOnClickListener(listener -> handleUpdate());

        mProductNameText.setText("Product:  " + mProduct.getProductName());
        mProductNumberText.setText("Number:  " + mProduct.getProductNumber());
        mCurrentQuantityText.setText("Current Quantity:  " + mProduct.getProductQuantity());
    }

    /**
     * Send the product id and updated quantity back to the inventory activity to handle update.
     */
    public void handleUpdate() {
        String mQuantityString = mQuantityEditText.getText().toString();

        if (mQuantityString.isEmpty()) {
            Toast.makeText(this, "Please enter the updated product quantity", Toast.LENGTH_SHORT).show();
        }
        else {
            long mQuantity = Long.parseLong(mQuantityString);

            // Send back the product id and updated quantity
            Intent intent = new Intent(this, InventoryActivity.class);
            intent.putExtra(EXTRA_PRODUCT_ID, mProduct.getProductId());
            intent.putExtra(EXTRA_PRODUCT_QUANTITY, mQuantity);
            setResult(RESULT_OK, intent);
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