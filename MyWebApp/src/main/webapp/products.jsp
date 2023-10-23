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
    </style>
</head>
<body>
<nav>
    <a href="/home">Home</a>
    <a href="/products">Products</a>
    <a href="#">New Arrivals</a>
    <a href="#">Men</a>
    <a href="#">Women</a>
    <a href="/cart">Cart</a>
    <a href="#" id="staff-login-button">Staff Login</a>
    <a hidden href="#" id="logout-button">Staff Logout</a>
    <a hidden href="/createProduct" id = "create-new-product">Create New Product</a>
</nav>
<div class="container">
    <h1>Product Listing</h1>
    <div id="product-container">
        <c:forEach items="${products}" var="product">
            <div class="product">
                <h2>${product.name}</h2>
                <p>${product.description}</p>
                <p>Price: $${product.price}</p>

                <form action="/addToCart" method="post">
                    <input type="hidden" name="productName" value="${product.name}">
                    <input type="hidden" name="productDescription" value="${product.description}">
                    <input type="hidden" name="productVendor" value="${product.vendor}">
                    <input type="hidden" name="productUrlSlug" value="${product.urlSlug}">
                    <input type="hidden" name="productPrice" value="${product.price}">

                    <button type="submit">Add to Cart</button>
                </form>
            </div>


        </c:forEach>
    </div>

</div>

<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
<script>
    function isAdminUser() {
        return document.cookie.split('; ').some((cookie) => cookie.includes('isAdmin=true'));
    }

    if (isAdminUser()) {
        document.getElementById("staff-login-button").hidden = true;
        document.getElementById("logout-button").hidden = false;
        document.getElementById("create-new-product").hidden = false;
    } else {
    }

    document.getElementById("staff-login-button").addEventListener("click", function() {
        const password = prompt("Please enter the staff password:");
        if (password === "secret") {
            document.cookie = "isAdmin=true; path=/";
            location.reload();
        } else {
            alert("Incorrect password. You are not authorized.");
        }
    });

    document.getElementById("logout-button").addEventListener("click", function() {
        document.cookie = "isAdmin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        window.location.href = "/home";
    });
</script>
</html>