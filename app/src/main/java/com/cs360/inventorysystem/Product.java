package com.cs360.inventorysystem;

public class Product {

    private String mProductName;
    private String mProductNumber;
    private String mProductQuantity;

    private static Product mProduct;

    public static Product getInstance(String pName, String pNbr, String pQty) {
        if (mProduct == null) {
            mProduct = new Product(pName, pNbr, pQty);
        }
        return mProduct;
    }

    private Product(String pName, String pNbr, String pQty) {
        mProductName = pName;
        mProductNumber = pNbr;
        mProductQuantity = pQty;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String pName) {
        mProductName = pName;
    }

    public String getProductNumber() {
        return mProductNumber;
    }

    public void setProductNumber(String pNbr) {
        mProductNumber = pNbr;
    }

    public String getProductQuantity() {
        return mProductQuantity;
    }

    public void setProductQuantity(String pQty) {
        mProductQuantity = pQty;
    }
}
