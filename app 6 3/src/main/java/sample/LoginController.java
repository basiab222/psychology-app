package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    void logIN(ActionEvent event) {

        if (User.loginIsValid(usernameTextField.getText(), passwordTextField.getText())) {
            Main.loggedUser = User.getUserByUsername(usernameTextField.getText());
            try {
                GridPane root;
                if (User.isTeacher(usernameTextField.getText())) {
                    root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/fxml/menuStudent.fxml"));
                }
                root.add(Calendar.getCalendar(LocalDate.now()), 1, 2);
                Main.stage.getScene().setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Password or username is wrong, or connect to the Internet.");
            alert.show();
        }
    }

    @FXML
    void signUp(ActionEvent event) {
        try {

            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/signup.fxml"));
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
