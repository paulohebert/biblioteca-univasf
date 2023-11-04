package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.TableUtil;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Users implements Initializable {
    private ObservableList<User> data = FXCollections.observableArrayList();
    @FXML
    private MFXTableView<User> table;
    @FXML
    private MFXTextField searchField;
    @FXML
    private MFXComboBox<SEARCH> dropdown;
    @FXML
    private Text results;

    public enum SEARCH {
        CPF,
        Username,
        Nome,
        Email,
        Endereço;
    };

    private Function<User, MFXTableRowCell<User, ?>> admRowCellFactory = (userModel) -> {
        MFXTableRowCell<User, Boolean> cell = new MFXTableRowCell<>(User::getTipoAdministrador);

        cell.textProperty().addListener(e -> {
            boolean adm = Boolean.parseBoolean(cell.getText());
            cell.setLeadingGraphic(adm ? new MFXFontIcon("fas-circle-check", Color.rgb(50, 205, 50))
                    : new MFXFontIcon("fas-circle-xmark", Color.rgb(255, 0, 0)));
        });
        cell.setAlignment(Pos.CENTER);
        cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return cell;
    };

    private EventHandler<ActionEvent> seeEvent = (e) -> {
        try {
            UserInfo userInfo = Window.<UserInfo>createWithController(FXMLResource.USER_INFO, 595, 350);
            if (userInfo != null) {
                String cpfStr = ((Labeled) ((MFXButton) e.getSource()).getParent().getParent()).getText();
                User user = UserService.getUser(cpfStr);
                if (user != null) {
                    userInfo.setUser(user);
                } else {
                    throw new IOException("O Usuário não existe");
                }
            } else {
                throw new IOException("Não é possível visualizar o usuário");
            }
        } catch (IOException err) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro ao Visualizar o Usuário", err.getMessage());
            err.printStackTrace();
        }
    };

    private EventHandler<ActionEvent> editEvent = (e) -> {
        try {
            EditUser editUser = Window.<EditUser>createWithController(FXMLResource.EDIT_USER, 595, 400);
            if (editUser != null) {
                String cpfStr = ((Labeled) ((MFXButton) e.getSource()).getParent()).getText();
                User user = UserService.getUser(cpfStr);
                if (user != null) {
                    editUser.setUser(user);
                } else {
                    throw new IOException("O Usuário não existe");
                }
            } else {
                throw new IOException("Não é possível editar o usuário");
            }
        } catch (IOException err) {
            DialogFactory.showDialog(DialogType.ERROR, "Erro ao Editar o Usuário", err.getMessage());
            err.printStackTrace();
        }
    };

    private EventHandler<ActionEvent> delEvent = (e) -> {
        String cpfStr = ((Labeled) ((MFXButton) e.getSource()).getParent().getParent()).getText();
        User user = UserService.getUser(cpfStr);
        if (user != null) {
            String msg = """

                    Tem certeza de que deseja excluir o Usuário?

                    CPF: %d
                    Username: %s
                    Nome: %s
                    """.formatted(user.getCpf(), user.getNomeUsuario(), user.getNome());

            EventHandler<MouseEvent> eventConfirm = (event) -> {
                if (UserService.deleteUser(user)) {
                    DialogFactory.showDialog(DialogType.INFO, "Excluir Usuário",
                            "O Usuário foi excluído com sucesso");
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Excluir Usuário",
                            "Não foi possível excluir o usuário");
                }
            };

            DialogFactory.showDialog(DialogType.WARNING, "Excluir Usuário", msg, eventConfirm);
        }
    };

    private void tableInit() {
        TableUtil<User> tableUtil = new TableUtil<>();

        var columns = table.getTableColumns();
        columns.add(tableUtil.createColumn("CPF", Comparator.comparing(User::getCpf), User::getCpf));
        columns.add(
                tableUtil.createColumn("Username", Comparator.comparing(User::getNomeUsuario), User::getNomeUsuario));
        columns.add(tableUtil.createColumn("Nome", Comparator.comparing(User::getNome), User::getNome));
        columns.add(tableUtil.createColumn("E-mail", Comparator.comparing(User::getEmail), userModel -> {
            String email = userModel.getEmail();
            return email != null ? email : "";
        }));
        columns.add(tableUtil.createColumn("Endereço", Comparator.comparing(User::getEndereco), userModel -> {
            String address = userModel.getEndereco();
            return address != null ? address : "";
        }));
        columns.add(tableUtil.createColumn("Administrador", Comparator.comparing(User::getTipoAdministrador),
                admRowCellFactory, Pos.CENTER));
        columns.add(tableUtil.createActions(User::getCpf, seeEvent, editEvent, delEvent));

        table.setItems(data);
        table.autosizeColumnsOnInitialization();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dropdown.setItems(FXCollections.observableArrayList(SEARCH.values()));
        dropdown.getSelectionModel().selectItem(SEARCH.Nome);

        search();
        tableInit();
    }

    @FXML
    public void addUser() throws IOException {
        Window.create(FXMLResource.ADD_USER, 595, 400);
    }

    @FXML
    public void search() {
        SEARCH type = dropdown.getSelectedItem();
        data.clear();
        switch (type) {
            case CPF:
                User userByCPF = UserService.getUser(searchField.getText());
                if (userByCPF != null) {
                    data.setAll(userByCPF);
                }
                break;
            case Username:
                User userByUserName = UserService.getUserByUserName(searchField.getText());
                if (userByUserName != null) {
                    data.setAll(userByUserName);
                }
                break;
            case Nome:
                List<User> usersByName = UserService.getUsersByName(searchField.getText());
                data.setAll(usersByName);
                break;
            case Email:
                List<User> usersByEmail = UserService.getUsersByEmail(searchField.getText());
                data.setAll(usersByEmail);
                break;
            case Endereço:
                List<User> usersByAddress = UserService.getUsersByAddress(searchField.getText());
                data.setAll(usersByAddress);
                break;
        }

        results.setText(Integer.toString(data.size()));
    }
}
