package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.FileDownloadException;
import com.mywebapp.logic.custom_errors.ProductNotFoundException;
import com.mywebapp.logic.custom_errors.UserNotFoundException;
import com.mywebapp.logic.models.Product;
import com.mywebapp.ConfigManager;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "cartServlet", value = {"/cart/*"})
public class CartServlet extends HttpServlet {
    LogicFacade logic = new LogicFacade();
    private String getCustomerID(String password) throws CsvValidationException, IOException, FileDownloadException { //TODO: private methods should go at the bottom
        String customerId = "";
        try (CSVReader reader = new CSVReader(new FileReader(ConfigManager.getCsvPath()))) { //TODO: shouldn't have try block without catch, here you could catch and rethrow a custom exception while still showing the original exception message "throw new CustomerLoginError("Error while .." + e)"
            String[] line;
            while ((line = reader.readNext()) != null) {
                String newPass = line[1];
                if (newPass.equals("password")) { // skip if the first row (titles) is being read
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { //TODO: ideally servlets should only throw servletException, catch everything else and set error codes of response
        String url = request.getRequestURI();

        if(url.equals("/cart")) {
            ArrayList<Product> cart;
            String password = UsersServlet.pass;
            String customerID = null;
            try {
                customerID = getCustomerID(password);
            } catch (CsvValidationException | FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            try { //TODO: the two try blocks should be merged
                cart = (ArrayList<Product>) logic.getCart(customerID);
                request.setAttribute("cart", cart);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            HttpSession session = request.getSession();
            session.setAttribute("isLoggedIn", UsersServlet.isValid);
            session.setAttribute("userType", UsersServlet.type);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            response.setStatus(HttpServletResponse.SC_OK);
            dispatcher.forward(request, response);
        }


    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        if (url.startsWith("/cart/products")) {
            String sku = request.getParameter("productSku");
            ArrayList<Product> cart = new ArrayList<>();
            String password = UsersServlet.pass;
            String customerId = null;
            try {
                customerId = getCustomerID(password);
            } catch (CsvValidationException | FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            try {
                logic.addProductToCart(customerId, sku);
                cart = (ArrayList<Product>) logic.getCart(customerId);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                request.setAttribute("isLoggedIn", "Log in or register to add items to the cart");
                response.sendRedirect("/products");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                request.setAttribute("isLoggedIn", "Log in or register to add items to the cart");
                response.sendRedirect("/products");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            request.setAttribute("cart", cart);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }
        if(url.equals("/cart/editQuantities")){
            String password = UsersServlet.pass;
            String sku = request.getParameter("productSku");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String customerId = null;
            try {
                customerId = getCustomerID(password);
            } catch (CsvValidationException | FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            try {
                logic.setProductQuantityInCart(customerId, sku, quantity);
            } catch (UserNotFoundException | DataMapperException | ProductNotFoundException e) { //TODO: all of these aren't server errors, usernotfound/productnotfound are both client errors
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }

    }
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();

        // remove product from cart
        if (url.startsWith("/cart/products")) {
            String[] fullUrl = url.split("/");
            String urlSlug = fullUrl[fullUrl.length-1];
            String password = UsersServlet.pass;
            String customerId = null;
            try {
                customerId = getCustomerID(password);
            } catch (CsvValidationException | FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            try {
                Product product = logic.getProductBySlug(urlSlug);
                logic.removeProductFromCart(customerId, product.getSku().toString());
                request.setAttribute("cart", logic.getCart(customerId));
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //TODO: maybe 404?
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }

        //clear the entire cart //TODO: either comment every endpoint or none
        if (url.equals("/cart/clearCart")){
            String password = UsersServlet.pass;
            String customerId = null;
            try {
                customerId = getCustomerID(password);
            } catch (CsvValidationException | FileDownloadException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            try {
                logic.clearCart(customerId);
            } catch (UserNotFoundException | DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); //TODO: usernotfound is client error
            }
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }
    }
}
