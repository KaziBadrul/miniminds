package com.example.miniminds;

import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:miniminds.db";

    // Create users table
    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                category TEXT CHECK(category IN ('kid', 'parent')) NOT NULL,
                math INTEGER DEFAULT 0,
                spelling INTEGER DEFAULT 0,
                memory INTEGER DEFAULT 0,
                iq INTEGER DEFAULT 0,
                numbers INTEGER DEFAULT 0,
                timed INTEGER DEFAULT 0
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Users table ready.");
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        }
    }

    // Insert user (password should be hashed before passing here)
    public static void insertUser(String name, int age, String email, String password, String category) {
        String sql = "INSERT INTO users(name, age, email, password, category) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.setString(4, password); // Already hashed
            pstmt.setString(5, category);

            pstmt.executeUpdate();
            System.out.println("✅ User inserted successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Insert failed: " + e.getMessage());
        }
    }

    // Logging in user (returns true if successful, false otherwise)
    public static boolean validateUser(String email, String hashedPassword) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
