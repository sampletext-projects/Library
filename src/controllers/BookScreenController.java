package controllers;

import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Book;
import models.BookStorage;
import utils.FXMLHelper;

public class BookScreenController implements FXMLHelper.PreloadableController {

    Book activeBook;

    @FXML
    private Label labelInfo;

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TextField textFieldAuthor;

    @SafeVarargs
    @Override
    public final <T> void preload(T... objects) {
        if (objects.length == 0) {
            labelInfo.setText("Creating Item");
        } else {
            activeBook = (Book) objects[0];
            labelInfo.setText("Editing Item " + activeBook.getId());
            textFieldTitle.setText(activeBook.getTitle());
            textFieldAuthor.setText(activeBook.getAuthor());
        }
    }

    public void onButtonSaveClick(ActionEvent actionEvent) {
        String title = textFieldTitle.getText();
        String author = textFieldAuthor.getText();

        if (title.trim().length() == 0 || author.trim().length() == 0 ) {
            FXMLHelper.alertAndWait("Error", "Title or Author are empty", "Please fill in all fields!");
            return;
        }

        if (activeBook == null) {
            activeBook = new Book();
        }

        activeBook.setTitle(title);
        activeBook.setAuthor(author);
        activeBook.push();

        BookStorage mainStorage = Database.getBookStoragesDb().getMainStorage();
        mainStorage.getBooksIds().add(activeBook.getId());
        mainStorage.push();

        FXMLHelper.alertAndWait("Success", "Operation succeeded", "Book was saved!");

        FXMLHelper.backScreen();
    }

    public void onButtonBackClick(ActionEvent actionEvent) {
        FXMLHelper.backScreen();
    }
}
