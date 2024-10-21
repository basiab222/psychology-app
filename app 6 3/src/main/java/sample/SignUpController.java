package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.management.Notification;
import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private PasswordField textFieldRepeatPassword;

    @FXML
    public void createAccount(ActionEvent event) {
        String name = textFieldName.getText();
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        String repeatedPassword = textFieldRepeatPassword.getText();
        boolean isTeacher = false;
        boolean isTaken = false;

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()){
            return;
        }

        for (int i = 0; i < Main.users.size(); i++) {
            if (username.equals(Main.users.get(i).getUsername())) {
                isTaken = true;
                break;
            }
        }

        if (isTaken){
            Alert alert = new Alert(Alert.AlertType.WARNING,"This username is already taken, try different one.");
            alert.show();
        }

        if (password.equals(repeatedPassword)){
            User user = new User(username,password,name,isTeacher);
            FirebaseController.saveUser(user);
            try {
                GridPane root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
                Main.stage.getScene().setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Passwords are not the same.");
            alert.show();
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        GridPane root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.stage.getScene().setRoot(root);

    }

}
