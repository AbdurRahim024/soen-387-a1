<%--
  Created by IntelliJ IDEA.
  User: emmuh
  Date: 2023-12-12
  Time: 6:08 a.m.
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
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

        .btn {
            background-color: #333;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
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
    </style>
</head>
<body>
<nav>
    <a href="/home">Home</a>
    <a href="/products">Products</a>
    <%  String isLoggedIn = (String) request.getAttribute("isLoggedIn");
        String userType = (String) request.getAttribute("userType");
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
    <% } %>
</nav>
<div class="container">
    <form method = "post" action="/claimOrder">
        <div class="form-group">
            <label for="orderId">Enter the Order Id:</label>
            <input type="text" id="orderId" name="orderId" required>
        </div>
        <button type="submit" class="btn">Claim Order</button>
    </form>
</div>



<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
</html>

