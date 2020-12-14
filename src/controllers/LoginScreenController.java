package controllers;

import database.Database;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import utils.FXMLHelper;

public class LoginScreenController {
    public TextField loginTextField;
    public PasswordField passwordTextField;

    public void onButtonLogInClick(ActionEvent actionEvent) {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();

        if (login.trim().length() == 0 || password.trim().length() == 0) {
            FXMLHelper.alertAndWait("Error", "Login or password are empty", "Please fill in all fields!");
            return;
        }

        User loginedUser = Database.getUsersDb().selectByLoginAndPassword(login, password);

        if (loginedUser == null) {
            FXMLHelper.alertAndWait("Error", "User not found", "Please check all fields!");
            return;
        }

        User.activeUser = loginedUser;
        switch (loginedUser.getPosition()) {
            case "Admin":
                AdminScreenController adminScreenController = FXMLHelper.loadScreenReturnController("AdminScreen");
                adminScreenController.preload();
                break;
            case "Librarian":
                // ReaderScreenController userScreenController = FXMLHelper.loadScreenReturnController("LibrarianScreen");
                // userScreenController.preload();
                break;
            case "Reader":
                ReaderScreenController readerScreenController = FXMLHelper.loadScreenReturnController("ReaderScreen");
                readerScreenController.preload();
                break;
        }
    }

    public void onButtonRegistrationCLick(ActionEvent actionEvent) {
        FXMLHelper.loadScreen("RegistrationScreen");
    }
}
