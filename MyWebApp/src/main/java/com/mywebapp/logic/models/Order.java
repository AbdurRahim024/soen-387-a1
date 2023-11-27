package com.mywebapp.logic.models;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.OrderNotFoundException;
import com.mywebapp.logic.mappers.OrderDataMapper;
import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private int orderId; //primary key
    private UUID userId; //foreign key
    private ArrayList<CartItem> items; //BLOB
    private String shippingAddress;
    private UUID trackingNumber;
    private boolean isShipped;

    public Order(UUID userId, String shippingAddress) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.isShipped = false;
    }

    public Order(int orderId, UUID userId, String shippingAddress, UUID trackingNumber, boolean isShipped, ArrayList<CartItem> items) {
        this.orderId = orderId;
        this.userId = userId;
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

    public void setOrderOwner(UUID user_id) throws DataMapperException {
        //TODO: check if the owner already belongs to someone, if so throw error
        this.userId = user_id;
        OrderDataMapper.update(this);
    }

    public static ArrayList<Order> getAllOrders() throws DataMapperException {
        return OrderDataMapper.getOrders(-1, null);
    }

    public static Order getOrderByGuid(int orderId) throws DataMapperException, OrderNotFoundException {
        ArrayList<Order> ordersResult = OrderDataMapper.getOrders(orderId, null);
        if (ordersResult.isEmpty()) {
            throw new OrderNotFoundException("No order was found with this order_id.");
        }
        return ordersResult.get(0);
    }

    public static ArrayList<Order> getOrdersByUser(UUID userId) throws DataMapperException {
        return OrderDataMapper.getOrders(-1, userId);
    }


    //*******************************************************************************
    //* getters and setters
    //*******************************************************************************

    public UUID getUserId() {
        return userId;
    }

    public void setUser(UUID userId) {
        this.userId = userId;
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

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }
}
