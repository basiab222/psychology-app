package sample;

public class RowModel {
    private String value0 = "";
    private String value1 = "";
    private String value2 = "";
    private String value3 = "";
    private String value4 = "";
    private String comment = "";


    public String getValue0() {
        return value0;
    }

    public void setValue0(String value0) {
        this.value0 = value0;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public void setValue(int i, String value) {
        switch (i) {
            case 1:
                value1 = value;
                break;
            case 2:
                value2 = value;
                break;
            case 3:
                value3 = value;
                break;
            case 4:
                value4 = value;
                break;
            case 0:
                value0 = value;
                break;
        }
    }

    public String getValue(int i) {
        switch (i) {
            case 1:
                return value1;
            case 2:
                return value2;
            case 3:
                return value3;
            case 4:
                return value4;
            case 0:
                return value0;
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
