package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import utils.FXMLHelper;

public class RegistrationScreenController {
    public TextField nameTextField;
    public TextField loginTextField;
    public PasswordField passwordTextField;
    public ComboBox<String> positionComboBox;

    public void onButtonRegistrationClick(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        String position;
        if (!positionComboBox.getSelectionModel().isEmpty()) {
            position = positionComboBox.getItems().get(positionComboBox.getSelectionModel().getSelectedIndex());
        } else {
            FXMLHelper.alertAndWait("Error", "Position is empty", "Please select position!");
            return;
        }

        if (login.trim().length() == 0 || password.trim().length() == 0 || name.trim().length() == 0) {
            FXMLHelper.alertAndWait("Error", "Login, password or name is empty", "Please fill in all fields!");
            return;
        }

        User user = new User(-1, login, password, position, name).push();

        // If needed, initialize related entities for user here

        FXMLHelper.loadScreen("LoginScreen");
    }
}