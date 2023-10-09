package com.mywebapp.logic.models;

import java.util.HashMap;

public class Customer {
    private String name;
    private HashMap<String,Product> cart;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Product> getCart() {
        return cart;
    }

    public void setCart(HashMap<String, Product> cart) {
        this.cart = cart;
    }



}
