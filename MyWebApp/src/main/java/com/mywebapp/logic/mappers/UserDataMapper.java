package com.mywebapp.logic.mappers;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.Order;
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
            String statement = "INSERT INTO `users` (`user_id`, `cart_id`, `passcode`, `user_type`) VALUES (?, ?, ?, ?)";

            PreparedStatement dbStatement = db.prepareStatement(statement);
            dbStatement.setString(1, user.getUserId().toString());
            dbStatement.setString(2, user.getCartId().toString());
            dbStatement.setString(3, user.getPasscode());
            dbStatement.setString(4, user.getUserType().name());

            dbStatement.executeUpdate();

            dbStatement.close();
            db.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while inserting a row in the Users table: " + e);
        }
    }


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

            dbStatement.close();
            db.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while updating a row in the CartItems table: " + e);
        }
    }

    public static ArrayList<User> findUsers(UUID user_id, String passcode) throws DataMapperException {
        ArrayList<User> users = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection db = DriverManager.getConnection(ConfigManager.getDbParameter(ConfigManager.DbParameter.URL));
            PreparedStatement dbStatement;

            if (user_id == null && passcode.isEmpty()) { //get all users
                String statement = "SELECT * FROM `users`";
                dbStatement = db.prepareStatement(statement);

            }
            else if (passcode.isEmpty()) { // get a user by id
                String statement = "SELECT * FROM `users` WHERE `user_id`=?";
                dbStatement = db.prepareStatement(statement);
                dbStatement.setString(1, user_id.toString());
            }
            else { // get a user by passcode
                String statement = "SELECT * FROM `users` WHERE `passcode`=?";
                dbStatement = db.prepareStatement(statement);
                dbStatement.setString(1, passcode);
            }

            ResultSet rs = dbStatement.executeQuery();

            while (rs.next()) {
                UUID userId = UUID.fromString(rs.getString("user_id"));
                UUID cartId = UUID.fromString(rs.getString("cart_id"));
                String passCode = rs.getString("passcode");
                String userType = rs.getString("user_type");

                User user = new User(userId, cartId, passCode, userType);
                users.add(user);
            }

            dbStatement.close();
            rs.close();
            db.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DataMapperException("Error occurred while retrieving orders: " + e);
        }

        return users;
    }

}
