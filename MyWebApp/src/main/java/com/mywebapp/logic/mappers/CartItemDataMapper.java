package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.FileDownloadException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.Product;
import com.mywebapp.ConfigManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class CartItemDataMapper {

    public static CartItem findBySkuAndCartId(UUID p_id, UUID cart_id) throws DataMapperException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "SELECT * FROM `cartItems` WHERE `sku`=? AND `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, p_id.toString());
            dbStatement.setString(2, cart_id.toString());

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID sku = UUID.fromString(rs.getString("sku"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));
                int quantity = rs.getInt("quantity");

                Product product = ProductDataMapper.findBySkuOrSlug(sku, "");
                return new CartItem(product, cartId, quantity);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while getting a row in the CartItems table: " + e);
        }

        return null;
    }

    
    public static void insert(CartItem item) throws DataMapperException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "INSERT INTO `cartItems` (`sku`, `cart_id`, `quantity`) VALUES (?, ?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, item.getSku().toString());
            dbStatement.setString(2, item.getCartId().toString());
            dbStatement.setInt(3, item.getQuantity());

            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while inserting a row in the CartItems table: " + e);
        }
    }

    
    public static void update(CartItem item) throws DataMapperException {
        try {

            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "UPDATE `cartItems` SET `quantity`=? WHERE `sku`=? AND `cart_id`=?";

            PreparedStatement dbStatement = db.prepareStatement(statement);

            dbStatement.setInt(1, item.getQuantity());
            dbStatement.setString(2, item.getSku().toString());
            dbStatement.setString(3, item.getCartId().toString());
            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while updating a row in the CartItems table: " + e);
        }
    }

    public static void delete(CartItem item) throws DataMapperException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "DELETE FROM `cartItems` where `sku`=? AND `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, item.getSku().toString());
            dbStatement.setString(2, item.getCartId().toString());
            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while deleting a row from the CartItems table: " + e);
        }
    }

    public static ArrayList<CartItem> getAllItemsInCart(UUID cart_id) throws DataMapperException{
        ArrayList<CartItem> items = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "SELECT * FROM `cartItems` WHERE `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, cart_id.toString());

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID sku = UUID.fromString(rs.getString("sku"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));
                int quantity = rs.getInt("quantity");

                Product product = ProductDataMapper.findBySkuOrSlug(sku, "");
                CartItem item = new CartItem(product, cartId, quantity);
                items.add(item);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while retrieving all items in the cart: " + e);
        }

        return items;
    }

    public static void deleteAllItemsInCart(UUID cart_id) throws DataMapperException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "DELETE FROM `cartItems` where `cart_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, cart_id.toString());
            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while deleting all items in the cart: " + e);
        }
    }

}
