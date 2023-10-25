package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import com.mywebapp.logic.ProductNotFoundException;
import com.mywebapp.logic.UserNotFoundException;
import com.mywebapp.logic.models.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet(name = "productServlet", value = {"/home", "/products/*", "/cart/*", "/addProductToList", "/createProduct", "/updateProduct", "/removeFromCart", "/addToCart"})
public class ProductServlet extends HttpServlet {

    LogicFacade logic = new LogicFacade();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();
        if(url.equals("/home")) {
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }
        else if(url.equals("/cart")) {
            ArrayList<Product> cart;
            try {
                cart = logic.getCart("guest");
                request.setAttribute("cart", cart);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }

        // GET /products/:slug
        if (url.startsWith("/products")) {
            String sku = request.getParameter("productSku");
            Product product;
            try {
                product = logic.getProduct(sku);
                request.setAttribute("product", product);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/productListing.jsp");
            dispatcher.forward(request, response);

        }

        else if(url.equals("/createProduct")) {
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/createProduct.jsp");
            dispatcher.forward(request, response);
        }

        // GET /products
        else if (url.equals("/products")) {
            request.setAttribute("products", logic.getProducts());
            response.setStatus(HttpServletResponse.SC_OK);
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
                File catalog_path = logic.downloadProductCatalog();
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=\"product_catalog.csv\"");
                try (InputStream fileInputStream = new FileInputStream(catalog_path);
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        if (url.equals("/addProductToList")) {
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            String vendor = request.getParameter("productVendor");
            String urlSlug = request.getParameter("productUrlSlug");
            double price = Double.parseDouble(request.getParameter("productPrice"));

            logic.createProduct(name, description, vendor, urlSlug, price);
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/createProduct.jsp");
            dispatcher.forward(request, response);
        }

        // POST /products/:slug
        if (url.equals("/updateProduct")) {
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            String vendor = request.getParameter("productVendor");
            String urlSlug = request.getParameter("productUrlSlug");
            double price = Double.parseDouble(request.getParameter("productPrice"));

            try {
                logic.updateProduct(name, description, vendor, urlSlug, price);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/updateProduct.jsp");
            dispatcher.forward(request, response);
        }

        // POST /cart/products/:slug
        if (url.startsWith("/addToCart") && url.endsWith("prodName")) {
            String sku = request.getParameter("productSku");

            try {
                logic.addProductToCart("guest", sku);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/productsListings.jsp");
            dispatcher.forward(request, response);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURI();

        // DELETE /cart/products/:slug
        if (url.startsWith("/carts/products")) {
            String sku = request.getParameter("productSku");
            try {
                logic.removeProductFromCart("guest", sku);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }
    }
}
