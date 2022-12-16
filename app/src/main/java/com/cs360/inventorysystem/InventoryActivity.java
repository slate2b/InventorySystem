package com.cs360.inventorysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ADD_PRODUCT = 0;
    private final int REQUEST_CODE_UPDATE_QUANTITY = 1;

    public static final String EXTRA_PRODUCT_ID = "com.cs360.inventorysystem.product_id";

    private InventoryDatabase mInventoryDb;

    private List<Product> mProductList;

    private Product mSelectedProduct;
    private int mSelectedProductPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;

    private boolean mSmsNotifications;
    private boolean mInAppNotifications;
    private SharedPreferences mSharedPreferences;

    RecyclerView.LayoutManager mGridLayoutManager;
    RecyclerView mRecyclerView;
    InventoryAdapter mInventoryAdapter;
    ProductHolder mProductHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSmsNotifications = mSharedPreferences.getBoolean(SettingsFragment.PREFERENCE_SMS, false);
        if (mSmsNotifications) {
            //TODO IMPLEMENT SMS NOTIFICATIONS
        }
        mInAppNotifications = mSharedPreferences.getBoolean(SettingsFragment.PREFERENCE_IN_APP, false);
        if (mInAppNotifications) {
            //TODO IMPLEMENT INAPP NOTIFICATIONS
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setTitle("Product Inventory");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inventory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Using a switch case to prepare for additional menu items in future releases
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(InventoryActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If SMS preference changed, recreate the activity so preference is applied
        boolean smsNotifications = mSharedPreferences.getBoolean(SettingsFragment.PREFERENCE_SMS, false);
        if (smsNotifications != mSmsNotifications) {
            recreate();
        }

        // If In-app preference changed, recreate the activity so preference is applied
        boolean inAppNotifications = mSharedPreferences.getBoolean(SettingsFragment.PREFERENCE_IN_APP, false);
        if (inAppNotifications != mInAppNotifications) {
            recreate();
        }

        // Reload products in case preferences changed
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
         * Triggers the contextual action bar, currently implemented to delete products from the
         * inventory.
         * @param view - The view which was clicked
         * @return - true if action mode is null, false if action mode is not null.
         */
        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            }

            mSelectedProduct = mProduct;
            mSelectedProductPosition = getAbsoluteAdapterPosition();

            // Trigger the adapter's bind method for the selected item
            mInventoryAdapter.notifyItemChanged(mSelectedProductPosition);

            // Show the contextual action bar
            mActionMode = InventoryActivity.this.startActionMode(mActionModeCallback);

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

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            mProductHolder = new ProductHolder(layoutInflater, parent);
            return mProductHolder;
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, int position){
            holder.bind(mProductList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mProductList.size();
        }

        /**
         * Adds a new product to the class member product list
         * @param product - The product to be added
         */
        public void addProduct(Product product) {
            // Add the new product at the beginning of the product list
            mProductList.add(0, product);

            // Let the adapter know a product was inserted at index zero
            notifyItemInserted(0);

            // Scroll to the top of the recycler view
            mRecyclerView.scrollToPosition(0);
        }

        /**
         * Removes selected product from the class member product list
         * @param product - The product to be removed
         */
        public void removeProduct(Product product) {
            // Retrieve index of the product to be deleted
            int index = mProductList.indexOf(product);

            // Check to make sure index is valid
            if (index >= 0 && index < mProductList.size()) {

                // Remove the product from the product list
                mProductList.remove(index);

                // Tell the adapter that a product was removed and pass the index
                notifyItemRemoved(index);
            }

            // Scroll to the top of the recycler view
            mRecyclerView.scrollToPosition(0);
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
     * Callback to handle actions selected in the contextual action bar. Called by the RecyclerView's
     * product holder onLongClick method.
     */
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate the contextual action bar
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            boolean isDeleted = false;

            try {
                // Delete the product from the database
                mInventoryDb.deleteProduct(mSelectedProduct.getProductId());

                // Remove the product from the RecyclerView adapter
                mInventoryAdapter.removeProduct(mSelectedProduct);

                // Close the contextual action bar
                mode.finish();

                isDeleted = true;
            }
            catch(Exception e) {
                System.out.println("Failed to delete product " + mSelectedProduct.getProductName());
            }
            return isDeleted;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Set action mode back to null
            mActionMode = null;

            // Clear selected item when the contextual action bar closes
            mInventoryAdapter.notifyItemChanged(mSelectedProductPosition);
            mSelectedProductPosition = RecyclerView.NO_POSITION;
        }
    };

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
            // Retrieve the data for the new product
            String productName = data.getStringExtra(AddProductActivity.EXTRA_PRODUCT_NAME);
            String productNumber = data.getStringExtra(AddProductActivity.EXTRA_PRODUCT_NUMBER);
            long productQuantity = data.getLongExtra(AddProductActivity.EXTRA_PRODUCT_QUANTITY, -1);

            // Create new product
            Product newProduct = new Product(
                    productName,
                    productNumber,
                    productQuantity);

            // Pass new product to inventory db to create new product record
            mInventoryDb.addProduct(newProduct);

            // Add the new product to the product list
            mInventoryAdapter.addProduct(newProduct);

            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
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

            // Reload the activity to pull updated data from the db and update the recycler view
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Product quantity updated", Toast.LENGTH_SHORT).show();
        }
    }
}