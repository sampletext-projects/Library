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
import models.BookStorage;
import models.User;
import utils.FXMLHelper;

public class AdminScreenController implements FXMLHelper.PreloadableController, FXMLHelper.NotifiableController {

    private BookStorage mainStorage;

    @FXML
    private Label greetingLabel;
    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableColumn<Book, String> columnAuthor;
    @FXML
    private TableColumn<Book, String> columnTitle;

    @SafeVarargs
    @Override
    public final <T> void preload(T... objects) {
        if (User.activeUser != null) {
            greetingLabel.setText(String.format("Hello, %s %s", User.activeUser.getPosition(), User.activeUser.getName()));
        }

        columnAuthor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAuthor()));
        columnTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));

        mainStorage = Database.getBookStoragesDb().getMainStorage();
        fillTable();
    }

    @SafeVarargs
    @Override
    public final <T> void onNotify(T... objects) {
        String command = (String) objects[0];
        if ("Update".equals(command)) {
            mainStorage = Database.getBookStoragesDb().getMainStorage();
            fillTable();
        }
    }

    private void fillTable() {
        booksTableView.setItems(FXCollections.observableArrayList(mainStorage.getBooks()));
        booksTableView.refresh();
    }

    public void onButtonAddClick(ActionEvent actionEvent) {
        BookScreenController bookScreenController = FXMLHelper.loadScreenReturnController("BookScreen");
        bookScreenController.preload();
    }

    public void onButtonEditClick(ActionEvent actionEvent) {
        int selectedIndex = booksTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "Entity is not selected", "Please select an entity to edit!");
            return;
        }
        Book book = booksTableView.getItems().get(selectedIndex);

        BookScreenController bookScreenController = FXMLHelper.loadScreenReturnController("BookScreen");
        bookScreenController.preload(book);
    }

    public void onButtonDeleteClick(ActionEvent actionEvent) {
        int selectedIndex = booksTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "Entity is not selected", "Please select a book to delete!");
            return;
        }
        Book book = booksTableView.getItems().get(selectedIndex);
        mainStorage.getBooksIds().remove((Integer) book.getId());//remove book from library storage
        mainStorage.push().pull();//commit mainStorage and reload book storage
        book.erase();//erase book
        fillTable();

        FXMLHelper.alertAndWait("Success", "Operation succeeded", "Book was deleted!");
    }

    public void onButtonLogOutClick(ActionEvent actionEvent) {
        User.activeUser = null;
        FXMLHelper.backScreen();
    }
}