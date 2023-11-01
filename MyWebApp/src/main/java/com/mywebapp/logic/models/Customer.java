package com.mywebapp.logic.models;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.mappers.CustomerDataMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Customer {
    private UUID customerId; //primary key
    private UUID cartId; //foreign key
    private String name;

    public Customer(String name) {
        this.customerId = UUID.randomUUID();

        this.name = name;
        Cart cart = new Cart();
        this.cartId = cart.getCartId();
    }

    public Customer(UUID customerId, UUID cartId, String name) {
        this.customerId = customerId;

        this.name = name;
        this.cartId = cartId;
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************
    public void addCustomerToDb() throws DataMapperException {
        CustomerDataMapper.insert(this);
    }
    public void clearCart() throws DataMapperException {
        CartItem.deleteAllItemsInCart(this.cartId);
    }
    public static Customer findCustomerByName(String name) {
        //TODO: throw CustomerNotFoundException here
        return CustomerDataMapper.findByName(name);
    }



    //*******************************************************************************
    //* getters and setters
    //*******************************************************************************

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }


}
