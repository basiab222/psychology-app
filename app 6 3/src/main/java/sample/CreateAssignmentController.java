package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class CreateAssignmentController {

    @FXML
    public TableView<RowModel> assignmentTable;

    @FXML
    public TextArea assignmentTextArea;

    @FXML
    private ComboBox<StudentsGroup> groupComboBox;

    @FXML
    private TextField numberOfRows, numberOfColumns;

    @FXML
    private DatePicker deadline;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ListView<User> listViewStudents;

    @FXML
    public Button selectAllButton;

    Integer numberOfRowsInt = null;
    Integer numberOfColumnsInt = null;

    private List<User> selectedStudents;
    private List<RowModel> rowModels;

    private StudentsGroup activeStudentsGroup;

    @FXML
    private void initialize() {
        selectedStudents = new ArrayList<>();
        rowModels = new ArrayList<>();
        categoryComboBox.getItems().addAll(Main.categories);
        numberOfRows.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfRows.setText(oldValue);
            } else {
                buildTable();
            }
        });

        numberOfColumns.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfColumns.setText(oldValue);
            } else {
                buildTable();
            }
        });

        groupComboBox.getItems().clear();
        groupComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            refreshView(newValue, false);
        });
        groupComboBox.setConverter(new StringConverter<StudentsGroup>() {
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
            groupComboBox.getItems().add(Main.groups.get(i));
        }
    }

    private void buildTable() {
        try {
            numberOfRowsInt = Integer.parseInt(numberOfRows.getText());
            numberOfColumnsInt = Integer.min(Integer.parseInt(numberOfColumns.getText()), 5);
        } catch (Exception ex) {
            return;
        }
        if (numberOfRows.getText().equals("1") && numberOfColumns.getText().equals("1")){
            assignmentTable.setVisible(false);
            assignmentTextArea.setVisible(true);
        }
        else {
            assignmentTable.setVisible(true);
            assignmentTextArea.setVisible(false);
            assignmentTable.getColumns().clear();
            assignmentTable.setEditable(true);
            for (int i = 0; i < numberOfColumnsInt; i++) {
                int finalI = i;
                TableColumn<RowModel, String> tableColumn = new TableColumn<>();
                tableColumn.setPrefWidth(100);
                tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                tableColumn.setCellValueFactory(new PropertyValueFactory<>("value" + finalI));
                tableColumn.setOnEditCommit(event -> event.getTableView().getItems().get(event.getTablePosition().getRow())
                        .setValue(finalI, event.getNewValue()));
                assignmentTable.getColumns().add(tableColumn);
            }
            assignmentTable.getItems().clear();
            for (int j = 0; j < numberOfRowsInt; j++) {
                RowModel rowModel = new RowModel();
                rowModels.add(rowModel);
                assignmentTable.getItems().add(j, rowModel);
            }
        }
    }

    private void refreshView(StudentsGroup studentsGroup, boolean allChecked) {
        activeStudentsGroup = studentsGroup;
        listViewStudents.getItems().clear();

        listViewStudents.getItems().addAll(studentsGroup.getUsers());

        listViewStudents.setCellFactory(CheckBoxListCell.forListView(user -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.set(allChecked);
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
    void goBack(ActionEvent event) {
        try {

            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            root.add(Calendar.getCalendar(LocalDate.now()), 1, 2);
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ok(ActionEvent event) {
        if (numberOfRowsInt == null || numberOfColumnsInt == null) {
            return;
        }
        LocalDate deadLineDate = deadline.valueProperty().get();
        String category = categoryComboBox.getSelectionModel().getSelectedItem();
        if (deadLineDate == null || category == null || selectedStudents.isEmpty()) {
            return;
        }

        List<Integer> studentsIds = new ArrayList<>();
        for (User student : selectedStudents) {
            studentsIds.add(student.getId());
        }

        List<String> databaseValues = new ArrayList<>();
        for (RowModel row : rowModels) {
            for (int i = 0; i < numberOfColumnsInt; i++) {
                String value;
                if (row.getValue(i).isEmpty()) {
                    value = row.getValue(i);
                } else {
                    value = "*" + row.getValue(i);
                }
                databaseValues.add(value.replace("#", ""));
            }
        }

        String codedDatabaseValues = String.join("#", databaseValues);

        if (numberOfRows.getText().equals("1") && numberOfColumns.getText().equals("1")){
            Assignment assignment = new Assignment(deadLineDate, numberOfRowsInt, numberOfColumnsInt,
                    false, false, category, studentsIds, assignmentTextArea.getText());
            FirebaseController.saveAssignment(assignment);
        }
        else {
            Assignment assignment = new Assignment(deadLineDate, numberOfRowsInt, numberOfColumnsInt,
                    false, false, category, studentsIds, codedDatabaseValues);
            FirebaseController.saveAssignment(assignment);
        }

        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            root.add(Calendar.getCalendar(LocalDate.now()), 1, 2);
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void selectAll(ActionEvent actionEvent) {
        if (selectedStudents.isEmpty() && activeStudentsGroup != null) {
            selectedStudents.addAll(activeStudentsGroup.getUsers());
            refreshView(activeStudentsGroup, true);
        } else if (activeStudentsGroup != null){
            selectedStudents.clear();
            refreshView(activeStudentsGroup, false);
        }
    }
}
