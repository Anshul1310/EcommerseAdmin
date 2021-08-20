package com.anstudios.ecommerseadmin.models;

public class modelPincode {
    private String pincode, codAvailable, deliveryCharge;

    public modelPincode(String pincode, String codAvailable, String deliveryCharge) {
        this.pincode = pincode;
        this.codAvailable = codAvailable;
        this.deliveryCharge = deliveryCharge;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCodAvailable() {
        return codAvailable;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }
}
