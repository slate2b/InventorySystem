package com.cs360.inventorysystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * The Database Manager
 */
public class InventoryDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "inventory.db";

    private static InventoryDatabase mInventoryDb;

    public static InventoryDatabase getInstance(Context context) {
        if (mInventoryDb == null) {
            mInventoryDb = new InventoryDatabase(context);
        }
        return mInventoryDb;
    }

    private InventoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class LoginInfoTable {
        private static final String TABLE = "login";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_UPDATE_TIME = "updated";
    }

    private static final class ProductTable {
        private static final String TABLE = "product";
        private static final String COL_ID = "id";
        private static final String COL_NAME = "product";
        private static final String COL_NUMBER = "product_number";
        private static final String COL_QUANTITY = "in_stock";
        private static final String COL_UPDATE_TIME = "updated";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create login info table
        db.execSQL("create table " + LoginInfoTable.TABLE + " (" +
                LoginInfoTable.COL_USERNAME + " primary key, " +
                LoginInfoTable.COL_PASSWORD + ", " +
                LoginInfoTable.COL_UPDATE_TIME + " int)");

        db.execSQL("create table " + ProductTable.TABLE + " (" +
                ProductTable.COL_ID + " integer primary key autoincrement, " +
                ProductTable.COL_NAME + ", " +
                ProductTable.COL_NUMBER + ", " +
                ProductTable.COL_QUANTITY + " integer, " +
                ProductTable.COL_UPDATE_TIME + " int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LoginInfoTable.TABLE);
        db.execSQL("drop table if exists " + ProductTable.TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    /**
     * Authenticates login credentials
     *
     * @param usrnm - the username
     * @param pw    - the password
     * @return - Returns true if the credentials are valid, returns false if they are not valid.
     */
    public boolean authenticateLogin(String usrnm, String pw) {
        boolean isAuthenticated = false;

        SQLiteDatabase db = this.getReadableDatabase();

        // Securing against SQL injection by passing parameters separate from the query string
        String getLoginSQL = "select * from " + LoginInfoTable.TABLE +
                " WHERE " + LoginInfoTable.COL_USERNAME + " = ?" +
                " AND " + LoginInfoTable.COL_PASSWORD + " = ?";

        // Passing query parameters and assigning result to a cursor
        Cursor loginCursor = db.rawQuery(getLoginSQL, new String[]{usrnm, pw});

        // moveToFirst returns true if able to move to first row, false if cursor is empty
        if (loginCursor.moveToFirst()) {
            isAuthenticated = true;
        }

        return isAuthenticated;
    }

    /**
     * Creates a new login info record in the database for a new user
     *
     * @param usrnm - the username
     * @param pw    - the password
     * @return - Returns true if login info created successfully, otherwise false
     */
    public boolean createLogin(String usrnm, String pw) {
        boolean isCreated = false;

        SQLiteDatabase db = this.getWritableDatabase();

        // Call the existingUsername method to make sure username doesn't already exist
        if (!existingUsername(usrnm, pw)) {
            try {
                LoginInfo loginInfo = LoginInfo.getInstance(usrnm, pw);
                ContentValues values = new ContentValues();
                values.put(LoginInfoTable.COL_USERNAME, loginInfo.getUsername());
                values.put(LoginInfoTable.COL_PASSWORD, loginInfo.getPassword());
                values.put(LoginInfoTable.COL_UPDATE_TIME, loginInfo.getUpdateTime());
                long id = db.insert(LoginInfoTable.TABLE, null, values);
                if (id != -1) {
                    isCreated = true;
                }
            } catch (Exception exception) {
                System.out.println("Failed to register new user.");
            }
        }

        return isCreated;
    }

    /**
     * Checks to see whether username is already in the database
     *
     * @param usrnm - the username
     * @param pw    - the password
     * @return - Returns true if the username was found, returns false if it was not found.
     */
    public boolean existingUsername(String usrnm, String pw) {
        boolean isExisting = false;

        SQLiteDatabase db = this.getReadableDatabase();
        String getUsernameSQL = "select * from " + LoginInfoTable.TABLE +
                " where " + LoginInfoTable.COL_USERNAME + " = ?";

        // Assigning results of query to a cursor
        Cursor cursor = db.rawQuery(getUsernameSQL, new String[]{usrnm});

        // If cursor is empty, create new user account
        if (cursor.moveToFirst()) {
            isExisting = true;
        }
        return isExisting;
    }

    /**
     * Retrieves products from the db and adds them to a list
     *
     * @return - List of products
     */
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Setting sort to asc by product id
        String orderBy = ProductTable.COL_ID + " asc";

        String sql = "select * from " + ProductTable.TABLE + " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setProductId(cursor.getLong(0));
                product.setProductName(cursor.getString(1));
                product.setProductNumber(cursor.getString(2));
                product.setProductQuantity(cursor.getLong(3));
                product.setUpdateTime(cursor.getLong(4));
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return products;
    }

    /**
     * Retrieves a single product record from the db
     *
     * @param productId - The Product Id
     * @return Product - The Product object
     */
    public Product getProduct(long productId) {
        Product product = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + ProductTable.TABLE +
                " where " + ProductTable.COL_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Float.toString(productId)});

        if (cursor.moveToFirst()) {
            product = new Product();
            product.setProductId(cursor.getInt(0));
            product.setProductName(cursor.getString(1));
            product.setProductNumber(cursor.getString(2));
            product.setProductQuantity(cursor.getInt(3));
            product.setUpdateTime(cursor.getInt(4));
        }

        return product;
    }


    /**
     * Adds product to the SQLite Database
     *
     * @param product - The product
     * @return boolean - Whether the product was added successfully
     */
    public boolean addProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductTable.COL_NAME, product.getProductName());
        values.put(ProductTable.COL_NUMBER, product.getProductNumber());
        values.put(ProductTable.COL_QUANTITY, product.getProductQuantity());
        values.put(ProductTable.COL_UPDATE_TIME, product.getUpdateTime());
        long id = db.insert(ProductTable.TABLE, null, values);
        return id != -1;
    }

    /**
     * Deletes product from the SQLite Database
     *
     * @param productId - The id of the product to be deleted
     */
    public void deleteProduct(long productId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ProductTable.TABLE,
                ProductTable.COL_ID + " = " + productId, null);
    }

    /**
     * Updates the quantity of a product in the SQLite Database
     *
     * @param product  - The product to be updated
     * @param quantity - The updated quantity
     */
    public void updateQuantity(Product product, long quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductTable.COL_ID, product.getProductId());
        values.put(ProductTable.COL_NAME, product.getProductName());
        values.put(ProductTable.COL_NUMBER, product.getProductNumber());
        values.put(ProductTable.COL_QUANTITY, quantity);
        values.put(ProductTable.COL_UPDATE_TIME, System.currentTimeMillis());
        db.update(ProductTable.TABLE, values,
                ProductTable.COL_ID + " = " + product.getProductId(), null);
    }
}
