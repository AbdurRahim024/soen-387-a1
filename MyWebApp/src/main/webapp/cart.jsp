<%@ page import="java.util.HashMap" %>
<%@ page import="com.mywebapp.logic.models.Product" %>
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
    <h1>My Cart</h1>


    <div class="d-flex">
        <form action="/clearcart" method="get">
            <button type="submit" class="btn">Clear cart</button>
        </form>
    </div>


    <table>
        <thead>
        <tr>
            <th></th>
            <th>Product Name</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Total Price</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <%
            HashMap<String, Product> cart = (HashMap<String, Product>) request.getAttribute("cartMap");
            for (HashMap.Entry<String,Product> product : cart.entrySet()) {
                Product p = product.getValue();
        %>
            <tr>
                <td><%=product.getValue().getName()%></td>
<%--                <td><%=product.getValue().getQuantity()%></td>--%>
                <td>${cart.value.price}</td>
                <td>${cart.value.totalPrice}</td>
                <td>
                    <form action="/removeFromCart" method="post">
                        <input type="hidden" name="sku" value="${cartItem.key}">
                        <input type="submit" id="removeFromCart" class="btn-remove" value="Remove">
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>



    <p>Total: $<span id="totalAmount">0.00</span></p>
    <a href="/buy" class="btn">Check out</a>
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
    // Display admin-specific options
        document.getElementById("staff-login-button").hidden = true;
        document.getElementById("logout-button").hidden = false;
        document.getElementById("create-new-product").hidden = false;

    } else {
        // Display customer options
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
        // Clear the isAdmin cookie on logout
        document.cookie = "isAdmin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        // Redirect to the home page or any other suitable page after logout
        window.location.href = "/home"; // Replace "/home" with the desired URL
});






</script>
</html>
