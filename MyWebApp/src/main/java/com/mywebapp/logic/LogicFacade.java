package com.mywebapp.logic;

import com.mywebapp.logic.custom_errors.*;
import com.mywebapp.logic.mappers.UserDataMapper;
import com.mywebapp.logic.models.*;

import java.util.ArrayList;
import java.io.*;
import java.util.UUID;

public class LogicFacade {

    public LogicFacade() {
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************

    public void createProduct(String name, String description, String vendor, String urlSlug, double price) throws ProductAlreadyExistsException, DataMapperException {

        if (Product.productAlreadyExists(name, description, vendor, urlSlug, price)) {
            throw new ProductAlreadyExistsException("This product already exists");
        }

        Product product = new Product(name, description, vendor, urlSlug, price);
        product.addProductToDb();
    }

    public void updateProduct(String name, String description, String vendor, String urlSlug, String sku, double price) throws ProductNotFoundException, DataMapperException {
        Product product = Product.findProductBySku(UUID.fromString(sku));
        product.updateProductInDb(name, description, vendor, urlSlug, price);

    }

    public Product getProduct(String sku) throws ProductNotFoundException, DataMapperException {
        return Product.findProductBySku(UUID.fromString(sku));
    }

    public Product getProductBySlug(String urlSlug) throws ProductNotFoundException, DataMapperException {
        return Product.findProductBySlug(urlSlug);
    }

    public ArrayList<? extends Product> getCart(String user_id) throws UserNotFoundException, DataMapperException {
        User user = User.getUser(user_id);
        return CartItem.findCartItemsByCartId(user.getCartId());
    }

    public void addProductToCart(String user_id, String sku) throws UserNotFoundException, ProductNotFoundException, DataMapperException {
        User user = User.getUser(user_id);
        Cart cart = new Cart(user.getCartId());
        cart.incrementItem(UUID.fromString(sku));
    }

    public void removeProductFromCart(String user_id, String sku) throws UserNotFoundException, ProductNotFoundException, DataMapperException {
        User user = User.getUser(user_id);
        Cart cart = new Cart(user.getCartId());

        CartItem item = CartItem.findCartItemBySkuAndCartId(UUID.fromString(sku), user.getCartId());
        item.setQuantity(1);
        cart.decrementItem(UUID.fromString(sku));
    }

    public void decrementProductInCart(String user_id, String sku) throws UserNotFoundException, ProductNotFoundException, DataMapperException {
        User user = User.getUser(user_id);
        Cart cart = new Cart(user.getCartId());
        cart.decrementItem(UUID.fromString(sku));
    }

    public void setProductQuantityInCart(String user_id, String sku, int quantity) throws UserNotFoundException, DataMapperException, ProductNotFoundException {
        User user = User.getUser(user_id);
        CartItem item = CartItem.findCartItemBySkuAndCartId(UUID.fromString(sku), user.getCartId());
        item.setQuantity(quantity);
    }

    public void clearCart(String user_id) throws UserNotFoundException, DataMapperException {
        User user = User.getUser(user_id);
        user.clearCart();
    }

    public void createOrder(String user_id, String shippingAddress) throws UserNotFoundException, DataMapperException {
        User user = User.getUser(user_id);
        Order order = new Order(user.getUserId(), shippingAddress);
        order.placeOrder(user.getCartId());
        user.clearCart();
    }

    public ArrayList<Order> getOrdersByUser(String user_id) throws UserNotFoundException, DataMapperException {
        User user = User.getUser(user_id);
        return Order.getOrdersByUser(user.getUserId());
    }

    public Order getOrderDetails(String userType, String user_id, int orderId) throws DataMapperException, CustomerOrderMismatchException, UserNotFoundException, OrderNotFoundException {
        Order order = Order.getOrderByGuid(orderId);

        if (!user_id.isEmpty() && userType.equals("user")) {
            User user = User.getUser(user_id);
            if (!order.getUserId().equals(user.getUserId())) {
                throw new CustomerOrderMismatchException("This order does not belong to this customer");
            }
        }

        return order;
    }

    public ArrayList<Order> getAllOrders() throws DataMapperException, OrderNotFoundException {
        return Order.getAllOrders();
    }

    public void shipOrder(int orderId) throws DataMapperException, OrderNotFoundException {
        Order order = Order.getOrderByGuid(orderId);
        order.ship();
    }

    public File downloadProductCatalog() throws DataMapperException, FileDownloadException {
        return Product.downloadProductCatalog();
    }

    public ArrayList<Product> getProducts() throws DataMapperException {
        return Product.getAllProducts();
    }

    public void createUser(String passcode) throws DataMapperException, UserAlreadyExistsException {
        User.addUserToDb(passcode);
    }

    public void setOrderOwner(int orderId, String userId) throws OrderNotFoundException, DataMapperException {
        Order order = Order.getOrderByGuid(orderId);
        order.setOrderOwner(UUID.fromString(userId));
    }

    public void setPasscode(String oldPasscode, String newPasscode) throws UserNotFoundException, DataMapperException {
        User.changePasscode(oldPasscode, newPasscode);
    }
    
    public ArrayList<User> getUsers() {
        return User.getAllUsers();
    }

    public void changeRole(String passcode) throws UserNotFoundException, DataMapperException {
        User.changeRole(passcode);

    }

    //TODO: add unit tests
    
}

