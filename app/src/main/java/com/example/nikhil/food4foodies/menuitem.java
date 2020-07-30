package com.example.nikhil.food4foodies;

public class menuitem {
    String name,link;
    int price;

    public menuitem(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public menuitem(String name, String link, int price) {
        this.name = name;
        this.link = link;
        this.price=price;
    }

    public String removeLink() {
        return null;
    }

    public String removeName() {
        return null;
    }

    public String removePrice() {
        return null;
    }

}