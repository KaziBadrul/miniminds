package com.example.miniminds;

public class Session {
    private static String currentUserEmail;

    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }
}
