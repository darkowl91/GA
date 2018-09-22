package util;

import javax.swing.*;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public final class CircleCreator {

  private CircleCreator() {}

  public static Circle createCircle(double x, double y, int radius, Color color) {
    final Circle circle =
        new Circle(
            radius,
            new RadialGradient(
                0,
                0,
                0.2,
                0.3,
                1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop[] {new Stop(0, Color.rgb(250, 250, 255)), new Stop(1, color)}));

    circle.setEffect(new InnerShadow(7, color.darker().darker()));
    circle.setCenterX(x);
    circle.setCenterY(y);
    DropShadow ds = new DropShadow(3, 3, 3, color.darker().darker());
    circle.setEffect(ds);
    // change a cursor when it is over circle
    circle.setCursor(Cursor.HAND);
    circle.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            JOptionPane.showMessageDialog(
                null,
                "X" + circle.getCenterX() + "Y" + circle.getCenterY(),
                "Push",
                JOptionPane.ERROR_MESSAGE);
          }
        });
    return circle;
  }
}
