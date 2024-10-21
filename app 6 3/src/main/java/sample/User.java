package sample;

import java.util.Objects;

public class User {
    private static int count;
    private int id;
    private String username;
    private String password;
    private String name;
    private boolean isTeacher;

    public User() {
        count++;
    }

    public User(String username, String password, String name, boolean isTeacher) {
        count++;
        this.id = count;
        this.username = username;
        this.password = password;
        this.name = name;
        this.isTeacher = isTeacher;
    }

    public User(Integer id, String username, String password, String name, boolean isTeacher) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.isTeacher = isTeacher;
    }

    public static User getUserByUsername(String username) {
        for (int i = 0; i < Main.users.size(); i++) {
            if (username.equals(Main.users.get(i).username)) {
                return Main.users.get(i);
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public static boolean isTeacher(String username) {
        for (int i = 0; i < Main.users.size(); i++) {
            if (username.equals(Main.users.get(i).username) && Main.users.get(i).isTeacher()) {
                return true;
            }
        }
        return false;
    }

    public static boolean loginIsValid (String username, String password){
        for (int i = 0; i < Main.users.size(); i++) {
            if (username.equals(Main.users.get(i).username) && password.equals(Main.users.get(i).password)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", isTeacher=" + isTeacher +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
