package sample;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseController {
    private static final String FILE_NAME = "ia-computerscience-firebase-adminsdk-44iia-3b7fc88928.json";
    private static final String DATABASE_NAME = "https://ia-computerscience-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String USERS_URL = "/users/";
    private static final String GROUPS_URL = "/groups/";
    private static final String ASSIGNMENTS_URL = "/assignments";

    private static FirebaseDatabase db;

    public static void initDatabase() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(FILE_NAME);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(DATABASE_NAME)
                .build();

        FirebaseApp.initializeApp(options);

        db = FirebaseDatabase.getInstance();
    }

    public static void saveUser(User user) {
        DatabaseReference reference = db.getReference(USERS_URL);
        reference.child(String.valueOf(user.getId())).setValueAsync(user);

    }

    public static void saveGroup(StudentsGroup group) {
        DatabaseReference reference = db.getReference(GROUPS_URL);
        reference.child(String.valueOf(group.getId())).setValueAsync(group);

    }

    public static void saveAssignment(Assignment assignment) {
        DatabaseReference reference = db.getReference(ASSIGNMENTS_URL);
        reference.child(String.valueOf(assignment.getId())).setValueAsync(assignment);
    }


    public static void getUsers() {
        DatabaseReference reference = db.getReference(USERS_URL);


        reference.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                Main.users.add(user);
                if (!user.isTeacher()) {
                    Main.students.add(user);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }


            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("error");
            }
        });
    }

    public static void getGroups() {
        DatabaseReference reference = db.getReference(GROUPS_URL);

        reference.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudentsGroup group = dataSnapshot.getValue(StudentsGroup.class);
                Main.groups.add(group);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }


            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("error");
            }
        });
    }

    public static void getAssignments() {
        DatabaseReference reference = db.getReference(ASSIGNMENTS_URL);


        reference.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Assignment assignment = dataSnapshot.getValue(Assignment.class);
                Main.assignments.add(assignment);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }


            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("error");
            }
        });
    }

    public static void deleteStudentFromGroup(User user, StudentsGroup studentsGroup) {
        User userToRemove = null;
        for (int i = 0; i < studentsGroup.getUsers().size(); i++) {
            User userNext = studentsGroup.getUsers().get(i);
            if (user.getId() == userNext.getId()){
                userToRemove = userNext;
                break;
            }
        }
        if (userToRemove != null){
            studentsGroup.getUsers().remove(userToRemove);
            saveGroup(studentsGroup);
        }
    }

    public static void addStudentToGroup(User user, StudentsGroup studentsGroup) {
        if (user != null) {
            studentsGroup.getUsers().add(user);
            saveGroup(studentsGroup);
        }
    }
}


