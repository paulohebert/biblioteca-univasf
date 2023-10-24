package com.univasf.biblioteca.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static void initialize() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
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
