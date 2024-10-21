package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CalendarTask {
    private final Text text;
    private final Rectangle rectangle;

    public CalendarTask(Text text, Rectangle rectangle) {
        this.text = text;
        this.rectangle = rectangle;
    }

    public void setContent(String content, Integer index){
        text.setText(content);
        text.setFill(Color.WHITE);
        if (index == 0) {
            rectangle.setFill(Color.RED);
        } else if (index == 1) {
            rectangle.setFill(Color.LIGHTBLUE);
        } else if (index == 2) {
            rectangle.setFill(Color.LIGHTCORAL);
        } else {
            rectangle.setFill(Color.GREEN);
        }
    }
}



