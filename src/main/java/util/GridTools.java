package util;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import constants.ModelConsatants;

public class GridTools {

  private Color color;
  private double lineWidth;
  private PointNode[][] rectNodesArray;
  private List<PointNode> rectNodes;
  private List<PointNode> boxNodes;
  private double h;
  private double w;
  private Group groupGrid;


  public GridTools(Color color, double lineWidth, double w, double h) {
    this.rectNodes = new ArrayList<>();
    this.boxNodes = new ArrayList<>(5);
    this.color = color;
    this.lineWidth = lineWidth;
    this.h = h;
    this.w = w;
  }

  private Path pathCreate() {
    Path path = new Path();
    path.setFill(color);
    path.setStrokeWidth(lineWidth);
    return path;
  }

  public Group getRectangleGrid(int pointNumber) {
    Path path = pathCreate();
    Group gridGroup = new Group(path);
    rectNodesArray = generateRectGrid(pointNumber);
    for (int i = 0; i < rectNodesArray.length; i++) {
      for (int j = 0; j < rectNodesArray[i].length; j++) {
        double x = i * h;
        double y = j * w;
        //move to start point
        MoveTo moveToh = new MoveTo(h, y);
        path.getElements().add(moveToh);
        //draw line
        LineTo lineToh = new LineTo(x, y);
        path.getElements().add(lineToh);
        //move to start point
        MoveTo moveTov = new MoveTo(x, w);
        path.getElements().add(moveTov);
        //drawline
        LineTo lineTov = new LineTo(x, y);
        path.getElements().add(lineTov);
        PointNode pn = new PointNode(x, y);
        rectNodesArray[i][j] = pn;
        rectNodes.add(pn);
      }
    }
    this.groupGrid = gridGroup;
    return gridGroup;
  }

  public Group getBoxGrid(int pointNumber) {
    Path path = pathCreate();
    Group gridGroup = getRectangleGrid(4);
    boxNodes.addAll(rectNodes);
    gridGroup.getChildren().add(path);
    for (int i = 0; i < 1; i++) {
      for (int j = 0; j < 1; j++) {
        //--------\-------
        PointNode p1Left = new PointNode(rectNodesArray[i][j].getX(), rectNodesArray[i][j].getY());
        MoveTo moveToh = new MoveTo(p1Left.getX(), p1Left.getY());
        PointNode p2Left = new PointNode(rectNodesArray[i + 1][j + 1].getX(), rectNodesArray[i + 1][j + 1].getY());
        LineTo righBottomCorner = new LineTo(p2Left.getX(), p2Left.getY());

        PointNode middlePoint1Left = getMiddlePoint(p1Left, p2Left);
        PointNode middlePoint2Left = getMiddlePoint(p1Left, middlePoint1Left);
        PointNode middlePoint3Left = getMiddlePoint(middlePoint1Left, p2Left);
        boxNodes.add(middlePoint2Left);
        boxNodes.add(middlePoint3Left);
        //------/--------
        PointNode p1Right = new PointNode(rectNodesArray[i][j + 1].getX(), rectNodesArray[i][j + 1].getY());
        MoveTo moveToW = new MoveTo(p1Right.getX(), p1Right.getY());
        PointNode p2Right = new PointNode(rectNodesArray[i + 1][j].getX(), rectNodesArray[i + 1][j].getY());
        LineTo rightTopCorner = new LineTo(p2Right.getX(), p2Right.getY());

        PointNode middlePoint1Right = getMiddlePoint(p1Right, p2Right);
        PointNode middlePoint2Right = getMiddlePoint(p1Right, middlePoint1Right);
        PointNode middlePoint3Right = getMiddlePoint(middlePoint1Right, p2Right);
        boxNodes.add(middlePoint1Right);
        boxNodes.add(middlePoint2Right);
        boxNodes.add(middlePoint3Right);
        //aditional points to large grid
        if (pointNumber > 9) {
          //--------__.__----------
          boxNodes.add(getMiddlePoint(p1Left, p1Right));
          boxNodes.add(getMiddlePoint(p2Left, p1Right));
          boxNodes.add(getMiddlePoint(p2Left, p2Right));
          boxNodes.add(getMiddlePoint(p1Left, p2Right));
        }
        path.getElements().addAll(moveToh, righBottomCorner, moveToW, rightTopCorner);
      }
    }
    this.groupGrid = gridGroup;
    return gridGroup;
  }

