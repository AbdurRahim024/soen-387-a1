package com.mywebapp.logic.models;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.ProductNotFoundException;
import com.mywebapp.logic.mappers.CartDataMapper;
import com.mywebapp.logic.mappers.CartItemDataMapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Cart {
    private UUID cartId; //primary key

    public Cart() {
        this.cartId = UUID.randomUUID();
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************
    public void add(UUID sku) throws DataMapperException, ProductNotFoundException {
        CartItem item;

        if (Cart.isItemInCart(this.cartId, sku)) {
            item = CartItem.findCartItemBySkuAndCartId(sku, this.cartId);

        }
        else {
            Product product = Product.findProductBySku(sku);
            item = new CartItem(product, this.cartId);
        }

        item.incrementQuantity();
    }

    public void remove(UUID sku) throws DataMapperException, ProductNotFoundException {
        CartItem item = CartItem.findCartItemBySkuAndCartId(sku, this.cartId);
        item.decrementQuantity();
    }

    public static Cart findCartByGuid(UUID guid) throws DataMapperException {
        return CartDataMapper.findByGuid(guid);
    }

    public static boolean isItemInCart(UUID cartId, UUID sku) throws DataMapperException {
        return CartItemDataMapper.findBySkuAndCartId(sku, cartId) != null;
    }


    //*******************************************************************************
    //* getters and setters
    //*******************************************************************************

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

}
