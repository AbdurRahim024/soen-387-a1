package com.mywebapp.servlets;

import com.mywebapp.ConfigManager;
import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.*;
import com.mywebapp.logic.models.Order;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ordersServlet", value = {"/orders/*", "/orderForm", "/createOrder", "/shipOrder", "/claim"})
public class OrdersServlet extends HttpServlet{
    LogicFacade logic = new LogicFacade();
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        String url = request.getRequestURI();

        if (url.equals("/orderForm")){
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/orderForm.jsp");
            try {
                dispatcher.forward(request, response);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }

        else if (url.equals("/orders")){
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            String password = UsersServlet.pass;
            String type = UsersServlet.type;
            boolean found = false;
            File users_file = null;
            String customerId = null;
            ArrayList<Order> orders = null;
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                users_file = new File(ConfigManager.getCsvPath());

                try (CSVReader reader = new CSVReader(new FileReader(users_file))) {
                    String[] line;
                    while ((line = reader.readNext()) != null) {
                        String newPass = line[1];
                        type = line[2];
                        if (newPass.equals("password")) { // skip if the first row (titles) is being read
                            continue;
                        }
                        if (password.equals(newPass)) {
                            found = true;
                            break;
                        }
                    }
                    customerId = getCustomerID(password);
                }

                if (found) {
                    if (type.equals("user")) {
                        orders = logic.getOrdersByUser(customerId);
                    } else {
                        orders = logic.getAllOrders();
                    }
                }
            } catch (IOException | FileDownloadException | CsvValidationException | DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch(UserNotFoundException | OrderNotFoundException e){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

            request.setAttribute("orders", orders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/orders.jsp");

            try {
                dispatcher.forward(request, response);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }

        else if(url.startsWith("/orders/:")) {
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            String[] fullUrl = url.split("/");
            String urlSlug = fullUrl[fullUrl.length-1];
            String[] fullOrderId = urlSlug.split(":");
            int orderId = Integer.parseInt(fullOrderId[fullOrderId.length-1]);
            String password = UsersServlet.pass;
            String customerId = null;
            Order order = null;
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                customerId = getCustomerID(password);
                order = logic.getOrderDetails(UsersServlet.type, customerId, orderId);
            } catch (IOException | CsvValidationException | FileDownloadException | DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException | OrderNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (CustomerOrderMismatchException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            request.setAttribute("order", order);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/orderListing.jsp");
            try {
                dispatcher.forward(request,response);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String url = request.getRequestURI();
        ArrayList<Order> orders = null;
        if (url.equals("/createOrder")){
            String password = UsersServlet.pass;
            String customerId = null;
            String shippingAddress = request.getParameter("shippingAddress");
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                customerId = getCustomerID(password);
                logic.createOrder(customerId, shippingAddress);
            } catch (CsvValidationException | FileDownloadException | DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            response.sendRedirect("/cart");
        }
        if (url.equals("/shipOrder")){
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                logic.shipOrder(orderId);
                orders = logic.getAllOrders();
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch(OrderNotFoundException e ) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            request.setAttribute("orders", orders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("orders.jsp");
            dispatcher.forward(request, response);

        }

        //TODO: test /claim
        else if (url.equals("/claim")) {
            int order_id = (Integer) request.getAttribute("order_id");
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                logic.setOrderOwner(order_id, UsersServlet.pass);
            } catch (OrderNotFoundException | OrderAlreadyBelongsToCustomerException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("orders.jsp");
            dispatcher.forward(request, response);
        }



    }
    private String getCustomerID(String password) throws CsvValidationException, IOException, FileDownloadException {
        String customerId = "";
        try (CSVReader reader = new CSVReader(new FileReader(ConfigManager.getCsvPath()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String newPass = line[1];
                if (newPass.equals("password")) {
                    continue;
                }
                if(password.equals(newPass)){
                    customerId = line[0];
                    break;
                }
            }
        }
        return customerId;
    }
}
