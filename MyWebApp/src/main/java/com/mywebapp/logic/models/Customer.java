package com.mywebapp.logic.models;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.UserNotFoundException;
import com.mywebapp.logic.mappers.CustomerDataMapper;

import java.util.UUID;

public class Customer {
    private UUID customerId; //primary key
    private UUID cartId;

    public Customer() {
        this.customerId = UUID.randomUUID();

        Cart cart = new Cart();
        this.cartId = cart.getCartId();
    }

    public Customer(UUID customerId, UUID cartId) {
        this.customerId = customerId;
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
    public static Customer getCustomer(String customer_id) throws UserNotFoundException, DataMapperException {
        Customer customer = CustomerDataMapper.findByGuid(UUID.fromString(customer_id));

        if (customer == null) {
            throw new UserNotFoundException("This customer was not found.");
        }
        return customer;
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

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }


}
