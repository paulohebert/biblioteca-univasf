package com.univasf.biblioteca.controller;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

class Model {
    String nome, sobrenome;
    long idade;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Model(String nome, String sobrenome, long idade) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
    }

    public long getIdade() {
        return idade;
    }

    public void setIdade(long idade) {
        this.idade = idade;
    }

}

public class Books implements Initializable {

    @FXML
    private MFXTableView<Model> table;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MFXTableColumn<Model> nameColumn = new MFXTableColumn<>("Nome", true, Comparator.comparing(Model::getNome));
        MFXTableColumn<Model> surnameColumn = new MFXTableColumn<>("Sobrenome", true,
                Comparator.comparing(Model::getSobrenome));
        MFXTableColumn<Model> ageColumn = new MFXTableColumn<>("Idade", true, Comparator.comparing(Model::getIdade));

        Class<?> classe = Model.class;

        Field[] campos = classe.getDeclaredFields();

        for (Field campo : campos) {
            String nomeCampo = campo.getName();
            System.out.println("Nome da propriedade: " + nomeCampo);
        }
        nameColumn.setRowCellFactory(model -> new MFXTableRowCell<>(Model::getNome));
        surnameColumn.setRowCellFactory(model -> new MFXTableRowCell<>(Model::getSobrenome));
        ageColumn.setRowCellFactory(model -> new MFXTableRowCell<>(Model::getIdade));

        table.getTableColumns().add(nameColumn);
        table.getTableColumns().add(surnameColumn);
        table.getTableColumns().add(ageColumn);

        var a = FXCollections.observableArrayList(
                new Model("TEST", "WiFi Extender", 10),
                new Model("TEST", "WiFi Extender", 10),
                new Model("TEST", "WiFi Extender", 10),
                new Model("TEST", "WiFi Extender", 10),
                new Model("TEST", "WiFi Extender", 10),
                new Model("TEST", "WiFi Extender", 10),
                new Model("TEST", "WiFi Extender", 10));

        table.setItems(a);

        table.autosizeColumnsOnInitialization();
    }
}
