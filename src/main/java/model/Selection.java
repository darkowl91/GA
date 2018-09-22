package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import util.ChromosomeComparator;

public class Selection {

  public void createRangSelection(int startCount, List<Chromosome> candidates) {
    int resultCount = (int) Math.ceil((double) startCount * 0.28);
    if (resultCount < 2) {
      resultCount = 2;
    }
    Collections.sort(candidates, new ChromosomeComparator());
    List<Chromosome> resultSet = new ArrayList<>();
    for (int i = 0; i < candidates.size(); i++) {
      for (int j = 0; j < candidates.size() - i; j++) {
        resultSet.add(candidates.get(i));
      }
    }
    Collections.shuffle(resultSet);
    Random rnd = new Random();
    candidates.clear();
    int index;
    Chromosome forAdd;
    for (int i = 0; i < resultCount; i++) {
      int repeat = 0;
      do {
        index = rnd.nextInt(resultSet.size());
        forAdd = resultSet.get(index);
        repeat++;
      } while (candidates.contains(forAdd) && repeat < 10);
      candidates.add(forAdd);
    }
  }

  public void createRulletSelection(int startCount, List<Chromosome> candidates) {
    List<Chromosome> chromosomes = new ArrayList<>();
    int resultCount = (int) Math.ceil((double) startCount * 0.28);
    if (resultCount < 2) {
      resultCount = 2;
    }
    double sum = 0;
    for (Chromosome ch : candidates) {
      sum += Math.abs(ch.getAdaptValue());
    }
    ArrayList<Double> modifiedList = new ArrayList<>();
    for (Chromosome ch : candidates) {
      modifiedList.add(sum - ch.getAdaptValue());
    }
    double modifiedSum = 0;
    for (double d : modifiedList) {
      modifiedSum += d;
    }

    ArrayList<Double> percent = new ArrayList<>();
    for (double d : modifiedList) {
      percent.add((d / modifiedSum));
    }

    int index;
    double value;
    Chromosome c;
    ArrayList<Double> difference = new ArrayList<>();
    for (int i = 0; i < resultCount; i++) {
      int repeat = 0;
      do {
        repeat++;
        difference.clear();
        value = Math.random();
        for (double d : percent) {
          difference.add(Math.abs(value - d));
        }
        double minDif = Collections.min(difference);
        index = difference.indexOf(minDif);
        c = candidates.get(index);
        // TODO: Fix
      } while (chromosomes.contains(c) && repeat < 10);
      chromosomes.add(c);
    }
    // TODO: Fix
    candidates.clear();
    candidates.addAll(chromosomes);
  }
}
