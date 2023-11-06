package com.mywebapp;

public class ConfigManager {

    public static String getDbUrl() {
        return "jdbc:mysql://localhost:3306/soen_387";
    }

    public static String getDbUsername() {
        return "root";
    }

    public static String getDbPassword() {
        return "12345678";
    }

    public static String getCSVPath() {
        return "/Users/malshikh/git/soen-387-a1/MyWebApp/src/main/java/com/mywebapp/servlets/users.csv";
    }
}
