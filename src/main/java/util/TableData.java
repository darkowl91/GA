package util;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.Chromosome;

public final class TableData {

  public static void fillNew(TableView<Chromosome> tv, List<Chromosome> cs) {
    List<Chromosome> list = new ArrayList<>(cs);
    ObservableList<Chromosome> chromosomes = FXCollections.observableList(list);
    tv.setItems(chromosomes);
  }

  public static void addToCurrent(TableView<Chromosome> tv, List<Chromosome> cs) {
    List<Chromosome> list = new ArrayList<>();
    for (Chromosome chromosome : cs) {
      if (chromosome.getAdaptValue() != Double.MAX_VALUE) {
        list.add(chromosome);
      }
    }
    tv.getItems().addAll(FXCollections.observableList(list));
  }

  public static void addOne(TableView<Chromosome> tv, Chromosome e) {
    int id = tv.getItems().size();
    e.setId(String.valueOf(++id));
    tv.getItems().add(e);
  }

  public static void clear(TableView<Chromosome> tv) {
    tv.getItems().clear();
  }

  public static void replace(TableView<Chromosome> tv, Chromosome c, int i) {
    c.setId(String.valueOf(i));
    tv.getItems().set(i, c);
  }

  public static List<Chromosome> getSubList(TableView<Chromosome> tv, Chromosome c, int i, int j) {
    return tv.getItems().subList(i, j);
  }
}
