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

public class MenuController {

    @FXML
    public Text textWelcome;

    @FXML
    private Text statisticsText;

    @FXML
    private void initialize() {
        Calendar.clearTasks();
        refreshView();
        textWelcome.setText("Welcome " + Main.loggedUser.getName());
        statisticsText.setVisible(false);

        for (Assignment assignment : Main.assignments) {
            if (assignment.getEditedByUser() != null && assignment.getComment() == null) {
                Calendar.addTask(LocalDate.ofEpochDay(assignment.getTeacherDeadline()), assignment);
            }
        }
    }

    private void refreshView(){
        statisticsText.setText(null);
    }


    @FXML
    void buttonCreateAssignment(ActionEvent event) {
        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/createAssignment.fxml"));
            Main.stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonEditGroups(ActionEvent event) {
        try {

            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/editGroups.fxml"));
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void buttonEEDrafts(ActionEvent event) {
        Main.selectedCategory = "EE Draft";
        openTaskTeacherTab();
    }

    @FXML
    void buttonEEFinals(ActionEvent event) {
        Main.selectedCategory = "EE Final";
        openTaskTeacherTab();
    }

    @FXML
    void buttonEEReflections(ActionEvent event) {
        Main.selectedCategory = "EE Reflection";
        openTaskTeacherTab();
    }

    @FXML
    void buttonIAApplication(ActionEvent event) {
        Main.selectedCategory = "EE Application";
        openTaskTeacherTab();
    }

    @FXML
    void buttonIADrafts(ActionEvent event) {
        Main.selectedCategory = "IA Draft";
        openTaskTeacherTab();
    }

    @FXML
    void buttonIAFinals(ActionEvent event) {
        Main.selectedCategory = "IA Final";
        openTaskTeacherTab();
    }

    @FXML
    void buttonIAMaterials(ActionEvent event) {
        Main.selectedCategory = "IA Materials";
        openTaskTeacherTab();
    }

    @FXML
    void buttonIAPlans(ActionEvent event) {
        Main.selectedCategory = "EE Plan";
        openTaskTeacherTab();
    }

    private void openTaskTeacherTab() {
        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/taskTeacher.fxml"));
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
