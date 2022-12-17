package com.cs360.inventorysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
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

    private SharedPreferences mSharedPreferences;

    RecyclerView.LayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    InventoryAdapter inventoryAdapter;
    ProductHolder productHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setTitle("Product Inventory");

        // Load shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get instance of the db
        mInventoryDb = InventoryDatabase.getInstance(getApplicationContext());

        // Find the inventory activity recycler view
        recyclerView = findViewById(R.id.inventoryRecyclerView);

        // Create a grid with a single column
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Load the inventory data
        inventoryAdapter = new InventoryAdapter(loadInventory());
        recyclerView.setAdapter(inventoryAdapter);

        // Create an SMS manager
        SmsManager mSmsManager = SmsManager.getDefault();
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

    // Not currently implemented
    @Override
    protected void onResume() {
        super.onResume();

        // Load preferences in case they changed
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
         * Triggers the contextual action bar, currently implemented to provide the user with the
         * option to delete product the selected product from the inventory.
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
            inventoryAdapter.notifyItemChanged(mSelectedProductPosition);

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
            productHolder = new ProductHolder(layoutInflater, parent);
            return productHolder;
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
            recyclerView.scrollToPosition(0);
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
            recyclerView.scrollToPosition(0);
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
     * Current implementation includes option to delete a product on a long-click.
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
                inventoryAdapter.removeProduct(mSelectedProduct);

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
            inventoryAdapter.notifyItemChanged(mSelectedProductPosition);
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
            inventoryAdapter.addProduct(newProduct);

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

            Toast.makeText(this, "Product quantity updated", Toast.LENGTH_SHORT).show();

            /*
            / NOTE: The current implementation of these notifications serves little practical
            / purpose, but it lays some groundwork for more effective implementations in potential
            / multi-user server-based releases of the app in the future.
            */
            // Show a Toast if quantity equals zero and if in-app notifications are turned on
            boolean inAppNotifications = mSharedPreferences.getBoolean(SettingsFragment.PREFERENCE_IN_APP, false);
            if (inAppNotifications && updatedQuantity == 0) {
                Toast.makeText(this, productToUpdate.getProductName() +
                        " is now out of stock", Toast.LENGTH_SHORT).show();
            }

            /*
            / NOTE: The current implementation of these notifications serves little practical
            / purpose, but it lays some groundwork for more effective implementations in potential
            / multi-user server-based releases of the app in the future.
            */
            // Send an SMS message if quantity is zero, SMS preference is true, phone is not empty
            boolean smsNotifications = mSharedPreferences.getBoolean(SettingsFragment.PREFERENCE_SMS, false);
            String mPhone = mSharedPreferences.getString(SettingsFragment.PREFERENCE_PHONE, "");
            if (smsNotifications && !mPhone.isEmpty() && updatedQuantity == 0) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    String smsMessageText =
                            "Product Name: " + productToUpdate.getProductName() +
                            " / Product Number: " + productToUpdate.getProductNumber() +
                            " is now out of stock.";
                    smsManager.sendTextMessage(mPhone, null, smsMessageText, null, null);
                    Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e) {
                    Toast.makeText(this, "SMS failed", Toast.LENGTH_SHORT).show();
                }
            }

            // Reload the activity to pull updated data from the db and update the recycler view
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
        }
    }
}