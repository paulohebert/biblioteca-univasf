package com.univasf.biblioteca.service;

import java.util.List;
import org.hibernate.query.Query;
import com.univasf.biblioteca.model.Loan;
import com.univasf.biblioteca.util.HibernateUtil;
import org.hibernate.Session;

public class LoanService {

    // ........................................................................//
    // Salva um emprestimo no banco de dados
    public static void saveEmprestimo(Loan emprestimo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(emprestimo);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista com todos os emprestimos
    public static List<Loan> getAllEmprestimos() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna um emprestimo usando seu id como parametro
    public static Loan getEmprestimo(Long id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Loan emprestimo = session.get(Loan.class, id);
            session.getTransaction().commit();
            return emprestimo;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Atualiza um emprestimo no banco de dados
    public static void updateEmprestimo(Loan emprestimo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.merge(emprestimo);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Deleta todos os emprestimo do banco de dados
    public static void deleteAllEmprestimos() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "DELETE FROM Loan";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.executeUpdate();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Deleta um emprestimo usando seu id como parametro
    public static void deleteEmprestimo(Long id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Loan emprestimo = session.get(Loan.class, id);
            if (emprestimo != null) {
                session.remove(emprestimo);
                session.getTransaction().commit();
            }
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista de todos os emprestimos relacionados a um cpf
    public static List<Loan> getEmprestimosPorCPF(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE usuario.cpf = :cpf";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("cpf", cpf);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista com os emprestimos em aberto relacionados a um cpf
    public static List<Loan> getEmprestimosAbertosPorCPF(Long cpf) {
        // Get a session
        Session session = HibernateUtil.getSession();
        try {
            // Use the session to get all Emprestimos with the given cpf
            session.beginTransaction();
            String hql = "FROM Loan WHERE usuario.cpf = :cpf AND data_devolucao IS NULL";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("cpf", cpf);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            // Close the session
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista de todos os emprestimos relacionados a um nome de usuario
    public static List<Loan> getEmprestimosNomeUsuario(String nome_usuario) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE usuario.nome_usuario = :nome_usuario";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("nome_usuario", nome_usuario);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista com os emprestimos em aberto relacionados a um nome de
    // usuario
    public static List<Loan> getEmprestimosAbertosNomeUsuario(String nome_usuario) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE usuario.nome_usuario = :nome_usuario AND data_devolucao IS NULL";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("nome_usuario", nome_usuario);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista com todos os emprestimo relacionados a um livro
    public static List<Loan> getEmprestimosPorLivro(Long ISBN_Livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE livro.ISBN = :ISBN_Livro";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("ISBN_Livro", ISBN_Livro);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista de emprestimos em aberto relacionados a um livro
    public static List<Loan> getEmprestimosAbertosPorLivro(Long ISBN_Livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE livro.ISBN = :ISBN_Livro AND data_devolucao IS NULL";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("ISBN_Livro", ISBN_Livro);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna o número de emprestimos em aberto que um livro tem
    public static int getNumeroEmprestimosAbertosPorLivro(Long ISBN_Livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE livro.ISBN = :ISBN_Livro AND data_devolucao IS NULL";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("ISBN_Livro", ISBN_Livro);
            List<Loan> emprestimos = query.list();
            session.getTransaction().commit();
            return emprestimos.size();
        } finally {
            session.close();
        }
    }

}
