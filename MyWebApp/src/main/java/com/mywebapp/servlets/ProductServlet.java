package com.mywebapp.servlets;

import com.mywebapp.logic.*;
import com.mywebapp.logic.custom_errors.*;
import com.mywebapp.logic.mappers.UserDataMapper;
import com.mywebapp.logic.models.Product;
import com.mywebapp.logic.models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.UUID;

@WebServlet(name = "productServlet", value = {"/home", "/products/*", "/addProductToList", "/createProduct", "/updateProduct"})
public class ProductServlet extends HttpServlet {

    LogicFacade logic = new LogicFacade();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = request.getRequestURI();

        // home page
        if(url.equals("/home")) {
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
            dispatcher.forward(request, response);
        }

        // [STAFF ONLY] create product page
        else if (url.equals("/createProduct")) {
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            response.setStatus(HttpServletResponse.SC_OK);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/createProduct.jsp");
            dispatcher.forward(request, response);
        }

        // view all products
        else if (url.equals("/products")) {
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                request.setAttribute("products", logic.getProducts());
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/products.jsp");
            dispatcher.forward(request, response);
        }

        // view a specific product
        else if (url.startsWith("/products") && !url.equals("/products/download")) {
            String[] fullUrl = url.split("/");
            String urlSlug = fullUrl[fullUrl.length-1];
            Product product;
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                product = logic.getProductBySlug(urlSlug);
                request.setAttribute("product", product);
            } catch (ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            request.setAttribute("isLoggedIn", UsersServlet.isValid);
            request.setAttribute("userType", UsersServlet.type);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/productListing.jsp");
            dispatcher.forward(request, response);

        }

        // [STAFF ONLY] download products list
        else if (url.equals("/products/download")) {

            if (UsersServlet.type.equals("admin") && UsersServlet.isValid.equals("true")) {
                File catalog_path = null;
                response.setStatus(HttpServletResponse.SC_OK);
                try {
                    catalog_path = logic.downloadProductCatalog();
                } catch (DataMapperException | FileDownloadException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
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
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                logic.createProduct(name, description, vendor, urlSlug, price);
            } catch (ProductAlreadyExistsException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
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
            response.setStatus(HttpServletResponse.SC_OK);

            try {
                logic.updateProduct(name, description, vendor, urlSlug, sku, price);
                request.setAttribute("products", logic.getProducts());
            } catch (ProductNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (DataMapperException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            response.sendRedirect("/products");
        }
    }
}

