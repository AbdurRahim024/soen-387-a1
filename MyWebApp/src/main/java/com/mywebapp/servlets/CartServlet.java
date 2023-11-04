package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.ProductNotFoundException;
import com.mywebapp.logic.custom_errors.UserNotFoundException;
import com.mywebapp.logic.models.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "cartServlet", value = {"/cart/*"})
public class CartServlet extends HttpServlet {
    LogicFacade logic = new LogicFacade();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        if(url.equals("/cart")) {
            ArrayList<Product> cart;
            try {
                cart = (ArrayList<Product>) logic.getCart("guest");
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
            try {
                logic.addProductToCart("guest", sku);
                cart = (ArrayList<Product>) logic.getCart("guest");
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

        }

    }
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURI();

        // remove product from cart
        if (url.startsWith("/cart/products")) {
            String[] fullUrl = url.split("/");
            String urlSlug = fullUrl[fullUrl.length-1];
            try {
                Product product = logic.getProductBySlug(urlSlug);
                logic.removeProductFromCart("guest", product.getSku().toString());
                request.setAttribute("cart", logic.getCart("guest"));
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.setStatus(HttpServletResponse.SC_OK);

        }
    }
}
