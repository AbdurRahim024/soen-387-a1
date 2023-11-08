package com.mywebapp.logic.models;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.ProductNotFoundException;
import com.mywebapp.logic.mappers.CartItemDataMapper;

import java.util.UUID;

public class Cart {
    private UUID cartId; //primary key

    public Cart() {
        this.cartId = UUID.randomUUID();
    }

    public Cart(UUID cartId) {
        this.cartId = cartId;
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************
    public void incrementItem(UUID sku) throws DataMapperException, ProductNotFoundException {
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

    public void decrementItem(UUID sku) throws DataMapperException, ProductNotFoundException {
        CartItem item = CartItem.findCartItemBySkuAndCartId(sku, this.cartId);
        item.decrementQuantity();
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
