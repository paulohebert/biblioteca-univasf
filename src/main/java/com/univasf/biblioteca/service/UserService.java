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

    public static User getUser(String cpf) {
        try {
            long cpfLong = Long.parseLong(cpf);
            return getUser(cpfLong);
        } catch (NumberFormatException numErr) {
            return null;
        }
    }

    public static User getUserByUsername(String username) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE nome_usuario = :nome_usuario";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("nome_usuario", username);
            User user = query.getSingleResult();
            session.getTransaction().commit();
            return user;
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsers() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsersByCPF(String cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE lower(CAST(cpf AS text)) LIKE lower(:cpf)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("cpf", "%" + cpf + "%");
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsersByUsername(String username) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE lower(nome_usuario) LIKE lower(:nome_usuario)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("nome_usuario", "%" + username + "%");
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsersByName(String name) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE lower(nome) LIKE lower(:nome)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("nome", "%" + name + "%");
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsersByEmail(String email) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE lower(email) LIKE lower(:email)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", "%" + email + "%");
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static List<User> getAllUsersByAddress(String address) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE lower(endereco) LIKE lower(:endereco)";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("endereco", "%" + address + "%");
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
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
            Query<User> query = session.createNativeQuery("DELETE FROM User", User.class);
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

    public static boolean deleteUser(User user) {
        Session session = HibernateUtil.getSession();
        boolean status = false;
        try {
            session.beginTransaction();
            if (user != null) {
                session.remove(user);
                session.getTransaction().commit();
                status = true;
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            session.close();
        }
        return status;
    }

    public static boolean checkPassword(String passwordToCheck, User user) {
        BCrypt.Result result = BCrypt.verifyer().verify(passwordToCheck.toCharArray(),
                user.getSenha());
        return result.verified;
    }
}
