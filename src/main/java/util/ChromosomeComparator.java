package util;

import java.util.Comparator;

import model.Chromosome;

public class ChromosomeComparator implements Comparator<Chromosome> {

  @Override
  public int compare(Chromosome o1, Chromosome o2) {
    if (o1.getAdaptValue() < o2.getAdaptValue()) {
      return -1;
    } else if (o1.getAdaptValue() > o2.getAdaptValue()) {
      return 1;
    }
    return 0;
  }
}
