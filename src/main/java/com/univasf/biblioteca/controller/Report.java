package com.univasf.biblioteca.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import com.univasf.biblioteca.model.Book;
import com.univasf.biblioteca.model.Loan;
import com.univasf.biblioteca.model.User;
import com.univasf.biblioteca.service.BookService;
import com.univasf.biblioteca.service.BookService.TopBooksDTO;
import com.univasf.biblioteca.service.LoanService;
import com.univasf.biblioteca.service.UserService;
import com.univasf.biblioteca.service.UserService.TopUsersDTO;
import com.univasf.biblioteca.util.DialogFactory;
import com.univasf.biblioteca.util.DialogFactory.DialogType;
import com.univasf.biblioteca.view.FXMLResource;
import com.univasf.biblioteca.view.Window;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.mfxcore.utils.converters.FunctionalStringConverter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class Report implements Initializable {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private ObservableList<TopBooksDTO> topBooksData = FXCollections.observableArrayList();
    private ObservableList<TopUsersDTO> topUsersData = FXCollections.observableArrayList();

    @FXML
    private Text numBooks, numBooksCopies, numBooksAvailable,
            numOutstandingLoan, numLoans, numUsers, numActiveUsers;

    @FXML
    private Label cpf, name, email, numUserLoans, numUserOutstandingLoan;

    @FXML
    private MFXTextField searchField;

    @FXML
    private MFXListView<Loan> latestLoans;
    @FXML
    private MFXListView<TopBooksDTO> topBooks;
    @FXML
    private MFXListView<TopUsersDTO> topUsers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        update();

        latestLoansInit();
        topBooksInit();
        topUsersInit();
    }

    private void latestLoansInit() {
        StringConverter<Loan> converter = FunctionalStringConverter.to(loan -> (loan == null) ? ""
                : dateFormat.format(loan.getData_emprestimo()) + " - " + loan.getTituloLivro());

        latestLoans.setConverter(converter);

        latestLoans.setCellFactory(loan -> new MFXListCell<Loan>(latestLoans, loan) {
            @Override
            protected void render(Loan data) {
                super.render(data);
                this.getChildren().add(0, new MFXFontIcon("fas-handshake", 20));
            }
        });
    }

    private void topBooksInit() {
        topBooks.setItems(topBooksData);
        topBooks.setCellFactory(book -> new MFXListCell<TopBooksDTO>(topBooks, book) {
            @Override
            protected void render(TopBooksDTO data) {
                if (topBooksData.size() == 1) {
                    this.setWidth(topBooks.getWidth() - 40);
                    this.setHeight(27);
                }

                var children = this.getChildren();

                var title = new Label((topBooksData.indexOf(book) + 1) + "º  " + book.getTitle());
                HBox.setHgrow(title, Priority.ALWAYS);

                var numLoans = new Text(book.getNumLoans().toString());

                MFXButton seeBtn = new MFXButton(null, new MFXFontIcon("fas-eye"));
                seeBtn.getStyleClass().add("button-gray");
                seeBtn.setOnAction((e) -> {
                    try {
                        BookInfo bookInfo = Window.<BookInfo>createWithController(FXMLResource.BOOK_INFO, 800, 500);
                        if (bookInfo != null) {
                            Book bookData = BookService.getLivro(book.getISBN());
                            if (bookData != null) {
                                bookInfo.setBook(bookData);
                            } else {
                                throw new IOException("O Livro não existe");
                            }
                        } else {
                            throw new IOException("Não é possível visualizar o livro");
                        }
                    } catch (IOException err) {
                        DialogFactory.showDialog(DialogType.ERROR, "Erro ao Visualizar o Livro", err.getMessage());
                        err.printStackTrace();
                    }
                });

                children.setAll(new HBox(14, new MFXFontIcon("fas-book", 20), title, numLoans, seeBtn));
            }
        });
    }

    private void topUsersInit() {
        topUsers.setItems(topUsersData);
        topUsers.setCellFactory(user -> new MFXListCell<TopUsersDTO>(topUsers, user) {
            @Override
            protected void render(TopUsersDTO data) {
                if (topUsersData.size() == 1) {
                    this.setWidth(topUsers.getWidth() - 40);
                    this.setHeight(27);
                }

                var children = this.getChildren();

                var name = new Label((topUsersData.indexOf(user) + 1) + "º  " + user.getName());
                HBox.setHgrow(name, Priority.ALWAYS);

                var numLoans = new Text(user.getNumLoans().toString());

                MFXButton seeBtn = new MFXButton(null, new MFXFontIcon("fas-eye"));
                seeBtn.getStyleClass().add("button-gray");
                seeBtn.setOnAction((e) -> {
                    try {
                        UserInfo userInfo = Window.<UserInfo>createWithController(FXMLResource.USER_INFO, 595, 350);
                        if (userInfo != null) {
                            User userData = UserService.getUser(user.getCPF());
                            if (userData != null) {
                                userInfo.setUser(userData);
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
                });

                children.setAll(new HBox(14, new MFXFontIcon("fas-user", 20), name, numLoans, seeBtn));
            }
        });
    }

    @FXML
    public void search() {
        try {
            long cpfLong = Long.parseLong(searchField.getText());
            User user = UserService.getUser(cpfLong);
            if (user != null) {
                cpf.setText(user.getCpf().toString());
                name.setText(user.getNome());
                email.setText(user.getEmail());

                numUserLoans.setText(LoanService.getNumLoansByCPF(cpfLong).toString());
                numUserOutstandingLoan.setText(LoanService.getNumOutstandingLoanByCPF(cpfLong).toString());

                latestLoans.setItems(FXCollections.observableArrayList(LoanService.getLatestLoansByCPF(cpfLong)));

                searchField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

            } else {
                throw new IOException("Usuário Não Existe");
            }
        } catch (Exception numErr) {
            searchField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
    }

    @FXML
    public void update() {
        numBooks.setText(BookService.getBookCount().toString());
        numBooksCopies.setText(BookService.getBookCopyCount().toString());
        numBooksAvailable.setText(BookService.getBookAvailableCount().toString());
        numOutstandingLoan.setText(LoanService.getOutstandingLoanCount().toString());
        numLoans.setText(LoanService.getLoanCount().toString());

        numUsers.setText(UserService.getUserCount().toString());
        numActiveUsers.setText(UserService.getActiveUserCount().toString());

        topBooksData.setAll(BookService.getTopBooks());
        topUsersData.setAll(UserService.getTopUsers());
    }

    @FXML
    public void help() throws IOException {
        Window.create(FXMLResource.HELP, 700, 414);
    }
}
