package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Calendar {
    private static GridPane calendar;

    private static LocalDate current;

    private static LocalDate start;

    private static final Map<LocalDate, List<Assignment>> tasksMap = new HashMap<>();

    private static final List<CalendarSquare> calendarSquares = new ArrayList<>();

    public static void clearTasks() {
        tasksMap.clear();
    }

    public static void addTask(LocalDate localDate, Assignment task) {
        List<Assignment> currentTasks = tasksMap.getOrDefault(localDate, new ArrayList<>());
        currentTasks.add(task);
        tasksMap.put(localDate, currentTasks);
        if (start != null) {
            refreshTasks();
        }
    }

    public static GridPane getCalendar(LocalDate now) {
        GridPane root = new GridPane();
        current = now;
        root.setPrefSize(1000, 800);

        calendar = swapCalendar();
        root.add(calendar, 0, 2);

        GridPane monthYear = new GridPane();
        monthYear.setPrefSize(600, 100);

        Text calendarTitle = new Text();
        calendarTitle.setText(current.getMonth() + " " + current.getYear() + " ");

        StackPane stackPane = new StackPane();
        Rectangle rectangle = new Rectangle(150, 20);
        stackPane.getChildren().add(rectangle);
        stackPane.getChildren().add(calendarTitle);
        rectangle.setFill(new Color(1, 1, 1, 0));


        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(event -> {
            current = current.minusMonths(1);
            root.getChildren().remove(calendar);
            calendar = swapCalendar();
            root.add(calendar, 0, 1);

            StackPane stackPane1 = new StackPane();
            Rectangle rectangle1 = new Rectangle(150, 20);
            stackPane1.getChildren().add(rectangle1);
            stackPane1.getChildren().add(calendarTitle);
            rectangle1.setFill(new Color(1, 1, 1, 0));

            monthYear.getChildren().remove(calendarTitle);
            calendarTitle.setText(current.getMonth() + " " + current.getYear() + " ");
            monthYear.add(stackPane1, 1, 0);
            refreshTasks();
        });

        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(event -> {
            current = current.plusMonths(1);
            root.getChildren().remove(calendar);
            calendar = swapCalendar();
            root.add(calendar, 0, 1);

            StackPane stackPane1 = new StackPane();
            Rectangle rectangle1 = new Rectangle(150, 20);
            stackPane1.getChildren().add(rectangle1);
            stackPane1.getChildren().add(calendarTitle);
            rectangle1.setFill(new Color(1, 1, 1, 0));

            monthYear.getChildren().remove(calendarTitle);
            calendarTitle.setText(current.getMonth() + " " + current.getYear() + " ");
            monthYear.add(stackPane1, 1, 0);
            refreshTasks();
        });

        monthYear.add(stackPane, 1, 0);
        monthYear.add(previousMonth, 0, 0);
        monthYear.add(nextMonth, 2, 0);

        monthYear.setAlignment(Pos.CENTER);
        root.add(monthYear, 0, 0);

        refreshTasks();

        return root;
    }

    private static void refreshTasks() {
        for (Map.Entry<LocalDate, List<Assignment>> localDateListEntry : tasksMap.entrySet()) {
            LocalDate taskDate = localDateListEntry.getKey();
            List<Assignment> assignments = localDateListEntry.getValue();
            for (int i = 0; i < assignments.size(); i++) {
                if (i == 4) {
                    break;
                }
                long daysBetween = ChronoUnit.DAYS.between(start, taskDate);
                if (daysBetween >= 0 && daysBetween <= 35) {
                    CalendarSquare calendarSquare = calendarSquares.get((int) daysBetween);
                    calendarSquare.getTasks().get(i).setContent(assignments.get(i).getCategory(), i);
                }
            }
        }
    }

    private static GridPane swapCalendar() {
        GridPane calendar = new GridPane();
        start = LocalDate.of(current.getYear(), current.getMonthValue(), 1);
        while (!start.getDayOfWeek().toString().equals("MONDAY")) {
            start = start.minusDays(1);
        }

        int d = 0;

        calendarSquares.clear();

        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane task1 = new StackPane();
                Text text1 = createText("");
                Rectangle rectangle1 = createRectangle(100, 20);
                task1.getChildren().addAll(rectangle1, text1);

                StackPane task2 = new StackPane();
                Text text2 = createText("");
                Rectangle rectangle2 = createRectangle(100, 20);
                task2.getChildren().addAll(rectangle2, text2);

                StackPane dayCenter = new StackPane();
                LocalDate squareDate = start.plusDays(d);
                dayCenter.getChildren().addAll(createRectangle(100, 20), createBoldText(squareDate.getDayOfMonth() + ""));
                dayCenter.setOnMouseClicked(event -> clickedNumberOfCalendar(squareDate));

                StackPane task3 = new StackPane();
                Text text3 = createText("");
                Rectangle rectangle3 = createRectangle(100, 20);
                task3.getChildren().addAll(rectangle3, text3);

                StackPane task4 = new StackPane();
                Text text4 = createText("");
                Rectangle rectangle4 = createRectangle(100, 20);
                task4.getChildren().addAll(rectangle4, text4);

                calendarSquares.add(new CalendarSquare(Arrays.asList(
                    new CalendarTask(text1, rectangle1),
                    new CalendarTask(text2, rectangle2),
                    new CalendarTask(text3, rectangle3),
                    new CalendarTask(text4, rectangle4)))
                );

                VBox tasks = new VBox();
                tasks.getChildren().addAll(task1, task2, dayCenter, task3, task4);

                Rectangle bigSquare = new Rectangle(100, 100);
                bigSquare.setFill(Color.WHITE);
                bigSquare.setStroke(Color.BLACK);

                StackPane stackPane2 = new StackPane();
                stackPane2.getChildren().add(bigSquare);
                stackPane2.getChildren().add(tasks);

                calendar.add(stackPane2, j, i);
                d++;
            }
        }

        Text[] dayNames = new Text[]{new Text("Monday"), new Text("Tuesday"),
            new Text("Wednesday"), new Text("Thursday"), new Text("Friday"), new Text("Saturday"), new Text("Sunday")
        };

        for (int i = 0; i < dayNames.length; i++) {
            StackPane stackPane = new StackPane();

            Rectangle rectangle = new Rectangle(70, 20);
            rectangle.setFill(Color.WHITE);

            stackPane.getChildren().add(rectangle);
            stackPane.getChildren().add(dayNames[i]);

            calendar.add(stackPane, i, 0);
        }
        return calendar;
    }

    private static Rectangle createRectangle(double width, double height) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.TRANSPARENT);
        return rectangle;
    }

    private static Text createText(String message) {
        return new Text(message);
    }

    private static Text createBoldText(String message) {
        Text text = new Text(message);
        text.setStyle("-fx-font-weight: bold");
        return text;
    }

    private static void clickedNumberOfCalendar(LocalDate squareDate) {
        List<Assignment> assignments = tasksMap.getOrDefault(squareDate, Collections.emptyList());

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);

        List<Text> texts = new ArrayList<>();
        texts.add(new Text("Day: " + squareDate.toString()));
        texts.add(new Text());

        for (Assignment assignment : assignments) {
            String name = "";
            if (assignment.getEditedByUser() != null) {
                User editedBy = assignment.getEditedByUser();
                name = " - " + editedBy.getName();
            }
            texts.add(new Text(assignment.getCategory() + name));
        }

        if (texts.size() == 2) {
            texts.add(new Text("No assignments"));
        }

        VBox hBox = new VBox();
        hBox.getChildren().addAll(texts);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(15));
        dialogStage.setTitle("Assignments");
        dialogStage.setWidth(400);
        dialogStage.setHeight(300);
        dialogStage.setScene(new Scene(hBox));
        dialogStage.show();
    }
}
