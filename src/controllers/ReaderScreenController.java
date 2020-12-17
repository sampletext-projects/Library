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

import java.util.List;

public class ReaderScreenController implements FXMLHelper.PreloadableController, FXMLHelper.NotifiableController {

    private BookStorage mainStorage;
    private List<BookOrder> userOrders;

    @FXML
    private Label greetingLabel;

    @FXML
    private TableView<Book> libraryStorageTable;

    @FXML
    private TableColumn<Book, String> columnLibraryAuthor;

    @FXML
    private TableColumn<Book, String> columnLibraryTitle;

    @FXML
    private TableView<BookOrder> userStorageTable;

    @FXML
    private TableColumn<BookOrder, String> columnUserAuthor;

    @FXML
    private TableColumn<BookOrder, String> columnUserTitle;

    @SafeVarargs
    @Override
    public final <T> void preload(T... object) {
        if (User.activeUser != null) {
            greetingLabel.setText(String.format("Hello, %s %s", User.activeUser.getPosition(), User.activeUser.getName()));
            userOrders = Database.getBookOrdersDb().getByUserId(User.activeUser.getId());
        }

        columnLibraryAuthor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthor()));
        columnLibraryTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));

        columnUserAuthor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBook().getAuthor()));
        columnUserTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBook().getTitle()));

        mainStorage = Database.getBookStoragesDb().getMainStorage();

        fillTables();
    }

    @SafeVarargs
    @Override
    public final <T> void onNotify(T... objects) {
        String command = (String) objects[0];
        if ("Update".equals(command)) {
            mainStorage = Database.getBookStoragesDb().getMainStorage();
            fillTables();
        }
    }

    private void fillTables() {
        libraryStorageTable.setItems(FXCollections.observableArrayList(mainStorage.getBooks()));
        userStorageTable.setItems(FXCollections.observableArrayList(userOrders));
        libraryStorageTable.refresh();
        userStorageTable.refresh();
    }

    public void onButtonGetBookClick(ActionEvent actionEvent) {
        int selectedIndex = libraryStorageTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "Book is not selected", "Please select a book to take!");
            return;
        }

        int bookId = libraryStorageTable.getSelectionModel().getSelectedItem().getId();

        mainStorage.getBooksIds().remove((Integer) bookId);
        mainStorage.push().pull();

        BookOrder bookOrder = new BookOrder(-1, bookId, User.activeUser.getId()).push().pull();
        userOrders.add(bookOrder);

        fillTables();
    }

    public void onButtonReturnBookClick(ActionEvent actionEvent) {
        int selectedIndex = userStorageTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "Book is not selected", "Please select a book to return!");
            return;
        }

        BookOrder bookOrder = userStorageTable.getSelectionModel().getSelectedItem();
        int bookId = bookOrder.getBookId();

        mainStorage.getBooksIds().add(bookId);
        mainStorage.push().pull();

        // update UI-only
        userOrders.remove(bookOrder);
        bookOrder.erase();

        fillTables();
    }

    public void onButtonLogOutClick(ActionEvent actionEvent) {
        User.activeUser = null;
        FXMLHelper.backScreen();
    }
}