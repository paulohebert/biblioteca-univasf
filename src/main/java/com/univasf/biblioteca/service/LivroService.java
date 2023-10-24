package com.univasf.biblioteca.service;

import java.util.List;
import org.hibernate.query.Query;
import com.univasf.biblioteca.model.Livro;
import com.univasf.biblioteca.util.HibernateUtil;
import org.hibernate.Session;

public class LivroService {
    
//........................................................................//
    //Salva um livro no banco de dados
    public static void saveLivro(Livro livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.save(livro);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna todos os livros
    public static List<Livro> getAllLivros() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Livro");
            List<Livro> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Recebe o ISBN e retorna o livro correspondente
    public static Livro getLivro(Long ISBN) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Livro livro = session.get(Livro.class, ISBN);
            session.getTransaction().commit();
            return livro;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Atualiza um livro no banco de dados
    public static void updateLivro(Livro livro) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.update(livro);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Deleta todos os livros
    public static void deleteAllLivros() {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Livro");
            query.executeUpdate();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }
    
//........................................................................//
    //Recebe um ISBN e deleta o livro correspondente
    public static void deleteLivro(Long ISBN) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Livro livro = session.get(Livro.class, ISBN);
            if (livro != null) {
                session.delete(livro);
                session.getTransaction().commit();
            }
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista de livros por titulo
    public static List<Livro> getLivrosPorTitulo(String titulo) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Livro WHERE lower(titulo) LIKE lower(:titulo)");
            query.setParameter("titulo", "%" + titulo + "%");
            List<Livro> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

//........................................................................//    
    //Retorna uma lista de livros por autor
    public static List<Livro> getLivrosPorAutor(String autor) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Livro WHERE lower(autor) LIKE lower(:autor)");
            query.setParameter("autor", "%" + autor + "%");
            List<Livro> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna uma lista de livros por categoria
    public static List<Livro> getLivrosPorCategoria(String categoria) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("FROM Livro WHERE lower(categoria) LIKE lower(:categoria)");
            query.setParameter("categoria", "%" + categoria + "%");
            List<Livro> livros = query.list();
            session.getTransaction().commit();
            return livros;
        } finally {
            session.close();
        }
    }

//........................................................................//
    //Retorna quantas copias um livro tem disponivel
        public int CopiasDisponiveis(Long ISBN){
            return LivroService.getLivro(ISBN).getNumero_copias_totais()-
            EmprestimoService.getNumeroEmprestimosAbertosPorLivro(ISBN);
        }
}
