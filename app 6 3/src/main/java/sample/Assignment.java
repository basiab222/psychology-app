package sample;

import java.time.LocalDate;
import java.util.List;

public class Assignment {
    private static int id;
    private Long studentDeadline;
    private Long teacherDeadline;
    private int numberOfRows;
    private int numberOfColumns;
    private boolean isChecked;
    private boolean isHandedIn;
    private String category;
    private List<Integer> studentsIds;
    private String text;
    private User editedByUser = null;
    private String comment = null;

    public Assignment() {
    }

    public Assignment(LocalDate studentDeadline, int numberOfRows, int numberOfColumns,
                      boolean isChecked, boolean isHandedIn, String category, List<Integer> studentsIds,
                      String text) {
        Assignment.id++;
        this.studentDeadline = studentDeadline.toEpochDay();
        this.teacherDeadline = studentDeadline.plusWeeks(2).toEpochDay();
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.isChecked = isChecked;
        this.isHandedIn = isHandedIn;
        this.category = category;
        this.studentsIds = studentsIds;
        this.text = text;
    }

    public User getEditedByUser() {
        return editedByUser;
    }

    public void setEditedByUser(User editedByUser) {
        this.editedByUser = editedByUser;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        Assignment.id = id;
    }

    public Long getStudentDeadline() {
        return studentDeadline;
    }

    public void setStudentDeadline(Long studentDeadline) {
        this.studentDeadline = studentDeadline;
    }

    public Long getTeacherDeadline() {
        return teacherDeadline;
    }

    public void setTeacherDeadline(Long teacherDeadline) {
        this.teacherDeadline = teacherDeadline;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isHandedIn() {
        return isHandedIn;
    }

    public void setHandedIn(boolean handedIn) {
        isHandedIn = handedIn;
    }

    public List<Integer> getStudentsIds() {
        return studentsIds;
    }

    public void setStudentsIds(List<Integer> studentsIds) {
        this.studentsIds = studentsIds;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
