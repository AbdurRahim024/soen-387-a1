package com.mywebapp.servlets;

import com.mywebapp.ConfigManager;
import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.*;
import com.mywebapp.logic.models.Product;
import com.mywebapp.logic.models.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
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
    static String pass = "";
    private void addToUsers(String customerId, String password, String type) throws IOException, FileDownloadException{
        FileWriter fileWriter = new FileWriter(ConfigManager.getCsvPath(), true);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        String newData = customerId + "," +  password + "," +  type;

        writer.write(newData);
        writer.newLine();
        writer.close();

    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        if (url.equals("/logout")) {
            isValid = "false";
            pass = "";
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

        //TODO: check dispatched
        else if (url.equals("/grant") || url.equals("/revoke")) {
            if (type.equals("admin")) {
                String passcode = request.getParameter("passcode");
                response.setStatus(HttpServletResponse.SC_OK);

                try {
                    logic.changeRole(passcode);
                } catch (UserNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } catch (DataMapperException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } catch (UserNotAuthorized e) {
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
            File users_file = null;
            try {
                users_file = new File(ConfigManager.getCsvPath());
            } catch (FileDownloadException e) {
                throw new RuntimeException(e);
            }
            String password = request.getParameter("password");
            response.setStatus(HttpServletResponse.SC_OK);
            try (CSVReader reader = new CSVReader(new FileReader(users_file))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    String newPass = line[1];
                    type = line[2];
                    if (newPass.equals("password")) { // skip if the first row (titles) is being read
                        continue;
                    }

                    if(password.equals(newPass)){
                        UsersServlet.isValid = "true";
                        pass = password;
                        break;
                    }
                }
            } catch (CsvValidationException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            if (isValid.equals("true")) {
                request.setAttribute("isLoggedIn", isValid);
                request.setAttribute("userType", type);
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
            File users_file = null;
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                users_file = new File(ConfigManager.getCsvPath());
            } catch (FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            String isRegistered = "Successfully registered";

            try (CSVReader reader = new CSVReader(new FileReader(users_file))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    if (line.length < 2) {
                        break;
                    }
                    String newPass = line[1];
                    if (newPass.equals("password")) { // skip if the first row (titles) is being read
                        continue;
                    }
                    if(password.equals(newPass)){
                        isRegistered = "Password already exists, try registering with a different password";
                        break;
                    }
                }

                if (isRegistered.equals("Successfully registered")) {
                    logic.createUser("emma");
                    addToUsers("000001", password, "user");
                }
            }  catch (FileNotFoundException | DataMapperException | CsvValidationException | FileDownloadException |
                      UserAlreadyExistsException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            request.setAttribute("isLoggedIn", isRegistered);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }

        //Changing user's passcode
        if (url.equals("/changePasscode")) {
            String password = request.getParameter("password");
            File users_file = null;
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                users_file = new File(ConfigManager.getCsvPath());
            } catch (FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            String isChanged = "Successfully changed passcode";

            try (CSVReader reader = new CSVReader(new FileReader(users_file))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    if (line.length < 2) {
                        break;
                    }
                    String newPass = line[1];
                    if (newPass.equals("password")) { // skip if the first row (titles) is being read
                        continue;
                    }
                    if(password.equals(newPass)){
                        isChanged = "Password already exists, try changing to a different password";
                        break;
                    }
                }

                if (isChanged.equals("Successfully changed passcode")) {
                    // TODO: to change passcode call logic method()
                }
            }  catch (FileNotFoundException | CsvValidationException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            request.setAttribute("isLoggedIn", isValid);
            request.setAttribute("userType", type);
            request.setAttribute("isChanged", isChanged);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }
    }

}
