<%@ page import="java.util.HashMap" %>
<%@ page import="com.mywebapp.logic.models.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.mywebapp.logic.models.CartItem" %>
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

        .empty-cart-message {
            display: none;
            color: #fff;
        }

        #total {
            color: #fff;
            font-size: 18px;
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
<script>
    function removeFromCart(urlSlug) {
        var xhr = new XMLHttpRequest();
        xhr.open("DELETE", "/cart/products/" + urlSlug, true);

        xhr.send();

        let totalElement = document.getElementById("total").innerText;
        let priceElement = document.getElementById("price").innerText;
        let quantityElement = document.getElementById("quantity").innerText;
        console.log(totalElement);
        console.log(priceElement);
        document.getElementById("total").innerText = parseFloat(totalElement) - (parseFloat(priceElement) * parseFloat(quantityElement));
        document.getElementById("remove-row").remove();

    }
</script>

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
 <% } else { %>
    <a href="/cart">Cart</a>
    <a href="#" id="login-button">Login</a>
    <a href="#" id="register-button">Set Passcode</a>
    <% } %>
</nav>
<div class="container">
    <h1>My Cart</h1>

    <div class="d-flex">
        <form action="/cart/clearCart" method="get">
            <button type="submit" class="btn">Clear cart</button>
        </form>
    </div>
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

    <%
        double total = 0;
        double price = 0;
        int quantity = 0;
    %>
    <table>
        <thead>
        <tr>
            <th></th>
            <th>Product Name</th>
            <th>Price</th>
            <th></th>
            <th style="text-align: center">Quantity</th>
            <th style="width: 40%"></th>
            <th style="text-align: left"></th>
        </tr>
        </thead>
        <tbody>
        <%
            ArrayList<CartItem> list = (ArrayList<CartItem>) request.getAttribute("cart");
            for (CartItem item : list) {
                price = item.getPrice();
                String name = item.getName();
                quantity = item.getQuantity();

                total = total + (quantity * price);
        %>
        <tr id="remove-row">
            <td></td>
            <td><%=name%></td>
            <td id="price"><%=price%></td>
            <td style="text-align: right">
                <form method="post" action="/cart/decrementQuantities" name="dec-<%=item.getSku()%>">
                    <input name="productSku" hidden type="text" value="<%=item.getSku()%>">
                    <button type="submit" style="width: 30px; height: 30px; font-size: 22px" class="btn-decrement">
                        -
                    </button>
                </form>
            </td>
            <td style="text-align: center" id="quantity"><%=quantity%></td>
            <td style="width: 40%">
                <form method="post" action="/cart/incrementQuantities" name="inc-<%=item.getSku()%>">
                    <input name="productSku" hidden type="text" value="<%=item.getSku()%>">
                    <button type="submit" style="width: 30px; height: 30px; font-size: 22px" class="btn-increment">
                        +
                    </button>
                </form>
            </td>
            <td style="text-align: left">
                <button class="btn-remove" onclick="removeFromCart('<%=item.getUrlSlug()%>');">
                    Remove
                </button>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>



    <p>Total:</p> <p id="total"><%=String.format("%.2f", total)%></p>
    <a href="/orderForm" class="btn">Check out</a>
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
