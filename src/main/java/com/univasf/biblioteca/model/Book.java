package com.univasf.biblioteca.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
    @Id
    private Long ISBN;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String autor;
    private String editora;
    private LocalDate ano_publicacao;
    private String categoria;
    private int numero_paginas;
    @Column(nullable = false)
    private int numero_copias_totais;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricao;

    // ........................................................................//
    // Construtores
    public Book() {
    }

    public Book(Long ISBN, String titulo, String autor, String editora,
            LocalDate ano_publicacao, String categoria, int numero_paginas, int numero_copias_totais,
            String descricao) {
        this.titulo = titulo;
        this.autor = autor;
        this.ISBN = ISBN;
        this.editora = editora;
        this.ano_publicacao = ano_publicacao;
        this.categoria = categoria;
        this.numero_paginas = numero_paginas;
        this.numero_copias_totais = numero_copias_totais;
        this.descricao = descricao;

    }

    // ........................................................................//
    // Metodos Set

    public void setISBN(Long iSBN) {
        ISBN = iSBN;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public void setAno_publicacao(LocalDate ano_publicacao) {
        this.ano_publicacao = ano_publicacao;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setNumero_paginas(int numero_paginas) {
        this.numero_paginas = numero_paginas;
    }

    public void setNumero_copias_totais(int numero_copias_totais) {
        this.numero_copias_totais = numero_copias_totais;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // ........................................................................//
    // Metodos Get

    public String getTitulo() {
        return this.titulo;
    }

    public String getAutor() {
        return this.autor;
    }

    public Long getISBN() {
        return this.ISBN;
    }

    public String getEditora() {
        return this.editora;
    }

    public LocalDate getAno_publicacao() {
        return this.ano_publicacao;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public int getNumero_copias_totais() {
        return this.numero_copias_totais;
    }

    public int getNumero_paginas() {
        return this.numero_paginas;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
