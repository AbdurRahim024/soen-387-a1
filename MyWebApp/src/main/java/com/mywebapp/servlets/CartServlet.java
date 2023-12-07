package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.FileDownloadException;
import com.mywebapp.logic.custom_errors.ProductNotFoundException;
import com.mywebapp.logic.custom_errors.UserNotFoundException;
import com.mywebapp.logic.models.CartItem;
import com.mywebapp.logic.models.Product;
import com.mywebapp.logic.models.User;
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        //Displaying the cart
        if (url.equals("/cart")) {
            String passcode = UsersServlet.pass;
            try {
                request.setAttribute("cart", logic.getCart(passcode));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }

        //Clears the cart
        if (url.equals("/cart/clearCart")) {
            try {
                logic.clearCart(UsersServlet.pass);
                request.setAttribute("cart", logic.getCart(UsersServlet.pass));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            response.sendRedirect("/cart");
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        //Adds a particular product to the cart
        if (url.startsWith("/cart/products")) {
            String sku = request.getParameter("productSku");
            String passcode = UsersServlet.pass;
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            try {
                logic.addProductToCart(passcode, sku);
                request.setAttribute("cart", logic.getCart(passcode));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }

        //Changes the quantity of a particular product
        if (url.equals("/cart/incrementQuantities")) {
            String sku = request.getParameter("productSku");
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            try {
                logic.addProductToCart(UsersServlet.pass, sku);
                request.setAttribute("cart", logic.getCart(UsersServlet.pass));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            response.sendRedirect("/cart");
        }

        if (url.equals("/cart/decrementQuantities")) {
            String sku = request.getParameter("productSku");
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            try {
                logic.decrementProductInCart(UsersServlet.pass, sku);
                request.setAttribute("cart", logic.getCart(UsersServlet.pass));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            response.sendRedirect("/cart");
        }

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();

        // remove product from cart
        if (url.startsWith("/cart/products")) {
            String[] fullUrl = url.split("/");
            String urlSlug = fullUrl[fullUrl.length-1];
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            try {
                Product product = logic.getProductBySlug(urlSlug);
                logic.removeProductFromCart(UsersServlet.pass, product.getSku().toString());
                request.setAttribute("cart", logic.getCart(UsersServlet.pass));
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException | ProductNotFoundException | DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }


    }

}