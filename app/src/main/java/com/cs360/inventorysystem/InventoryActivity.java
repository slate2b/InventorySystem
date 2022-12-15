package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ADD_PRODUCT = 0;
    private final int REQUEST_CODE_DELETE_PRODUCT = 1;
    private final int REQUEST_CODE_UPDATE_QUANTITY = 2;

    public static final String EXTRA_PRODUCT_ID = "com.cs360.inventorysystem.product_id";

    private InventoryDatabase mInventoryDb;

    private List<Product> mProductList;

    private String mAddProductName;
    private String mAddProductNumber;
    private long mAddProductQuantity;

    RecyclerView.LayoutManager mGridLayoutManager;
    RecyclerView mRecyclerView;
    InventoryAdapter mInventoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mAddProductName = "";
        mAddProductNumber = "";
        mAddProductQuantity = -1;

        // Get instance of the db
        mInventoryDb = InventoryDatabase.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.inventoryRecyclerView);

        // Create a grid with a single column
        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        // Load the inventory data
        mInventoryAdapter = new InventoryAdapter(loadInventory());
        mRecyclerView.setAdapter(mInventoryAdapter);

    }

    private List<Product> loadInventory() {
        return mInventoryDb.getProducts();
    }

    private class ProductHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private Product mProduct;
        private TextView mNameText;
        private TextView mNumberText;
        private TextView mQuantityText;
        private ImageButton mButtonUpdateQuantity;

        public ProductHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mNameText = itemView.findViewById(R.id.nameTextView);
            mNumberText = itemView.findViewById(R.id.numberTextView);
            mQuantityText = itemView.findViewById(R.id.quantityTextView);

            mButtonUpdateQuantity = itemView.findViewById(R.id.updateQuantityButton);
            mButtonUpdateQuantity.setOnClickListener(listener -> handleUpdateQuantity());
        }

        public void bind(Product product, int position) {
            mProduct = product;
            mNameText.setText(product.getProductName());
            mNumberText.setText(product.getProductNumber());
            mQuantityText.setText(String.valueOf(product.getProductQuantity()));
        }

        // Not currently implemented
        @Override
        public void onClick(View view) {}

        /**
         * Starts the delete product activity when user long clicks on a product
         * @param view - The card view which was long clicked
         * @return - Returns true when complete
         */
        @Override
        public boolean onLongClick(View view) {
            Intent intent = new Intent(InventoryActivity.this, DeleteProductActivity.class);
            intent.putExtra(EXTRA_PRODUCT_ID, mProduct.getProductId());
            startActivityForResult(intent, REQUEST_CODE_DELETE_PRODUCT);
            return true;
        }

        /**
         * Handles updating product quantity when user presses the update quantity button
         */
        public void handleUpdateQuantity() {
            Intent intent = new Intent(InventoryActivity.this, UpdateQuantityActivity.class);
            intent.putExtra(EXTRA_PRODUCT_ID, mProduct.getProductId());
            startActivityForResult(intent, REQUEST_CODE_UPDATE_QUANTITY);
        }
    }

    /**
     * The RecyclerView Adapter for the products in InventoryActivity
     */
    private class InventoryAdapter extends RecyclerView.Adapter<ProductHolder> {

        public InventoryAdapter(List<Product> products) {
            mProductList = products;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new ProductHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, int position){
            holder.bind(mProductList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mProductList.size();
        }
    }

    /**
     * Handles when user clicks the Add Product FAB
     * @param view - The view
     */
    public void handleAddProductClick(View view) {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT);
    }

    /**
     * Handles results from other activities
     * @param requestCode - The request code
     * @param resultCode - The result code
     * @param data - The intent data from the other activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // When receiving results from the add product activity
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_PRODUCT
        ) {
            // Retrieve the product object which was just created
            long productId = data.getLongExtra(AddProductActivity.EXTRA_PRODUCT_ID, -1);
            Product newProduct = mInventoryDb.getProduct(productId);

            // Add the new product to the product list
            mProductList.add(newProduct);

            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
        }

        // When receiving results from the delete product activity
        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DELETE_PRODUCT
        ) {
            // Get product to delete
            long productId = data.getLongExtra(DeleteProductActivity.EXTRA_PRODUCT_ID, -1);
            Product productToDelete = mInventoryDb.getProduct(productId);

            // Pass product id to inventory db to delete the product record
            mInventoryDb.deleteProduct(productId);

            // Remove product from the product list
            mProductList.remove(productToDelete);

            // Refresh the activity
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
        }

        // When receiving results from the update quantity activity
        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_UPDATE_QUANTITY
        ) {
            // Get product to update
            long productId = data.getLongExtra(UpdateQuantityActivity.EXTRA_PRODUCT_ID, -1);
            Product productToUpdate = mInventoryDb.getProduct(productId);

            // Get updated quantity
            long updatedQuantity = data.getLongExtra(UpdateQuantityActivity.EXTRA_PRODUCT_QUANTITY, -1);

            // Pass product and updated quantity to inventory db to update the product record
            mInventoryDb.updateQuantity(productToUpdate, updatedQuantity);

            // Refresh the activity
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Product quantity updated", Toast.LENGTH_SHORT).show();
        }
    }
}