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
import models.User;
import utils.FXMLHelper;

import java.util.List;

public class BookOrderScreenController implements FXMLHelper.PreloadableController {

    List<BookOrder> associatedBookOrders;

    @FXML
    private Label labelInfo;

    @FXML
    private TableView<BookOrder> ordersTable;

    @FXML
    private TableColumn<BookOrder, String> columnAuthor;
    @FXML
    private TableColumn<BookOrder, String> columnTitle;
    @FXML
    private TableColumn<BookOrder, String> columnUserName;

    @SafeVarargs
    @Override
    public final <T> void preload(T... objects) {

        Book book = (Book) objects[0];
        User user = (User) objects[1];

        if (book != null) {
            associatedBookOrders = Database.getBookOrdersDb().getByBookId(book.getId());
            labelInfo.setText("Book orders for book " + book.getId());
        } else if (user != null) {
            associatedBookOrders = Database.getBookOrdersDb().getByUserId(user.getId());
            labelInfo.setText("Book orders for user " + user.getId());
        }

        columnAuthor.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBook().getAuthor()));
        columnTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBook().getTitle()));
        columnUserName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUser().getName()));

        fillTable();
    }

    private void fillTable() {
        ordersTable.setItems(FXCollections.observableArrayList(associatedBookOrders));
        ordersTable.refresh();//force table update
    }

    public void onButtonBackClick(ActionEvent actionEvent) {
        FXMLHelper.backScreen();
    }
}
