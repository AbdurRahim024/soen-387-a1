<%@ page import="com.mywebapp.logic.models.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.mywebapp.logic.models.Order" %>
<%@ page import="com.mywebapp.servlets.UsersServlet" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BestClothes - My Cart</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            background-color: rgba(0, 0, 0, 0.5);
            color: #fff;
        }

        nav {
            background-color: #333;
            padding: 15px;
            text-align: center;
        }

        nav a {
            color: #fff;
            text-decoration: none;
            margin: 0 15px;
            font-size: 18px;
        }

        .container {
            margin: 20px;
        }

        h1 {
            font-size: 36px;
            margin-bottom: 20px;
        }


        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #333;
            color: #fff;
        }

        .footer {
            background-color: #333;
            color: #fff;
            text-align: center;
            padding: 15px 0;
            position: fixed;
            bottom: 0;
            width: 100%;
        }

        .btn {
            background-color: #333;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #333;
            color: #fff;
        }
        .container {
            margin: 20px;
        }
        .product {
            background-color: #fff;
            color: #333;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            transition: 0.3s;
            display: inline-block; /* Display products in a horizontal line */
            width: calc(30% - 20px); /* Set the width to accommodate three products in a row */
            margin: 0 10px; /* Add some margin between products */
            vertical-align: top; /* Align the tops of the products */
        }

        .product h2 {
            font-size: 24px;
            margin: 0;
        }

        .product p {
            font-size: 16px;
        }

        .product p:last-child {
            font-weight: bold;
        }

        .footer {
            background-color: #333;
            color: #fff;
            text-align: center;
            padding: 15px 0;
            position: fixed;
            bottom: 0;
            width: 100%;
        }
        .button-container {
            display: flex;
            justify-content: space-between;
        }

        form {
            margin: 0; /* Remove default form margin */
        }

        button {
            padding: 10px 20px;
        }




    </style>
</head>

<body>
<nav>
    <a href="/home">Home</a>
    <a href="/products">Products</a>
<%  String isLoggedIn = (String) request.getAttribute("isLoggedIn");
    String userType = (String) request.getAttribute("userType");
    String currentUser = (String) request.getAttribute("currentUser");
    if (isLoggedIn != null && isLoggedIn.equals("true")) { %>
        <% if (userType.equals("admin")) { %>
          <a href="/createProduct">Create New Product</a>
          <a href="/products/download">Download Catalog</a>
          <a href="/users">User Control</a>
        <% } %>
        <a href="/cart">Cart</a>
        <a href="/orders">View Orders</a>
        <a href="/claimOrder">Claim Order</a>
        <a href="/logout">Logout</a>
    <% }
    else if (isLoggedIn != null && isLoggedIn.equals("Log in or register to add items to the cart")){ %>
    <br>
    <p>${isLoggedIn}</p>
    <% } %>

</nav>
<div class="container">
    <div id="product-container">
        <%
            ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
            for (User user : users){ %>
        <div class="product">
            <h2><b>Username: </b> <%=user.getPasscode()%></h2>
            <p><b>User type: </b><%=user.getUserType().name()%></p>
            <div class="button-container">
                <form action="/grant" method="get">
                    <input type="hidden" name="password" value="<%=user.getPasscode()%>">
                        <% if (!user.getPasscode().equals(currentUser)) { %>
                            <%if (user.getUserType().name().equals("STAFF")) { %>
                                <button class="btn" type="submit">Revoke Staff Access</button>
                            <% } else if (user.getUserType().name().equals("CUSTOMER")) {%>
                                <button class="btn" type="submit">Grant Staff Access</button>
                            <%}%>
                        <%}%>
                </form>
            </div>
        </div>

        <% } %>

    </div>
</div>

<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
<%--<script>--%>
<%--        function grant(user) {--%>
<%--        var xhr = new XMLHttpRequest();--%>
<%--        xhr.open("POST", "/grant" + user, true);--%>
<%--        xhr.send();--%>
<%--        }--%>

<%--        function revoke(user) {--%>
<%--        var xhr = new XMLHttpRequest();--%>
<%--        xhr.open("POST", "/revoke" + user, true);--%>
<%--        xhr.send();--%>
<%--        }--%>
<%--</script>--%>
</html>


