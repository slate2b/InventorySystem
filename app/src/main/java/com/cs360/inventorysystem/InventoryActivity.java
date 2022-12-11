package com.cs360.inventorysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private InventoryDatabase mInventoryDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Get instance of the db
        mInventoryDb = InventoryDatabase.getInstance(getApplicationContext());

        RecyclerView mRecyclerView = findViewById(R.id.inventoryRecyclerView);

        // Create a grid with a single column
        RecyclerView.LayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Load the inventory data
        InventoryAdapter mInventoryAdapter = new InventoryAdapter(loadInventory());
        mRecyclerView.setAdapter(mInventoryAdapter);
    }

    private List<Product> loadInventory() {
        return mInventoryDb.getProducts();
    }

    private class ProductHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mNameText;
        private TextView mNumberText;
        private TextView mQuantityText;

        public ProductHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            mNameText = itemView.findViewById(R.id.nameTextView);
            mNumberText = itemView.findViewById(R.id.numberTextView);
            mQuantityText = itemView.findViewById(R.id.quantityTextView);
        }

        public void bind(Product product, int position) {
            mNameText.setText(product.getProductName());
            mNumberText.setText(product.getProductNumber());
            mQuantityText.setText(String.valueOf(product.getProductQuantity()));
        }

        @Override
        public void onClick(View view) {

            /*
            // Start QuestionActivity, indicating what subject was clicked
            Intent intent = new Intent(SubjectActivity.this, QuestionActivity.class);
            intent.putExtra(QuestionActivity.EXTRA_SUBJECT, mSubject.getText());
            startActivity(intent);
            */
        }
    }

    private class InventoryAdapter extends RecyclerView.Adapter<ProductHolder> {

        private List<Product> mProductList;

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
}