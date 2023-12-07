package com.mywebapp.servlets;

import com.mywebapp.ConfigManager;
import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.*;
import com.mywebapp.logic.models.Product;
import com.mywebapp.logic.models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

@WebServlet(name = "usersServlet", value = {"/registerUser", "/authenticateUser", "/logout", "/changePasscode", "/users", "/grant", "/revoke"})
public class UsersServlet extends HttpServlet {

    LogicFacade logic = new LogicFacade();
    static String type = "user";
    static String isValid = "false";
    static String pass = "guest";
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        
        if (url.equals("/logout")) {
            isValid = "false";
            pass = "guest";
            type = "user";
            request.setAttribute("isLoggedIn", "Successfully logged out");
            request.setAttribute("userType", type);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            response.setStatus(HttpServletResponse.SC_OK);
            dispatcher.forward(request, response);
        }

        else if (url.equals("/users")) {
            if (type.equals("admin")) {
                try {
                    ArrayList<User> users = logic.getUsers();
                    request.setAttribute("users", users);
                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                request.setAttribute("isLoggedIn", UsersServlet.isValid);
                request.setAttribute("userType", UsersServlet.type);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/users.jsp");
                dispatcher.forward(request, response);
            }
        }

        else if (url.equals("/grant") || url.equals("/revoke")) {
            if (type.equals("admin")) {
                String isAllowed = "You are not authorized!";
                String password = request.getParameter("password");
                response.setStatus(HttpServletResponse.SC_OK);

                try {
                    logic.changeRole(password);
                } catch (UserNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } catch (DataMapperException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } catch (UserNotAuthorized e) {
                    request.setAttribute("isAllowed", isAllowed);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/users.jsp");
                dispatcher.forward(request, response);

            }
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        if(url.equals("/authenticateUser")){
            String password = request.getParameter("password");
            boolean exist;
            try {
                exist = logic.doesUserExist(password);
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }

            if (exist){
                isValid = "true";
                try {
                    type = logic.getUserType(password);
                } catch (UserNotFoundException | DataMapperException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                isValid = "false";
            }
            pass = password;
            response.setStatus(HttpServletResponse.SC_OK);

            if (isValid.equals("true")) {
                request.setAttribute("isLoggedIn", isValid);
                request.setAttribute("userType", type);
                try {
                    logic.clearUnknownCart();
                } catch (UserNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } catch (DataMapperException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                request.setAttribute("isLoggedIn", "Incorrect password or password does not exist");
                request.setAttribute("userType", type);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }

        //Checking if the user exists else adding the user to the text file
        if (url.equals("/registerUser")) {
            String password = request.getParameter("password");
            String isRegistered = "Successfully registered";
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                logic.createUser(password);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserAlreadyExistsException e) {
                isRegistered = "Password already exists, try registering with a different password";
            }
            request.setAttribute("isLoggedIn", isRegistered);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }

        //Changing user's passcode
        if (url.equals("/changePasscode")) {
            String password = request.getParameter("password");
            response.setStatus(HttpServletResponse.SC_OK);
            String isChanged = "Successfully changed passcode";
            try {
                logic.setPasscode(pass, password);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserAlreadyExistsException e) {
                isChanged = "Password already exists, try changing to a different password";
            }
            request.setAttribute("isLoggedIn", isValid);
            request.setAttribute("userType", type);
            request.setAttribute("isChanged", isChanged);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }
    }

}
