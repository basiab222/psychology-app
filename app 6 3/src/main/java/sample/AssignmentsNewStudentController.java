package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AssignmentsNewStudentController {

    @FXML
    public TableView<RowModel> assignmentTable;

    @FXML
    public ComboBox<Assignment> assignmentComboBox;

    @FXML
    public TextArea assignmentTextArea;

    private List<RowModel> rowModels = new ArrayList<>();

    private Assignment teacherAssignment = null;
    private boolean isTextArea = false;

    @FXML
    private void initialize() {
        List<Assignment> userAssignments = new ArrayList<>();
        for (Assignment assignment : Main.assignments) {
            if (assignment.getStudentsIds() != null && assignment.getStudentsIds().contains(Main.loggedUser.getId())
                && assignment.getEditedByUser() == null) {
                userAssignments.add(assignment);
            }
        }
        assignmentComboBox.getItems().addAll(userAssignments);
        assignmentComboBox.setConverter(new StringConverter<Assignment>() {
            @Override
            public String toString(Assignment assignment) {
                return assignment.getCategory() + " - " + LocalDate.ofEpochDay(assignment.getStudentDeadline());
            }

            @Override
            public Assignment fromString(String string) {
                return null;
            }
        });
        assignmentComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            refreshView(newValue);
        });
    }

    private void refreshView(Assignment assignment) {
        this.teacherAssignment = assignment;
        int numberOfRowsInt = assignment.getNumberOfRows();
        int numberOfColumnsInt = assignment.getNumberOfColumns();

        if(assignment.getNumberOfColumns() == 1 && assignment.getNumberOfRows() == 1){
            assignmentTextArea.setVisible(true);
            assignmentTable.setVisible(false);
            isTextArea = true;

            assignmentTextArea.setText(assignment.getText());
            assignmentTextArea.setEditable(true);

        }
        else {
            rowModels.clear();
            assignmentTable.getColumns().clear();
            assignmentTable.setEditable(true);

            List<String> values = Arrays.asList(assignment.getText().split("#", -1));
            int index = 0;
            for (int i = 0; i < numberOfRowsInt; i++) {
                RowModel rowModel = new RowModel();
                for (int j = 0; j < numberOfColumnsInt; j++) {
                    rowModel.setValue(j, values.get(index));
                    index++;
                }
                rowModels.add(rowModel);
            }
            assignmentTable.setItems(FXCollections.observableArrayList(rowModels));

            for (int i = 0; i < numberOfColumnsInt; i++) {
                final int finalI = i;
                TableColumn<RowModel, String> tableColumn = new TableColumn<>();
                tableColumn.setPrefWidth(300);
                tableColumn.setCellFactory(new Callback<TableColumn<RowModel, String>, TableCell<RowModel, String>>() {
                    @Override
                    public TableCell<RowModel, String> call(TableColumn<RowModel, String> param) {
                        return new TextFieldTableCell<RowModel, String>(new StringConverter<String>() {
                            @Override
                            public String toString(String object) {
                                return object.replace("*", "");
                            }

                            @Override
                            public String fromString(String string) {
                                return string;
                            }
                        }) {
                            @Override
                            public void updateItem(String text, boolean empty) {
                                if (text != null && text.length() > 0 && text.charAt(0) == '*') {
                                    setEditable(false);
                                }
                                super.updateItem(text, empty);
                            }
                        };
                    }
                });
                tableColumn.setCellValueFactory(new PropertyValueFactory<>("value" + finalI));
                tableColumn.setOnEditCommit(event -> {
                    event.getTableView().getItems().get(event.getTablePosition().getRow())
                            .setValue(finalI, event.getNewValue());
                });
                assignmentTable.getColumns().add(tableColumn);
            }
        }
    }

    @FXML
    void buttonGoBack(ActionEvent event) {
        try {

            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/menuStudent.fxml"));
            root.add(Calendar.getCalendar(LocalDate.now()), 1, 2);
            Main.stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ok(ActionEvent actionEvent) {
        if (teacherAssignment == null) {
            return;
        }

        if (isTextArea) {
            teacherAssignment.getStudentsIds().remove(Integer.valueOf(Main.loggedUser.getId()));
            FirebaseController.saveAssignment(teacherAssignment);

            Assignment assignment = new Assignment(LocalDate.ofEpochDay(teacherAssignment.getStudentDeadline()),
                    teacherAssignment.getNumberOfRows(), teacherAssignment.getNumberOfColumns(), false, true,
                    teacherAssignment.getCategory(), teacherAssignment.getStudentsIds(), assignmentTextArea.getText());
            assignment.setEditedByUser(Main.loggedUser);

            FirebaseController.saveAssignment(assignment);
        }
        else {
            List<String> databaseValues = new ArrayList<>();
            for (RowModel row : rowModels) {
                for (int i = 0; i < teacherAssignment.getNumberOfColumns(); i++) {
                    String value = row.getValue(i);
                    databaseValues.add(value);
                }
            }

            String codedDatabaseValues = String.join("#", databaseValues);
            teacherAssignment.getStudentsIds().remove(Integer.valueOf(Main.loggedUser.getId()));
            FirebaseController.saveAssignment(teacherAssignment);

            Assignment assignment = new Assignment(LocalDate.ofEpochDay(teacherAssignment.getStudentDeadline()),
                    teacherAssignment.getNumberOfRows(), teacherAssignment.getNumberOfColumns(), false, true,
                    teacherAssignment.getCategory(), teacherAssignment.getStudentsIds(), codedDatabaseValues);
            assignment.setEditedByUser(Main.loggedUser);

            FirebaseController.saveAssignment(assignment);
        }

        try {
            GridPane root = FXMLLoader.load(getClass().getResource("/fxml/menuStudent.fxml"));
            root.add(Calendar.getCalendar(LocalDate.now()), 1, 2);
            Main.stage.getScene().setRoot(root);
        } catch (Exception ex) {

        }
    }
}
