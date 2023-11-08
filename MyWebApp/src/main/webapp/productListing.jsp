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
            display: flex;
            justify-content: center;
            align-items: flex-start;
            height: 100vh;
        }

        .product-form {
            margin-top: 2px;
            background-color: #fff;
            color: #333;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            width: 50%;
            max-width: 600px;

        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            font-weight: bold;
        }

        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box; /* Ensures the specified width includes padding and border */
        }

        .form-group textarea {
            height: 100px;
        }
        h1{
            margin-bottom: 0px;
            text-align: center;
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
    <% } %>
    <a href="/cart">Cart</a>
    <a href="/logout">Logout</a>
    <% } %>
</nav>
<% Product product = (Product) request.getAttribute("product");

%>
<div class="container">
    <% if (userType.equals("admin")) { %>
    <form action="/updateProduct" method="post" class="product-form">
        <div class="form-group">
            <label for="AdminproductName">Product Name:</label>
            <input class="formButtons" type="text" id="AdminproductName" name="productName" required value="<%=product.getName()%>">
        </div>
        <div class="form-group">
            <label for="productDescription">Product Description:</label>
            <input class="formButtons" type="text" id="AdminproductDescription" name="productDescription"  required value="<%=product.getDescription()%>">
        </div>
        <div class="form-group">
            <label for="AdminproductVendor">Vendor:</label>
            <input class="formButtons" type="text" id="AdminproductVendor" name="productVendor" required value="<%=product.getVendor()%>">
        </div>
        <div class="form-group">
            <label for="AdminproductUrlSlug">URL Slug:</label>
            <input class="formButtons" type="text" id="AdminproductUrlSlug" name="productUrlSlug" required value="<%=product.getUrlSlug()%>">
        </div>
        <div class="form-group">
            <label for="AdminproductPrice">Price:</label>
            <input class="formButtons" type="number" id="AdminproductPrice" name="productPrice" step="0.01" required value="<%=product.getPrice()%>">
        </div>
        <input hidden class="formButtons" type="text" id="AdminproductSku" name="productSku" required value="<%=product.getSku()%>">

        <button id="submitButton" type="submit" class="btn formButtons">Update Product</button>
    </form>
    <% } else { %>
    <form action="/updateProduct" method="post" class="product-form">
        <div class="form-group">
            <label for="productName">Product Name:</label>
            <input class="formButtons" type="text" id="productName" name="productName" required value="<%=product.getName()%>" readonly>
        </div>
        <div class="form-group">
            <label for="productDescription">Product Description:</label>
            <input class="formButtons" type="text" id="productDescription" name="productDescription"  required value="<%=product.getDescription()%>" readonly>
        </div>
        <div class="form-group">
            <label for="productVendor">Vendor:</label>
            <input class="formButtons" type="text" id="productVendor" name="productVendor" required value="<%=product.getVendor()%>" readonly>
        </div>
        <div class="form-group">
            <label for="productUrlSlug">URL Slug:</label>
            <input class="formButtons" type="text" id="productUrlSlug" name="productUrlSlug" required value="<%=product.getUrlSlug()%>" readonly>
        </div>
        <div class="form-group">
            <label for="productPrice">Price:</label>
            <input class="formButtons" type="number" id="productPrice" name="productPrice" step="0.01" required value="<%=product.getPrice()%>" readonly>
        </div>
        <input hidden class="formButtons" type="text" id="productSku" name="productSku" required value="<%=product.getSku()%>" readonly>

    </form>
    <% } %>
</div>

<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
<script>

</script>
</html>