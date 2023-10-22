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
    </style>
</head>
<body>
<nav>
    <a href="/index.jsp">Home</a>
    <a href="/products">Shop</a>
    <a href="#">New Arrivals</a>
    <a href="#">Men</a>
    <a href="#">Women</a>
    <a href="#">Cart</a>
</nav>
<div class="container">
    <h1>My Cart</h1>

    <div class="d-flex">
        <form action="/clearcart" method="get">
            <button type="submit" class="btn">Clear cart</button>
        </form>
        <form action="/movecustomtocart" method="get">
            <button type="submit" class="btn">Add custom cart to cart</button>
        </form>
        <a href="/custom-cart" class="btn">Show custom cart</a>
        <input type="button" value="Edit quantities" class="btn" onclick="editMode()">
        <button hidden type="submit" form="updateQuantity" class="btn">Confirm changes</button>
        <input hidden type="button" value="Cancel" class="btn" onclick="cancel()">
    </div>

    <form action="/updateCartItemQuantity" id="updateQuantity" method="get">
        <table>
            <thead>
            <tr>
                <th></th>
                <th>Product Name</th>
                <th>Quantity</th>
                <th>Total Price</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <!-- Add your loop here when you have data -->
            </tbody>
        </table>
    </form>

    <p class="empty-cart-message" id="emptyCartMessage">Your cart is currently empty, add some products to view them here.
        <br><a href="/shop">Go to shop page</a></p>
    <p id="total">Total: $${total}</p>
    <a href="/buy" class="btn">Check out</a>
</div>

<div class="footer">
    <p>&copy; 2023 Your Clothing Store</p>
</div>
</body>
</html>
