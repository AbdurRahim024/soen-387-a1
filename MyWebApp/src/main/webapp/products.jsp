<%@ page import="com.mywebapp.logic.models.Product" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Clothing Store - My Cart</title>
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
        .link {
            color: black;
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

        .d-flex {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
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
    <% String isLoggedIn = (String) request.getSession().getAttribute("isLoggedIn");
     String userType = (String) request.getSession().getAttribute("userType");
     if (isLoggedIn != null && isLoggedIn.equals("true")) { %>
        <% if (userType.equals("admin")) { %>
          <a href="/createProduct">Create New Product</a>
          <a href="/products/download">Download Catalog</a>
        <% } %>
        <a href="/cart">Cart</a>
        <a href="/logout">Logout</a>
  <% } else if (isLoggedIn != null && isLoggedIn.equals("Log in or register to add items to the cart")){ %>
        <br>
        <p>${isLoggedIn}</p>
  <% } %>
</nav>
<div class="container">
    <h1>Product Listing</h1>
    <div id="product-container">
        <%
            ArrayList<Product> list = (ArrayList<Product>) request.getAttribute("products");
            for (Product product : list){ %>
            <div class="product">
                <h2><%=product.getName()%></h2>
                <p><%=product.getPrice()%></p>
                <div class="button-container">
                    <form action="/cart/products/<%=product.getUrlSlug()%>" method="post">
                        <input type="hidden" name="productSku" value="<%=product.getSku()%>">
                        <button class="btn" type="submit">Add to Cart</button>
                    </form>

                    <form action="/products/<%=product.getUrlSlug()%>" method="get">
<%--                        <input type="hidden" name="productSku" value="<%=product.getSku()%>">--%>
                        <button class="btn" type="submit">View Details</button>
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
<script>

</script>
</html>