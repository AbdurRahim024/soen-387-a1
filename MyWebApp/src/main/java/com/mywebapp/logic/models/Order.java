package com.mywebapp.logic.models;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.mappers.OrderDataMapper;
import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private UUID orderId; //primary key
    private UUID customerId; //foreign key
    private UUID cartId;
    private String shippingAddress;
    private int trackingNumber;
    private boolean isShipped;

    public Order(UUID cartId, UUID customerId, String shippingAddress) throws DataMapperException {
        this.orderId = UUID.randomUUID();

        this.cartId = cartId;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = -1;
        this.isShipped = false;
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************

    public void addOrderToDb() throws DataMapperException {
        OrderDataMapper.insert(this);
    }

    public ArrayList<Order> getOrders() throws DataMapperException {
        return OrderDataMapper.findAllOrders();
    }

    public Order findOrderById(String orderId) throws DataMapperException {
        UUID orderGuid = UUID.fromString(orderId);
        return OrderDataMapper.findByGuid(orderGuid);
    }

    public void ship() throws DataMapperException {
        this.setShipped(true);

        //TODO: figure out a way to auto gen this
        this.trackingNumber = 5;

        OrderDataMapper.update(this);
    }

    public static Order getOrderByGuid(UUID orderId) throws DataMapperException {
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

    public UUID getCartId() {
        return cartId;
    }

    public void setCart(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID order_id) {
        this.orderId = order_id;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shipping_address) {
        this.shippingAddress = shipping_address;
    }

    public int getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(int tracking_number) {
        this.trackingNumber = tracking_number;
    }

    public boolean isShipped() {
        return isShipped;
    }

    public void setShipped(boolean shipped) {
        isShipped = shipped;
    }

}
