package com.univasf.biblioteca.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.univasf.biblioteca.service.LoanService;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "cpf_usuario", nullable = false)
    private User usuario;
    @ManyToOne
    @JoinColumn(name = "id_livro", nullable = false)
    private Book livro;
    @Temporal(TemporalType.DATE)
    private Date data_emprestimo;
    @Temporal(TemporalType.DATE)
    private Date data_vencimento_emprestimo;
    @Temporal(TemporalType.DATE)
    private Date data_devolucao;

    // ........................................................................//
    // Construtores
    public Loan() {
    }

    public Loan(User usuario, Book livro) {
        Calendar c = Calendar.getInstance();

        this.usuario = usuario;
        this.livro = livro;

        // Data de emprestimo recebe a data atual
        this.data_emprestimo = c.getTime();
        c.add(Calendar.DATE, 7);

        // Data de devolucao máxima vai ser 7 dias após a data de emprestimo
        this.data_vencimento_emprestimo = c.getTime();

    }

    // ........................................................................//
    // Metodos gets e sets
    public UUID getId() {
        return this.id;
    }

    public User getUsuario() {
        return usuario;
    }

    public Long getCPFUsuario() {
        return usuario.getCpf();
    }

    public String getNomeUsuario() {
        return usuario.getNome();
    }

    public Book getLivro() {
        return livro;
    }

    public Long getISBNLivro() {
        return livro.getISBN();
    }

    public String getTituloLivro() {
        return livro.getTitulo();
    }

    public Date getData_emprestimo() {
        return this.data_emprestimo;
    }

    public Date getData_vencimento_emprestimo() {
        return data_vencimento_emprestimo;
    }

    public Date getData_devolucao() {
        return data_devolucao;
    }

    public Book getLivro_emprestimo() {
        return this.livro;
    }

    public User getUsuario_emprestimo() {
        return this.usuario;
    }

    // ........................................................................//
    // Metodo que renova um emprestimo por mais 7 dias
    public boolean renovacao() {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(this.data_vencimento_emprestimo);
            c.add(Calendar.DATE, 7);
            this.data_vencimento_emprestimo = c.getTime();
            LoanService.updateEmprestimo(this);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    // ........................................................................//
    // Metodo que devolve um livro registrando a data no banco de dados
    public boolean devolucao() {
        if (this.data_devolucao == null) {
            try {
                Calendar c = Calendar.getInstance();
                this.data_devolucao = c.getTime();
                LoanService.updateEmprestimo(this);
                return true;
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        return false;
    }
}
