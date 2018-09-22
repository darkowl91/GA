package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import util.ChromosomeComparator;
import util.ChromosomeConverter;

public final class Sex {

  private String[] doSex(String chromosome1, String chromosome2) {
    String[] children = new String[2];
    Random rnd = new Random();
    int length = chromosome1.length();
    int a = rnd.nextInt(length - 1);
    int b = rnd.nextInt(length - a);
    b += a;
    StringBuilder child1 = new StringBuilder();
    StringBuilder child2 = new StringBuilder();
    child1.append(chromosome1.subSequence(0, a + 1));
    child2.append(chromosome2.subSequence(0, a + 1));
    child1.append(chromosome2.subSequence(a + 1, b + 1));
    child2.append(chromosome1.subSequence(a + 1, b + 1));
    child1.append(chromosome1.subSequence(b + 1, length));
    child2.append(chromosome2.subSequence(b + 1, length));
    children[0] = new String(child1);
    children[1] = new String(child2);
    return children;
  }

  public void findMinDistancePair(List<Chromosome> candidates) {
    ArrayList<Chromosome> chromosomes = new ArrayList<>();
    double[][] distance = new double[candidates.size()][candidates.size()];
    for (int i = 0; i < distance.length; i++) {
      for (int j = 0; j < distance[0].length; j++) {
        distance[i][j] = 1000;
      }
    }
    double x, y;
    double min = 100;
    int parent1 = 0, parent2 = 0;

    for (int i = 0; i < candidates.size(); i++) {
      for (int j = i; j < candidates.size(); j++) {
        if (i != j) {
          x = candidates.get(i).getPoint().getX() - candidates.get(j).getPoint().getX();
          y = candidates.get(i).getPoint().getY() - candidates.get(j).getPoint().getY();
          distance[i][j] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        }

      }
    }
    int number = 0;

    while (number < candidates.size()) {
      for (int i = 0; i < distance.length; i++) {
        for (int j = 0; j < distance[0].length - 1; j++) {
          if (distance[i][j + 1] != 1000 && distance[i][j + 1] <= distance[i][j]) {
            min = distance[i][j + 1];
            parent1 = i;
            parent2 = j + 1;
          }
        }
      }
      distance[parent1][parent2] = Double.MAX_VALUE;

      String[] zigota;
      Chromosome result;
      zigota = doSex(candidates.get(parent1).getChromosome(), candidates.get(parent2).getChromosome());
      for (String chromosome : zigota) {

        if (ChromosomeConverter.checkPoint(chromosome)) {
          result = new Chromosome(chromosome);
          chromosomes.add(result);
        }

      }
      number++;
    }
    candidates.clear();
    candidates.addAll(chromosomes);
  }

  public void findBetterWorsePair(List<Chromosome> candidates) {

    ArrayList<Chromosome> candidatesList = new ArrayList<>(candidates);
    Collections.sort(candidatesList, new ChromosomeComparator());
    Chromosome alphaSamec = candidatesList.get(0);
    String[] zigota;
    ArrayList<Chromosome> chromosomes = new ArrayList<>();
    Chromosome result;
    double[] value;
    for (int i = 1; i < candidatesList.size(); i++) {
      do {
        zigota = doSex(alphaSamec.getChromosome(), candidatesList.get(i).getChromosome());
      } while (!ChromosomeConverter.checkPoint(zigota[0]) && !ChromosomeConverter.checkPoint(zigota[1]));
      for (String chromosome : zigota) {
        if (ChromosomeConverter.checkPoint(chromosome)) {
          result = new Chromosome(chromosome);
          chromosomes.add(result);
        }
      }
    }
    candidates.clear();
    candidates.addAll(chromosomes);
  }

