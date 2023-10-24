package com.univasf.biblioteca.model;

import javax.persistence.*;
import com.univasf.biblioteca.service.EmprestimoService;
import java.util.Date;
import java.util.Calendar;

@Entity
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cpf_usuario", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;
    @Temporal(TemporalType.DATE)
    private Date data_emprestimo;
    @Temporal(TemporalType.DATE)
    private Date data_devolucao;
    @Temporal(TemporalType.DATE)
    private Date data_devolucao_maxima;

//........................................................................//
    // Construtores
    public Emprestimo() {}

    public Emprestimo(Usuario usuario, Livro livro) {
        Calendar c = Calendar.getInstance();

        this.usuario = usuario;
        this.livro = livro;

        //Data de emprestimo recebe a data atual
        this.data_emprestimo = c.getTime();
        c.add(Calendar.DATE, 7);

        //Data de devolucao máxima vai ser 7 dias após a data de emprestimo
        this.data_devolucao_maxima = c.getTime();
        
    }

//........................................................................//
    //Metodos gets e sets
    public Long getId(){
        return this.id;
    }
    
    public Date getData_emprestimo(){
        return this.data_emprestimo;
    }

    public Date get_DataPrevista(){
        return this.data_devolucao_maxima;
    }

    public Livro getLivro_emprestimo(){
        return this.livro;
    }

    public Usuario getUsuario_emprestimo(){
        return this.usuario;
    }

//........................................................................//
    //Metodo que renova um emprestimo por mais 7 dias
    public void Renovacao(){
        Calendar c = Calendar.getInstance();
        c.setTime(this.data_devolucao_maxima);
        c.add(Calendar.DATE, 7);
        this.data_devolucao_maxima = c.getTime();
        EmprestimoService.updateEmprestimo(this);
    }

//........................................................................//
    //Metodo que devolve um livro registrando a data no banco de dados
    public void Devolucao() {
        if(this.data_devolucao == null){
            Calendar c = Calendar.getInstance();
            this.data_devolucao = c.getTime();
            EmprestimoService.updateEmprestimo(this);
        } 
        else{
            System.out.println("Livro já Devolvido");
        }
    } 
}
