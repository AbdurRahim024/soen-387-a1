package com.mywebapp.logic;

import com.mywebapp.logic.models.Product;
import java.io.File;
import java.util.HashMap;

public class LogicFacade {

    public enum ProductDescriptor {
        name,
        description,
        vendor,
        urlSlug,
    }

    public void updateProduct(String sku, String name) {

    }

    public void updateProduct(String sku, ProductDescriptor descriptor, String newInfo) {

    }

    public Product getProduct(String sku) {
        return null;
    }

    public Product getProductBySlug(String slug) {
        return null;
    }

    public HashMap<String, Product> getCart(String user) {
        return null;
    }

    public void addProductToCart(String user, String sku) {

    }

    public void removeProductFromCart(String user, String sku) {

    }

    public File downloadProductCatalog() {
        return null;
    }

}

////////////////// CUSTOM EXCEPTIONS ///////////////////////

class UserNotFoundException extends Exception {
    public UserNotFoundException(String s) {
        super(s);
    }
}

class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String s) {
        super(s);
    }
}

class UserHasNoCartException extends Exception {
    public UserHasNoCartException(String s) {
        super(s);
    }
}
