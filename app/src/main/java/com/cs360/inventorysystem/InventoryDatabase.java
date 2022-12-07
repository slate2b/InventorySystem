package com.cs360.inventorysystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The Database Manager
 */
public class InventoryDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "inventory.db";

    private static InventoryDatabase mInventoryDb;

    //TODO Implement a sorter enum?

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

    private static final class PreferencesTable {
        private static final String TABLE = "preferences";
        private static final String COL_ID = "id";
        private static final String COL_USERNAME = "username";
        private static final String COL_SMS = "sms";
        private static final String COL_IN_APP = "in_app";
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

        //TODO Create tables for LoginInfo, Product, and Preferences

        // Create login info table
        db.execSQL("create table " + LoginInfoTable.TABLE + " (" +
                LoginInfoTable.COL_USERNAME + " primary key, " +
                LoginInfoTable.COL_PASSWORD + ", " +
                LoginInfoTable.COL_UPDATE_TIME + " int)");

        // Create preferences table
        db.execSQL("create table " + PreferencesTable.TABLE + " (" +
                PreferencesTable.COL_ID + " integer primary key autoincrement, " +
                PreferencesTable.COL_SMS + " integer, " +
                PreferencesTable.COL_IN_APP + " integer, " +
                PreferencesTable.COL_USERNAME + ", " +
                "foreign key(" + PreferencesTable.COL_USERNAME + ") references " +
                LoginInfoTable.TABLE + "(" + LoginInfoTable.COL_USERNAME + ") on delete cascade)");

        db.execSQL("create table " + ProductTable.TABLE + " (" +
                ProductTable.COL_ID + " integer primary key autoincrement, " +
                ProductTable.COL_NAME + ", " +
                ProductTable.COL_NUMBER + ", " +
                ProductTable.COL_QUANTITY + " integer, " +
                ProductTable.COL_UPDATE_TIME + " int)");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("drop table if exists " + LoginInfoTable.TABLE);
        db.execSQL("drop table if exists " + PreferencesTable.TABLE);
        db.execSQL("drop table if exists " + ProductTable.TABLE);
        onCreate(db);
    }



    //TODO ? Need to include an onOpen method to handle foreign keys in different android versions?


    /**
     * Checks to see whether username is already in the database
     * @param usrnm - the username
     * @param pw - the password
     * @return - Returns true if the username was found, returns false if it was not found.
     */
    public boolean existingUsername(String usrnm, String pw) {
        SQLiteDatabase db = this.getReadableDatabase();
        String getUsernameSQL = "select * from " + LoginInfoTable.TABLE +
                " where " + LoginInfoTable.COL_USERNAME + " = ?";

        // Assigning results of query to a cursor
        Cursor cursor = db.rawQuery(getUsernameSQL, new String[] { usrnm });

        // moveToFirst returns true if able to move to first row, false if cursor is empty
        if (!cursor.moveToFirst()) {
            createLogin(usrnm, pw);
            return false;
        }
        else {
            authenticateLogin(usrnm, pw);
            return true;
        }
    }

    /**
     * Creates a new login info record in the database for a new user
     * @param usrnm - the username
     * @param pw - the password
     * @return - Returns true if login info created successfully, otherwise false
     */
    public boolean createLogin(String usrnm, String pw) {
        boolean isCreated = false;

        SQLiteDatabase db = this.getReadableDatabase();

        // Securing against SQL injection by passing parameters separate from the query string
        String getUsernameSQL = "select * from " + LoginInfoTable.TABLE +
                " WHERE " + LoginInfoTable.COL_USERNAME + " = ?";

        // Passing query parameters and assigning result to a cursor
        Cursor cursor = db.rawQuery(getUsernameSQL, new String[] { usrnm });

        if (!cursor.moveToFirst()) {
            LoginInfo loginInfo = LoginInfo.getInstance(usrnm, pw);
            ContentValues values = new ContentValues();
            values.put(LoginInfoTable.COL_USERNAME, loginInfo.getUsername());
            values.put(LoginInfoTable.COL_PASSWORD, loginInfo.getPassword());
            values.put(LoginInfoTable.COL_UPDATE_TIME, loginInfo.getUpdateTime());
            isCreated = true;
        }
        return isCreated;
    }

    /**
     * Authenticates login credentials
     * @param usrnm - the username
     * @param pw - the password
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
        Cursor loginCursor = db.rawQuery(getLoginSQL, new String[] { usrnm, pw });

        if (loginCursor.moveToFirst()) {
            isAuthenticated = true;
        }

        return isAuthenticated;
    }



    //TODO TONS OF OTHER CRAP






}
