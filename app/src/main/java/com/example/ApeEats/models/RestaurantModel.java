package com.example.ApeEats.models;

public class RestaurantModel {

    String resID;
    String resImage;
    String resName;
    String resMobile;
    String resAbout;
    String resEmail;
    String resAddress;
    String resDistrict;
    String resBranchAvailable;
    String resDeliveryAvailable;
    String resUID;

    public RestaurantModel(String resID, String resImage, String resName, String resMobile, String resAbout, String resEmail, String resAddress, String resDistrict, String resBranchAvailable, String resDeliveryAvailable, String resUID) {
        this.resID = resID;
        this.resImage = resImage;
        this.resName = resName;
        this.resMobile = resMobile;
        this.resAbout = resAbout;
        this.resEmail = resEmail;
        this.resAddress = resAddress;
        this.resDistrict = resDistrict;
        this.resBranchAvailable = resBranchAvailable;
        this.resDeliveryAvailable = resDeliveryAvailable;
        this.resUID = resUID;
    }

    public String getResID() {
        return resID;
    }

    public String getResImage() {
        return resImage;
    }

    public String getResName() {
        return resName;
    }

    public String getResMobile() {
        return resMobile;
    }

    public String getResAbout() {
        return resAbout;
    }

    public String getResEmail() {
        return resEmail;
    }

    public String getResAddress() {
        return resAddress;
    }

    public String getResDistrict() {
        return resDistrict;
    }

    public String getResBranchAvailable() {
        return resBranchAvailable;
    }

    public String getResDeliveryAvailable() {
        return resDeliveryAvailable;
    }

    public String getResUID() {
        return resUID;
    }
}