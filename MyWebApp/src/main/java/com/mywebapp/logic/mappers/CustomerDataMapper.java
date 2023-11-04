package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.Customer;

import java.sql.*;
import java.util.UUID;

public class CustomerDataMapper {
    
    public static void insert(Customer customer) throws DataMapperException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "INSERT INTO `customers` (`customer_id`, `cart_id`, `name`) VALUES (?, ?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, customer.getCustomerId().toString());
            dbStatement.setString(2, customer.getCartId().toString());
            dbStatement.setString(3, customer.getName());

            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while inserting a row in the Customers table: " + e);
        }
    }

    public static Customer findByName(String userName) throws DataMapperException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "SELECT * FROM `customers` WHERE `name`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, userName);

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID customerId = UUID.fromString(rs.getString("customer_id"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));
                String name = rs.getString("name");

                return new Customer(customerId, cartId, name);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while getting a row in the CartItems table: " + e);
        }

        return null;
    }


}
