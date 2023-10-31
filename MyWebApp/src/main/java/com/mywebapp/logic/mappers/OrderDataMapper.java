package com.mywebapp.logic.mappers;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.models.Order;

import java.util.ArrayList;
import java.util.UUID;

public class OrderDataMapper {

    
    public static Order findByGuid(UUID uniqueID) throws DataMapperException {
        return null;
    }

    
    public static void insert(Object object) throws DataMapperException {

    }

    
    public static void update(Object object) throws DataMapperException {

    }

    
    public static void delete(Object object) throws DataMapperException {

    }

    public static ArrayList<Order> findAllOrders() throws DataMapperException {
        //TODO: convert ResultSet into ArrayList
        return null;
    }

    public static ArrayList<Order> findOrdersByCustomer(UUID customerId) throws DataMapperException {
        return null;
    }
}
