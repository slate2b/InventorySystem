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
    public static final String EXTRA_ADAPTER_POS = "com.cs360.inventorysystem.adapter_pos";

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
            //mButtonUpdateQuantity.setOnClickListener(this);

            mNameText = itemView.findViewById(R.id.nameTextView);
            mNumberText = itemView.findViewById(R.id.numberTextView);
            mQuantityText = itemView.findViewById(R.id.quantityTextView);

            mButtonUpdateQuantity = itemView.findViewById(R.id.updateQuantityButton);

            //TODO IMPLEMENT HANDLE UPDATE QUANTITY AND UPDATE QUANTITY ACTIVITY
            //mButtonUpdateQuantity.setOnClickListener(listener -> handleUpdateQuantity());
        }

        public void bind(Product product, int position) {
            mProduct = product;
            mNameText.setText(product.getProductName());
            mNumberText.setText(product.getProductNumber());
            mQuantityText.setText(String.valueOf(product.getProductQuantity()));
        }

        //TODO Create a method to handle when a user clicks on a product card
        @Override
        public void onClick(View view) {

            /*
            // Start QuestionActivity, indicating what subject was clicked
            Intent intent = new Intent(SubjectActivity.this, QuestionActivity.class);
            intent.putExtra(QuestionActivity.EXTRA_SUBJECT, mSubject.getText());
            startActivity(intent);
            */
        }

        @Override
        public boolean onLongClick(View view) {
            int adapterPos = getBindingAdapterPosition();
            Intent intent = new Intent(InventoryActivity.this, DeleteProductActivity.class);
            intent.putExtra(EXTRA_PRODUCT_ID, mProduct.getProductId());
            intent.putExtra(EXTRA_ADAPTER_POS, adapterPos);
            startActivityForResult(intent, REQUEST_CODE_DELETE_PRODUCT);
            return true;
        }
    }



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

    //TODO Create an onActivityResult method and use request codes to handle results from various activities
    // Use Study Helper QuestionActivity and QuestionEditActivity as a guide
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_PRODUCT
        ) {
            // Get new product
            long productId = data.getLongExtra(AddProductActivity.EXTRA_PRODUCT_ID, -1);
            Product newProduct = mInventoryDb.getProduct(productId);

            // Add new product to the product list
            mProductList.add(newProduct);
            mRecyclerView.getAdapter();

            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
        }

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

        //TODO IMPLEMENT FOR REQUEST_CODE_UPDATE_QUANTITY

        /*
        else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_UPDATE_QUESTION) {
            // Get updated question
            long questionId = data.getLongExtra(QuestionEditActivity.EXTRA_QUESTION_ID, -1);
            Question updatedQuestion = mStudyDb.getQuestion(questionId);

            // Replace current question in question list with updated question
            Question currentQuestion = mQuestionList.get(mCurrentQuestionIndex);
            currentQuestion.setText(updatedQuestion.getText());
            currentQuestion.setAnswer(updatedQuestion.getAnswer());
            showQuestion(mCurrentQuestionIndex);

            Toast.makeText(this, R.string.question_updated, Toast.LENGTH_SHORT).show();
        }
        */

    }
}