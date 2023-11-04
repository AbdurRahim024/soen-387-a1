package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.DataMapperException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

@WebServlet(name = "userServlet", value = {"/registerUser", "/authenticateUser"})
public class UsersServlet {
    LogicFacade logic = new LogicFacade();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        if(url.equals("/authenticateUser")){
            String password = request.getParameter("password");
            boolean message = false;
            try {
                File file = new File("users.txt");
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (password.equals(data)) {
                        message = true;
                    }
                }
                myReader.close();
                request.setAttribute("message", message);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            response.setStatus(HttpServletResponse.SC_OK);

        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        //Checking if the user exists else adding the user to the text file
        if (url.equals("/registerUser")) {
            String password = request.getParameter("password");
            String message = "User has been added.";
            try {
                File file = new File("users.txt");
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (password.equals(data)) {
                        message = "Password already exist, try a different password.";
                    }
                }
                myReader.close();
                request.setAttribute("message", message);
                logic.createCustomer(password);
            } catch (FileNotFoundException | DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);

        }
    }

}
