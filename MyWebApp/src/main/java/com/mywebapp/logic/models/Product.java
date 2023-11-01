package com.mywebapp.logic.models;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.ProductNotFoundException;
import com.mywebapp.logic.mappers.ProductDataMapper;

import java.util.ArrayList;
import java.util.UUID;

public class Product {
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

    public String[] getCsvFormat() {
        return new String[]{
                this.sku.toString(),
                this.name,
                this.description,
                this.vendor,
                this.urlSlug,
                String.valueOf(this.price)
        };
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

}
