<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>BestClothes</title>
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
      background: url('https://images.unsplash.com/photo-1567401893414-76b7b1e5a7a5?auto=format&fit=crop&q=80&w=1000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8ZmFzaGlvbiUyMGJhY2tncm91bmR8ZW58MHx8MHx8fDA%3D') no-repeat center center fixed;
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
      display: flex;
      text-align: center;
      padding: 50px 0;
      justify-content: center;
    }

    .product-grid {
      /*display: grid;*/
      /*grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));*/
      /*gap: 20px;*/
      display: flex;
      justify-content: center;
    }

    .product-item {
      width: 300px;
      position: relative;
      overflow: hidden;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s ease-in-out;
      margin: 10px;
    }

    .product-item:hover {
      transform: scale(1.05);
    }

    .product-item img {
      width: 100%;
      height: 400px;
      border-radius: 10px 10px 0 0;
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

<div class="hero-section">
  <h1>Discover the Latest Fashion Trends</h1>
  <a href="/products" class="cta-button">View All Products</a>
</div>

<div class="product-section">
  <div class="product-grid">
    <div class="product-item">
      <img src="data:image/jpeg;base64,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" alt="Product 1">
      <div class="product-info">
        <h2>Stylish Jacket</h2>
        <p>Explore our latest collection of jackets for men and women.</p>
        <form action="/addToCart" method="post">
          <!-- Hard code the values for these products -->
          <input type="hidden" name="productName" value="${product.name}">
          <input type="hidden" name="productDescription" value="${product.description}">
          <input type="hidden" name="productVendor" value="${product.vendor}">
          <input type="hidden" name="productUrlSlug" value="${product.urlSlug}">
          <input type="hidden" name="productPrice" value="${product.price}">

          <button class="cta-button" type="submit">Add to Cart</button>
        </form>
      </div>
    </div>
    <div class="product-item">
      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQA-8QLbrTpGn9DbzZJnOg7VRk7q6QTVbr5Ag&usqp=CAU" alt="Product 2">
      <div class="product-info">
        <h2>Denim Jeans</h2>
        <p>Classic denim jeans for a timeless look. Available in various styles and fits.</p>
        <form action="/addToCart" method="post">
          <!-- Hard code the values for these products -->
          <input type="hidden" name="productName" value="${product.name}">
          <input type="hidden" name="productDescription" value="${product.description}">
          <input type="hidden" name="productVendor" value="${product.vendor}">
          <input type="hidden" name="productUrlSlug" value="${product.urlSlug}">
          <input type="hidden" name="productPrice" value="${product.price}">

          <button class="cta-button" type="submit">Add to Cart</button>
        </form>
      </div>
    </div>
    <div class="product-item">
      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1HbXwbi5zSWkckVtR4Yo4pg5qxwcG0CxmICwpL0UXM4KEY5xFhNgBp-3Ma4t6WGZ8lwc&usqp=CAU" alt="Product 3">
      <div class="product-info">
        <h2>Sports Wear</h2>
        <p>Elevate your wardrobe with our collection of classic shirts for any occasion.</p>
        <form action="/addToCart" method="post">
          <!-- Hard code the values for these products -->
          <input type="hidden" name="productName" value="${product.name}">
          <input type="hidden" name="productDescription" value="${product.description}">
          <input type="hidden" name="productVendor" value="${product.vendor}">
          <input type="hidden" name="productUrlSlug" value="${product.urlSlug}">
          <input type="hidden" name="productPrice" value="${product.price}">

          <button class="cta-button" type="submit">Add to Cart</button>
        </form>
      </div>

    </div>
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

  document.getElementById("staff-login-button").addEventListener("click", function() {
    const password = prompt("Please enter the staff password:");
    if (password === "secret") {
        document.cookie = "isAdmin=true; path=/";
        location.reload();
    } else {
        alert("Incorrect password. You are not authorized.");
    }
});

  if (isAdminUser()) {
    // Display admin-specific options
    document.getElementById("staff-login-button").hidden = true;
    document.getElementById("logout-button").hidden = false;
    document.getElementById("create-new-product").hidden = false;
} else {
    // Display customer options
}

  document.getElementById("logout-button").addEventListener("click", function() {
    // Clear the isAdmin cookie on logout
    document.cookie = "isAdmin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    // Redirect to the home page or any other suitable page after logout
    window.location.href = "/home"; // Replace "/home" with the desired URL
});
</script>
</html>