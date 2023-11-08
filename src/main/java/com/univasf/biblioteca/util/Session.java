package com.univasf.biblioteca.util;

import com.univasf.biblioteca.model.User;

import javafx.application.HostServices;

public class Session {
    private static User user;
    private static HostServices hostServices;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Session.user = user;
    }

    public static HostServices getHostServices() {
        return hostServices;
    }

    public static void setHostServices(HostServices hostServices) {
        Session.hostServices = hostServices;
    }
}
