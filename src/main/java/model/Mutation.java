package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import util.ChromosomeConverter;

public final class Mutation {

  private double probability;

  public Mutation() {
  }

  public Mutation(double probability) {
    this.probability = probability;
  }

  public double getProbability() {
    return probability;
  }

  public void setProbability(double probability) {
    this.probability = probability;
  }

  private String mutate(String chromosome) {
    int count_of_mutations = 0;
    StringBuilder chromosome_to_change = new StringBuilder(chromosome);
    int length = chromosome_to_change.length();
    for (int i = 0; i < length - 2; i++) {
      if (Math.random() < probability) {
        count_of_mutations++;
        if (chromosome_to_change.charAt(i) == '1') {
          chromosome_to_change.replace(i, i + 1, "0");
        } else {
          chromosome_to_change.replace(i, i + 1, "1");
        }
      }
      if (count_of_mutations == 2) {
        break;
      }
    }
    if (count_of_mutations < 2) {

      for (int i = 1; i <= 2 - count_of_mutations; i++) {
        if (chromosome_to_change.charAt(length - i) == '1') {
          chromosome_to_change.replace(length - i, length - i + 1, "0");
        } else {
          chromosome_to_change.replace(length - i, length - i + 1, "1");
        }
      }
    }
    String mutant = new String(chromosome_to_change);
    return mutant;
  }

  public static String reverse(String chromosome) {
    StringBuilder reverseChromosome = new StringBuilder(chromosome);
    for (int i = 0; i < reverseChromosome.length(); i++) {
      if (reverseChromosome.charAt(i) == '1') {
        reverseChromosome.replace(i, i + 1, "0");
      } else {
        reverseChromosome.replace(i, i + 1, "1");
      }
    }
    String out_chromosome = new String(reverseChromosome);
    return out_chromosome;
  }

  public void doMutation(List<Chromosome> parents) {
    List<Chromosome> resultSet = new ArrayList<>();
    String mutant;
    Chromosome mutant_ch;
    int id = 0;
    for (Chromosome ch : parents) {
      do {
        mutant = mutate(ch.getChromosome());
      } while (!ChromosomeConverter.checkPoint(mutant));
      mutant_ch = new Chromosome(mutant);
      resultSet.add(mutant_ch);
    }
    parents.clear();
    parents.addAll(resultSet);
  }

  public void doMutationAlgorithm(List<Chromosome> parents) {
    List<Chromosome> resultSet = new ArrayList<>();
    Random rnd = new Random();
    String mutant;
    String str_pointCount, str_generatioNumber, str_probability;
    String prefixPointCount, prefixGenNumber, prefixProbability;
    for (Chromosome ch : parents) {

      mutant = mutate(ch.getChromosome());
      int pointCount = Integer.parseInt(mutant.substring(0, 5), 2);
      int genNumber = Integer.parseInt(mutant.substring(8, 14), 2);
      int d_probability = Integer.parseInt(mutant.substring(14, 21), 2);
      String gridView = mutant.substring(5, 6);
      String selection = mutant.substring(6, 7);
      String sex = mutant.substring(7, 8);
      if (pointCount < 7 || pointCount > 11) {
        pointCount = rnd.nextInt(5) + 7;
        str_pointCount = Integer.toBinaryString(pointCount);
        prefixPointCount = ChromosomeConverter.getPrefix(str_pointCount, 5);
      } else {
        prefixPointCount = "";
        str_pointCount = mutant.substring(0, 5);
      }
      if (genNumber == 0) {
        genNumber = rnd.nextInt(54) + 10;
        str_generatioNumber = Integer.toBinaryString(genNumber);
        prefixGenNumber = ChromosomeConverter.getPrefix(str_generatioNumber, 6);
      } else {
        prefixGenNumber = "";
        str_generatioNumber = mutant.substring(8, 14);
      }
      if (d_probability > 90 || d_probability < 10) {
        d_probability = rnd.nextInt(81) + 10;
        str_probability = Integer.toBinaryString(d_probability);
        prefixProbability = ChromosomeConverter.getPrefix(str_probability, 7);
      } else {
        prefixProbability = "";
        str_probability = mutant.substring(14, 21);
      }

      mutant = prefixPointCount + str_pointCount + gridView + selection + sex + prefixGenNumber + str_generatioNumber + prefixProbability + str_probability;
      resultSet.add(new Chromosome(mutant));
    }
    parents.clear();
    parents.addAll(resultSet);
  }
}
