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

@WebServlet(name = "ordersServlet", value = {"/orders/*", "/orderForm", "/createOrder", "/shipOrder", "/claimOrder"})
public class OrdersServlet extends HttpServlet{
    LogicFacade logic = new LogicFacade();
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (url.equals("/orderForm")){
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/orderForm.jsp");

            dispatcher.forward(request, response);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        }

        else if (url.equals("/orders")){
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);

            ArrayList<Order> orders = null;
            try {
                if (UsersServlet.type.equals("user")) {
                    orders = logic.getOrdersByUser(UsersServlet.pass);
                } else {
                    orders = logic.getAllOrders();
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (DataMapperException | OrderNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            request.setAttribute("orders", orders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/orders.jsp");


            dispatcher.forward(request, response);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        }

        else if(url.startsWith("/orders/:")) {
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            String[] fullUrl = url.split("/");
            String urlSlug = fullUrl[fullUrl.length-1];
            String[] fullOrderId = urlSlug.split(":");
            int orderId = Integer.parseInt(fullOrderId[fullOrderId.length-1]);
            Order order = null;
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                order = logic.getOrderDetails(UsersServlet.type, UsersServlet.pass, orderId);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException | OrderNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (CustomerOrderMismatchException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            request.setAttribute("order", order);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/orderListing.jsp");

            dispatcher.forward(request,response);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String url = request.getRequestURI();
        ArrayList<Order> orders = null;


        if (url.equals("/createOrder")){
            String shippingAddress = request.getParameter("shippingAddress");

            try {
                logic.createOrder(UsersServlet.pass, shippingAddress);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            response.sendRedirect("/cart");
        }

        else if (url.equals("/shipOrder")){
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
        else if (url.equals("/claimOrder")) {
            int order_id = (Integer) request.getAttribute("order_id");
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                logic.setOrderOwner(order_id, UsersServlet.pass);
                if (UsersServlet.type.equals("user")) {
                    orders = logic.getOrdersByUser(UsersServlet.pass);
                } else {
                    orders = logic.getAllOrders();
                }

            } catch (OrderNotFoundException | OrderAlreadyBelongsToCustomerException | UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            request.setAttribute("orders", orders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("orders.jsp");
            dispatcher.forward(request, response);
        }



    }
}
