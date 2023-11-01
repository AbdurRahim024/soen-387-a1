package com.mywebapp.logic;

import com.mywebapp.logic.models.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.*;
import java.util.UUID;

public class LogicFacade {
    private final File products_file = new File("/Users/emmuh/Documents/COMP/SOEN 387/soen-387-a1/MyWebApp/src/main/java/com/mywebapp/logic/products.csv");

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
        Cart cart = Cart.findCartByGuid(customer.getCartId());
        cart.add(UUID.fromString(sku));
    }

    public void removeProductFromCart(String userName, String sku) throws UserNotFoundException, ProductNotFoundException, DataMapperException {
        Customer customer = Customer.findCustomerByName(userName);
        Cart cart = Cart.findCartByGuid(customer.getCartId());
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

    public Order getOrderDetails(String userName, int orderId) throws DataMapperException, CustomerOrderMismatchException {
        Order order = Order.getOrderByGuid(orderId);

        if (!userName.isEmpty()) {
            Customer customer = Customer.findCustomerByName(userName);
            if (!order.getCustomerId().equals(customer.getCustomerId())) {
                throw new CustomerOrderMismatchException("This order does not belong to this customer");
            }
        }

        return order;
    }

    public ArrayList<Order> getAllOrders() throws DataMapperException {
        return Order.getAllOrders();
    }

    public void shipOrder(int orderId) throws DataMapperException {
        Order order = Order.getOrderByGuid(orderId);
        order.ship();
    }

    public File downloadProductCatalog() {
        return this.products_file;
    }

    public ArrayList<Product> getProducts() throws DataMapperException {
        return Product.getAllProducts();
    }

    //TODO: sync with abdur to make sure whenever there's a new customer he calls this method
    public void createCustomer(String userName) throws DataMapperException {
        Customer customer = new Customer(userName);
        customer.addCustomerToDb();
    }


    //*******************************************************************************
    //* helper methods
    //*******************************************************************************

//    private void readProductsFromCSV() {
//        try (CSVReader reader = new CSVReader(new FileReader(this.products_file))) {
//            String[] line;
//            while ((line = reader.readNext()) != null) {
//                String sku = line[0];
//                if (sku.equals("sku")) { // skip if the first row (titles) is being read
//                    continue;
//                }
//                String name = line[1];
//                String description = line[2];
//                String vendor = line[3];
//                String urlSlug = line[4];
//                double price = Double.valueOf(line[5]);
//
//                Product product = new Product(name, description, vendor, urlSlug, price);
//                this.products.put(sku, product);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void updateProductInCSV() {
//        try (CSVWriter writer = new CSVWriter(new FileWriter(this.products_file))) {
//            for (Product product : products.values()) {
//                String[] entry = product.getCsvFormat();
//                writer.writeNext(entry);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void addProductToCSV(Product product) {
//        try (CSVWriter writer = new CSVWriter(new FileWriter(this.products_file, true))) {
//            String[] entry = product.getCsvFormat();
//            writer.writeNext(entry);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



}
