package sample;

import java.util.List;

public class StudentsGroup {
    private static int count;
    private String name;
    private int id;
    private List<User> users;

    public StudentsGroup() {
        count++;
    }

    public StudentsGroup(String name, List<User> users) {
        count++;
        this.name = name;
        this.id = count;
        this.users = users;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        StudentsGroup.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "StudentsGroup{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", users=" + users +
                '}';
    }
}
