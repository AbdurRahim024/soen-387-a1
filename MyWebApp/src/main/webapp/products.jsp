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
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }

        .modal-content {
            background-color: #fff;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            border-radius: 5px;
            width: 300px;
        }

        .close {
            float: right;
            cursor: pointer;
        }
    </style>
</head>
<body>
<nav>
<a href="/home">Home</a>
<a href="/products">Products</a>
<% String isLoggedIn = (String) request.getAttribute("isLoggedIn");
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

<% } else if (isLoggedIn != null && isLoggedIn.equals("Incorrect password or password does not exist")){ %>
<a href="#" id="login-button">Login</a>
<a href="#" id="register-button">Register</a>
<br>
<p>${isLoggedIn}</p>
<% } else if (isLoggedIn != null && isLoggedIn.equals("Successfully logged out")){ %>
<a href="#" id="login-button">Login</a>
<a href="#" id="register-button">Register</a>
<br>
<p>${isLoggedIn}</p>
<% } else if (isLoggedIn != null && isLoggedIn.equals("Successfully registered")){ %>
<a href="#" id="login-button">Login</a>
<a href="#" id="register-button">Register</a>
<br>
<p>${isLoggedIn}</p>
<% } else if (isLoggedIn != null && isLoggedIn.equals("Password already exists, try registering with a different password")){ %>
<a href="#" id="login-button">Login</a>
<a href="#" id="register-button">Register</a>
<br>
<p>${isLoggedIn}</p>
<% }  else { %>
<a href="#" id="login-button">Login</a>
<a href="#" id="register-button">Register</a>
<% } %>
</nav>
<div id="loginModal" class="modal">
    <div class="modal-content">
        <span class="close" id="loginClose">&times;</span>
        <h2>Login</h2>
        <form action="/authenticateUser" method="post">
            <label for="loginPassword">Password:</label>
            <input type="password" id="loginPassword" name="password" required>
            <input type="submit" value="Login">
        </form>
    </div>
</div>

<!-- Register Modal -->
<div id="registerModal" class="modal">
    <div class="modal-content">
        <span class="close" id="registerClose">&times;</span>
        <h2>Register</h2>
        <form action="/registerUser" method="POST">
            <label for="registerPassword">Password:</label>
            <input type="password" id="registerPassword" name="password" required>
            <input type="submit" value="Register">
        </form>
    </div>
</div>
<div class="container">
    <h1>Product Listing</h1>
    <div id="product-container">
        <%
            ArrayList<Product> list = (ArrayList<Product>) request.getAttribute("products");
            for (Product product : list){ %>
            <div class="product">
                <h3><b>Name: </b> <%=product.getName()%></h3>
                <p><b>Price: $</b><%=product.getPrice()%></p>
                <div class="button-container">
                    <form action="/cart/products/<%=product.getUrlSlug()%>" method="post">
                        <input type="hidden" name="productSku" value="<%=product.getSku()%>">
                       <% if (isLoggedIn != null && isLoggedIn.equals("true")) { %>
                        <button class="btn" type="submit">Add to Cart</button>
                        <% } else { %>
                        <p>Log in to add items to cart</p>
                        <% } %>
                    </form>

                    <form action="/products/<%=product.getUrlSlug()%>" method="get">
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
    const loginModal = document.getElementById("loginModal");
    const registerModal = document.getElementById("registerModal");
    const loginButton = document.getElementById("login-button");
    const registerButton = document.getElementById("register-button");
    const loginClose = document.getElementById("loginClose");
    const registerClose = document.getElementById("registerClose");

    loginButton.addEventListener("click", () => {
        loginModal.style.display = "block";
    });

    registerButton.addEventListener("click", () => {
        registerModal.style.display = "block";
    });

    loginClose.addEventListener("click", () => {
        loginModal.style.display = "none";
    });

    registerClose.addEventListener("click", () => {
        registerModal.style.display = "none";
    });
</script>
</html>