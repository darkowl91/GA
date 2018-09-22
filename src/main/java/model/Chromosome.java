package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import util.PointNode;

public class Chromosome {

  private final SimpleIntegerProperty generationNumber;
  private final SimpleStringProperty chromosome;
  private final SimpleDoubleProperty x1;
  private final SimpleDoubleProperty x2;
  private final SimpleStringProperty id;
  private final SimpleDoubleProperty adaptValue;

  public Chromosome(String chromosome, double adaptValue, int generationNumber) {
    this.chromosome = new SimpleStringProperty(chromosome);
    this.adaptValue = new SimpleDoubleProperty(adaptValue);
    this.generationNumber = new SimpleIntegerProperty(generationNumber);
    this.id = new SimpleStringProperty();
    this.x1 = new SimpleDoubleProperty();
    this.x2 = new SimpleDoubleProperty();
  }

  public Chromosome(PointNode point, double adaptValue) {
    this.generationNumber = new SimpleIntegerProperty();
    this.chromosome = new SimpleStringProperty();
    this.id = new SimpleStringProperty();
    this.adaptValue = new SimpleDoubleProperty(adaptValue);
    this.x1 = new SimpleDoubleProperty(point.getX());
    this.x2 = new SimpleDoubleProperty(point.getY());
  }

  public Chromosome(String x1, String x2) {
    this.generationNumber = new SimpleIntegerProperty();
    this.chromosome = new SimpleStringProperty(x1 + x2);
    this.id = new SimpleStringProperty();
    this.adaptValue = new SimpleDoubleProperty();
    this.x1 = new SimpleDoubleProperty();
    this.x2 = new SimpleDoubleProperty();
  }

  public Chromosome(String chromosome) {
    this.generationNumber = new SimpleIntegerProperty();
    this.adaptValue = new SimpleDoubleProperty();
    this.chromosome = new SimpleStringProperty(chromosome);
    this.id = new SimpleStringProperty();
    this.x1 = new SimpleDoubleProperty();
    this.x2 = new SimpleDoubleProperty();
  }

  public String getId() {
    return id.get();
  }

  public void setId(String id) {
    this.id.set(id);
  }

  public Integer getGenerationNumber() {
    return generationNumber.get();
  }

  public void setGenerationNumber(int i) {
    this.generationNumber.set(i);
  }

  public String getChromosomeX1() {
    if (!chromosome.get().isEmpty()) {
      return separatedChromosome()[0];
    }
    return null;
  }

  public String getChromosomeX2() {
    if (!chromosome.get().isEmpty()) {
      return separatedChromosome()[1];
    }
    return null;
  }

  public Double getX1() {
    return x1.get();
  }

  public Double getX2() {
    return x2.get();
  }

  public PointNode getPoint() {
    return new PointNode(x1.get(), x2.get());
  }

  public void setPoint(PointNode point) {
    this.x1.set(point.getX());
    this.x2.set(point.getY());
  }

  public double getAdaptValue() {
    return adaptValue.get();
  }

  public void setAdaptValue(double adaptValue) {
    this.adaptValue.setValue(adaptValue);
  }

  public void setChromosome(String s) {
    this.chromosome.set(s);
  }

  public String getChromosome() {
    return chromosome.get();
  }

  private String[] separatedChromosome() {
    String[] ch = new String[2];
    int length = chromosome.get().length() / 2;
    ch[0] = (chromosome.get().substring(0, length));
    ch[1] = (chromosome.get().substring(length, chromosome.get().length()));
    return ch;
  }

  @Override
  public String toString() {
    return "Chromosome{" + "generationNumber=" + generationNumber.get()
    + ", chromosome=" + chromosome.get() + ", adaptValue=" + adaptValue.get() + '}';
  }
}
