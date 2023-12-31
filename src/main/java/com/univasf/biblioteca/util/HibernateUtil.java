package com.univasf.biblioteca.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.UserService;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private static void createAdmin() {
        if (UserService.getUser(0L) == null) {
            UserService.saveUser(new User(0L, "Administrador", "admin",
                    "$2a$12$0.ZOQmgDdspUAZlxvtdvrOeC7xwiiXTQn2SFx2TrVKU7GUJuAA.my", true, false));
        }
    }

    public static void initialize() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        createAdmin();
    }

    public static Session getSession() {
        if (sessionFactory == null) {
            return null;
        }
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
