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
<script>
    function removeFromCart(urlSlug) {
        var xhr = new XMLHttpRequest();
        xhr.open("DELETE", "/cart/products/" + urlSlug, true);

        xhr.send();

        let totalElement = document.getElementById("total").innerText;
        let priceElement = document.getElementById("price").innerText;
        console.log(totalElement);
        console.log(priceElement);
        document.getElementById("total").innerText = parseFloat(totalElement) - parseFloat(priceElement);
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
        <% } %>
        <a href="/cart">Cart</a>
        <a href="/logout">Logout</a>
 <% } %>
</nav>
<div class="container">
    <h1>My Cart</h1>

    <div class="d-flex">
        <form action="/cart/clearCart" method="get">
            <button type="submit" class="btn">Clear cart</button>
        </form>
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

                total = total + price;
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



    <p>Total:</p> <p id="total"><%=total%></p>
    <a href="/orderForm" class="btn">Check out</a>
</div>

<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
<script>
</script>
</html>
