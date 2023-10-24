package com.univasf.biblioteca.service;

import java.util.List;
import org.hibernate.query.Query;
import com.univasf.biblioteca.model.Emprestimo;
import com.univasf.biblioteca.util.HibernateUtil;
import org.hibernate.Session;

public class EmprestimoService {

//........................................................................//
    //Salva um emprestimo no banco de dados
    public static void saveEmprestimo(Emprestimo emprestimo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.save(emprestimo);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista com todos os emprestimos
    public static List<Emprestimo> getAllEmprestimos() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo");
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna um emprestimo usando seu id como parametro
    public static Emprestimo getEmprestimo(Long id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Emprestimo emprestimo = session.get(Emprestimo.class, id);
            session.getTransaction().commit();
            return emprestimo;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Atualiza um emprestimo no banco de dados
    public static void updateEmprestimo(Emprestimo emprestimo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.update(emprestimo);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Deleta todos os emprestimo do banco de dados
    public static void deleteAllEmprestimos() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Emprestimo");
            query.executeUpdate();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Deleta um emprestimo usando seu id como parametro
    public static void deleteEmprestimo(Long id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Emprestimo emprestimo = session.get(Emprestimo.class, id);
            if (emprestimo != null) {
                session.delete(emprestimo);
                session.getTransaction().commit();
            }
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista de todos os emprestimos relacionados a um cpf
    public static List<Emprestimo> getEmprestimosPorCPF(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo WHERE usuario.cpf = :cpf");
            query.setParameter("cpf", cpf);
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista com os emprestimos em aberto relacionados a um cpf
    public static List<Emprestimo> getEmprestimosAbertosPorCPF(Long cpf) {
        // Get a session
        Session session = HibernateUtil.getSession();
        try {
            // Use the session to get all Emprestimos with the given cpf
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo WHERE usuario.cpf = :cpf AND data_devolucao IS NULL");
            query.setParameter("cpf", cpf);
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            // Close the session
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista de todos os emprestimos relacionados a um nome de usuario
    public static List<Emprestimo> getEmprestimosNomeUsuario(String nome_usuario) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo WHERE usuario.nome_usuario = :nome_usuario");
            query.setParameter("nome_usuario", nome_usuario);
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

//........................................................................//
   //Retorna uma lista com os emprestimos em aberto relacionados a um nome de usuario
    public static List<Emprestimo> getEmprestimosAbertosNomeUsuario(String nome_usuario) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo WHERE usuario.nome_usuario = :nome_usuario AND data_devolucao IS NULL");
            query.setParameter("nome_usuario", nome_usuario);
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }
  
//........................................................................//
    //Retorna uma lista com todos os emprestimo relacionados a um livro
    public static List<Emprestimo> getEmprestimosPorLivro(Long ISBN_Livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo WHERE livro.ISBN = :ISBN_Livro");
            query.setParameter("ISBN_Livro", ISBN_Livro);
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista de emprestimos em aberto relacionados a um livro
    public static List<Emprestimo> getEmprestimosAbertosPorLivro(Long ISBN_Livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo WHERE livro.ISBN = :ISBN_Livro AND data_devolucao IS NULL");
            query.setParameter("ISBN_Livro", ISBN_Livro);
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna o número de emprestimos em aberto que um livro tem
    public static int getNumeroEmprestimosAbertosPorLivro(Long ISBN_Livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Emprestimo WHERE livro.ISBN = :ISBN_Livro AND data_devolucao IS NULL");
            query.setParameter("ISBN_Livro", ISBN_Livro);
            List<Emprestimo> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos.size();
        } finally {
            session.close();
        }
    }
     
}
