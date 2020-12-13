package controllers;

import database.Database;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.BookOrder;
import models.User;
import utils.FXMLHelper;

import java.util.List;

public class LibrarianScreenController implements FXMLHelper.PreloadableController {

    private List<BookOrder> allOrders;

    @FXML
    private Label greetingLabel;

    @FXML
    private TableView<BookOrder> allBookOrdersTable;
    @FXML
    private TableColumn<BookOrder, String> columnAuthor;
    @FXML
    private TableColumn<BookOrder, String> columnTitle;
    @FXML
    private TableColumn<BookOrder, String> columnUserName;
    @FXML
    private TableColumn<BookOrder, Boolean> finishedColumn;

    @SafeVarargs
    @Override
    public final <T> void preload(T... objects) {
        if (User.activeUser != null) {
            greetingLabel.setText(String.format("Hello, %s %s", User.activeUser.getPosition(), User.activeUser.getName()));
        }

        allOrders = Database.getOrdersDb().select(null);

        userNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUser().getName()));
        itemsCountColumn.setCellValueFactory(param -> new SimpleStringProperty(Integer.toString(param.getValue().getStorage().getEntities_ids().size())));
        costColumn.setCellValueFactory(param -> new SimpleStringProperty(Double.toString(param.getValue().getStorage().getCost())));
        finishedColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().isFinished()));

        fillTable();
    }

    private void fillTable() {
        allBookOrdersTable.setItems(FXCollections.observableArrayList(allOrders));
        allBookOrdersTable.refresh();//force table update
    }

    public void onButtonFinishClick(ActionEvent actionEvent) {
        int selectedIndex = allBookOrdersTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "Order is not selected", "Please select an order to finish!");
            return;
        }

        Order order = allBookOrdersTable.getItems().get(selectedIndex);

        if (order.isFinished()) {
            FXMLHelper.alertAndWait("Notice", "Unsupported action", "You can't finish a finished order");
            return;
        }

        order.setFinished(true);
        order.push();
        fillTable();

        FXMLHelper.alertAndWait("Success", "Operation succeeded", "Order was finished!");
    }

    public void onButtonShowClick(ActionEvent actionEvent) {
        int selectedIndex = allBookOrdersTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            FXMLHelper.alertAndWait("Error", "Order is not selected", "Please select an order to show!");
            return;
        }
        Order order = allBookOrdersTable.getItems().get(selectedIndex);
        OrderScreenController orderScreenController = FXMLHelper.loadScreenReturnController("OrderScreen");
        orderScreenController.preload(order);
    }

    public void onButtonLogOutClick(ActionEvent actionEvent) {
        User.activeUser = null;
        FXMLHelper.backScreen();
    }
}