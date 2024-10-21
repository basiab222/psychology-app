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

public class AssignmentsAssessedStudentController {

    @FXML
    public TableView<RowModel> assignmentTable;

    @FXML
    public ComboBox<Assignment> assignmentComboBox;

    @FXML
    public TextArea assignmentTextArea;

    private List<RowModel> rowModels = new ArrayList<>();

    private Assignment teacherAssignment = null;

    @FXML
    private void initialize() {
        List<Assignment> assignmentsAssessed = new ArrayList<>();
        for (Assignment assignment : Main.assignments) {
            if (assignment.getEditedByUser() != null && assignment.getEditedByUser().getId() == Main.loggedUser.getId()
                && assignment.getComment() != null) {
                assignmentsAssessed.add(assignment);
            }
        }
        assignmentComboBox.getItems().addAll(assignmentsAssessed);
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

        if (numberOfRowsInt == 1 && numberOfColumnsInt == 1) {
            assignmentTextArea.setVisible(true);
            assignmentTable.setVisible(false);
            assignmentTextArea.setText(assignment.getText());

        } else {
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
                tableColumn.setPrefWidth(100);
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
                                setEditable(false);
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
            List<String> comments = Arrays.asList(assignment.getComment().split("#", -1));
            for (int i = 0; i < rowModels.size(); i++) {
                RowModel rowModel = rowModels.get(i);
                rowModel.setComment(comments.get(i));
            }

            TableColumn<RowModel, String> commentColumn = new TableColumn<>("Comment");
            commentColumn.setPrefWidth(300);
            commentColumn.setCellFactory(new Callback<TableColumn<RowModel, String>, TableCell<RowModel, String>>() {
                @Override
                public TableCell<RowModel, String> call(TableColumn<RowModel, String> param) {
                    return new TextFieldTableCell<RowModel, String>(new StringConverter<String>() {
                        @Override
                        public String toString(String object) {
                            return object;
                        }

                        @Override
                        public String fromString(String string) {
                            return string;
                        }
                    }) {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            setEditable(false);
                            super.updateItem(item, empty);
                        }
                    };
                }
            });
            commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
            assignmentTable.getColumns().add(commentColumn);
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
}
