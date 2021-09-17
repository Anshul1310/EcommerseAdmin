package com.anstudios.ecommerseadmin.models;

public class modelProducts {
    private String smallImage, bigImage;
    private String name, description, price, measuringUnit, productId, category;

    public modelProducts(String category, String smallImage, String bigImage, String name, String description, String price, String measuringUnit, String productId) {
        this.smallImage = smallImage;
        this.bigImage = bigImage;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.measuringUnit = measuringUnit;
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public String getBigImage() {
        return bigImage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public String getProductId() {
        return productId;
    }
}
