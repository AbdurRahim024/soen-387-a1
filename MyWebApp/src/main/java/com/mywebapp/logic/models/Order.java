package com.mywebapp.logic.models;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.mappers.OrderDataMapper;
import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private int orderId; //primary key
    private UUID customerId; //foreign key
    private ArrayList<CartItem> items; //BLOB
    private String shippingAddress;
    private UUID trackingNumber;
    private boolean isShipped;

    public Order(UUID customerId, String shippingAddress) {
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.isShipped = false;
    }

    public Order(int orderId, UUID customerId, String shippingAddress, UUID trackingNumber, boolean isShipped, ArrayList<CartItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = trackingNumber;
        this.isShipped = isShipped;
        this.items = items;
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************

    public void placeOrder(UUID cartId) throws DataMapperException {
        this.items = CartItem.findCartItemsByCartId(cartId);
        OrderDataMapper.insert(this);
    }

    public void ship() throws DataMapperException {
        this.setShipped(true);
        this.setTrackingNumber(UUID.randomUUID());
        OrderDataMapper.update(this);
    }

    public static ArrayList<Order> getAllOrders() throws DataMapperException {
        return OrderDataMapper.findAllOrders();
    }

    public static Order getOrderByGuid(int orderId) throws DataMapperException {
        return OrderDataMapper.findByGuid(orderId);
    }

    public static ArrayList<Order> getOrdersByCustomer(UUID customerId) throws DataMapperException {
        return OrderDataMapper.findOrdersByCustomer(customerId);
    }


    //*******************************************************************************
    //* getters and setters
    //*******************************************************************************

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomer(UUID customerId) {
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int order_id) {
        this.orderId = order_id;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shipping_address) {
        this.shippingAddress = shipping_address;
    }

    public UUID getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(UUID tracking_number) {
        this.trackingNumber = tracking_number;
    }

    public boolean isShipped() {
        return isShipped;
    }

    public void setShipped(boolean shipped) {
        isShipped = shipped;
    }

}
