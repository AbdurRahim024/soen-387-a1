package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.User;
import com.mywebapp.ConfigManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class UserDataMapper {
    
    public static void insert(User user) throws DataMapperException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "INSERT INTO `customers` (`customer_id`, `cart_id`) VALUES (?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, user.getUserId().toString());
            dbStatement.setString(2, user.getCartId().toString());

            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while inserting a row in the Customers table: " + e);
        }
    }

    public static User findByGuid(UUID user_id) throws DataMapperException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL), ConfigManager.getDbParameter(ConfigManager.DbParameter.USERNAME), ConfigManager.getDbParameter(ConfigManager.DbParameter.PASSWORD));
            String statement = "SELECT * FROM `customers` WHERE `customer_id`=?";
            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, user_id.toString());

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID userId = UUID.fromString(rs.getString("customer_id"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));

                return new User(userId, cartId, "passcode", "STAFF");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while getting a row in the CartItems table: " + e);
        }

        return null;
    }

    //TODO: fill these up
    public static void update(User user) throws DataMapperException {
        try {

            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            String statement = "UPDATE `users` SET `passcode`=?, `user_type`=? WHERE `cart_id`=?";

            PreparedStatement dbStatement = db.prepareStatement(statement);

            dbStatement.setString(1, user.getPasscode());
            dbStatement.setString(2, user.getUserType().toString());
            dbStatement.setString(3, user.getCartId().toString());
            dbStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while updating a row in the CartItems table: " + e);
        }
    }

    public static boolean passcodeAlreadyInDb(String passcode) {
        return false;
    }

    public static User findUserByPasscode(String passcode) {
        return null;
    }

    public static ArrayList<User> getAllUsers() {
        return null;
    }


}
