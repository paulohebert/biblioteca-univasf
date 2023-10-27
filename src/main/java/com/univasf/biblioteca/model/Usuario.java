package com.univasf.biblioteca.model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    private Long cpf;
    private String nome;
    private String email;
    private String endereco;
    @Column(unique = true)
    private String nome_usuario;
    private String senha;
    private Boolean tipo_administrador;

    // ........................................................................//
    // Construtores
    public Usuario() {
    }

    public Usuario(Long cpf, String nome, String nome_usuario, String senha,
            Boolean tipo_administrador) {
        this.cpf = cpf;
        this.nome = nome;
        this.nome_usuario = nome_usuario;
        this.senha = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
        this.tipo_administrador = tipo_administrador;
    }

    public Usuario(Long cpf, String nome, String email, String endereco,
            String nome_usuario, String senha, Boolean tipo_administrador) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.nome_usuario = nome_usuario;
        this.senha = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
        this.tipo_administrador = tipo_administrador;
    }

    // ........................................................................//
    // Metodos sets

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setSenha(String senha) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
        this.senha = hashedPassword;
    }

    // ........................................................................//
    // Metodos gets

    public Long getCpf() {
        return this.cpf;
    }

    public String getSenha() {
        return this.senha;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public String getNomeUsuario() {
        return this.nome_usuario;
    }

    public Boolean getTipoAdministrador() {
        return this.tipo_administrador;
    }
}
