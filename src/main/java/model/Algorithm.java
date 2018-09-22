package model;

import constants.ModelConsatants;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Algorithm {

  private SimpleIntegerProperty number;
  private SimpleStringProperty gridView;
  private SimpleIntegerProperty pointCount;
  private SimpleStringProperty selectionMod;
  private SimpleStringProperty sexMod;
  private SimpleIntegerProperty generationNumber;
  private SimpleDoubleProperty probability;
  private SimpleDoubleProperty adaptValue;
  private SimpleDoubleProperty delta;
  private GeneticModel model;
  private SimpleStringProperty chromosomeBest;
  private Chromosome chromosome;


  public Algorithm(GeneticModel model, int number, Chromosome chromosome) {
    this.number = new SimpleIntegerProperty(number);
    this.gridView = new SimpleStringProperty(model.getGridView());
    this.pointCount = new SimpleIntegerProperty(model.getPointCount());
    this.selectionMod = new SimpleStringProperty(model.getSelectionMod());
    this.sexMod = new SimpleStringProperty(model.getSexMod());
    this.generationNumber = new SimpleIntegerProperty(model.getGenerationNumber());
    this.probability = new SimpleDoubleProperty(model.getProbability());
    this.adaptValue = new SimpleDoubleProperty(chromosome.getAdaptValue());
    this.delta = new SimpleDoubleProperty(countDelta());
    this.model = model;
    this.chromosomeBest = new SimpleStringProperty(chromosome.getChromosome());
    this.chromosome = chromosome;
  }

  public int getNumber() {
    return number.get();
  }

  public String getGridView() {
    return gridView.get();
  }

  public int getPointCount() {
    return pointCount.get();
  }

  public String getSelectionMod() {
    return selectionMod.get();
  }

  public String getSexMod() {
    return sexMod.get();
  }

  public int getGenerationNumber() {
    return generationNumber.get();
  }

  public Double getProbability() {
    return probability.get();
  }

  public Double getAdaptValue() {
    return adaptValue.get();
  }

  public Double getDelta() {
    return delta.get();
  }

  public GeneticModel getModel() {
    return model;
  }

  public String getChromosomeBest() {
    return chromosomeBest.get();
  }

  public Chromosome getChromosome() {
    return chromosome;
  }

  private double countDelta(){
    double deltaValue;
    deltaValue = Math.abs(this.getAdaptValue() - ModelConsatants.MIN_ADAPTATION_VALUE);
    return deltaValue;
  }

}
