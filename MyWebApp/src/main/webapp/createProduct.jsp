
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
    <a href="/cart">Cart</a>
    <a href="#" id="login-button">Staff Login</a>
    <a hidden href="#" id="logout-button">Staff Logout</a>
    <a hidden href="/createProduct" id = "create-new-product">Create New Product</a>
</nav>

<h1>Create Product</h1>
<div class="container">
    <form action="/addProductToList" method="post" class="product-form">
        <div class="form-group">
            <label for="productName">Product Name:</label>
            <input type="text" id="productName" name="productName" required>
        </div>
        <div class="form-group">
            <label for="productDescription">Product Description:</label>
            <textarea id="productDescription" name="productDescription" rows="4" required></textarea>
        </div>
        <div class="form-group">
            <label for="productVendor">Vendor:</label>
            <input type="text" id="productVendor" name="productVendor" required>
        </div>
        <div class="form-group">
            <label for="productUrlSlug">URL Slug:</label>
            <input type="text" id="productUrlSlug" name="productUrlSlug" required>
        </div>
        <div class="form-group">
            <label for="productPrice">Price:</label>
            <input type="number" id="productPrice" name="productPrice" step="0.01" required>
        </div>
        <button type="submit" class="btn">Add Product</button>
    </form>
</div>


<div class="footer">
    <p>&copy; 2023 BestClothes</p>
</div>
</body>
<script>

</script>
</html>