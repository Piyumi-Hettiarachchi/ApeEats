package com.example.ApeEats.models;

public class ProductModel {

    String pImage;
    String pFoodname;
    String pDescription;
    String pPrice;
    String pCetogory;
    String pDeliveryAvailable;

    public ProductModel(String pImage, String pFoodname, String pDescription, String pPrice, String pCetogory, String pDeliveryAvailable) {
        this.pImage = pImage;
        this.pFoodname = pFoodname;
        this.pDescription = pDescription;
        this.pPrice = pPrice;
        this.pCetogory = pCetogory;
        this.pDeliveryAvailable = pDeliveryAvailable;
    }

    public String getpImage() {
        return pImage;
    }

    public String getpFoodname() {
        return pFoodname;
    }

    public String getpDescription() {
        return pDescription;
    }

    public String getpPrice() {
        return pPrice;
    }

    public String getpCetogory() {
        return pCetogory;
    }

    public String getpDeliveryAvailable() {
        return pDeliveryAvailable;
    }
}
