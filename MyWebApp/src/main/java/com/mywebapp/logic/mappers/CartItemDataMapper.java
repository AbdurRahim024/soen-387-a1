package com.mywebapp.logic.mappers;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.Order;
import com.mywebapp.logic.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class CartItemDataMapper {
    
    public static CartItem findBySkuAndCartId(UUID p_id, UUID cart_id) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "SELECT * FROM `cartItems` WHERE `sku`=? AND `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, p_id.toString());
            dbStatement.setString(2, cart_id.toString());

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID sku = UUID.fromString(rs.getString("sku"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));
                String name = rs.getString("name");
                String description = rs.getString("description");
                String vendor = rs.getString("vendor");
                String urlSlug = rs.getString("urlSlug");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Product product = new Product(sku, name, description, vendor, urlSlug, price);
                return new CartItem(product, cartId, quantity);
            }
        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while getting a row in the CartItems table");
        }

        return null;
    }

    
    public static void insert(CartItem item) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "INSERT INTO `cartItems` (`sku`, `cart_id`, `name`, `description`, `vendor`, `urlSlug`, `price`, `quantity`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, item.getSku().toString());
            dbStatement.setString(2, item.getCartId().toString());
            dbStatement.setString(3, item.getName());
            dbStatement.setString(4, item.getDescription());
            dbStatement.setString(5, item.getVendor());
            dbStatement.setString(6, item.getUrlSlug());
            dbStatement.setDouble(7, item.getPrice());
            dbStatement.setInt(8, item.getQuantity());

            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while inserting a row in the CartItems table");
        }
    }

    
    public static void update(CartItem item) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "UPDATE `cartItems` SET `name`=?, `description`=?, `vendor`=?, `urlSlug`=?, `price`=?, `quantity`=? WHERE `sku`=? AND `cart_id`=?";

            PreparedStatement dbStatement = db.prepareStatement(statement);

            dbStatement.setString(1, item.getName());
            dbStatement.setString(2, item.getDescription());
            dbStatement.setString(3, item.getVendor());
            dbStatement.setString(4, item.getUrlSlug());
            dbStatement.setDouble(5, item.getPrice());
            dbStatement.setInt(6, item.getQuantity());
            dbStatement.setString(7, item.getSku().toString());
            dbStatement.setString(8, item.getCartId().toString());
            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while updating a row in the CartItems table");
        }
    }

    public static void delete(CartItem item) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");

            String statement = "DELETE FROM `cartItems` where `sku`=? AND `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, item.getSku().toString());
            dbStatement.setString(2, item.getCartId().toString());
            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while deleting a row from the CartItems table.");
        }
    }

    public static ArrayList<CartItem> getAllItemsInCart(UUID cart_id) throws DataMapperException{
        ArrayList<CartItem> items = new ArrayList<>();

        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "SELECT * FROM `cartItems` WHERE `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, cart_id.toString());

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID sku = UUID.fromString(rs.getString("sku"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));
                String name = rs.getString("name");
                String description = rs.getString("description");
                String vendor = rs.getString("vendor");
                String urlSlug = rs.getString("urlSlug");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Product product = new Product(sku, name, description, vendor, urlSlug, price);
                CartItem item = new CartItem(product, cartId, quantity);
                items.add(item);
            }
        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while retrieving all items in the cart");
        }

        return items;
    }

    public static void deleteAllItemsInCart(UUID cart_id) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");

            String statement = "DELETE FROM `cartItems` where `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, cart_id.toString());
            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while deleting all items in the cart.");
        }
    }

}
