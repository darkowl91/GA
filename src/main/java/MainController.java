import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import constants.ViewConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Algorithm;
import model.Chromosome;
import model.FunctionAdaptation;
import model.GeneticModel;
import model.Mutation;
import model.Selection;
import model.Sex;
import util.BestWorkFinder;
import util.ChromosomeConverter;
import util.CircleCreator;
import util.GridTools;
import util.PointNode;
import util.TableData;

public class MainController implements Initializable {

  @FXML private Circle circleRoughtCast;
  @FXML private Circle circleSelection;
  @FXML private Circle circleMutation;
  @FXML private Circle circleSex;
  @FXML private Pane drawingPane;
  @FXML private ChoiceBox<Integer> pointCountCh;
  @FXML private ChoiceBox<String> gridViewCh;
  @FXML private ChoiceBox<String> selectionCh;
  @FXML private ChoiceBox<String> sexCh;
  @FXML private TextField generationNumberTf;
  @FXML private Slider probabilitySl;
  @FXML private ProgressIndicator indicator;
  @FXML private TableView<Chromosome> tvChromosome;
  @FXML private TextField repeatTf;
  @FXML private TitledPane propertiesPane;
  @FXML private Label labelTick;
  @FXML private TableView<Algorithm> algorithmTv;
  @FXML private Button byStepBtn;
  @FXML private Button resetBtn;
  @FXML private Text generationlbl;
  @FXML private ImageView imgeDefFunction;
  @FXML private CheckBox checkBox;
  @FXML private TextField configNumberTf;
  @FXML private TableView<GeneticModel> geneticConfigTv;
  @FXML private Button runBtn;
  @FXML private ProgressIndicator configProgress;
  @FXML private Button countForAllBtn;
  @FXML private Button findBestOneBtn;
  @FXML private Button findBestAlgorithm;
  @FXML private TableColumn pointColum;

  private int stepNumber;
  private FunctionAdaptation functionAdaptation;
  private int generationNumber;
  private int tickCount;
  private List<Algorithm> algorithms;
  private GeneticModel tGeneticModel;
  private Timeline fiveSecondsWonder;
  private List<GeneticModel> geneticModels;
  private int configCounter;
  private ObservableList<GeneticModel> observableGeneticModels;
  private List<String> algorithmChromosomes;

  @FXML
  public void runGeneticAlgorithmByStep(Event event) throws Exception {
    ShowStepByCircle();
    if (stepNumber == 0) {
      reset();
      initModel(tGeneticModel);
      fillModel(tGeneticModel);
    }
    doGenrtickWork(tGeneticModel);
    final int repetNumber = Integer.valueOf(repeatTf.getText());
    if (repetNumber == tickCount) {
      tickCount = 0;
    }
  }

