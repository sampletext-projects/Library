package controllers;

import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Book;
import models.BookOrder;
import models.BookStorage;
import models.User;
import utils.FXMLHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class LibrarianScreenController implements FXMLHelper.PreloadableController {

    private BookStorage mainStorage;
    private List<User> readers;

    @FXML
    private Label greetingLabel;

    @FXML
    private TableView<Book> libraryStorageTable;

    @FXML
    private TableView<User> readersTable;

    @FXML
    private TableColumn<Book, String> columnLibraryAuthor;
    @FXML
    private TableColumn<Book, String> columnLibraryTitle;

    @FXML
    private TableColumn<User, String> columnReaderId;
    @FXML
    private TableColumn<User, String> columnReaderName;

    @SafeVarargs
    @Override
    public final <T> void preload(T... objects) {
        if (User.activeUser != null) {
            greetingLabel.setText(String.format("Hello, %s %s", User.activeUser.getPosition(), User.activeUser.getName()));
        }

        readers = Database.getUsersDb().select(u->u.getPosition().equals("Reader"));
        mainStorage = Database.getBookStoragesDb().getMainStorage();

        columnLibraryAuthor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthor()));
        columnLibraryTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));

        columnReaderId.setCellValueFactory(param -> new SimpleStringProperty(Integer.toString(param.getValue().getId())));
        columnReaderName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));

        fillTable();
    }

    private void fillTable() {
        libraryStorageTable.setItems(FXCollections.observableArrayList(mainStorage.getBooks()));
        readersTable.setItems(FXCollections.observableArrayList(readers));
        libraryStorageTable.refresh();
        readersTable.refresh();//force table update
    }

    public void onButtonLogOutClick(ActionEvent actionEvent) {
        User.activeUser = null;
        FXMLHelper.backScreen();
    }

    public void onButtonShowBookOrdersClick(ActionEvent actionEvent) {
        int selectedIndex = libraryStorageTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "Book is not selected", "Please select a book to show info!");
            return;
        }

        Book selectedBook = libraryStorageTable.getSelectionModel().getSelectedItem();
        BookOrderScreenController bookOrderScreenController = FXMLHelper.loadScreenReturnController("BookOrderScreen");
        bookOrderScreenController.preload(selectedBook, null);
    }
    public void onButtonShowUserOrdersClick(ActionEvent actionEvent) {
        int selectedIndex = readersTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "User is not selected", "Please select a user to show info!");
            return;
        }

        User selectedUser = readersTable.getSelectionModel().getSelectedItem();
        BookOrderScreenController bookOrderScreenController = FXMLHelper.loadScreenReturnController("BookOrderScreen");
        bookOrderScreenController.preload(null, selectedUser);
    }

    public void onButtonReturnUsersBooksClick(ActionEvent actionEvent) {
        int selectedIndex = readersTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "User is not selected", "Please select a user to show info!");
            return;
        }

        User selectedUser = readersTable.getSelectionModel().getSelectedItem();

        List<BookOrder> userActiveOrders = Database.getBookOrdersDb().getByUserId(selectedUser.getId());
        for (BookOrder userActiveOrder : userActiveOrders) {
            mainStorage.getBooksIds().add(userActiveOrder.getBookId());
            userActiveOrder.erase();
        }

        mainStorage.push().pull();
        fillTable();
        FXMLHelper.alertAndWait("Success", "Message", "All users books were returned");
    }
}