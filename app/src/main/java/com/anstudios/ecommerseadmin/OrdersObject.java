package com.anstudios.ecommerseadmin;

import java.io.Serializable;
import java.util.HashMap;

public class OrdersObject implements Serializable {
    private String addressId, paymentType, status, totalPrice, timeStamp, index, phoneNumber;
    private HashMap<String, HashMap<String, String>> products;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public HashMap<String, HashMap<String, String>> getProducts() {
        return products;
    }

    public void setProducts(HashMap<String, HashMap<String, String>> products) {
        this.products = products;
    }
}
