package com.mywebapp.logic.models;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.mappers.ProductDataMapper;

import java.util.UUID;

public class Product {
    private String name;
    private String description;
    private String vendor;
    private String urlSlug;
    private UUID sku;
    private double price;

    public Product(String name, String description, String vendor, String urlSlug, double price) {
        this.sku = UUID.randomUUID();

        this.name = name;
        this.description = description;
        this.vendor = vendor;
        this.urlSlug = urlSlug;
        this.price = price;
    }

    public Product(String sku, String name, String description, String vendor, String urlSlug, double price) {
        this.sku = UUID.fromString(sku);

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

    public static boolean productAlreadyExists(String name, String description, String vendor, String urlSlug, double price) {
        return ProductDataMapper.findByAttributes(name, description, vendor, urlSlug, price);
    }

    public static Product findProductByGuid(UUID sku) throws DataMapperException {
        //TODO: throw ProductNotFoundException here
        return ProductDataMapper.findByGuid(sku);
    }

    public static Product findProductBySlug(String urlSlug) throws DataMapperException {
        //TODO: throw ProductNotFoundException here
        return ProductDataMapper.findBySlug(urlSlug);
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

    public String getSku() {
        return sku.toString();
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
