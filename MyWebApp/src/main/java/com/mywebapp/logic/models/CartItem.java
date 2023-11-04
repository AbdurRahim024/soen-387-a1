package com.mywebapp.logic.models;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.ProductNotFoundException;
import com.mywebapp.logic.mappers.CartItemDataMapper;

import java.util.ArrayList;
import java.util.UUID;

public class CartItem extends Product {
    private UUID cartId; //foreign key + primary key
    private int quantity;

    public CartItem(Product product, UUID cartId) throws DataMapperException {
        super(product.getSku(), product.getName(), product.getDescription(), product.getVendor(), product.getUrlSlug(), product.getPrice());
        this.cartId = cartId;
        this.quantity = 0;

        CartItemDataMapper.insert(this);
    }

    public CartItem(Product product, UUID cartId, int quantity) throws DataMapperException {
        super(product.getSku(), product.getName(), product.getDescription(), product.getVendor(), product.getUrlSlug(), product.getPrice());
        this.cartId = cartId;
        this.quantity = quantity;
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
    public static ArrayList<CartItem> findCartItemsByCartId(UUID cartId) throws DataMapperException {
        return CartItemDataMapper.getAllItemsInCart(cartId);
    }

    public static CartItem findCartItemBySkuAndCartId(UUID sku, UUID cartId) throws DataMapperException, ProductNotFoundException {
        CartItem item = CartItemDataMapper.findBySkuAndCartId(sku, cartId);

        if (item == null) {
            throw new ProductNotFoundException("This product was not found in the cart");
        }

        return item;
    }

    public static void deleteAllItemsInCart(UUID cartId) throws DataMapperException {
        CartItemDataMapper.deleteAllItemsInCart(cartId);
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
