package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import constants.ModelConstants;
import util.ChromosomeComparator;
import util.ChromosomeConverter;
import util.PointNode;

public final class FunctionAdaptation {

  private double adaptationValue(double x1, double x2) {
    double a = 1 + Math.pow(x1 - 2, 2) + Math.pow(x2 - 10, 2);
    double b = 2 + Math.pow(x1 - 10, 2) + Math.pow(x2 - 15, 2);
    double c = 2 + Math.pow(x1 - 18, 2) + Math.pow(x2 - 4, 2 * x1);
    double result = (-1) * (1 / a + 1 / b + 1 / c);
    if (Double.isNaN(result)) {
      result = Double.MAX_VALUE;
    }
    return result;
  }

  public void countAdaptationInitial(GeneticModel model) throws Exception {
    List<Chromosome> chromosomesAdapted = new ArrayList<>();
    List<PointNode> rectNodes = null;
    switch (model.getGridView()) {
      case ModelConstants.GRID_VIEW_RECTANGLE:
        rectNodes = model.getGridTools().getRectNodes();
        break;
      case ModelConstants.GRID_VIEW_BOX:
        rectNodes = model.getGridTools().getBoxNodes();
        break;
    }
    for (PointNode pointNode : rectNodes) {
      PointNode point = model.getGridTools().converToX1X2(pointNode);
      Chromosome c = new Chromosome(point, adaptationValue(point.getX(), point.getY()));
      c.setGenerationNumber(1);
      c.setChromosome(ChromosomeConverter.numberToChromosome(point.getX(), point.getY()));
      chromosomesAdapted.add(c);
    }
    int chromosomeCount = chromosomesAdapted.size();
    for (int i = 0; i < chromosomeCount - model.getPointCount(); i++) {
      Chromosome max = Collections.max(chromosomesAdapted, new ChromosomeComparator());
      chromosomesAdapted.remove(max);
    }
    model.setChromosomes(chromosomesAdapted);
  }

  public void countAdaptation(GeneticModel model, int generationNumber) throws Exception {

    for (Chromosome c : model.getChromosomes()) {
      c.setGenerationNumber(generationNumber);
      double[] result = ChromosomeConverter.chromosomeToNumber(c);
      PointNode pn = new PointNode(result[0], result[1]);
      double adaptationValue = adaptationValue(pn.getX(), pn.getY());
      c.setAdaptValue(adaptationValue);
      c.setPoint(pn);
      if (Double.isNaN(c.getAdaptValue())) {
        c.setAdaptValue(Double.MAX_VALUE);
      }
    }
  }
}
