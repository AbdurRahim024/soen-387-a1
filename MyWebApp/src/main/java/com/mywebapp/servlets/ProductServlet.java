package com.mywebapp.servlets;

import com.mywebapp.logic.LogicFacade;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "productServlet", value = {"/home", "/products/*", "/cart/*"})
public class ProductServlet extends HttpServlet {

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
//          request.setAttribute("products", LogicFacade.products);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/products.jsp");
            dispatcher.forward(request, response);
        }
        else if (url.equals("/products/download")) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Download</h1>");
            out.println("</body></html>");
        }
        else if (url.equals("/cart/products")) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>This is your cart</h1>");
            out.println("</body></html>");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURI();
        if (url.startsWith("/products") && url.endsWith("prodName")) {

        }
        if (url.startsWith("/cart") && url.endsWith("prodName")) {

        }
    }


    ////// HAS TO BE A DELETE REQUEST, CAN ONLY BE DONE USING JS SCRIPT //////
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sku = request.getParameter("sku");
        boolean deletionSuccessful = LogicFacade.removeProductFromCart("l", sku);  //////HERE (MADE IT STATIC)/////
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