  private PointNode getMiddlePoint(PointNode p1, PointNode p2) {
    double x = (p1.getX() + p2.getX()) / 2;
    double y = (p1.getY() + p2.getY()) / 2;
    return new PointNode(x, y);
  }


   //TODO: fix for more or les points then 7..11
  private PointNode[][] generateRectGrid(int pointNumber) {
    //check size
    // for 7..9 get grid3x3
    if (pointNumber >= ModelConsatants.minPointNumber
    && pointNumber <= 9) {
      return new PointNode[3][3];
    } //for 9..11 get grid4x4
    else if (pointNumber > 9
    && pointNumber <= ModelConsatants.maxPointNumber) {
      return new PointNode[4][4];
    }
    return new PointNode[2][2];
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Axis static">
  public Group createAxis() throws Exception {
    Path path = pathCreate();
    path.setStrokeWidth(0.5);
    path.setStroke(Color.ORANGE);
    //delta
    // use for aditional legth to axis end or begin
    double delta = 5;
    double deltaY = 10;
    //first
    double x1 = getRectNodeses()[0][0].getX();
    double y1 = getRectNodeses()[0][0].getY();
    //nodes count
    int rectCountX = getRectNodeses().length;
    int rectCountY = getRectNodeses()[0].length;
    //last
    double x2 = getRectNodeses()[0][rectCountX - 1].getX();
    double y2 = getRectNodeses()[0][rectCountY - 1].getY();
    //coordinats real
    double realX1 = x1 + (rectCountX - 1) * w / 3;
    double realY1 = y1 - h / 3;
    double realX2 = x2 + (rectCountX - 1) * w / 3;
    double realY2 = y2 - (rectCountY - 1) * h / 3;

    //move to begin axis (top)
    MoveTo moveToY = new MoveTo(realX1, realY1);
    Text textbeginY = new Text(moveToY.getX() + delta, moveToY.getY() +15*delta, "20");
    //draw y axis
    LineTo lineToY = new LineTo(realX1, h * rectCountY - h / 3);
    Text textendY = new Text(lineToY.getX(), lineToY.getY()  -30*delta, "-10");
    //move to center coordinats
    MoveTo moveToX = new MoveTo(realX2, realY2);
    Text textZERO = new Text(moveToX.getX() + delta , moveToX.getY() - delta, "0");
    //draw x axis
    LineTo lineToX = new LineTo(w * rectCountX - w / 3, realY2);
    Text textbeginX = new Text(lineToX.getX() - 30*delta, lineToX.getY(), "20");
    LineTo lineToXInvert = new LineTo(x2 - w / 3 - delta, realY2);
    Text textendX = new Text(lineToXInvert.getX() + 10*delta, lineToXInvert.getY()  , "-10");

    path.getElements().addAll(moveToY, lineToY, moveToX, lineToX, moveToX, lineToXInvert);
    Group group = new Group(path, textbeginX, textbeginY, textZERO, textendX, textendY);
    groupGrid.getChildren().add(group);
    return group;
  }

  public PointNode[][] getRectNodeses() throws Exception {
    if (rectNodesArray == null) {
      throw new Exception("Grid not created!");
    } else {
      return rectNodesArray;
    }
  }

  public List<PointNode> getRectNodes() {
    return rectNodes;
  }

  public List<PointNode> getBoxNodes() throws Exception {
    if (boxNodes == null) {
      throw new Exception("Grid not created");
    } else {
      return boxNodes;
    }
  }

  public PointNode converToX1X2(PointNode pointNode) {
    double deltaX = (pointNode.getX() * ModelConsatants.GRID_DELTA) / (w * (rectNodesArray.length - 1));
    double x1 = ModelConsatants.GRID_BEGIN + deltaX;
    double deltaY = (pointNode.getY() * ModelConsatants.GRID_DELTA) / (h * (rectNodesArray.length - 1));
    double x2 = ModelConsatants.GRID_END - deltaY;
    return new PointNode(x1, x2);
  }

  public PointNode convertToXY(PointNode pointNode) {
    double X = ((pointNode.getX() + 10) * w * (rectNodesArray.length - 1)) / ModelConsatants.GRID_DELTA;
    double Y = ((ModelConsatants.GRID_END - pointNode.getY()) * h * (rectNodesArray.length - 1)) / ModelConsatants.GRID_DELTA;
    return new PointNode(X, Y);
  }

  public Group getGroupGrid() {
    return groupGrid;
  }
}
