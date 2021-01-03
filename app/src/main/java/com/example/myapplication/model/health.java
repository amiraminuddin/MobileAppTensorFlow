package com.example.myapplication.model;

public class health {

    private String price, picture;



    public health(String price, String picture){

        this.price = price;
        this.picture = picture;
    }

    public health(){}

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
