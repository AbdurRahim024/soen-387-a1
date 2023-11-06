package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.ProductNotFoundException;
import com.mywebapp.logic.custom_errors.UserNotFoundException;
import com.mywebapp.logic.models.Product;
import com.mywebapp.ConfigManager;
import com.opencsv.CSVReader;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "cartServlet", value = {"/cart/*"})
public class CartServlet extends HttpServlet {
    LogicFacade logic = new LogicFacade();
    private String getCustomerID(String password){
        String customerId = "";
        try (CSVReader reader = new CSVReader(new FileReader(ConfigManager.getCSVPath()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String newPass = line[0];
                if (newPass.equals("password")) { // skip if the first row (titles) is being read
                    continue;
                }
                if(password.equals(newPass)){
                    customerId = line[0];
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerId;
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        if(url.equals("/cart")) {
            ArrayList<Product> cart;
            String password = request.getParameter("password");
            String customerID = getCustomerID(password);
            try {
                cart = (ArrayList<Product>) logic.getCart(customerID);
                request.setAttribute("cart", cart);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }


    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        if (url.startsWith("/cart/products")) {
            String sku = request.getParameter("productSku");
            ArrayList<Product> cart = new ArrayList<>();
            String password = request.getParameter("password");
            String customerId = getCustomerID(password);
            try {
                logic.addProductToCart(customerId, sku);
                cart = (ArrayList<Product>) logic.getCart(customerId);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            request.setAttribute("cart", cart);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }
        if(url.equals("/cart/editQuantities")){
            String password = request.getParameter("password");
            String sku = request.getParameter("productSku");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String customerId = getCustomerID(password);
            try {
                logic.setProductQuantityInCart(customerId, sku, quantity);
            } catch (UserNotFoundException | DataMapperException | ProductNotFoundException e) {
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
            String password = request.getParameter("password");
            String customerId = getCustomerID(password);
            try {
                Product product = logic.getProductBySlug(urlSlug);
                logic.removeProductFromCart(customerId, product.getSku().toString());
                request.setAttribute("cart", logic.getCart(customerId));
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }

        //clear the entire cart
        if (url.equals("/cart/clearCart")){
            String password = request.getParameter("password");
            String customerId = getCustomerID(password);
            try {
                logic.clearCart(customerId);
            } catch (UserNotFoundException | DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }
    }
}
