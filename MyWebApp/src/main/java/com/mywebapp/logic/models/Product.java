package com.mywebapp.logic.models;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.FileDownloadException;
import com.mywebapp.logic.custom_errors.ProductNotFoundException;
import com.mywebapp.logic.mappers.ProductDataMapper;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Product implements Serializable {
    private String name;
    private String description;
    private String vendor;
    private String urlSlug;
    private UUID sku; //primary key
    private double price;

    public Product(String name, String description, String vendor, String urlSlug, double price) {
        this.sku = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.vendor = vendor;
        this.urlSlug = urlSlug;
        this.price = price;
    }

    public Product(UUID sku, String name, String description, String vendor, String urlSlug, double price) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.vendor = vendor;
        this.urlSlug = urlSlug;
        this.price = price;
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************

    public void addProductToDb() throws DataMapperException {
        ProductDataMapper.insert(this);
    }
    
    public void updateProductInDb(String name, String description, String vendor, String urlSlug, double price) throws DataMapperException {
        this.setName(name);
        this.setDescription(description);
        this.setVendor(vendor);
        this.setUrlSlug(urlSlug);
        this.setPrice(price);
        ProductDataMapper.update(this);
    }

    public static boolean productAlreadyExists(String name, String description, String vendor, String urlSlug, double price) throws DataMapperException {
        return ProductDataMapper.findByAttributes(name, description, vendor, urlSlug, price);
    }

    public static Product findProductBySku(UUID sku) throws DataMapperException, ProductNotFoundException {
        Product product = ProductDataMapper.findBySkuOrSlug(sku, "");

        if (product == null) {
            throw new ProductNotFoundException("This SKU is not associated to any Product");
        }
        return product;
    }

    public static Product findProductBySlug(String urlSlug) throws DataMapperException, ProductNotFoundException {
        Product product = ProductDataMapper.findBySkuOrSlug(null, urlSlug);

        if (product == null) {
            throw new ProductNotFoundException("This urlSlug is not associated to any Product");
        }
        return product;
    }

    public static ArrayList<Product> getAllProducts() throws DataMapperException {
        return ProductDataMapper.getAllProducts();
    }

    public static File downloadProductCatalog() throws DataMapperException, FileDownloadException {
        ArrayList<Product> products = getAllProducts();
        return writeToCSV(products, "products.csv");

    }


    //*******************************************************************************
    //* getters and setters
    //*******************************************************************************

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getUrlSlug() {
        return urlSlug;
    }

    public void setUrlSlug(String urlSlug) {
        this.urlSlug = urlSlug;
    }

    public UUID getSku() {
        return sku;
    }

    public void setSku(UUID sku) {
        this.sku = sku;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }


    //*******************************************************************************
    //* helper methods
    //*******************************************************************************

    private static File writeToCSV(ArrayList<Product> products, String filePath) throws FileDownloadException {
        String csvFile = filePath;

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {

            String[] header = {"sku", "name", "description", "vendor", "urlSlug", "price"};
            writer.writeNext(header);

            for (Product product : products) {
                String[] data = {
                        product.sku.toString(),
                        product.name,
                        product.description,
                        product.vendor,
                        product.urlSlug,
                        String.valueOf(product.price)
                };
                writer.writeNext(data);
            }

            System.out.println("CSV file written successfully!");
            return new File(csvFile);
        } catch (IOException e) {
            throw new FileDownloadException("Error occurred while writing products file " + e);
        }
    }

}
