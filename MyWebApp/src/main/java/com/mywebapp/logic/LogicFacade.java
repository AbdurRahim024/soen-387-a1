package com.mywebapp.logic;

import com.mywebapp.logic.custom_errors.*;
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

    public ArrayList<? extends Product> getCart(String userName) throws UserNotFoundException, DataMapperException {
        Customer customer = Customer.findCustomerByName(userName);
        return CartItem.findCartItemsByCartId(customer.getCartId());
    }

    public void addProductToCart(String userName, String sku) throws UserNotFoundException, ProductNotFoundException, DataMapperException {
        Customer customer = Customer.findCustomerByName(userName);
        Cart cart = new Cart(customer.getCartId());
        cart.add(UUID.fromString(sku));
    }

    //TODO: fix this (this is not reached)
    public void removeProductFromCart(String userName, String sku) throws UserNotFoundException, ProductNotFoundException, DataMapperException {
        Customer customer = Customer.findCustomerByName(userName);
        Cart cart = new Cart(customer.getCartId());
        cart.remove(UUID.fromString(sku));
    }

    public void setProductQuantityInCart(String userName, String sku, int quantity) throws UserNotFoundException, DataMapperException, ProductNotFoundException {
        Customer customer = Customer.findCustomerByName(userName);
        CartItem item = CartItem.findCartItemBySkuAndCartId(UUID.fromString(sku), customer.getCartId());
        item.setQuantity(quantity);
    }

    public void clearCart(String userName) throws UserNotFoundException, DataMapperException {
        Customer customer = Customer.findCustomerByName(userName);
        customer.clearCart();
    }

    public void createOrder(String userName, String shippingAddress) throws UserNotFoundException, DataMapperException {
        Customer customer = Customer.findCustomerByName(userName);
        Order order = new Order(customer.getCartId(), shippingAddress);
        order.placeOrder(customer.getCartId());

        customer.clearCart();
    }

    public ArrayList<Order> getOrdersByCustomer(String userName) throws UserNotFoundException, DataMapperException {
        Customer customer = Customer.findCustomerByName(userName);
        return Order.getOrdersByCustomer(customer.getCustomerId());
    }

    public Order getOrderDetails(String userName, int orderId) throws DataMapperException, CustomerOrderMismatchException, UserNotFoundException, OrderNotFoundException {
        Order order = Order.getOrderByGuid(orderId);

        if (!userName.isEmpty()) {
            Customer customer = Customer.findCustomerByName(userName);
            if (!order.getCustomerId().equals(customer.getCustomerId())) {
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
        String classpath = System.getProperty("java.class.path");
        System.out.println(classpath);
        return Product.getAllProducts();
    }

    //TODO: sync with abdur to make sure whenever there's a new customer he calls this method
    public void createCustomer(String userName) throws DataMapperException {
        Customer customer = new Customer(userName);
        customer.addCustomerToDb();
    }

}

