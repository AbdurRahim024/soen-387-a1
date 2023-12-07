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
     String isChanged = (String) request.getAttribute("isChanged");
     if (isLoggedIn != null && isLoggedIn.equals("true")) { %>
        <% if (userType.equals("admin")) { %>
          <a href="/createProduct">Create New Product</a>
          <a href="/products/download">Download Catalog</a>
          <a href="/users">User Control</a>
        <% }  %>
        <a href="/cart">Cart</a>
        <a href="/orders">View Orders</a>
        <a href="#" id="change-button">Change passcode</a>
        <a href="/logout">Logout</a>

        <% if (isChanged != null) { %>
        <br>
        <p>${isChanged}</p>
        <% } %>
    <% } else if (isLoggedIn != null && isLoggedIn.equals("Incorrect password or password does not exist")){ %>
        <a href="#" id="login-button">Login</a>
        <a href="#" id="register-button">Set Passcode</a>
        <br>
        <p>${isLoggedIn}</p>
  <% } else if (isLoggedIn != null && isLoggedIn.equals("Successfully logged out")){ %>
        <a href="#" id="login-button">Login</a>
        <a href="#" id="register-button">Set Passcode</a>
        <br>
        <p>${isLoggedIn}</p>
  <% } else if (isLoggedIn != null && isLoggedIn.equals("Successfully registered")){ %>
        <a href="#" id="login-button">Login</a>
        <a href="#" id="register-button">Set Passcode</a>
        <br>
        <p>${isLoggedIn}</p>
  <% } else if (isLoggedIn != null && isLoggedIn.equals("Password already exists, try registering with a different password")){ %>
        <a href="#" id="login-button">Login</a>
        <a href="#" id="register-button">Set Passcode</a>
        <br>
        <p>${isLoggedIn}</p>
  <% }  else { %>
        <a href="/cart">Cart</a>
        <a href="#" id="login-button">Login</a>
        <a href="#" id="register-button">Set Passcode</a>
  <% } %>
</nav>

<div class="hero-section">
  <h1>Discover the Latest Fashion Trends</h1>
  <a href="/products" class="cta-button">View All Products</a>
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

<!-- Change Passcode Modal -->
<div id="changePassModal" class="modal">
  <div class="modal-content">
    <span class="close" id="changePassClose">&times;</span>
    <h2>Change Current Passcode</h2>
    <form id="change" action="/changePasscode" method="POST">
      <label for="changePassword">Password:</label>
      <input type="password" id="changePassword" name="password" required>
      <span id="passwordChangeError" class="error-change-message"></span>
      <input type="submit" value="Change Passcode">
    </form>
  </div>
</div>

<div class="product-section">
  <div class="product-grid">
    <div class="product-item">
      <img src="https://www.setlakwemode.com/65501-large_default/jacket-classy.jpg" alt="Product 1">
      <div class="product-info">
        <h2>Stylish Jacket</h2>
        <p>Explore our latest collection of jackets for men and women.</p>
        <form action="/addToCart" method="post">
          <input type="hidden" name="productName" value="${product.name}">
          <input type="hidden" name="productDescription" value="${product.description}">
          <input type="hidden" name="productVendor" value="${product.vendor}">
          <input type="hidden" name="productUrlSlug" value="${product.urlSlug}">
          <input type="hidden" name="productPrice" value="${product.price}">
        </form>
      </div>
    </div>
    <div class="product-item">
      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQA-8QLbrTpGn9DbzZJnOg7VRk7q6QTVbr5Ag&usqp=CAU" alt="Product 2">
      <div class="product-info">
        <h2>Denim Jeans</h2>
        <p>Classic denim jeans for a timeless look. Available in various styles and fits.</p>
        <form action="/addToCart" method="post">
          <input type="hidden" name="productName" value="${product.name}">
          <input type="hidden" name="productDescription" value="${product.description}">
          <input type="hidden" name="productVendor" value="${product.vendor}">
          <input type="hidden" name="productUrlSlug" value="${product.urlSlug}">
          <input type="hidden" name="productPrice" value="${product.price}">
        </form>
      </div>
    </div>
    <div class="product-item">
      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1HbXwbi5zSWkckVtR4Yo4pg5qxwcG0CxmICwpL0UXM4KEY5xFhNgBp-3Ma4t6WGZ8lwc&usqp=CAU" alt="Product 3">
      <div class="product-info">
        <h2>Sports Wear</h2>
        <p>Elevate your wardrobe with our collection of classic shirts for any occasion.</p>
        <form action="/addToCart" method="post">
          <input type="hidden" name="productName" value="${product.name}">
          <input type="hidden" name="productDescription" value="${product.description}">
          <input type="hidden" name="productVendor" value="${product.vendor}">
          <input type="hidden" name="productUrlSlug" value="${product.urlSlug}">
          <input type="hidden" name="productPrice" value="${product.price}">
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
<script>
        document.getElementById("reg").addEventListener("submit", function(event) {
            var passwordInput = document.getElementById("registerPassword");
            var passwordError = document.getElementById("passwordError");

            // Regular expression for alphanumeric characters
            var alphanumericRegex = /^[a-zA-Z0-9]+$/;

            if (!alphanumericRegex.test(passwordInput.value)) {
                passwordError.textContent = "Passcode must contain only alphanumeric characters.";
                event.preventDefault();
            } else if (passwordInput.value.length < 4) {
                passwordError.textContent = "Passcode must be at least 4 characters long.";
                event.preventDefault();
            } else {
                passwordError.textContent = "";
            }
        });
</script>
<script>
        document.getElementById("change").addEventListener("submit", function(event) {
            var passwordChangeInput = document.getElementById("changePassword");
            var passwordChangeError = document.getElementById("passwordChangeError");

            // Regular expression for alphanumeric characters
            var alphanumericChangeRegex = /^[a-zA-Z0-9]+$/;

            if (!alphanumericChangeRegex.test(passwordChangeInput.value)) {
                passwordChangeError.textContent = "Passcode must contain only alphanumeric characters.";
                event.preventDefault();
            } else if (passwordChangeInput.value.length < 4) {
                passwordChangeError.textContent = "Passcode must be at least 4 characters long.";
                event.preventDefault();
            } else {
                passwordChangeError.textContent = "";
            }
        });
</script>

</html>