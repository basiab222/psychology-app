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

public class TaskTeacherController {

    @FXML
    public TableView<RowModel> assignmentTable;

    @FXML
    public ComboBox<Assignment> assignmentComboBox;

    @FXML
    public TextArea assignmentTextArea;

    private List<RowModel> rowModels = new ArrayList<>();

    private Assignment currentAssignment = null;
    private boolean isTextArea = false;

    @FXML
    private void initialize() {
        List<Assignment> userAssignments = new ArrayList<>();
        for (Assignment assignment : Main.assignments) {
            if (assignment.getEditedByUser() != null && assignment.getCategory().equals(Main.selectedCategory)) {
                userAssignments.add(assignment);
            }
        }
        assignmentComboBox.getItems().addAll(userAssignments);
        assignmentComboBox.setConverter(new StringConverter<Assignment>() {
            @Override
            public String toString(Assignment assignment) {
                return assignment.getEditedByUser().getUsername() + " - " + LocalDate.ofEpochDay(assignment.getTeacherDeadline());
            }

            @Override
            public Assignment fromString(String string) {
                return null;
            }
        });
        assignmentComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            refreshTable(newValue);
        });

    }

    private void refreshTable(Assignment assignment) {
        this.currentAssignment = assignment;
        int numberOfRowsInt = assignment.getNumberOfRows();
        int numberOfColumnsInt = assignment.getNumberOfColumns();

        if (numberOfRowsInt == 1 && numberOfColumnsInt == 1) {
            assignmentTextArea.setVisible(true);
            assignmentTable.setVisible(false);
            assignmentTextArea.setText(assignment.getText());
            isTextArea = true;
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
                            public void updateItem(String item, boolean empty) {
                                setEditable(false);
                                super.updateItem(item, empty);
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
            if (assignment.getComment() != null) {
                List<String> comments = Arrays.asList(assignment.getComment().split("#", -1));
                for (int i = 0; i < rowModels.size(); i++) {
                    RowModel rowModel = rowModels.get(i);
                    rowModel.setComment(comments.get(i));
                }
            }

            TableColumn<RowModel, String> commentColumn = new TableColumn<>("Comment");
            commentColumn.setPrefWidth(150);
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
                            setEditable(true);
                            super.updateItem(item, empty);
                        }
                    };
                }
            });
            commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
            commentColumn.setOnEditCommit(event -> {
                event.getTableView().getItems().get(event.getTablePosition().getRow())
                        .setComment(event.getNewValue());
            });
            assignmentTable.getColumns().add(commentColumn);
        }
    }

    @FXML
    public void ok(ActionEvent actionEvent) {
        if (isTextArea){
            currentAssignment.setText(assignmentTextArea.getText());
            currentAssignment.setComment(assignmentTextArea.getText());
        }
        else {
            List<String> comments = new ArrayList<>();
            for (RowModel rowModel : rowModels) {
                comments.add(rowModel.getComment());
            }
            String comment = String.join("#", comments);
            currentAssignment.setComment(comment);
        }
        currentAssignment.setChecked(true);
        FirebaseController.saveAssignment(currentAssignment);
        try {
                GridPane root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
                root.add(Calendar.getCalendar(LocalDate.now()), 1, 2);
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
}
