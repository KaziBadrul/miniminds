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

    // Get math score for a user
    public static int getMathScore(String email) {
        String sql = "SELECT math FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("math");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting math score: " + e.getMessage());
        }
        return 0;
    }

    // Update math score for a user
    public static void updateMathScore(String email, int newScore) {
        String sql = "UPDATE users SET math = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newScore);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error updating math score: " + e.getMessage());
        }
    }

    // Get Letter to Image score (stored in spelling column)
    public static int getLetterToImageScore(String email) {
        String sql = "SELECT spelling FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("spelling");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Update Letter to Image score (stored in spelling column)
    public static void updateLetterToImageScore(String email, int newScore) {
        int previousScore = getLetterToImageScore(email);
        int updatedScore = previousScore + (int) Math.ceil(newScore / 2.0);

        String sql = "UPDATE users SET spelling = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, updatedScore);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("✅ Letter to Image score updated in Spelling: " + updatedScore);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Odd One Out score (stored in iq column)
    public static int getOddOneOutScore(String email) {
        String sql = "SELECT iq FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("iq");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Update Odd One Out score (stored in iq column)
    public static void updateOddOneOutScore(String email, int newScore) {
        int previousScore = getOddOneOutScore(email);
        int updatedScore = previousScore + (int) Math.ceil(newScore / 2.0);

        String sql = "UPDATE users SET iq = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, updatedScore);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("✅ Odd One Out score updated in IQ: " + updatedScore);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
