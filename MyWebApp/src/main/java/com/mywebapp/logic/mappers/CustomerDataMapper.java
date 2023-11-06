package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.Customer;
import com.mywebapp.ConfigManager;

import java.sql.*;
import java.util.UUID;

public class CustomerDataMapper {
    
    public static void insert(Customer customer) throws DataMapperException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection(ConfigManager.getDbUrl(), ConfigManager.getDbUsername(), ConfigManager.getDbPassword());
            String statement = "INSERT INTO `customers` (`customer_id`, `cart_id`) VALUES (?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, customer.getCustomerId().toString());
            dbStatement.setString(2, customer.getCartId().toString());

            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while inserting a row in the Customers table: " + e);
        }
    }

    public static Customer findByGuid(UUID customer_id) throws DataMapperException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection(ConfigManager.getDbUrl(), ConfigManager.getDbUsername(), ConfigManager.getDbPassword());
            String statement = "SELECT * FROM `customers` WHERE `customer_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, customer_id.toString());

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID customerId = UUID.fromString(rs.getString("customer_id"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));

                return new Customer(customerId, cartId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while getting a row in the CartItems table: " + e);
        }

        return null;
    }


}
