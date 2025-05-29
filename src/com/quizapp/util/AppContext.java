
package com.quizapp.util;

public class AppContext {

    private static String currentUser;
    private static String role;

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setRole(String r) {
        role = r;
    }

    public static String getRole() {
        return role;
    }

    public static void clear() {
        currentUser = null;
        role = null;
    }
}
