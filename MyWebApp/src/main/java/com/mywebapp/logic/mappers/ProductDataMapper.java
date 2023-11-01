package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class ProductDataMapper {

    public static Product findBySkuOrSlug(UUID p_id, String slug) throws DataMapperException {

        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement;
            PreparedStatement dbStatement;

            if (slug.isEmpty()) {
                statement = "SELECT * FROM `products` WHERE `sku`=?";
                dbStatement = db.prepareStatement(statement);
                dbStatement.setString(1, p_id.toString());
            }
            else {
                statement = "SELECT * FROM `products` WHERE `urlSlug`=?";
                dbStatement = db.prepareStatement(statement);
                dbStatement.setString(1, slug);
            }

            ResultSet rs = dbStatement.executeQuery();
            Product product;

            while (rs.next()) {
                UUID sku = UUID.fromString(rs.getString("sku"));
                String name = rs.getString("name");
                String description = rs.getString("description");
                String vendor = rs.getString("vendor");
                String urlSlug = rs.getString("urlSlug");
                double price = rs.getDouble("price");

                product = new Product(sku, name, description, vendor, urlSlug, price);
                return product;
            }
        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while getting a row in the Products table");
        }

        return null;
    }

    public static void insert(Product product) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "INSERT INTO `products` (`sku`, `name`, `description`, `vendor`, `urlSlug`, `price`) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, product.getSku().toString());
            dbStatement.setString(2, product.getName());
            dbStatement.setString(3, product.getDescription());
            dbStatement.setString(4, product.getVendor());
            dbStatement.setString(5, product.getUrlSlug());
            dbStatement.setDouble(6, product.getPrice());
            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while inserting a row in the Products table");
        }
    }

    
    public static void update(Product product) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "UPDATE `products` SET `name`=?, `description`=?, `vendor`=?, `urlSlug`=?, `price`=? WHERE `sku`=?";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, product.getName());
            dbStatement.setString(2, product.getDescription());
            dbStatement.setString(3, product.getVendor());
            dbStatement.setString(4, product.getUrlSlug());
            dbStatement.setDouble(5, product.getPrice());
            dbStatement.setString(6, product.getSku().toString());
            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while updating a row in the Products table");
        }
    }

    
    public static void delete(Product product) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");

            String statement = "DELETE FROM `products` where `sku`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, product.getSku().toString());
            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while deleting a row from the Products table.");
        }
    }

    public static boolean findByAttributes(String name, String description, String vendor, String urlSlug, double price) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "SELECT * FROM `products` WHERE `name`=? AND `description`=? AND `vendor`=? AND `urlSlug`=? AND `price`=?";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, name);
            dbStatement.setString(2, description);
            dbStatement.setString(3, vendor);
            dbStatement.setString(4, urlSlug);
            dbStatement.setDouble(5, price);
            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while searching for a row in the Products table");
        }

        return false;
    }

    public static ArrayList<Product> getAllProducts() throws DataMapperException {
        ArrayList<Product> products = new ArrayList<>();

        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "SELECT * FROM `products`";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID sku = UUID.fromString(rs.getString("sku"));
                String name = rs.getString("name");
                String description = rs.getString("description");
                String vendor = rs.getString("vendor");
                String urlSlug = rs.getString("urlSlug");
                double price = rs.getDouble("price");

                Product product = new Product(sku, name, description, vendor, urlSlug, price);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while getting a row in the Products table");
        }

        return products;
    }


}
