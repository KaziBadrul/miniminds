package com.example.miniminds;

public class Session {
    private static String currentUserEmail;
    private static int HEIGHT = 1920;
    private static int WIDTH = 1080;

    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getWIDTH() {
        return WIDTH;
    }
}
