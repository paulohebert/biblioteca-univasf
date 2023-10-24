package com.univasf.biblioteca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Livro {
    @Id
    private Long ISBN;
    private String titulo;
    private String autor;
    private String editora;
    private int ano_publicacao;
    private String categoria;
    private int numero_paginas;
    private int numero_copias_totais;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricao;

//........................................................................//
    // Construtores
    public Livro(){}
    
    public Livro( Long ISBN, String titulo, String autor, String editora,
    int ano_publicacao, String categoria, int numero_paginas, int numero_copias_totais,
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

//........................................................................//
    //Metodos Get

    public String getTitulo(){
        return this.titulo;
    }

    public String getAutor(){
        return this.autor;
    }

    public Long getISBN(){
        return this.ISBN;
    }

    public String getEditora(){
        return this.editora;
    }

    public int getAno_publicacao(){
        return this.ano_publicacao;
    }

    public String getCategoria(){
        return this.categoria;
    }

    public int getNumero_copias_totais(){
        return this.numero_copias_totais;
    }

    public int getNumero_paginas(){
        return this.numero_paginas;
    }

    public String getDescricao(){
        return this.descricao;
    }
}
