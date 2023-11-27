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
        if (UserDataMapper.passcodeAlreadyInDb(passcode)) {
            throw new UserAlreadyExistsException("This passcode is taken");
        }

        User user = new User(passcode);
        UserDataMapper.insert(user);
    }

    public static User getUser(String userId) throws UserNotFoundException, DataMapperException {
        User user = UserDataMapper.findByGuid(UUID.fromString(userId));

        if (user == null) {
            throw new UserNotFoundException("This customer was not found.");
        }
        return user;
    }

    public static void changePasscode(String oldPasscode, String newPasscode) throws UserNotFoundException, DataMapperException {
        if (!UserDataMapper.passcodeAlreadyInDb(oldPasscode)) {
            throw new UserNotFoundException("This user does not exist");
        }

        User user = UserDataMapper.findUserByPasscode(oldPasscode);
        if (user != null) {
            user.passcode = newPasscode;
        }
        UserDataMapper.update(user);
    }

    public static ArrayList<User> getAllUsers() {
        return UserDataMapper.getAllUsers();
    }

    public static void changeRole(String passcode) throws UserNotFoundException, DataMapperException {
        User user = UserDataMapper.findUserByPasscode(passcode);

        if (user == null) {
            throw new UserNotFoundException("This user does not exist");
        }

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

    public void setCustomerId(UUID customerId) {
        this.userId = userId;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
