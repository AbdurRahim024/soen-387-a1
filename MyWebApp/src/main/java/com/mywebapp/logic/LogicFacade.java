package com.mywebapp.logic;

import com.mywebapp.logic.models.Product;
import com.mywebapp.logic.models.Customer;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.*;

public class LogicFacade {

    private HashMap<String,Product> products; //The hashmap's key is the sku
    private ArrayList<Customer> customers;
    private File products_file = new File("/Users/abdurrahimgigani/Documents/SOEN 387/soen-387-a1/MyWebApp/src/main/java/com/mywebapp/logic/products.csv");

    public LogicFacade() {
        this.products = new HashMap<>();
        this.customers = new ArrayList<>();
        Customer default_customer = new Customer("guest");
        this.customers.add(default_customer);
        readProductsFromCSV();
    }

    public void createProduct(String name, String description, String vendor, String urlSlug, double price) {
        String sku = String.valueOf(name.hashCode());

        if (products.containsKey(sku)) {
            return;
        }

        Product product = new Product(name, description, vendor, urlSlug, sku, price);
        products.put(sku, product);
        addProductToCSV(product);
    }

    public void updateProduct(String name, String description, String vendor, String urlSlug, double price, String sku) throws ProductNotFoundException {
        Product product = products.get(sku);
        if (product == null) {
            throw new ProductNotFoundException("This product doesn't exist.");
        }

        product.setName(name);
        product.setDescription(description);
        product.setVendor(vendor);
        product.setUrlSlug(urlSlug);
        product.setPrice(price);

        //update csv file
        updateProductInCSV();
    }

    public Product getProduct(String sku) throws ProductNotFoundException {
        Product product = products.get(sku);
        if (product == null) {
            throw new ProductNotFoundException("This product doesn't exist.");
        }

        return product;
    }

    public Product getProductBySlug(String slug) throws ProductNotFoundException {
        for (Product product : products.values()) {
            if (product.getUrlSlug().equals(slug)) {
                return product;
            }
        }

        throw new ProductNotFoundException("This product doesn't exist.");
    }

    public ArrayList<Product> getCart(String userName) throws UserNotFoundException {
        Customer customer = findCustomerByName(userName);
        if (customer == null) {
            throw new UserNotFoundException("This customer does not exist");
        }

        HashMap<String,Product> cart = customer.getCart();

        return new ArrayList<>(cart.values());
    }

    public void addProductToCart(String userName, String sku) throws UserNotFoundException, ProductNotFoundException {
        Customer customer;
        Product newProduct;
        try {
            customer = findCustomerByName(userName);
            newProduct = products.get(sku);
        }
        catch(Exception e) {
            throw e;
        }

        customer.addToCart(newProduct);
    }

    public void removeProductFromCart(String userName, String sku) throws UserNotFoundException, ProductNotFoundException {
        Customer customer;
        Product productToRemove;
        try {
            customer = findCustomerByName(userName);
            productToRemove = products.get(sku);
        }
        catch(Exception e) {
            throw e;
        }

        customer.removeFromCart(productToRemove);
    }

    public File downloadProductCatalog() {
        return this.products_file;
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    public void setProducts(HashMap<String,Product> products) {
        this.products = products;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }




    ////////////////////////////////////////// HELPER METHODS ///////////////////////////////////////////////////////




    public Customer findCustomerByName(String name) throws UserNotFoundException {
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }

        throw new UserNotFoundException("This customer does not exist");
    }

    public void readProductsFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(this.products_file))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String sku = line[0];
                if (sku.equals("sku")) { // skip if the first row (titles) is being read
                    continue;
                }
                String name = line[1];
                String description = line[2];
                String vendor = line[3];
                String urlSlug = line[4];
                double price = Double.valueOf(line[5]);

                Product product = new Product(name, description, vendor, urlSlug, sku, price);
                this.products.put(sku, product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProductInCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(this.products_file))) {
            for (Product product : products.values()) {
                String[] entry = {
                        product.getSku(),
                        product.getName(),
                        product.getDescription(),
                        product.getVendor(),
                        product.getUrlSlug(),
                        String.valueOf(product.getPrice())
                };
                writer.writeNext(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProductToCSV(Product product) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(this.products_file, true))) {
            String[] entry = {
                    product.getSku(),
                    product.getName(),
                    product.getDescription(),
                    product.getVendor(),
                    product.getUrlSlug(),
                    String.valueOf(product.getPrice())
            };
            writer.writeNext(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
