package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Function;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.model.Loan;
import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.LoanService;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.util.Session;
import com.univasf.biblioteca.util.TableUtil;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
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
import javafx.scene.text.Text;

public class Loans implements Initializable {
    private ObservableList<Loan> data = FXCollections.observableArrayList();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private User user;
    @FXML
    private MFXTableView<Loan> table;
    @FXML
    private MFXTextField searchField;
    @FXML
    private MFXComboBox<SEARCH> dropdown;
    @FXML
    private MFXToggleButton outstandingLoan;
    @FXML
    private MFXButton addLoanButton;
    @FXML
    private Text results;

    public enum SEARCH {
        CPF,
        ISBN,
    };

    private EventHandler<ActionEvent> returnEvent = (e) -> {
        String uuid = ((Labeled) ((MFXButton) e.getSource()).getParent()).getText();
        Loan loan = LoanService.getEmprestimo(UUID.fromString(uuid));
        if (loan != null) {
            User user = loan.getUsuario();
            Book book = loan.getLivro();
            String msg = """
                    Tem certeza de que deseja devolver o livro?

                    CPF: %d
                    Nome: %s
                    ISBN: %d
                    Título: %s

                    """.formatted(user.getCpf(), user.getNome(), book.getISBN(), book.getTitulo());

            EventHandler<MouseEvent> eventConfirm = (event) -> {
                if (loan.devolucao()) {
                    DialogFactory.showDialog(DialogType.INFO, "Devolução do Livro",
                            "O Livro foi devolvido com sucesso");
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Erro na Devolução",
                            "Não foi possível devolver o livro");
                }
            };

            DialogFactory.showDialog(DialogType.WARNING, "Devolução do Livro", msg, eventConfirm);
        } else {
            DialogFactory.showDialog(DialogType.ERROR, "Erro na Devolução",
                    "Não foi possível devolver o livro");
        }
    };

    private EventHandler<ActionEvent> renewEvent = (e) -> {
        String uuid = ((Labeled) ((MFXButton) e.getSource()).getParent().getParent()).getText();
        Loan loan = LoanService.getEmprestimo(UUID.fromString(uuid));
        if (loan != null) {
            User user = loan.getUsuario();
            Book book = loan.getLivro();
            String msg = """
                    Deseja renovar o empréstimo por mais 7 dias?

                    CPF: %d
                    Nome: %s
                    ISBN: %d
                    Título: %s

                    """.formatted(user.getCpf(), user.getNome(), book.getISBN(), book.getTitulo());

            EventHandler<MouseEvent> eventConfirm = (event) -> {
                if (loan.renovacao()) {
                    DialogFactory.showDialog(DialogType.INFO, "Renovar Empréstimo",
                            "O Empréstimo foi renovado com sucesso");
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Erro na Renovação",
                            "Não foi possível renovar o empréstimo");
                }
            };

            DialogFactory.showDialog(DialogType.WARNING, "Renovar Empréstimo", msg, eventConfirm);
        } else {
            DialogFactory.showDialog(DialogType.ERROR, "Erro na Renovação",
                    "Não foi possível renovar o empréstimo");
        }
    };

    private EventHandler<ActionEvent> delEvent = (e) -> {
        String uuid = ((Labeled) ((MFXButton) e.getSource()).getParent()).getText();
        Loan loan = LoanService.getEmprestimo(UUID.fromString(uuid));
        if (loan != null) {
            User user = loan.getUsuario();
            Book book = loan.getLivro();
            String msg = """
                    Tem certeza de que deseja excluir o Empréstimo?

                    CPF: %d
                    Nome: %s
                    ISBN: %d
                    Título: %s

                    """.formatted(user.getCpf(), user.getNome(), book.getISBN(), book.getTitulo());

            EventHandler<MouseEvent> eventConfirm = (event) -> {
                if (LoanService.deleteLoan(loan)) {
                    DialogFactory.showDialog(DialogType.INFO, "Excluir Empréstimo",
                            "O Empréstimo foi excluído com sucesso");
                } else {
                    DialogFactory.showDialog(DialogType.ERROR, "Excluir Empréstimo",
                            "Não foi possível excluir o empréstimo");
                }
            };

            DialogFactory.showDialog(DialogType.WARNING, "Excluir Empréstimo", msg, eventConfirm);
        }
    };

    private Function<Loan, MFXTableRowCell<Loan, ?>> returnRowCellFactory = (loanModel) -> {
        MFXTableRowCell<Loan, UUID> cell = new MFXTableRowCell<>(Loan::getId);

        Text pending = new Text("PENDENTE");
        MFXButton returnBtn = new MFXButton("DEVOLVER", new MFXFontIcon("fas-arrow-up-from-bracket"));
        returnBtn.getStyleClass().add("button-gray");
        returnBtn.setOnAction(returnEvent);

        cell.setMinWidth(135);
        cell.textProperty().addListener(e -> {
            Loan loan = LoanService.getEmprestimo(UUID.fromString(cell.getText()));
            if (loan != null) {
                Date date = loan.getData_devolucao();
                cell.setGraphic(date != null ? new Text(dateFormat.format(date))
                        : user.getTipoAdministrador() ? returnBtn : pending);
            }
        });

        cell.setAlignment(Pos.CENTER);
        cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return cell;
    };

    private Function<Loan, MFXTableRowCell<Loan, ?>> actionRowCellFactory = (loanModel) -> {
        MFXTableRowCell<Loan, UUID> cell = new MFXTableRowCell<>(Loan::getId);

        MFXButton renewBtn = new MFXButton(null, new MFXFontIcon("fas-repeat"));
        renewBtn.getStyleClass().add("button-green");
        renewBtn.setOnAction(renewEvent);
        cell.setLeadingGraphic(renewBtn);

        MFXButton delBtn = new MFXButton(null, new MFXFontIcon("fas-trash-can"));
        delBtn.getStyleClass().add("button-red");
        delBtn.setOnAction(delEvent);
        cell.setGraphic(delBtn);

        cell.textProperty().addListener(e -> {
            Loan loan = LoanService.getEmprestimo(UUID.fromString(cell.getText()));
            if (loan != null) {
                renewBtn.setVisible(loan.getData_devolucao() == null);
            }
        });

        cell.setAlignment(Pos.CENTER_LEFT);
        cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return cell;
    };

    private void tableInit() {
        TableUtil<Loan> tableUtil = new TableUtil<>();

        var columns = table.getTableColumns();

        if (user.getTipoAdministrador())
            columns.add(tableUtil.createColumn("CPF", Comparator.comparing(Loan::getCPFUsuario),
                    loanModel -> String.format("%011d", loanModel.getCPFUsuario())));

        columns.add(tableUtil.createColumn("ISBN", Comparator.comparing(Loan::getISBNLivro), Loan::getISBNLivro));
        columns.add(tableUtil.createColumn("Início", Pos.CENTER, Comparator.comparing(Loan::getData_emprestimo),
                loanModel -> {
                    return dateFormat.format(loanModel.getData_emprestimo());
                }));
        columns.add(tableUtil.createColumn("Vencimento", Pos.CENTER,
                Comparator.comparing(Loan::getData_vencimento_emprestimo), loanModel -> {
                    return dateFormat.format(loanModel.getData_vencimento_emprestimo());
                }));
        columns.add(tableUtil.createColumn("Devolução", Comparator.comparing(Loan::getData_devolucao),
                returnRowCellFactory, Pos.CENTER, 135));

        if (user.getTipoAdministrador())
            columns.add(tableUtil.createColumn(null, null, actionRowCellFactory, Pos.CENTER_LEFT, 100));

        table.setItems(data);
        table.autosizeColumnsOnInitialization();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = Session.getUser();
        if (user == null) {
            try {
                Window.change(FXMLResource.LOGIN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            dropdown.setItems(FXCollections.observableArrayList(SEARCH.values()));

            if (user.getTipoAdministrador() == false) {
                addLoanButton.setVisible(false);
                addLoanButton.setManaged(false);

                searchField.setPromptText("ISBN");
                dropdown.getSelectionModel().selectItem(SEARCH.ISBN);
                dropdown.setVisible(false);
                dropdown.setManaged(false);
            } else {
                dropdown.getSelectionModel().selectItem(SEARCH.CPF);
            }

            search();
            if (data.isEmpty()) {
                data.setAll(new Loan[1]);
            }
            tableInit();
        }
    }

    @FXML
    public void addLoan() throws IOException {
        Window.create(FXMLResource.ADD_LOAN, 700, 400);
    }

    @FXML
    public void search() {
        SEARCH type = dropdown.getSelectedItem();
        data.clear();
        switch (type) {
            case CPF:
                List<Loan> loansByCPF = LoanService.getAllLoansByCPF(
                        searchField.getText(),
                        outstandingLoan.isSelected());
                data.setAll(loansByCPF);
                break;
            case ISBN:
                if (user.getTipoAdministrador()) {
                    List<Loan> loansByISBN = LoanService.getAllLoansByISBN(
                            searchField.getText(),
                            outstandingLoan.isSelected());
                    data.setAll(loansByISBN);
                } else {
                    List<Loan> loansByISBN = LoanService.getAllUserLoansByISBN(
                            searchField.getText(),
                            user.getCpf(),
                            outstandingLoan.isSelected());
                    data.setAll(loansByISBN);
                }
                break;
        }

        results.setText(Integer.toString(data.size()));
    }
}
