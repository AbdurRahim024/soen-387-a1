package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.Cart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CartDataMapper {
    public static void insert(Cart cart) throws DataMapperException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection("jdbc:mysql://localhost:3306/soen_387", "root", "killmenow");
            String statement = "INSERT INTO `carts` (`cart_id`) VALUES (?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, cart.getCartId().toString());

            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while inserting a row in the Carts table: " + e);
        }
    }
}
