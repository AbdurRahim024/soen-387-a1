package com.mywebapp.logic.models;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.mappers.CartDataMapper;
import com.mywebapp.logic.mappers.CartItemDataMapper;

import java.util.ArrayList;
import java.util.UUID;

public class CartItem extends Product {
    private UUID cartId;
    private int quantity;

    public CartItem(Product product, UUID cartId) throws DataMapperException {
        super(product.getName(), product.getDescription(), product.getVendor(), product.getUrlSlug(), product.getPrice());
        this.cartId = cartId;
        this.quantity = 0;

        CartItemDataMapper.insert(this);
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************

    public void incrementQuantity() throws DataMapperException {
        this.quantity += 1;
        CartItemDataMapper.update(this);
    }

    public void decrementQuantity() throws DataMapperException {
        this.quantity -= 1;
        CartItemDataMapper.update(this);

        if (this.quantity == 0) {
            CartItemDataMapper.delete(this);
        }
    }
    public static ArrayList<CartItem> findCartItemsByCartId(UUID cartId) {
        return CartItemDataMapper.findByCartId(cartId);
    }

    public static CartItem findCartItemBySkuAndCartId(UUID sku, UUID cartId) throws DataMapperException {
        return CartItemDataMapper.findByGuid(sku, cartId);
    }

    public static void deleteCartItemsInCart(UUID cartId) {
        CartItemDataMapper.deleteItemsInCart(cartId);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws DataMapperException {
        this.quantity = quantity;
        CartItemDataMapper.update(this);
    }
}
