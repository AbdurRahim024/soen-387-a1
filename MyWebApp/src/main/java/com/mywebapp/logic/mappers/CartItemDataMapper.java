package com.mywebapp.logic.mappers;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.Order;

import java.util.ArrayList;
import java.util.UUID;

public class CartItemDataMapper {
    //TODO: use both sku and cart_id as the primary key
    
    public static CartItem findByGuid(UUID sku, UUID cartId) throws DataMapperException {
        return null;
    }

    
    public static void insert(Object object) throws DataMapperException {

    }

    
    public static void update(Object object) throws DataMapperException {

    }

    
    public static void delete(Object object) throws DataMapperException {

    }

    public static ArrayList<CartItem> findByCartId(UUID cartId) {
        return null;
    }

    public static void deleteItemsInCart(UUID cartId) {

    }

}
