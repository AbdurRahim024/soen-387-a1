package com.mywebapp.servlets;

import com.mywebapp.ConfigManager;
import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.FileDownloadException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Scanner;

@WebServlet(name = "usersServlet", value = {"/registerUser", "/authenticateUser", "/logout"})
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
                    String customer_id = logic.createCustomer();
                    addToUsers(customer_id, password, "user");
                }
            }  catch (FileNotFoundException | DataMapperException | CsvValidationException | FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            request.setAttribute("isLoggedIn", isRegistered);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }
    }

}
