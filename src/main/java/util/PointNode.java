package util;

import java.util.Objects;

import javafx.beans.property.SimpleDoubleProperty;

public class PointNode {

  private final SimpleDoubleProperty x;
  private final SimpleDoubleProperty y;

  public PointNode(double x, double y) {
    this.x = new SimpleDoubleProperty(x);
    this.y = new SimpleDoubleProperty(y);
  }

  public double getX() {
    return x.get();
  }

  public void setX(double x) {
    this.x.set(x);
  }

  public double getY() {
    return y.get();
  }

  public void setY(double y) {
    this.y.setValue(y);
  }

  @Override
  public String toString() {
    return "PointNode{" + "x=" + x + ", y=" + y + '}';
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 17 * hash + Objects.hashCode(this.x);
    hash = 17 * hash + Objects.hashCode(this.y);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PointNode other = (PointNode) obj;
    if (!Objects.equals(this.x, other.x)) {
      return false;
    }
    if (!Objects.equals(this.y, other.y)) {
      return false;
    }
    return true;
  }
}
