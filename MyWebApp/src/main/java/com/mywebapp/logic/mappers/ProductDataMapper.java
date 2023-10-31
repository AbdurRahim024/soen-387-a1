package com.mywebapp.logic.mappers;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.models.Order;
import com.mywebapp.logic.models.Product;

import java.util.ArrayList;
import java.util.UUID;

public class ProductDataMapper {

    
    public static Product findByGuid(UUID uniqueID) throws DataMapperException {
        return null;
    }

    
    public static void insert(Object object) throws DataMapperException {

    }

    
    public static void update(Object object) throws DataMapperException {

    }

    
    public static void delete(Object object) throws DataMapperException {

    }

    public static boolean findByAttributes(String name, String description, String vendor, String urlSlug, double price) {
        return false;
    }

    public static Product findBySlug(String urlSlug) throws DataMapperException {
        return null;
    }

    public static ArrayList<Product> findAllProducts() throws DataMapperException {
        return null;
    }


}
