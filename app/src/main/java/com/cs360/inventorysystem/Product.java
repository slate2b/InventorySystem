package com.cs360.inventorysystem;

/**
 * The product records in the inventory database
 */
public class Product {

    private long mProductId;
    private String mProductName;
    private String mProductNumber;
    private long mProductQuantity;
    private long mUpdateTime;

    public Product() {}

    public Product(String name, String number, long quantity) {

        mProductName = name;
        mProductNumber = number;
        mProductQuantity = quantity;
        mUpdateTime = System.currentTimeMillis();
    }

    public long getProductId() {
        return this.mProductId;
    }

    public void setProductId(long id) {
        mProductId = id;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String pName) {
        this.mProductName = pName;
    }

    public String getProductNumber() {
        return mProductNumber;
    }

    public void setProductNumber(String pNbr) {
        this.mProductNumber = pNbr;
    }

    public long getProductQuantity() {
        return mProductQuantity;
    }

    public void setProductQuantity(long pQty) {
        this.mProductQuantity = pQty;
    }

    public long getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime(long time) {
        mUpdateTime = time;
    }
}
