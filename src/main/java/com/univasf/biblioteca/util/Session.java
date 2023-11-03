package com.univasf.biblioteca.util;

import com.univasf.biblioteca.model.User;

public class Session {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }
}
