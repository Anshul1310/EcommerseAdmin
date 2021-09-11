package com.anstudios.ecommerseadmin.models;

public class modelOrders {
    private String status, date, paymentType, orderId, price;

    public modelOrders(String status, String date, String paymentType, String orderId, String price) {
        this.status = status;
        this.date = date;
        this.paymentType = paymentType;
        this.orderId = orderId;
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
