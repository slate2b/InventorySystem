package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeleteProductActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "com.cs360.inventorysystem.product_id";
    public static final String EXTRA_ADAPTER_POS = "com.cs360.inventorysystem.adapter_pos";

    private TextView mProductNameText;
    private TextView mProductNumberText;

    private InventoryDatabase mInventoryDb;

    private Product mProduct;
    private long mProductId;
    private int mAdapterPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        mInventoryDb = InventoryDatabase.getInstance(getApplicationContext());

        // Hosting activity provides the subject of the questions to display
        Intent intent = getIntent();
        mProductId = intent.getLongExtra(EXTRA_PRODUCT_ID, -1);
        mAdapterPos = intent.getIntExtra(EXTRA_ADAPTER_POS, -1);

        mProduct = mInventoryDb.getProduct(mProductId);

        mProductNameText = findViewById(R.id.textViewDeleteProductName);
        mProductNumberText = findViewById(R.id.textViewDeleteProductNumber);

        Button mButtonCancel = findViewById(R.id.cancelDeleteProductButton);
        mButtonCancel.setOnClickListener(listener -> handleCancel());

        Button mButtonSave = findViewById(R.id.deleteProductButton);
        mButtonSave.setOnClickListener(listener -> handleDelete());

        mProductNameText.setText(mProduct.getProductName());
        mProductNumberText.setText(mProduct.getProductNumber());
    }

    /**
     * Deletes product from db, retrieves the productId, and sends the productId to the
     * inventory activity.
     */
    public void handleDelete() {
        // Send back the product ID
        Intent intent = new Intent(this, InventoryActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, mProduct.getProductId());
        setResult(RESULT_OK, intent);
        finish();
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