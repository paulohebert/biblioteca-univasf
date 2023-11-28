package com.univasf.biblioteca.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.util.HibernateUtil;

public class BookService {

    public static class TopBooksDTO {
        private String title;
        private Long numLoans, isbn;

        public TopBooksDTO(String title, Long isbn, Long numLoans) {
            this.title = title;
            this.isbn = isbn;
            this.numLoans = numLoans;
        }

        public String getTitle() {
            return title;
        }

        public Long getISBN() {
            return isbn;
        }

        public Long getNumLoans() {
            return numLoans;
        }
    }

    // ........................................................................//
    // Salva um livro no banco de dados
    public static void saveLivro(Book livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(livro);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna todos os livros
    public static List<Book> getAllLivros() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Book";
            Query<Book> query = session.createQuery(hql, Book.class);
            List<Book> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Recebe o ISBN e retorna o livro correspondente
    public static Book getLivro(Long ISBN) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Book livro = session.get(Book.class, ISBN);
            session.getTransaction().commit();
            return livro;
        } finally {
            session.close();
        }
    }

    public static Book getLivro(String ISBN) {
        try {
            long isbnLong = Long.parseLong(ISBN);
            return getLivro(isbnLong);
        } catch (NumberFormatException numErr) {
            return null;
        }
    }

    // ........................................................................//
    // Atualiza um livro no banco de dados
    public static void updateLivro(Book livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.merge(livro);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Deleta todos os livros
    public static void deleteAllLivros() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Book";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.executeUpdate();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Recebe um ISBN e deleta o livro correspondente
    public static void deleteLivro(Long ISBN) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Book livro = session.get(Book.class, ISBN);
            if (livro != null) {
                session.remove(livro);
                session.getTransaction().commit();
            }
        } finally {
            session.close();
        }
    }

    public static boolean deleteLivro(Book book) {
        Session session = HibernateUtil.getSession();
        boolean status = false;
        try {
            session.beginTransaction();
            if (book != null) {
                session.remove(book);
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

    public static Long getBookCount() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT COUNT(*) FROM Book";
            Query<Long> query = session.createQuery(hql, Long.class);
            Long books = query.uniqueResult();
            return books;
        } catch (Exception err) {
            return 0L;
        } finally {
            session.close();
        }
    }

    public static Long getBookCopyCount() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "SELECT SUM(numero_copias_totais) FROM Book";
            Query<Long> query = session.createQuery(hql, Long.class);
            Long copies = query.uniqueResult();
            return copies != null ? copies : 0L;
        } catch (Exception err) {
            return 0L;
        } finally {
            session.close();
        }
    }

    public static Long getBookAvailableCount() {
        return BookService.getBookCopyCount() - LoanService.getOutstandingLoanCount();
    }

    public static List<Book> getAllBooksByISBN(String isbn) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Book WHERE lower(CAST(ISBN AS text)) LIKE lower(:isbn)";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("isbn", "%" + isbn + "%");
            List<Book> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista de livros por titulo
    public static List<Book> getLivrosPorTitulo(String titulo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Book WHERE lower(titulo) LIKE lower(:titulo)";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("titulo", "%" + titulo + "%");
            List<Book> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista de livros por autor
    public static List<Book> getLivrosPorAutor(String autor) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Book WHERE lower(autor) LIKE lower(:autor)";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("autor", "%" + autor + "%");
            List<Book> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna uma lista de livros por categoria
    public static List<Book> getLivrosPorCategoria(String categoria) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Book WHERE lower(categoria) LIKE lower(:categoria)";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("categoria", "%" + categoria + "%");
            List<Book> livros = query.list();
            return livros;
        } finally {
            session.close();
        }
    }

    public static List<TopBooksDTO> getTopBooks() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();

            Query<TopBooksDTO> query = session.createQuery(
                    "SELECT new TopBooksDTO(b.titulo, b.ISBN, COUNT(l)) " +
                            "FROM Book b " +
                            "LEFT JOIN Loan l ON b.ISBN = l.livro.ISBN " +
                            "GROUP BY b.ISBN, b.titulo " +
                            "ORDER BY COUNT(l) DESC",
                    TopBooksDTO.class);

            query.setMaxResults(100);

            return query.getResultList();
        } finally {
            session.close();
        }
    }

    // ........................................................................//
    // Retorna quantas copias um livro tem disponivel
    public static Long copiasDisponiveis(Book book) {
        return book.getNumero_copias_totais() -
                LoanService.getNumeroEmprestimosAbertosPorLivro(book.getISBN());
    }
}
