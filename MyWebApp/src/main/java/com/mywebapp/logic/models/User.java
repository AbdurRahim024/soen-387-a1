package com.mywebapp.logic.models;

import com.mywebapp.logic.custom_errors.UserAlreadyExistsException;
import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.UserNotFoundException;
import com.mywebapp.logic.mappers.UserDataMapper;

import java.util.ArrayList;
import java.util.UUID;

public class User {

    public enum UserType {
        CUSTOMER,
        STAFF
    }

    private UUID userId; //primary key
    private UUID cartId;
    private String passcode;
    private UserType userType;

    public User(String passcode) {
        this.userId = UUID.randomUUID();
        this.passcode = passcode;
        this.userType = UserType.CUSTOMER;

        Cart cart = new Cart();
        this.cartId = cart.getCartId();
    }

    public User(UUID userId, UUID cartId, String passcode, String userType) {
        this.userId = userId;
        this.cartId = cartId;
        this.passcode = passcode;
        this.userType = UserType.valueOf(userType);
    }

    //*******************************************************************************
    //* domain logic functions
    //*******************************************************************************


    public void clearCart() throws DataMapperException {
        CartItem.deleteAllItemsInCart(this.cartId);
    }

    public static void addUserToDb(String passcode) throws DataMapperException, UserAlreadyExistsException {
        if (!UserDataMapper.findUsers(null, passcode).isEmpty()) {
            throw new UserAlreadyExistsException("This passcode is taken");
        }

        User user = new User(passcode);
        UserDataMapper.insert(user);
    }

    public static User getUser(String userId) throws UserNotFoundException, DataMapperException {
        ArrayList<User> users = UserDataMapper.findUsers(UUID.fromString(userId), "");

        if (users.isEmpty()) {
            throw new UserNotFoundException("This user was not found.");
        }
        return users.get(0);
    }

    public static void changePasscode(String oldPasscode, String newPasscode) throws UserNotFoundException, DataMapperException {
        if (!UserDataMapper.findUsers(null, oldPasscode).isEmpty()) {
            throw new UserNotFoundException("This user does not exist");
        }

        ArrayList<User> users = UserDataMapper.findUsers(null, oldPasscode);
        if (users.isEmpty()) {
            throw new UserNotFoundException("This user was not found.");
        }

        User user = users.get(0);
        user.passcode = newPasscode;
        UserDataMapper.update(user);
    }

    public static ArrayList<User> getAllUsers() throws DataMapperException {
        return UserDataMapper.findUsers(null, "");
    }

    public static void changeRole(String passcode) throws UserNotFoundException, DataMapperException {
        ArrayList<User> users = UserDataMapper.findUsers(null, passcode);
        if (users.isEmpty()) {
            throw new UserNotFoundException("This user was not found.");
        }

        User user = users.get(0);

        if (user.userType == UserType.CUSTOMER) {
            user.userType = UserType.STAFF;
        } else {
            user.userType = UserType.CUSTOMER;
        }

        UserDataMapper.update(user);
    }


    //*******************************************************************************
    //* getters and setters
    //*******************************************************************************

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
