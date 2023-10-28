package com.univasf.biblioteca.service;

import java.util.List;
import org.hibernate.query.Query;
import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.util.HibernateUtil;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.hibernate.Session;

public class UserService {
    public static void saveUser(User user) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsers() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM Usuario", User.class);
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static User getUser(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            User user = session.get(User.class, cpf);
            session.getTransaction().commit();
            return user;
        } finally {
            session.close();
        }
    }

    public static User getUserByUserName(String username) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Usuario WHERE nome_usuario = :nome_usuario";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("nome_usuario", username);
            User usuario = query.getSingleResult();
            session.getTransaction().commit();
            return usuario;
        } finally {
            session.close();
        }
    }

    public static void updateUser(User user) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public static void deleteAllUsers() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createNativeQuery("DELETE FROM Usuario", User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public static void deleteUser(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            User user = session.get(User.class, cpf);
            if (user != null) {
                session.remove(user);
                session.getTransaction().commit();
            }
        } finally {
            session.close();
        }
    }

    public static List<User> getUsersByName(String name) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Usuario WHERE lower(nome) LIKE lower(:nome)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("nome", "%" + name + "%");
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static boolean checkPassword(String passwordToCheck, User user) {
        BCrypt.Result result = BCrypt.verifyer().verify(passwordToCheck.toCharArray(),
                user.getSenha());
        return result.verified;
    }
}
