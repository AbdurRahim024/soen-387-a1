package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.models.Product;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "userServlet", value = {"/registerUser", "/authenticateUser"})
public class UsersServlet {
    LogicFacade logic = new LogicFacade();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        File users_file = new File("/Users/abdurrahimgigani/Documents/SOEN 387/soen-387-a1/MyWebApp/src/main/java/com/mywebapp/servlets/users.csv");
        if(url.equals("/authenticateUser")){
            String password = request.getParameter("password");
            String type = "user";
            String isValid = "false";
            String[] message = {isValid, type};
            try (CSVReader reader = new CSVReader(new FileReader(users_file))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    String newPass = line[0];
                    type = line[1];
                    if (newPass.equals("password")) { // skip if the first row (titles) is being read
                        continue;
                    }

                    if(password.equals(newPass)){
                        message[0] = "true";
                        message[1] = type;
                    }
                }
                request.setAttribute("message", message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        //Checking if the user exists else adding the user to the text file
        if (url.equals("/registerUser")) {
            String password = request.getParameter("password");
            File users_file = new File("/Users/abdurrahimgigani/Documents/SOEN 387/soen-387-a1/MyWebApp/src/main/java/com/mywebapp/servlets/users.csv");
            boolean message = true;
            try (CSVReader reader = new CSVReader(new FileReader(users_file))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    String newPass = line[0];
                    if (newPass.equals("password")) { // skip if the first row (titles) is being read
                        continue;
                    }
                    if(password.equals(newPass)){
                        message = false;
                    }
                }
                request.setAttribute("message", message);
                if (message) {
                    logic.createCustomer(password);
                }
            }  catch (FileNotFoundException | DataMapperException | CsvValidationException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
