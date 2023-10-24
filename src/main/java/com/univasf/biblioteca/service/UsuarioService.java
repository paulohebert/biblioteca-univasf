package com.univasf.biblioteca.service;

import java.util.List;
import org.hibernate.query.Query;
import com.univasf.biblioteca.model.Usuario;
import com.univasf.biblioteca.util.HibernateUtil;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.hibernate.Session;

public class UsuarioService {

//........................................................................//
    //Salva um usuario no banco de dados
    public static void saveUsuario(Usuario usuario) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.save(usuario);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista com todos os usuarios
    public static List<Usuario> getAllUsuarios() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Usuario");
            List<Usuario> usuarios = query.list();
            session.getTransaction().commit();
            return usuarios;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Recebe um cpf e retorna o usuario correspondente
    public static Usuario getUsuario(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, cpf);
            session.getTransaction().commit();
            return usuario;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Recebe um nome de usuario e retorna o usuario relaciona a ele
    public static Usuario getUsuarioPorNomeUsuario(String nome_usuario) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Usuario WHERE nome_usuario = :nome_usuario");
            query.setParameter("nome_usuario", nome_usuario);
            Usuario usuario = (Usuario) query.uniqueResult();
            session.getTransaction().commit();
            return usuario;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Atualiza um usuario
    public static void updateUsuario(Usuario usuario) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.update(usuario);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Deleta todos os usuarios
    public static void deleteAllUsuarios() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Usuario");
            query.executeUpdate();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Recebe um cpf e deleta o usuario correspondente a ele
    public static void deleteUsuario(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, cpf);
            if (usuario != null) {
                session.delete(usuario);
                session.getTransaction().commit();
            }
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Recebe um nome e retorna uma lista de usuarios que tem aquele nome
    public static List<Usuario> getUsuariosPorNome(String nome) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Usuario WHERE lower(nome) LIKE lower(:nome)");
            query.setParameter("nome", "%" + nome + "%");
            List<Usuario> usuarios = query.list();
            session.getTransaction().commit();
            return usuarios;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Analisa se a senha inserida corresponde a senha do usuario
    public static boolean checagemSenha(String passwordToCheck, Long cpf) {
        BCrypt.Result result = BCrypt.verifyer().
        verify(passwordToCheck.toCharArray(),UsuarioService.getUsuario(cpf).getSenha());
        return result.verified;
    }

}
