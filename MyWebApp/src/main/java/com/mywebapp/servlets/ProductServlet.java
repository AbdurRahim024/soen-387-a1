package com.mywebapp.servlets;

import com.mywebapp.logic.*;
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

        // home page
        if(url.equals("/home")) {
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }

        // view cart
        else if(url.equals("/cart")) {
            ArrayList<Product> cart;
            try {
                cart = (ArrayList<Product>) logic.getCart("guest");
                request.setAttribute("cart", cart);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
        }

        // view all products
        else if (url.equals("/products")) {
            try {
                request.setAttribute("products", logic.getProducts());
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/products.jsp");
            dispatcher.forward(request, response);
        }

        // view a specific product
        else if (url.startsWith("/products") && !url.equals("/products/download")) {
            String[] fullUrl = url.split("/");
            String urlSlug = fullUrl[fullUrl.length-1];
            Product product;
            try {
                product = logic.getProductBySlug(urlSlug);
                request.setAttribute("product", product);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/productListing.jsp");
            dispatcher.forward(request, response);

        }

        // [STAFF ONLY] download products list
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

        // [STAFF ONLY] create product
        if (url.equals("/addProductToList")) {
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            String vendor = request.getParameter("productVendor");
            String urlSlug = request.getParameter("productUrlSlug");
            double price = Double.parseDouble(request.getParameter("productPrice"));

            try {
                logic.createProduct(name, description, vendor, urlSlug, price);
            } catch (ProductAlreadyExistsException e) {
                throw new RuntimeException(e);
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/createProduct.jsp");
            dispatcher.forward(request, response);
        }

        // [STAFF ONLY] update product
        if (url.equals("/updateProduct")) {
            String sku = request.getParameter("productSku");
            String name = request.getParameter("productName");
            String description = request.getParameter("productDescription");
            String vendor = request.getParameter("productVendor");
            String urlSlug = request.getParameter("productUrlSlug");
            double price = Double.parseDouble(request.getParameter("productPrice"));

            try {
                logic.updateProduct(name, description, vendor, urlSlug, sku, price);
                request.setAttribute("products", logic.getProducts());
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/products.jsp");
            dispatcher.forward(request, response);
        }

        // add product to cart
        if (url.startsWith("/cart/products")) {
            String sku = request.getParameter("productSku");
            ArrayList<Product> cart = new ArrayList<>();
            try {
                logic.addProductToCart("guest", sku);
                cart = (ArrayList<Product>) logic.getCart("guest");
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }
            request.setAttribute("cart", cart);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
            dispatcher.forward(request, response);
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
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (UserNotFoundException | ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
