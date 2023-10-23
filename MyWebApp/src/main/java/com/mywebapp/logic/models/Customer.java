package com.mywebapp.logic.models;

import java.util.HashMap;

public class Customer {
    private String name;
    private HashMap<String,Product> cart; // the key is the sku and the value is the product itself

    public Customer(String name) {
        this.name = name;
        this.cart = new HashMap<>();
    }

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

    public void addToCart(Product newProduct) {
        cart.put(newProduct.getSku(), newProduct);
    }

    public void removeFromCart(Product productToRemove) {
        cart.remove(productToRemove.getSku());
    }
