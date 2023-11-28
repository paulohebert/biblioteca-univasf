package com.univasf.biblioteca.service;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.univasf.biblioteca.model.Loan;
import com.univasf.biblioteca.util.HibernateUtil;

public class LoanService {

    // ........................................................................//
    // Salva um emprestimo no banco de dados
    public static boolean saveEmprestimo(Loan emprestimo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(emprestimo);
            session.getTransaction().commit();
            return true;
        } catch (Exception err) {
            return false;
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
    public static Loan getEmprestimo(UUID id) {
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
    public static boolean updateEmprestimo(Loan emprestimo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.merge(emprestimo);
            session.getTransaction().commit();
            return true;
        } catch (Exception err) {
            return false;
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
    public static boolean deleteEmprestimo(UUID id) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Loan emprestimo = session.get(Loan.class, id);
            if (emprestimo != null) {
                session.remove(emprestimo);
                session.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception err) {
            return false;
        } finally {
            session.close();
        }
    }

    public static boolean deleteLoan(Loan loan) {
        Session session = HibernateUtil.getSession();
        boolean status = false;
        try {
            session.beginTransaction();
            if (loan != null) {
                session.remove(loan);
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

    public static Long getNumLoansByCPF(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM Loan WHERE usuario.cpf = :cpf";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("cpf", cpf);
            return query.uniqueResult();
        } catch (Exception err) {
            return 0L;
        } finally {
            session.close();
        }
    }

    public static Long getNumOutstandingLoanByCPF(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM Loan WHERE usuario.cpf = :cpf AND data_devolucao IS NULL";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("cpf", cpf);
            return query.uniqueResult();
        } catch (Exception err) {
            return 0L;
        } finally {
            session.close();
        }
    }

    public static List<Loan> getLatestLoansByCPF(Long cpf) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE usuario.cpf = :cpf ORDER BY data_emprestimo DESC";
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("cpf", cpf);
            query.setMaxResults(40);
            List<Loan> loans = query.getResultList();
            return loans;
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

    public static List<Loan> getAllLoansByCPF(String cpf, boolean isOutstandingLoan) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE lower(CAST(usuario.cpf AS text)) LIKE lower(:cpf)";
            if (isOutstandingLoan) {
                hql += " AND data_devolucao IS NULL";
            }
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("cpf", "%" + cpf + "%");
            List<Loan> loans = query.list();
            session.getTransaction().commit();
            return loans;
        } finally {
            session.close();
        }
    }

    public static List<Loan> getAllLoansByISBN(String isbn, boolean isOutstandingLoan) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE lower(CAST(livro.ISBN AS text)) LIKE lower(:isbn)";
            if (isOutstandingLoan) {
                hql += " AND data_devolucao IS NULL";
            }
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("isbn", "%" + isbn + "%");
            List<Loan> loans = query.list();
            session.getTransaction().commit();
            return loans;
        } finally {
            session.close();
        }
    }

    public static List<Loan> getAllUserLoansByISBN(String isbn, Long cpf, boolean isOutstandingLoan) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Loan WHERE lower(CAST(livro.ISBN AS text)) LIKE lower(:isbn) AND usuario.cpf = :cpf";
            if (isOutstandingLoan) {
                hql += " AND data_devolucao IS NULL";
            }
            Query<Loan> query = session.createQuery(hql, Loan.class);
            query.setParameter("isbn", "%" + isbn + "%");
            query.setParameter("cpf", cpf);
            List<Loan> loans = query.list();
            session.getTransaction().commit();
            return loans;
        } finally {
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

    public static Long getLoanCount() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM Loan";
            Query<Long> query = session.createQuery(hql, Long.class);
            Long loans = query.uniqueResult();
            return loans;
        } catch (Exception err) {
            return 0L;
        } finally {
            session.close();
        }
    }

    public static Long getOutstandingLoanCount() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM Loan WHERE data_devolucao IS NULL";
            Query<Long> query = session.createQuery(hql, Long.class);
            Long loans = query.uniqueResult();
            return loans;
        } catch (Exception err) {
            return 0L;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna o número de emprestimos em aberto que um livro tem
    public static Long getNumeroEmprestimosAbertosPorLivro(Long ISBN_Livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM Loan WHERE livro.ISBN = :ISBN_Livro AND data_devolucao IS NULL";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("ISBN_Livro", ISBN_Livro);
            Long emprestimos = query.uniqueResult();
            return emprestimos;
        } finally {
            session.close();
        }
    }

}
