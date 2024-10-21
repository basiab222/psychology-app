package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;

public class CreateNewGroupController {

    @FXML
    private TextField groupsNameTextField;

    @FXML
    private ListView<User> studentsListView;

    private List<User> selectedStudents;

    @FXML
    private void initialize() {
        selectedStudents = new ArrayList<>();
        List<User> allUsersInAllGroups = new ArrayList<>();
        for (StudentsGroup group : Main.groups) {
            List<User> users = group.getUsers();
            allUsersInAllGroups.addAll(users);
        }

        for (User student : Main.students) {
            boolean isNotStudentInGroup = true;
            for (User userInGroup : allUsersInAllGroups) {
                if (userInGroup.getId() == student.getId()) {
                    isNotStudentInGroup = false;
                    break;
                }
            }
            if (isNotStudentInGroup) {
                studentsListView.getItems().add(student);
            }
        }

        studentsListView.setCellFactory(CheckBoxListCell.forListView(user -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedStudents.add(user);
                } else {
                    selectedStudents.remove(user);
                }
            });
            return observable;
        }, new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user.getName();
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        }));
    }

    @FXML
    void buttonGoBack(ActionEvent event) {
        try {

            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/editGroups.fxml"));
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonOk(ActionEvent event) {
        if (selectedStudents.isEmpty() || groupsNameTextField.getText().isEmpty()) {
            return;
        }
        StudentsGroup studentsGroup = new StudentsGroup(groupsNameTextField.getText(), selectedStudents);
        FirebaseController.saveGroup(studentsGroup);

        try {

            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/editGroups.fxml"));
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