  public void findBetterWorsePairAlgorithm(List<Chromosome> candidates) {
    Random rnd = new Random();
    ArrayList<Chromosome> candidatesList = new ArrayList<>(candidates);
    Collections.sort(candidatesList, new ChromosomeComparator());
    Chromosome alphaSamec = candidatesList.get(0);
    String[] zigota;
    String str_pointCount, str_generatioNumber, str_probability;
    String prefixPointCount, prefixGenNumber, prefixProbability;
    ArrayList<Chromosome> chromosomes = new ArrayList<>();
    Chromosome result;
    double[] value;
    for (int i = 1; i < candidatesList.size(); i++) {

      zigota = doSex(alphaSamec.getChromosome(), candidatesList.get(i).getChromosome());

      for (String chromosome : zigota) {

        int pointCount = Integer.parseInt(chromosome.substring(0, 5), 2);
        int genNumber = Integer.parseInt(chromosome.substring(8, 14), 2);
        int d_probability = Integer.parseInt(chromosome.substring(14, 21), 2);
        String gridView = chromosome.substring(5, 6);
        String selection = chromosome.substring(6, 7);
        String sex = chromosome.substring(7, 8);
        if (pointCount < 7 || pointCount > 11) {
          pointCount = rnd.nextInt(5) + 7;
          str_pointCount = Integer.toBinaryString(pointCount);
          prefixPointCount = ChromosomeConverter.getPrefix(str_pointCount, 5);
        } else {
          prefixPointCount = "";
          str_pointCount = chromosome.substring(0, 5);
        }
        if (genNumber == 0) {
          genNumber = rnd.nextInt(54) + 10;
          str_generatioNumber = Integer.toBinaryString(genNumber);
          prefixGenNumber = ChromosomeConverter.getPrefix(str_generatioNumber, 6);
        } else {
          prefixGenNumber = "";
          str_generatioNumber = chromosome.substring(8, 14);
        }
        if (d_probability > 90 || d_probability < 10) {
          d_probability = rnd.nextInt(81) + 10;
          str_probability = Integer.toBinaryString(d_probability);
          prefixProbability = ChromosomeConverter.getPrefix(str_probability, 7);
        } else {
          prefixProbability = "";
          str_probability = chromosome.substring(14, 21);
        }

        chromosome = prefixPointCount + str_pointCount + gridView + selection + sex + prefixGenNumber + str_generatioNumber + prefixProbability + str_probability;

        result = new Chromosome(chromosome);
        chromosomes.add(result);

      }
    }
    candidates.clear();
    candidates.addAll(chromosomes);
  }

  public void findMinDistancePairAlgorithm(List<Chromosome> candidates) {
    Random rnd = new Random();
    String str_pointCount, str_generatioNumber, str_probability;
    String prefixPointCount, prefixGenNumber, prefixProbability;
    ArrayList<Chromosome> chromosomes = new ArrayList<>();
    double[][] distance = new double[candidates.size()][candidates.size()];
    for (int i = 0; i < distance.length; i++) {
      for (int j = 0; j < distance[0].length; j++) {
        distance[i][j] = 1000;
      }
    }
    double x, y;
    double min = 100;
    int parent1 = 0, parent2 = 0;

    for (int i = 0; i < candidates.size(); i++) {
      for (int j = i; j < candidates.size(); j++) {
        if (i != j) {
          x = candidates.get(i).getPoint().getX() - candidates.get(j).getPoint().getX();
          y = candidates.get(i).getPoint().getY() - candidates.get(j).getPoint().getY();
          distance[i][j] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        }

      }
    }
    int number = 0;

    while (number < candidates.size()) {
      for (int i = 0; i < distance.length; i++) {
        for (int j = 0; j < distance[0].length - 1; j++) {
          if (distance[i][j + 1] != 1000 && distance[i][j + 1] <= distance[i][j]) {
            min = distance[i][j + 1];
            parent1 = i;
            parent2 = j + 1;
          }
        }
      }
      distance[parent1][parent2] = Double.MAX_VALUE;

      String[] zigota;
      Chromosome result;
      zigota = doSex(candidates.get(parent1).getChromosome(), candidates.get(parent2).getChromosome());
      for (String chromosome : zigota) {
        int pointCount = Integer.parseInt(chromosome.substring(0, 5), 2);
        int genNumber = Integer.parseInt(chromosome.substring(8, 14), 2);
        int d_probability = Integer.parseInt(chromosome.substring(14, 21), 2);
        String gridView = chromosome.substring(5, 6);
        String selection = chromosome.substring(6, 7);
        String sex = chromosome.substring(7, 8);
        if (pointCount < 7 || pointCount > 11) {
          pointCount = rnd.nextInt(5) + 7;
          str_pointCount = Integer.toBinaryString(pointCount);
          prefixPointCount = ChromosomeConverter.getPrefix(str_pointCount, 5);
        } else {
          prefixPointCount = "";
          str_pointCount = chromosome.substring(0, 5);
        }
        if (genNumber == 0) {
          genNumber = rnd.nextInt(54) + 10;
          str_generatioNumber = Integer.toBinaryString(genNumber);
          prefixGenNumber = ChromosomeConverter.getPrefix(str_generatioNumber, 6);
        } else {
          prefixGenNumber = "";
          str_generatioNumber = chromosome.substring(8, 14);
        }
        if (d_probability > 90 || d_probability < 10) {
          d_probability = rnd.nextInt(81) + 10;
          str_probability = Integer.toBinaryString(d_probability);
          prefixProbability = ChromosomeConverter.getPrefix(str_probability, 7);
        } else {
          prefixProbability = "";
          str_probability = chromosome.substring(14, 21);
        }

        chromosome = prefixPointCount + str_pointCount + gridView + selection + sex + prefixGenNumber + str_generatioNumber + prefixProbability + str_probability;


        result = new Chromosome(chromosome);
        chromosomes.add(result);


      }
      number++;
    }
    candidates.clear();
    candidates.addAll(chromosomes);
  }
}
