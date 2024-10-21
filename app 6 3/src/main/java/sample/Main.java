package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {
    public static String selectedCategory = "";
    public static User loggedUser = null;
    public static Stage stage;
    public static List<User> users = new ArrayList<>();
    public static List<StudentsGroup> groups = new ArrayList<>();
    public static List<Assignment> assignments = new ArrayList<>();
    public static List<User> students = new ArrayList<>();
    double x, y = 0;
    public static List<String> categories = Arrays.asList(
            "IA Materials",
            "IA Draft",
            "IA Final",
            "EE Application",
            "EE Plan",
            "EE Drafts",
            "EE Finals",
            "EE Reflections"
    );

    @Override
    public void start(Stage primaryStage) throws Exception {
        FirebaseController.initDatabase();
        createTeacherAccount();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

        FirebaseController.getAssignments();
        FirebaseController.getGroups();
        FirebaseController.getUsers();

        stage = primaryStage;
        primaryStage.setScene(new Scene(root, 1200, 800));

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getSceneX() - x);
            primaryStage.setY(event.getSceneY() - y);
        });

        primaryStage.show();
    }

    public static void createTeacherAccount() {
        User teacher = new User(1, "teacher", "teacher", "Martha Clark", true);

        FirebaseController.saveUser(teacher);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
