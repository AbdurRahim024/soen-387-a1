<!-- login.jsp -->

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Your Clothing Store</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            background: url('bg5.avif') no-repeat center center fixed;
            background-size: cover;
        }

        .header {
            text-align: center;
            padding: 15px;
        }

        .header p {
            color: #fff;
            font-size: 24px;
        }

        .login-section {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 80vh;
        }

        form {
            width: 300px;
            padding: 20px;
            background-color: rgba(51, 51, 51, 0.8);
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 10px;
            color: #fff;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #fff;
            border-radius: 5px;
            background-color: rgba(255, 255, 255, 0.1);
            color: #fff;
        }

        button {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 5px;
            background-color: #e74c3c;
            color: #fff;
            cursor: pointer;
        }

        button:hover {
            background-color: #c0392b;
        }

        .new-user-link {
            color: #fff;
            text-decoration: none;
            text-align: center;
        }

        .new-user-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="header">
    <p>BestClothes</p>
</div>

<div class="login-section">
    <form action="/login" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Login</button>
    </form>

    <a href="/new-user" class="new-user-link">New user? Click here to create an account</a>
</div>

</body>
</html>
