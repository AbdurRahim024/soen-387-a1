<%@ page import="com.mywebapp.logic.models.User" %>
<%@ page import="java.util.ArrayList" %>
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
        .btn-remove {
            background-color: #333;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
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
        <a href="/logout">Logout</a>
 <% } %>
</nav>
<div class="container">
    <h1>Users List</h1>
    <table>
        <thead>
        <tr>
            <th></th>
            <th>User</th>
            <th style="text-align: left"></th>
            <th style="text-align: left"></th>
        </tr>
        </thead>
        <tbody>
<%--        <%--%>
<%--            ArrayList<User> list = (ArrayList<User>) request.getAttribute("users");--%>
<%--            String name =--%>
<%--            for (User user : list) {--%>
<%--                price = item.getPrice();--%>
<%--                String name = item.getName();--%>
<%--            TODO: GET USERS LIST ATTRIBUTE--%>
<%--        %>--%>
        <tr id="remove-row">
            <td></td>
            <td>Placeholder</td>
            <td style="text-align: left">
                <button class="btn-remove" onclick="grant('Placeholder');">
                    Grant access
                </button>
            </td>
            <td style="text-align: left">
<%--                <button class="btn-remove" onclick="revoke('<%=item.getUrlSlug()%>');">--%>
                <button class="btn-remove">
                    Revoke access
                </button>
            </td>
        </tr>
<%--        <%--%>
<%--            }--%>
<%--        %>--%>
        </tbody>
    </table>
</div>

<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
<script>
        function grant(user) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/grant" + user, true);
        xhr.send();
        }

        function revoke(user) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/revoke" + user, true);
        xhr.send();
        }
</script>
</html>