  @FXML
  public void runGeneticAlgorithm(final Event event) throws Exception {
    reset();
    lockPane(true);
    final int repetNumber = Integer.valueOf(repeatTf.getText());
    tickCount = 0;
    fiveSecondsWonder =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.1),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    ShowStepByCircle();
                    try {
                      if (stepNumber == 0) {
                        initModel(tGeneticModel);
                        fillModel(tGeneticModel);
                      }
                      doGenrtickWork(tGeneticModel);
                    } catch (Exception ex) {
                      // Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null,
                      // ex);
                    }
                    // end of repeations
                    if (repetNumber == tickCount) {
                      fiveSecondsWonder.stop();
                      tickCount = 0;
                      labelTick.setText(" - " + tickCount + " - ");
                      lockPane(false);
                    }
                  }
                }));
    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    fiveSecondsWonder.play();
  }

  @FXML
  public void onReset(Event event) {
    reset();
  }

  @FXML
  public void CountAdaptationForAll(Event event) {
    reset();
    generateConfiguration();
    lockPane(true);
    algorithmTv.getItems().clear();
    final int repetNumber = Integer.valueOf(repeatTf.getText());
    final int configCount = Integer.valueOf(configNumberTf.getText());
    final double stepCount = 1.0 / configCount;

    tGeneticModel = geneticModels.get(configCounter);
    fiveSecondsWonder =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.1),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    ShowStepByCircle();
                    try {
                      doGenrtickWork(tGeneticModel);
                    } catch (Exception ex) {
                      // Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null,
                      // ex);
                    }
                    if (repetNumber == tickCount) {
                      configCounter++;
                      configProgress.setProgress(configProgress.getProgress() + stepCount);
                      if (configCounter == configCount) {
                        fiveSecondsWonder.stop();
                        configProgress.setProgress(0);
                        configCounter = 0;
                        tickCount = 0;
                        stepNumber = 0;
                        lockPane(false);
                        geneticConfigTv.getItems().removeAll(observableGeneticModels);

                        for (int i = 0; i < algorithmTv.getItems().size(); i++) {
                          geneticModels
                              .get(i)
                              .setAdaptationValue(algorithmTv.getItems().get(i).getAdaptValue());
                          observableGeneticModels =
                              FXCollections.observableArrayList(geneticModels);
                          geneticConfigTv.setItems(observableGeneticModels);
                        }
                        findBestOneBtn.disableProperty().setValue(false);
                      } else {
                        tGeneticModel = geneticModels.get(configCounter);
                        tickCount = 0;
                        labelTick.setText(" - " + tickCount + " - ");
                      }
                    }
                  }
                }));
    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    fiveSecondsWonder.play();
  }

  @FXML
  public void showFunctionDefenition(Event event) {
    imgeDefFunction.visibleProperty().setValue(checkBox.isSelected());
  }

  @FXML
  public void findBestOne(Event event) {
    findBestOneBtn.disableProperty().setValue(true);
    propertiesPane.setExpanded(true);
    lockUnnecessary(true);
    findBestAlgorithm.visibleProperty().set(true);
    // pointColum.visibleProperty().set(false);
  }

  @FXML
  public void findBestAlgorithm(Event event) throws Exception {

    reset();
    lockPane(true);
    GeneticModel gm = new GeneticModel();
    gm.setPointCount(Integer.valueOf(configNumberTf.getText()));
    gm.setSelection(createSelection());
    gm.setMutation(createMutation());
    gm.setSex(createSex());
    gm.setProbability(probabilitySl.getValue());
    gm.setGenerationNumber(Integer.valueOf(generationNumberTf.getText()));
    List<Chromosome> cs = new ArrayList<>();
    for (int i = 0; i < algorithmChromosomes.size(); i++) {
      cs.add(
          new Chromosome(
              algorithmChromosomes.get(i), geneticModels.get(i).getAdaptationValue(), 1));
    }
    gm.setChromosomes(cs);
    gm.setSelectionMod(selectionCh.getValue());
    gm.setProbability(probabilitySl.getValue());
    gm.setSexMod(sexCh.getValue());
    gm.setGenerationNumber(Integer.valueOf(generationNumberTf.getText()));
    TableData.fillNew(tvChromosome, gm.getChromosomes());

    int generation = 1;
    int tick = 0;
    List<Algorithm> as = new ArrayList<>();
    List<Algorithm> asResult = new ArrayList<>();
    /** Genetick work */
    while (tick != Integer.valueOf(repeatTf.getText())) {
      gm.doSelection();
      TableData.addToCurrent(tvChromosome, gm.getChromosomes());
      /**
       * We get new algorithm configuration after mutation so we need to count function adaptation
       * again
       */
      gm.doMutationWithAlgorithm();
      countAdaptationForAlgorithm(gm, generation);
      TableData.addToCurrent(tvChromosome, gm.getChromosomes());

      gm.doAlgorithmSex();
      generation++;
      countAdaptationForAlgorithm(gm, generation);
      TableData.addToCurrent(tvChromosome, gm.getChromosomes());

      if (generation == Integer.valueOf(generationNumberTf.getText())) {
        tick++;
        labelTick.setText(" - " + tick + " - ");
        /*
         *  find best in last generation and add to list
         * nedt to compare repeating algoritms and fin best one
         */
        as.add(BestWorkFinder.findBestWorkInLastGen(gm, tick));
        generation = 1;
        if (tick == Integer.valueOf(repeatTf.getText())) {
          Algorithm bestAlgorithm = BestWorkFinder.findBestAlgorithm(as);
          asResult.add(bestAlgorithm);
          as.clear();
        }
      }
    }
    algorithmTv.getItems().clear();
    algorithmTv.setItems(FXCollections.observableArrayList(asResult));
    geneticConfigTv.getItems().clear();
    geneticModels.clear();
    for (Algorithm algorithm : asResult) {
      GeneticModel geneticModel =
          ChromosomeConverter.parseAlgorithmChromosome(algorithm.getChromosome().getChromosome());
      geneticModel.setAdaptationValue(algorithm.getAdaptValue());
      geneticModels.add(geneticModel);
    }
    geneticConfigTv.setItems(FXCollections.observableArrayList(geneticModels));
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    gridViewCh.setValue(ViewConstants.GIRID_RECTANGLE);
    pointCountCh.setValue(7);
    selectionCh.setValue(ViewConstants.SLECTION_RANG);
    sexCh.setValue(ViewConstants.SEX_MIN_DISTANCE);
    algorithms = new ArrayList<>();
    tGeneticModel = new GeneticModel();
    functionAdaptation = new FunctionAdaptation();
    initModel(tGeneticModel);
  }

  public void generateConfiguration() {
    algorithmChromosomes =
        ChromosomeConverter.createAlgorithmChromosome(Integer.valueOf(configNumberTf.getText()));
    geneticModels = new ArrayList<>();
    for (String chromosomeStr : algorithmChromosomes) {
      GeneticModel algorithmModel = ChromosomeConverter.parseAlgorithmChromosome(chromosomeStr);
      initModel(algorithmModel);
      algorithmModel.buidGrid();
      geneticModels.add(algorithmModel);
    }
    observableGeneticModels = FXCollections.observableArrayList(geneticModels);
  }

  private void doGenrtickWork(GeneticModel geneticModel) throws Exception {

    double progressStep = 1.0 / ((geneticModel.getGenerationNumber() * 3) - 2.1);

    if (stepNumber == 0) {
      generationNumber++;
      functionAdaptation.countAdaptationInitial(geneticModel);
      drawingPane.getChildren().add(geneticModel.getGridTools().getGroupGrid());
      plotPoints(Color.GREENYELLOW, geneticModel);
      TableData.fillNew(tvChromosome, geneticModel.getChromosomes());
      indicator.setProgress(indicator.getProgress() + progressStep);
    }

    if (generationNumber < Integer.valueOf(geneticModel.getGenerationNumber())) {

      if (stepNumber == 1) {
        geneticModel.doSelection();
        TableData.addToCurrent(tvChromosome, geneticModel.getChromosomes());
        plotPoints(Color.DEEPSKYBLUE, geneticModel);
        indicator.setProgress(indicator.getProgress() + progressStep);
      }

      if (stepNumber == 2) {
        geneticModel.doMutation();
        functionAdaptation.countAdaptation(geneticModel, generationNumber);
        TableData.addToCurrent(tvChromosome, geneticModel.getChromosomes());
        plotPoints(Color.PLUM, geneticModel);
        indicator.setProgress(indicator.getProgress() + progressStep);
      }

      if (stepNumber == 3) {
        geneticModel.doSex();
        generationlbl.setText(String.valueOf(generationNumber));
        generationNumber++;
        functionAdaptation.countAdaptation(geneticModel, generationNumber);
        TableData.addToCurrent(tvChromosome, geneticModel.getChromosomes());
        plotPoints(Color.ORANGERED, geneticModel);
        indicator.setProgress(indicator.getProgress() + progressStep);
        stepNumber = 0;
      }

      stepNumber++;

    } else if (generationNumber == Integer.valueOf(geneticModel.getGenerationNumber())) {
      tickCount++;
      labelTick.setText(" - " + tickCount + " - ");
      /*
       *  find best in last generation and add to list
       * nedt to compare repeating algoritms and fin best one
       */
      algorithms.add(BestWorkFinder.findBestWorkInLastGen(geneticModel, tickCount));
      reset();
      if (tickCount == Integer.valueOf(repeatTf.getText())) {
        Algorithm bestAlgorithm = BestWorkFinder.findBestAlgorithm(algorithms);
        algorithmTv.getItems().add(bestAlgorithm);
        algorithms.clear();
      }
    }
  }

  private void plotPoints(Color color, GeneticModel geneticModel) {
    for (Chromosome chromosome : geneticModel.getChromosomes()) {
      PointNode pn = geneticModel.getGridTools().convertToXY(chromosome.getPoint());
      drawingPane.getChildren().add(CircleCreator.createCircle(pn.getX(), pn.getY(), 8, color));
    }
  }

  private void ShowStepByCircle() {
    switch (stepNumber) {
      case 0:
        circleRoughtCast.setStroke(Color.AZURE);
        circleSelection.setStroke(Color.BLACK);
        circleMutation.setStroke(Color.BLACK);
        circleSex.setStroke(Color.BLACK);
        break;
      case 1:
        circleRoughtCast.setStroke(Color.BLACK);
        circleSelection.setStroke(Color.AZURE);
        circleMutation.setStroke(Color.BLACK);
        circleSex.setStroke(Color.BLACK);
        break;
      case 2:
        circleRoughtCast.setStroke(Color.BLACK);
        circleSelection.setStroke(Color.BLACK);
        circleMutation.setStroke(Color.AZURE);
        circleSex.setStroke(Color.BLACK);
        break;
      case 3:
        circleRoughtCast.setStroke(Color.BLACK);
        circleSelection.setStroke(Color.BLACK);
        circleMutation.setStroke(Color.BLACK);
        circleSex.setStroke(Color.AZURE);
        break;
    }
  }

  private void initModel(GeneticModel geneticModel) {
    geneticModel.setGridTools(createGridTools());
    geneticModel.setSelection(createSelection());
    geneticModel.setSex(createSex());
    geneticModel.setMutation(createMutation());
  }

  private void fillModel(GeneticModel geneticModel) {
    geneticModel.setPointCount(pointCountCh.getValue());
    geneticModel.setGridView(gridViewCh.getValue());
    geneticModel.setSelectionMod(selectionCh.getValue());
    geneticModel.setProbability(probabilitySl.getValue());
    geneticModel.setSexMod(sexCh.getValue());
    geneticModel.setGenerationNumber(Integer.valueOf(generationNumberTf.getText()));
  }

  private GridTools createGridTools() {
    GridTools gridTools = new GridTools(javafx.scene.paint.Color.GRAY, 2, 250, 250);
    drawingPane.setTranslateX(150);
    drawingPane.setTranslateY(50);
    return gridTools;
  }

  private Selection createSelection() {
    return new Selection();
  }

  private Mutation createMutation() {
    Mutation mutation = new Mutation();
    return mutation;
  }

  private Sex createSex() {
    Sex sex = new Sex();
    return sex;
  }

  public void reset() {
    tGeneticModel = new GeneticModel();
    stepNumber = 0;
    generationNumber = 0;
    indicator.setProgress(0);
    drawingPane.getChildren().clear();
    tvChromosome.getItems().clear();
  }

  private void lockPane(boolean lock) {
    runBtn.disableProperty().set(lock);
    pointCountCh.disableProperty().set(lock);
    gridViewCh.disableProperty().set(lock);
    selectionCh.disableProperty().set(lock);
    sexCh.disableProperty().set(lock);
    generationNumberTf.disableProperty().set(lock);
    probabilitySl.disableProperty().set(lock);
    repeatTf.disableProperty().set(lock);
    byStepBtn.disableProperty().set(lock);
    resetBtn.disableProperty().set(lock);
    countForAllBtn.disableProperty().set(lock);
    configNumberTf.disableProperty().set(lock);
    findBestOneBtn.disableProperty().setValue(lock);
  }

  private void lockUnnecessary(boolean lock) {
    pointCountCh.disableProperty().set(lock);
    gridViewCh.disableProperty().set(lock);
    runBtn.disableProperty().set(lock);
    byStepBtn.disableProperty().set(lock);
    resetBtn.disableProperty().set(lock);
  }

  private void countAdaptationForAlgorithm(GeneticModel gm, int generationNumber) throws Exception {
    geneticModels.clear();
    algorithmTv.getItems().clear();
    for (Chromosome chromosome : gm.getChromosomes()) {
      GeneticModel model = ChromosomeConverter.parseAlgorithmChromosome(chromosome.getChromosome());
      initModel(model);
      model.buidGrid();
      geneticModels.add(model);
    }
    int repeatCount = Integer.valueOf(repeatTf.getText());
    for (GeneticModel geneticModel : geneticModels) {
      tickCount = 0;
      while (tickCount != repeatCount) {
        doGenrtickWorkForAlgorithm(geneticModel);
      }
    }
    for (int i = 0; i < algorithmTv.getItems().size(); i++) {
      gm.getChromosomes().get(i).setAdaptValue(algorithmTv.getItems().get(i).getAdaptValue());
      gm.getChromosomes().get(i).setGenerationNumber(generationNumber);
    }
  }

  private void doGenrtickWorkForAlgorithm(GeneticModel geneticModel) throws Exception {

    double progressStep = 1.0 / ((geneticModel.getGenerationNumber() * 3) - 2.1);

    if (stepNumber == 0) {
      generationNumber++;
      functionAdaptation.countAdaptationInitial(geneticModel);
    }

    if (generationNumber < Integer.valueOf(geneticModel.getGenerationNumber())) {
      if (stepNumber == 1) {
        geneticModel.doSelection();
        indicator.setProgress(indicator.getProgress() + progressStep);
      }

      if (stepNumber == 2) {
        geneticModel.doMutation();
        functionAdaptation.countAdaptation(geneticModel, generationNumber);
      }

      if (stepNumber == 3) {
        geneticModel.doSex();
        generationNumber++;
        functionAdaptation.countAdaptation(geneticModel, generationNumber);
        stepNumber = 0;
      }
      stepNumber++;

    } else if (generationNumber == Integer.valueOf(geneticModel.getGenerationNumber())) {
      tickCount++;
      /*
       *  find best in last generation and add to list
       * nedt to compare repeating algoritms and fin best one
       */
      algorithms.add(BestWorkFinder.findBestWorkInLastGen(geneticModel, tickCount));

      stepNumber = 0;
      generationNumber = 0;
      indicator.setProgress(0);

      if (tickCount == Integer.valueOf(repeatTf.getText())) {
        Algorithm bestAlgorithm = BestWorkFinder.findBestAlgorithm(algorithms);
        algorithmTv.getItems().add(bestAlgorithm);
        algorithms.clear();
      }
    }
  }
}
