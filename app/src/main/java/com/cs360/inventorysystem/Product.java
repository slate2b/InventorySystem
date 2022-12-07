package com.cs360.inventorysystem;

/**
 * The product records in the inventory database
 */
public class Product {

    private long mProductId;
    private String mProductName;
    private String mProductNumber;
    private String mProductQuantity;
    private long mUpdateTime;

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

    public String getProductQuantity() {
        return mProductQuantity;
    }

    public void setProductQuantity(String pQty) {
        this.mProductQuantity = pQty;
    }

    public long getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime(long time) {
        mUpdateTime = time;
    }
}
