package model;

import java.util.List;

import constants.ModelConstants;
import javafx.scene.Group;
import util.GridTools;

public class GeneticModel {

  private int pointCount;
  private String gridView;
  private GridTools gridTools;
  private String selectionMod;
  private Selection selection;
  private Mutation mutation;
  private String sexMod;
  private Sex sex;
  private int generationNumber;
  private double probability;
  private List<Chromosome> chromosomes;
  private Group gridGroup;
  private double adaptationValue;

  public GeneticModel() {}

  public GeneticModel(GridTools gridTools, Selection selection, Sex sex, Mutation mutation) {
    this.gridTools = gridTools;
    this.selection = selection;
    this.sex = sex;
    this.mutation = mutation;
  }

  public int getPointCount() {
    return pointCount;
  }

  public void setPointCount(int pointCount) {
    this.pointCount = pointCount;
  }

  public String getGridView() {
    return gridView;
  }

  public void setGridView(String gridView) {
    this.gridView = gridView;
    if (gridTools != null) {
      buidGrid();
    }
  }

  public String getSelectionMod() {
    return selectionMod;
  }

  public void setSelectionMod(String selectionMod) {
    this.selectionMod = selectionMod;
  }

  public Selection doSelection() {
    buidSelection();
    return selection;
  }

  public void setSelection(Selection selection) {
    this.selection = selection;
  }

  public Sex doSex() {
    buildSex();
    return sex;
  }

  public Sex doAlgorithmSex() {
    buildAlgorithmSex();
    return sex;
  }

  public int getGenerationNumber() {
    return generationNumber;
  }

  public void setGenerationNumber(int generationNumber) {
    this.generationNumber = generationNumber;
  }

  public List<Chromosome> getChromosomes() {
    return chromosomes;
  }

  public void setChromosomes(List<Chromosome> chromosomes) {
    this.chromosomes = chromosomes;
  }

  public double getProbability() {
    return probability;
  }

  public void setProbability(double probability) {
    this.probability = probability;
    mutation.setProbability(probability);
  }

  public GridTools getGridTools() {
    return gridTools;
  }

  public void setGridTools(GridTools gridTools) {
    this.gridTools = gridTools;
  }

  public Mutation doMutation() {
    mutation.setProbability(probability);
    mutation.doMutation(chromosomes);
    return mutation;
  }

  public Mutation doMutationWithAlgorithm() {
    mutation.setProbability(probability);
    mutation.doMutationAlgorithm(chromosomes);
    return mutation;
  }

  public void setMutation(Mutation mutation) {
    this.mutation = mutation;
  }

  public String getSexMod() {
    return sexMod;
  }

  public void setSexMod(String sexMod) {
    this.sexMod = sexMod;
  }

  public Group getGridGroup() {
    return gridGroup;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public double getAdaptationValue() {
    return adaptationValue;
  }

  public void setAdaptationValue(double adaptationValue) {
    this.adaptationValue = adaptationValue;
  }

  public void buidGrid() {

    try {
      switch (gridView) {
        case ModelConstants.GRID_VIEW_RECTANGLE:
          gridGroup = gridTools.getRectangleGrid(pointCount);
          gridTools.createAxis();
          break;
        case ModelConstants.GRID_VIEW_BOX:
          gridGroup = gridTools.getBoxGrid(pointCount);
          gridTools.createAxis();
          break;
      }
      gridTools.createAxis();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  private void buidSelection() {
    try {
      switch (selectionMod) {
        case ModelConstants.SELECTION_MODE_RULETTE:
          selection.createRulletSelection(pointCount, chromosomes);
          break;
        case ModelConstants.SELECTION_MODE_RANG:
          selection.createRangSelection(pointCount, chromosomes);
          break;
      }
    } catch (Exception e) {
      System.err.print(e);
    }
  }

  private void buildSex() {
    try {
      switch (sexMod) {
        case ModelConstants.SEX_MODE_BETTERWORSE:
          sex.findBetterWorsePair(chromosomes);
          break;
        case ModelConstants.SEX_MODE_MINDIST:
          sex.findMinDistancePair(chromosomes);
          break;
      }
    } catch (Exception e) {
      System.err.print(e);
    }
  }

  private void buildAlgorithmSex() {
    try {
      switch (sexMod) {
        case ModelConstants.SEX_MODE_BETTERWORSE:
          sex.findBetterWorsePairAlgorithm(chromosomes);
          break;
        case ModelConstants.SEX_MODE_MINDIST:
          sex.findMinDistancePairAlgorithm(chromosomes);
          break;
      }
    } catch (Exception e) {
      System.err.print(e);
    }
  }

  @Override
  public String toString() {
    return "GeneticModel{"
        + "pointCount="
        + pointCount
        + ", gridView="
        + gridView
        + ", selectionMod="
        + selectionMod
        + ", sexMod="
        + sexMod
        + ", generationNumber="
        + generationNumber
        + ", probability="
        + probability
        + ", adaptValue="
        + adaptationValue
        + '}';
  }
}
