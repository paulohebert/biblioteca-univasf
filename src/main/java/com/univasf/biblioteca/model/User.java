package com.univasf.biblioteca.model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Long cpf;
    @Column(nullable = false)
    private String nome;
    private String email;
    private String endereco;
    @Column(unique = true, nullable = false)
    private String nome_usuario;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private Boolean tipo_administrador;

    // ........................................................................//
    // Construtores
    public User() {
    }

    public User(Long cpf, String nome, String nome_usuario, String senha,
            Boolean tipo_administrador, Boolean hashPassword) {
        this.cpf = cpf;
        this.nome = nome;
        this.nome_usuario = nome_usuario;
        if (hashPassword) {
            this.senha = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
        } else {
            this.senha = senha;
        }
        this.tipo_administrador = tipo_administrador;
    }

    public User(Long cpf, String nome, String email, String endereco,
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

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public void setSenha(String senha) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
        this.senha = hashedPassword;
    }

    public void setTipo_administrador(Boolean tipo_administrador) {
        this.tipo_administrador = tipo_administrador;
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
