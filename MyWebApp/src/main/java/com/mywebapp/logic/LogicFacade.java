package com.mywebapp.logic;

import com.mywebapp.logic.models.Product;
import com.mywebapp.logic.models.Customer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.*;
import java.util.List;

public class LogicFacade {

    private ArrayList<Product> products;
    private ArrayList<Customer> customers;

    public LogicFacade() {

        Product product1 = new Product("apple", "some apple", "applevendor", "apple2", "appl1", 3.45);
        Product product2 = new Product("apple3", "some apple3", "applevendor3", "apple3", "appl3", 3.95);
        products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        customers = new ArrayList<>();
    }

    public enum ProductDescriptor {
        undefined,
        name,
        description,
        vendor,
        urlSlug,
        price,
    }

    public void createProduct(String name, String description, String vendor, String urlSlug, double price) {
        // Set sku as the length of the products array so it's unique
        String sku = String.valueOf(products.size());

        Product product = new Product(name, description, vendor, urlSlug, sku, price);
        products.add(product);

        //TODO: add to csv file
    }

    public void updateProduct(String sku, ProductDescriptor descriptor, String newInfo) throws ProductNotFoundException {
        Product product = findProductBySku(sku);
        if (product == null) {
            throw new ProductNotFoundException("This product doesn't exist.");
        }

        switch (descriptor) {
            case name -> product.setName(newInfo);
            case description -> product.setDescription(newInfo);
            case vendor -> product.setVendor(newInfo);
            case urlSlug -> product.setUrlSlug(newInfo);
            case price -> {
                float newPrice = Float.parseFloat(newInfo);
                product.setPrice(newPrice);
            }
            default -> {
            }
        }

        //TODO: update csv file

    }

    public Product getProduct(String sku) throws ProductNotFoundException {
        Product product = findProductBySku(sku);
        if (product == null) {
            throw new ProductNotFoundException("This product doesn't exist.");
        }

        return product;
    }

    public Product getProductBySlug(String slug) throws ProductNotFoundException {
        for (Product product : products) {
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
            newProduct = findProductBySku(sku);
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
            productToRemove = findProductBySku(sku);
        }
        catch(Exception e) {
            throw e;
        }

        customer.removeFromCart(productToRemove);
    }

    public File downloadProductCatalog() {
        return null;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    ////////////////////////////////////////// HELPER METHODS ///////////////////////////////////////////////////////
    public Product findProductBySku(String sku) throws ProductNotFoundException {
        for (Product product : products) {
            if (product.getSku().equals(sku)) {
                return product;
            }
        }
        throw new ProductNotFoundException("This product doesn't exist.");

    }

    public Customer findCustomerByName(String name) throws UserNotFoundException {
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }

        throw new UserNotFoundException("This customer does not exist");
    }

//    public void addRowToCsv(String[] productInfo) {
//
//    }
//
//    public Product getProductFromCsv(String sku) {
//        String filePath = "your_file.csv"; // Replace with your file path
//
//        List<String[]> rows = readCSV(filePath);
//
//        String[] newRow = {"new data 1", "new data 2", "new data 3"}; // Replace with your data
//
//        rows.add(newRow);
//
//        writeCSV(filePath, rows);
//    }
//
//    public static List<String[]> readCSV(String filePath) {
//        List<String[]> rows = new ArrayList<>();
//        try {
//            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
//            String row;
//            while ((row = csvReader.readLine()) != null) {
//                String[] data = row.split(",");
//                rows.add(data);
//            }
//            csvReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return rows;
//    }
//
//    public static void writeCSV(String filePath, List<String[]> rows) {
//        try {
//            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filePath));
//            for (String[] row : rows) {
//                csvWriter.write(String.join(",", row));
//                csvWriter.newLine();
//            }
//            csvWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}

/////////////////////////////////////////// CUSTOM EXCEPTIONS ////////////////////////////////////////////////////////

