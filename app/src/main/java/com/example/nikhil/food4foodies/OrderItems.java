package com.example.nikhil.food4foodies;

import java.util.ArrayList;

public class OrderItems {


    public OrderItems() {
    }

    public OrderItems(ArrayList<orderitem> orderitems) {
        this.orderitems = orderitems;
    }

    ArrayList<orderitem> orderitems;



    public ArrayList<orderitem> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(ArrayList<orderitem> orderitems) {
        this.orderitems = orderitems;
    }
}
