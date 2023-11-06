package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.Order;
import com.mywebapp.ConfigManager;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class OrderDataMapper {
    public static void insert(Order order) throws DataMapperException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL), ConfigManager.getDbParameter(ConfigManager.DbParameter.USERNAME), ConfigManager.getDbParameter(ConfigManager.DbParameter.PASSWORD));
            String statement = "INSERT INTO `orders` (`customer_id`, `shipping_address`, `is_shipped`, `items`) VALUES (?, ?, ?, ?)";

            byte[] itemsBytes = serialize(order.getItems());
            ByteArrayInputStream bais = new ByteArrayInputStream(itemsBytes);

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, order.getCustomerId().toString());
            dbStatement.setString(2, order.getShippingAddress());
            dbStatement.setBoolean(3, order.isShipped());
            dbStatement.setBinaryStream(4, bais, itemsBytes.length);
            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while inserting a new order: " + e);
        }

    }

    public static void update(Order order) throws DataMapperException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL), ConfigManager.getDbParameter(ConfigManager.DbParameter.USERNAME), ConfigManager.getDbParameter(ConfigManager.DbParameter.PASSWORD));
            String statement = "UPDATE `orders` SET `is_shipped`=?, `tracking_number`=? WHERE `order_id`=?";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setBoolean(1, order.isShipped());
            dbStatement.setString(2, order.getTrackingNumber().toString());
            dbStatement.setInt(3, order.getOrderId());
            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while updating an order: " + e);
        }

    }

    public static ArrayList<Order> getOrders(int orderId, UUID customerId) throws DataMapperException {
        ArrayList<Order> orders = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL), ConfigManager.getDbParameter(ConfigManager.DbParameter.USERNAME), ConfigManager.getDbParameter(ConfigManager.DbParameter.PASSWORD));
            PreparedStatement dbStatement;

            if (orderId == -1 && customerId == null) { //get all orders
                String statement = "SELECT * FROM `orders`";
                dbStatement = db.prepareStatement(statement);

            }
            else if (customerId == null) { // get a customer's orders
                String statement = "SELECT * FROM `orders` WHERE `customer_id`=?";
                dbStatement = db.prepareStatement(statement);
                dbStatement.setString(1, customerId.toString());
            }
            else { // get a specific order
                String statement = "SELECT * FROM `orders` WHERE `order_id`=?";
                dbStatement = db.prepareStatement(statement);
                dbStatement.setInt(1, orderId);
            }

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                UUID customer_id = UUID.fromString(rs.getString("customer_id"));
                String shipping_address = rs.getString("shipping_address");
                UUID tracking_number = UUID.fromString(rs.getString("tracking_number"));
                boolean is_shipped = rs.getBoolean("is_shipped");
                ArrayList<CartItem> items = deserialize(rs.getBinaryStream("items"));

                Order order = new Order(order_id, customer_id, shipping_address, tracking_number, is_shipped, items);
                orders.add(order);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while retrieving orders: " + e);
        }

        return orders;
    }


    private static byte[] serialize(ArrayList<CartItem> items) throws DataMapperException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(items);
        } catch (IOException e) {
            throw new DataMapperException("Error occurred while serializing order items");
        }
        return baos.toByteArray();
    }

    private static ArrayList<CartItem> deserialize(InputStream is) throws DataMapperException {
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            return (ArrayList<CartItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while deserializing order items");
        }

    }

}
