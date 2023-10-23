package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.ProductNotFoundException;
import com.mywebapp.logic.UserNotFoundException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

import java.io.*;
import java.util.Arrays;

@WebServlet(name = "productServlet", value = {"/home", "/products/*", "/cart/*"})
public class ProductServlet extends HttpServlet {

    LogicFacade logic = new LogicFacade();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        if(url.equals("/home")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }
        else if(url.equals("/cart")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }
        else if (url.equals("/products")) {
//          request.setAttribute("products", logic.products);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/products.jsp");
            dispatcher.forward(request, response);
        }
        else if (url.equals("/products/download")) {
            String isAdminCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> "isAdmin".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);

            if (isAdminCookie != null && isAdminCookie.equals("true")) {
                File catalog = logic.downloadProductCatalog();
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=\"product_catalog.csv\"");
                try (InputStream fileInputStream = new FileInputStream(catalog);
                     OutputStream responseOutputStream = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    responseOutputStream.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                response.sendRedirect("/products");
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURI();
        if (url.startsWith("/products") && url.endsWith("prodName")) {

        }
        if (url.startsWith("/cart") && url.endsWith("prodName")) {

        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sku = request.getParameter("sku");
        boolean deletionSuccessful = false;
        try {
            logic.removeProductFromCart("l", sku);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (deletionSuccessful) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Product deleted successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to delete product");
        }
    }
        public void destroy() {
    }
}
