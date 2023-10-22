<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Online Store</title>
  <style>
    body {
      margin: 0;
      padding: 0;
      font-family: 'Arial', sans-serif;
      background-color: rgba(0, 0, 0, 0.5);
    }

    nav {
      background-color: #333;
      color: #fff;
      padding: 15px;
      text-align: center;
    }

    nav a {
      color: #fff;
      text-decoration: none;
      margin: 0 15px;
      font-size: 18px;
    }

    .hero-section {
      background: url('bg.jpeg') no-repeat center center fixed;
      background-size: cover;
      color: #fff;
      text-align: center;
      padding: 100px 0;

    }

    .hero-section h1 {
      font-size: 36px;
      margin-bottom: 20px;
    }

    .cta-button {
      display: inline-block;
      padding: 15px 30px;
      font-size: 18px;
      text-align: center;
      text-decoration: none;
      color: #fff;
      background-color: #e74c3c;
      border-radius: 5px;
      transition: background-color 0.3s ease;
    }

    .cta-button:hover {
      background-color: #c0392b;
    }

    .product-section {
      text-align: center;
      padding: 50px 0;
    }

    .product-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 20px;
      justify-content: center;
    }

    .product-item {
      position: relative;
      overflow: hidden;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s ease-in-out;
    }

    .product-item:hover {
      transform: scale(1.05);
    }

    .product-item img {
      width: 100%;
      height: 400px;
      border-radius: 10px 10px 0 0;
    }

    .product-info {
      padding: 20px;
      background-color: #fff;
      border-radius: 0 0 10px 10px;
    }

    .product-info h2 {
      font-size: 18px;
      margin-bottom: 10px;
    }

    .product-info p {
      font-size: 14px;
      color: #666;
    }

    .footer {
      background-color: #333;
      color: #fff;
      text-align: center;
      padding: 15px 0;
      position: relative;
      bottom: 0;
      width: 100%;
    }
    .product-info {
      padding: 20px;
      background-color: #fff;
      border-radius: 0 0 10px 10px;
      height: 120px; /* Set a fixed height */
      overflow: hidden;
    }

    .product-info h2 {
      font-size: 18px;
      margin-bottom: 10px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .product-info p {
      font-size: 14px;
      color: #666;
      max-height: 60px; /* Set a maximum height for the text */
      overflow: hidden;
      text-overflow: ellipsis;
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
  <a href="/cart.jsp">Cart</a>
</nav>

<div class="hero-section">
  <h1>Discover the Latest Fashion Trends</h1>
  <a href="/products" class="cta-button">View All Products</a>
</div>

<div class="product-section">
  <div class="product-grid">
    <div class="product-item">
      <img src="cillian.jpeg" alt="Product 1">
      <div class="product-info">
        <h2>Stylish Jacket</h2>
        <p>Explore our latest collection of jackets for men and women.</p>
      </div>
    </div>
    <div class="product-item">
      <img src="images.jpeg" alt="Product 2">
      <div class="product-info">
        <h2>Denim Jeans</h2>
        <p>Classic denim jeans for a timeless look. Available in various styles and fits.</p>
      </div>
    </div>
    <div class="product-item">
      <img src="hrithik.webp" alt="Product 3">
      <div class="product-info">
        <h2>Sports Wear</h2>
        <p>Elevate your wardrobe with our collection of classic shirts for any occasion.</p>
      </div>
    </div>
  </div>
</div>

<div class="footer">
  <p>&copy; 2023 Your Clothing Store</p>
</div>
<br/>
<a href="/products">View products</a>
<br>
<a href="/products/orange">Orange</a>
<br>
<a href="/products/download">Download products</a>
<br>
<a href="/cart/products">View cart</a>
</body>
</html>