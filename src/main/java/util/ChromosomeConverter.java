package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Chromosome;
import model.GeneticModel;
import model.Mutation;

public class ChromosomeConverter {

  private ChromosomeConverter() {}

  private static final String EMPTY_FIELD = "";
  private static final String DOT = "\\.";
  private static int MIN_LENGTH_INT_PART = 5;
  private static int MIN_LENGTH_FRACT_PART = 3;

  public static String numberToChromosome(double x1, double x2) {

    String binaryStrX1 = DoubleToBinaryString(x1);
    String binaryStrX2 = DoubleToBinaryString(x2);
    return (binaryStrX1 + binaryStrX2);
  }

  public static double[] chromosomeToNumber(Chromosome chromosome) {
    double[] result = new double[2];
    result[0] = Double.valueOf(BinaryStringToDouble(chromosome.getChromosomeX1()));
    result[1] = Double.valueOf(BinaryStringToDouble(chromosome.getChromosomeX2()));
    return result;
  }

  private static String DoubleToBinaryString(double d) {
    // get sing number
    String sign = EMPTY_FIELD;
    if (d < 0) {
      sign += "1";
    } else {
      sign += "0";
    }
    // conver to absolute value
    double absNumber = Math.abs(d);
    // get number parts
    String[] splitResult = Double.toString(absNumber).split(DOT);
    // convert parts to String
    String intPart = Integer.toBinaryString(Integer.valueOf(splitResult[0]));
    String fractPart = Integer.toBinaryString(Integer.valueOf(splitResult[1]));
    // make Prefix
    String intPrefix = getPrefix(intPart, MIN_LENGTH_INT_PART);
    String fractPrefix = getPrefix(fractPart, MIN_LENGTH_FRACT_PART);
    // get converted value
    String result = sign + intPrefix + intPart + fractPrefix + fractPart;
    return result;
  }

  public static String getPrefix(String part, int bitsCount) {
    String res = EMPTY_FIELD;
    if (part.length() < bitsCount) {
      int missingBits = bitsCount - part.length();
      for (int i = 0; i < missingBits; i++) {
        res += "0";
      }
    }
    return res;
  }

  private static double BinaryStringToDouble(String binaryString) {
    // get Values
    String signStr = binaryString.substring(0, 1);
    String intPartStr = binaryString.substring(1, 6);
    String fractPartStr = binaryString.substring(6, 9);
    // check sign
    String resultValue = EMPTY_FIELD;
    if (signStr.equals("1")) {
      resultValue += "-";
    }
    // get number representing
    int intValue = Integer.parseInt(intPartStr, 2);
    int fractValue = Integer.parseInt(fractPartStr, 2);
    resultValue += intValue + "." + fractValue;
    return Double.valueOf(resultValue);
  }

  public static boolean checkPoint(String string) {
    String secondpart = string.substring(9, 18);
    if (((BinaryStringToDouble(string) > 20) || (BinaryStringToDouble(string) < -10))
        || ((BinaryStringToDouble(secondpart) > 20) || (BinaryStringToDouble(secondpart) < -10))) {
      return false;
    }
    return true;
  }

  public static List<String> createAlgorithmChromosome(int count) {
    List<String> algorithmChromosomeList = new ArrayList<>();
    Random rnd = new Random();
    String chromosome;
    int pointCount, generationNumber, probability, gridView, selection, sex;
    String str_pointCount, str_generatioNumber, str_probability;
    String prefixPointCount, prefixGenNumber, prefixProbability;

    for (int i = 0; i < count; i++) {
      pointCount = rnd.nextInt(5) + 7;
      generationNumber = rnd.nextInt(54) + 10;
      probability = rnd.nextInt(81) + 10;
      gridView = rnd.nextInt(2);
      selection = rnd.nextInt(2);
      sex = rnd.nextInt(2);
      str_pointCount = Integer.toBinaryString(pointCount);
      str_generatioNumber = Integer.toBinaryString(generationNumber);
      str_probability = Integer.toBinaryString(probability);
      prefixPointCount = getPrefix(str_pointCount, 5);
      prefixGenNumber = getPrefix(str_generatioNumber, 6);
      prefixProbability = getPrefix(str_probability, 7);
      chromosome =
          prefixPointCount
              + str_pointCount
              + gridView
              + selection
              + sex
              + prefixGenNumber
              + str_generatioNumber
              + prefixProbability
              + str_probability;
      algorithmChromosomeList.add(chromosome);
    }

    return algorithmChromosomeList;
  }

  public static GeneticModel parseAlgorithmChromosome(String string) {
    GeneticModel model = new GeneticModel();
    model.setMutation(new Mutation());
    int pointCount, genNumber;
    double probability;
    String grid, selection, sex;
    String gridView, selectionMode, sexMode;
    pointCount = Integer.parseInt(string.substring(0, 5), 2);
    genNumber = Integer.parseInt(string.substring(8, 14), 2);
    probability = Integer.parseInt(string.substring(14, 21), 2) / 100.0;
    grid = string.substring(5, 6);
    selection = string.substring(6, 7);
    sex = string.substring(7, 8);
    if (grid.equals("0")) {
      gridView = "Rectangle";
    } else {
      gridView = "Box";
    }
    if (selection.equals("0")) {
      selectionMode = "Rang";
    } else {
      selectionMode = "Roulette";
    }
    if (sex.equals("0")) {
      sexMode = "MinDistance";
    } else {
      sexMode = "Better&Worse";
    }
    model.setPointCount(pointCount);
    model.setGridView(gridView);
    model.setSelectionMod(selectionMode);
    model.setSexMod(sexMode);
    model.setGenerationNumber(genNumber);
    model.setProbability(probability);

    return model;
  }
}
