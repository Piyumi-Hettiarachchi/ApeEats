package com.example.ApeEats.models;

public class ProductModel {

    String pID;
    String pImage;
    String pFoodname;
    String pDescription;
    String pPrice;
    String pCetogory;
    String pDeliveryAvailable;
    String uID;

    public ProductModel(String pID, String pImage, String pFoodname, String pDescription, String pPrice, String pCetogory, String pDeliveryAvailable, String uID) {
        this.pID = pID;
        this.pImage = pImage;
        this.pFoodname = pFoodname;
        this.pDescription = pDescription;
        this.pPrice = pPrice;
        this.pCetogory = pCetogory;
        this.pDeliveryAvailable = pDeliveryAvailable;
        this.uID = uID;
    }

    public String getpID() {
        return pID;
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

    public String getuID() {

        return uID;
    }
}
