package com.shopping.url.parse;

import java.util.Vector;

public class ProductData {

    String title;
    String brand;
    String details;
    String price;
    String [] image = new String[4];
    String url;
    String discount;
    String rating;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ProductData(String title, String brand, String details, String price, String image, String url, String discount, String rating) {
        this.title = title;
        this.brand = brand;
        this.details = details;
        this.price = price;
        this.image[0] = image;
        this.image[1] = "NULL";
        this.image[2] = "NULL";
        this.image[3] = "NULL";
        this.url = url;
        this.discount = discount;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage(int index) {
        return image[index];
    }

    public String getImage() {
        return getImage(0);
    }

    public void setImage(int index, String image) {
        this.image[index] = image;
    }

    public void setImage(String image) {
        setImage(0, image);
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public static Vector<ProductData> ourGlobalSavedProducts = new Vector<ProductData>();

}