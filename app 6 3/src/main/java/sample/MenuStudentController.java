package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuStudentController {

    @FXML
    public Text textWelcome;

    @FXML
    private Text statisticsText;

    @FXML
    private void initialize() {
        Calendar.clearTasks();
        textWelcome.setText("Welcome " + Main.loggedUser.getName());
        statisticsText.setVisible(false);

        for (Assignment assignment : Main.assignments) {
            if (assignment.getEditedByUser() == null && assignment.getStudentsIds() != null &&
                assignment.getStudentsIds().contains(Main.loggedUser.getId())) {
                Calendar.addTask(LocalDate.ofEpochDay(assignment.getStudentDeadline()), assignment);
            }
        }
    }

    @FXML
    void buttonAssessed(ActionEvent event) {
        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/assignmentsAssessedStudent.fxml"));
            Main.stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonNew(ActionEvent event) {
        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/assignmentsNewStudent.fxml"));
            Main.stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logOut(ActionEvent event) {
        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Main.stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
