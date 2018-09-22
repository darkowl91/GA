package util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.Algorithm;
import model.Chromosome;
import model.GeneticModel;

public class BestWorkFinder {


  /**
   * Find best chromosome in last generation for i tick
   *
   * @param model current model
   * @param tick current tick count
   * @return Algorithm object with bestAdaptFunction
   */
  public static Algorithm findBestWorkInLastGen(GeneticModel model, int tick) {
    List<Chromosome> chromosomes = model.getChromosomes();
    Collections.sort(chromosomes, new ChromosomeComparator());
    Algorithm algorithm = new Algorithm(model, tick, chromosomes.get(0));
    return algorithm;
  }

  /**
   * find best Algorithm by adaptable function
   *
   * @param algorithms list of algorithm objects
   * @return best Algorithm
   */
  public static Algorithm findBestAlgorithm(List<Algorithm> algorithms) {
    Collections.sort(algorithms, new AlgoritthmComparator());
    return algorithms.get(0);
  }

  public static class AlgoritthmComparator implements Comparator<Algorithm> {

    @Override
    public int compare(Algorithm o1, Algorithm o2) {
      if (o1.getDelta() < o2.getDelta()) {
        return -1;
      } else if (o1.getDelta() > o2.getDelta()) {
        return 1;
      }
      return 0;
    }
  }
}
