package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditGroupsController {

    @FXML
    private AnchorPane scAnchorPane;

    @FXML
    private AnchorPane scrollPane;

    @FXML
    private ListView<User> groupsListView;

    @FXML
    private ComboBox<StudentsGroup> comboBoxGroups;

    @FXML
    private void initialize() {
        comboBoxGroups.getItems().clear();
        comboBoxGroups.valueProperty().addListener((observable, oldValue, newValue) -> {
            refreshView(newValue);
        });
        comboBoxGroups.setConverter(new StringConverter<StudentsGroup>() {
            @Override
            public String toString(StudentsGroup studentsGroup) {
                return studentsGroup.getName();
            }

            @Override
            public StudentsGroup fromString(String string) {
                return null;
            }
        });

        for (int i = 0; i < Main.groups.size(); i++) {
            comboBoxGroups.getItems().add(Main.groups.get(i));
        }
    }

    private void refreshView(StudentsGroup studentsGroup) {
        groupsListView.getItems().clear();
        groupsListView.getItems().addAll(studentsGroup.getUsers());
        groupsListView.setCellFactory(lv -> {
            TextFieldListCell<User> cell = new TextFieldListCell<>();
            cell.setConverter(new StringConverter<User>() {
                @Override
                public String toString(User user) {
                    return user.getName();
                }

                @Override
                public User fromString(String string) {
                    return null;
                }
            });
            return cell;
        });
    }

    List<User> studentsToAdd = new ArrayList<>();
    
    @FXML
    void buttonAddStudents(ActionEvent event) {
        groupsListView.getItems().clear();
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
                groupsListView.getItems().add(student);
            }
        }

        groupsListView.setCellFactory(CheckBoxListCell.forListView(user -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    studentsToAdd.add(user);
                } else {
                    studentsToAdd.remove(user);
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
    void buttonCreateNewGroup(ActionEvent event) {
        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/createNewGroup.fxml"));
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonGoBack(ActionEvent event) {
        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            root.add(Calendar.getCalendar(LocalDate.now()), 1, 2);
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<User> studentsToRemove = new ArrayList<>();

    @FXML
    void buttonRemoveStudents(ActionEvent event) {
        groupsListView.setCellFactory(CheckBoxListCell.forListView(user -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    studentsToRemove.add(user);
                } else {
                    studentsToRemove.remove(user);
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
    void buttonSaveChanges(ActionEvent event) {
        for (int i = 0; i < studentsToRemove.size(); i++) {
            FirebaseController.deleteStudentFromGroup(studentsToRemove.get(i), comboBoxGroups.getValue());
        }

        for (int i = 0; i < studentsToAdd.size(); i++) {
            FirebaseController.addStudentToGroup(studentsToAdd.get(i),comboBoxGroups.getValue());
        }
    }
}