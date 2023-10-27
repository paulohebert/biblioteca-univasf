package com.univasf.biblioteca.service;

import java.util.List;
import org.hibernate.query.Query;
import com.univasf.biblioteca.model.Usuario;
import com.univasf.biblioteca.util.HibernateUtil;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.hibernate.Session;

public class UserService {
    public static void saveUser(Usuario user) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public static List<Usuario> getAllUsers() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query<Usuario> query = session.createQuery("FROM Usuario", Usuario.class);
            List<Usuario> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static Usuario getUser(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Usuario user = session.get(Usuario.class, cpf);
            session.getTransaction().commit();
            return user;
        } finally {
            session.close();
        }
    }

    public static Usuario getUserByUserName(String username) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Usuario WHERE nome_usuario = :nome_usuario";
            Query<Usuario> query = session.createQuery(hql, Usuario.class);
            query.setParameter("nome_usuario", username);
            Usuario usuario = query.getSingleResult();
            session.getTransaction().commit();
            return usuario;
        } finally {
            session.close();
        }
    }

    public static void updateUser(Usuario user) {
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
            Query<Usuario> query = session.createNativeQuery("DELETE FROM Usuario", Usuario.class);
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
            Usuario user = session.get(Usuario.class, cpf);
            if (user != null) {
                session.remove(user);
                session.getTransaction().commit();
            }
        } finally {
            session.close();
        }
    }

    public static List<Usuario> getUsersByName(String name) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Usuario WHERE lower(nome) LIKE lower(:nome)";
            Query<Usuario> query = session.createQuery(hql, Usuario.class);
            query.setParameter("nome", "%" + name + "%");
            List<Usuario> users = query.list();
            session.getTransaction().commit();
            return users;
        } finally {
            session.close();
        }
    }

    public static boolean checkPassword(String passwordToCheck, Usuario user) {
        BCrypt.Result result = BCrypt.verifyer().verify(passwordToCheck.toCharArray(),
                user.getSenha());
        return result.verified;
    }
}
