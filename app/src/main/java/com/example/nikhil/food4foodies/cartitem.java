package com.example.nikhil.food4foodies;

import androidx.annotation.NonNull;

public class cartitem {
    private String nam;
    private Double price;
    private Integer count;
    private String link;

    public cartitem() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    cartitem(String nam, Double price, Integer count, String link) {
        this.nam = nam;
        this.price = price;
        this.count = count;
        this.link = link;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}