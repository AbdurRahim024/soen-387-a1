<%--
  Created by IntelliJ IDEA.
  User: malshikh
  Date: 2023-11-07
  Time: 11:02 p.m.
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
    <% } else { %>
    <a href="/cart">Cart</a>
    <a href="#" id="login-button">Login</a>
    <a href="#" id="register-button">Set Passcode</a>
    <% } %>
</nav>
<!-- Login Modal -->
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

<!-- Set Passcode Modal -->
<div id="registerModal" class="modal">
    <div class="modal-content">
        <span class="close" id="registerClose">&times;</span>
        <h2>Set Passcode</h2>
        <form id="reg" action="/registerUser" method="POST">
            <label for="registerPassword">Password:</label>
            <input type="password" id="registerPassword" name="password" required>
            <span id="passwordError" class="error-message"></span>
            <input type="submit" value="Set Passcode">
        </form>
    </div>
</div>

<div class="container">
    <form method = "post" action="/createOrder">
        <div class="form-group">
            <label for="shippingAddress">Enter the Shipping Address:</label>
            <input type="text" id="shippingAddress" name="shippingAddress" required>
        </div>
        <button type="submit" class="btn">Create Order</button>
    </form>
</div>



<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
<script>
    const loginModal = document.getElementById("loginModal");
    const registerModal = document.getElementById("registerModal");
    if (document.getElementById("change-button") != null) {
        const changePassModal = document.getElementById("changePassModal");
        const changePassButton = document.getElementById("change-button");
        const changePassClose = document.getElementById("changePassClose");
        changePassButton.addEventListener("click", () => {
            changePassModal.style.display = "block";
        });

        changePassClose.addEventListener("click", () => {
            changePassModal.style.display = "none";
        });
    }
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
