package com.example.nikhil.food4foodies;

import java.util.HashMap;

public class MegaClass {
    static HashMap<Integer,cartitem> cartitems = new HashMap<>();

    void addItems(int position, cartitem c){
        cartitems.put(position,c);
    }

    HashMap<Integer,cartitem> getItems(){
        return cartitems;
    }
}
