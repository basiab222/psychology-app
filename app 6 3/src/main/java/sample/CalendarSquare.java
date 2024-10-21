package sample;

import java.util.List;

public class CalendarSquare {
    private final List<CalendarTask> tasks;

    public CalendarSquare(List<CalendarTask> tasks) {
        this.tasks = tasks;
    }

    public List<CalendarTask> getTasks() {
        return tasks;
    }
}
