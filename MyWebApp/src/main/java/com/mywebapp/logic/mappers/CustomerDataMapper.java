package com.mywebapp.logic.mappers;

import com.mywebapp.logic.DataMapperException;
import com.mywebapp.logic.UserNotFoundException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.Customer;
import com.mywebapp.logic.models.Order;
import com.mywebapp.logic.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class CustomerDataMapper {
    
    public static void insert(Customer customer) throws DataMapperException {
        try {
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "INSERT INTO `customers` (`customer_id`, `cart_id`, `name`) VALUES (?, ?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, customer.getCustomerId().toString());
            dbStatement.setString(2, customer.getCartId().toString());
            dbStatement.setString(3, customer.getName());

            dbStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while inserting a row in the Customers table");
        }
    }

    public static Customer findByName(String userName) throws DataMapperException {
        try {
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
        } catch (SQLException e) {
            throw new DataMapperException("Error occurred while getting a row in the CartItems table");
        }

        return null;
    }


}
