package datavisualization;

import java.io.*;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/*
 * The main file
 * @author S. Liu
 * 
 */
public class DataVisualization extends Application {

    // Declare instance variables
    private VBox databaseVBox;
    private VBox graphVBox;
    private HBox filterHBox;
    private HBox screenHBox;

    private TableView<DataPoint> databaseTable;
    private TableView<DataPoint> datapointTable;
    private TableView<SummaryData> summaryTable;

    private TableColumn<DataPoint, String> provinceCol;
    private TableColumn<DataPoint, Integer> yearCol;
    private TableColumn<DataPoint, Double> crimeCol;
    private TableColumn<DataPoint, String> provinceCol2;
    private TableColumn<DataPoint, Integer> yearCol2;
    private TableColumn<DataPoint, Double> crimeCol2;
    private TableColumn<SummaryData, Integer> countCol;
    private TableColumn<SummaryData, Double> maxCol;
    private TableColumn<SummaryData, Double> minCol;
    private TableColumn<SummaryData, Double> meanCol;
    private TableColumn<SummaryData, Double> medianCol;
    private TableColumn<SummaryData, Double> standardDeviationCol;

    private XYChart.Series<Integer, Double> bcSeries;
    private XYChart.Series<Integer, Double> abSeries;
    private XYChart.Series<Integer, Double> skSeries;
    private XYChart.Series<Integer, Double> mbSeries;
    private XYChart.Series<Integer, Double> onSeries;
    private XYChart.Series<Integer, Double> qcSeries;
    private XYChart.Series<Integer, Double> nbSeries;
    private XYChart.Series<Integer, Double> nsSeries;
    private XYChart.Series<Integer, Double> peSeries;
    private XYChart.Series<Integer, Double> nlSeries;

    private DataSet wholeDataSet;
    private Parent lineChart;
    private Parent pieChart;
    private Button switchGraphsButton;
    private boolean isLineChart;
    private Tooltip tooltip;

    private SummaryData summaryData;
    private ComboBox<String> filterList;
    private TextField filterField;

    private Stage popUpStage;
    private Scene mainScene;
    private Scene popUpScene;

    /*
     * The main method
     */
    public static void main(String args[]) {
        launch(args);
    }

    /*
     * The method that starts the program
     * 
     * @param primaryStage - the primary stage
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        // Initialize variables
        databaseVBox = new VBox(10);
        graphVBox = new VBox(10);
        filterHBox = new HBox(10);
        screenHBox = new HBox(30);

        databaseTable = new TableView<>();
        datapointTable = new TableView<>();
        summaryTable = new TableView<>();

        provinceCol = new TableColumn<>("Province");
        yearCol = new TableColumn<>("Year");
        crimeCol = new TableColumn<>("Crime index");
        provinceCol2 = new TableColumn<>("Province");
        yearCol2 = new TableColumn<>("Year");
        crimeCol2 = new TableColumn<>("Crime index");
        countCol = new TableColumn<>("Count");
        maxCol = new TableColumn<>("Max");
        minCol = new TableColumn<>("Min");
        meanCol = new TableColumn<>("μ");
        medianCol = new TableColumn<>("Median");
        standardDeviationCol = new TableColumn<>("σ");

        bcSeries = new XYChart.Series<>();
        abSeries = new XYChart.Series<>();
        skSeries = new XYChart.Series<>();
        mbSeries = new XYChart.Series<>();
        onSeries = new XYChart.Series<>();
        qcSeries = new XYChart.Series<>();
        nbSeries = new XYChart.Series<>();
        nsSeries = new XYChart.Series<>();
        peSeries = new XYChart.Series<>();
        nlSeries = new XYChart.Series<>();
        
        wholeDataSet = new DataSet(importData());
        lineChart = createLineGraph();
        pieChart = createPieChart();
        switchGraphsButton = new Button("Year vs Crime Index");
        isLineChart = true;

        summaryData = new SummaryData(wholeDataSet.allCrimeIndices(wholeDataSet.getDataPoints()));
        filterList = new ComboBox<>();
        filterField = new TextField();

        popUpStage = new Stage();
        mainScene = new Scene(screenHBox);
        popUpScene = new Scene(datapointTable, 410, 80);

        // Set properties to TableView columns
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        crimeCol.setCellValueFactory(new PropertyValueFactory<>("crimeIndex"));

        provinceCol2.setCellValueFactory(new PropertyValueFactory<>("province"));
        yearCol2.setCellValueFactory(new PropertyValueFactory<>("year"));
        crimeCol2.setCellValueFactory(new PropertyValueFactory<>("crimeIndex"));

        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("max"));
        minCol.setCellValueFactory(new PropertyValueFactory<>("min"));
        meanCol.setCellValueFactory(new PropertyValueFactory<>("mean"));
        medianCol.setCellValueFactory(new PropertyValueFactory<>("median"));
        standardDeviationCol.setCellValueFactory(new PropertyValueFactory<>("standardDeviation"));

        // Fix the width of the columns
        provinceCol.prefWidthProperty().bind(databaseTable.widthProperty().multiply(0.48));
        yearCol.prefWidthProperty().bind(databaseTable.widthProperty().multiply(0.23));
        crimeCol.prefWidthProperty().bind(databaseTable.widthProperty().multiply(0.23));
        yearCol.setResizable(false);
        provinceCol.setResizable(false);
        crimeCol.setResizable(false);

        provinceCol2.prefWidthProperty().bind(datapointTable.widthProperty().multiply(0.48));
        yearCol2.prefWidthProperty().bind(datapointTable.widthProperty().multiply(0.23));
        crimeCol2.prefWidthProperty().bind(datapointTable.widthProperty().multiply(0.23));
        provinceCol2.setResizable(false);
        yearCol2.setResizable(false);
        crimeCol2.setResizable(false);

        // Add columns to respective table views
        databaseTable.getColumns().addAll(provinceCol, yearCol, crimeCol);
        datapointTable.getColumns().addAll(provinceCol2, yearCol2, crimeCol2);
        summaryTable.getColumns().addAll(countCol, maxCol, minCol, meanCol, medianCol, standardDeviationCol);

        // Add padding 
        databaseVBox.setPadding(new Insets(10, 10, 10, 10));
        datapointTable.setPadding(new Insets(10, 10, 10, 10));

        // Remove default additional column of table view
        databaseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        datapointTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        summaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Fix number of rows
        summaryTable.setFixedCellSize(25);
        summaryTable.prefHeightProperty().bind(Bindings.size(summaryTable.getItems()).multiply(summaryTable.getFixedCellSize()).add(40));

        // Set default items to table views
        databaseTable.setItems(wholeDataSet.getDataPoints());
        summaryTable.getItems().add(summaryData);

        // Place nodes into horizontal and vertical box
        filterHBox.getChildren().addAll(filterField, filterList);
        HBox.setHgrow(filterField, Priority.ALWAYS);
        HBox.setHgrow(filterList, Priority.ALWAYS);
        databaseVBox.getChildren().addAll(filterHBox, databaseTable, summaryTable);
        graphVBox.getChildren().addAll(switchGraphsButton, lineChart);
        graphVBox.setAlignment(Pos.CENTER);
        screenHBox.getChildren().addAll(graphVBox, databaseVBox);

        // Configure popup stage
        popUpStage.setTitle("Data value");
        popUpStage.setScene(popUpScene);

        // Add tooltips to graph 
        tooltip = new Tooltip("Switch grades");
        Tooltip.install(switchGraphsButton, tooltip);
        bindTooltip(switchGraphsButton, tooltip);

        // Create list of filters
        filterList.getItems().addAll(
            "",
            "Newfoundland and Labrador", 
            "Prince Edward Island", 
            "Nova Scotia",
            "New Brunswick",
            "Quebec",
            "Ontario",
            "Manitoba",
            "Saskatchewan",
            "Alberta",
            "British Columbia"
        );
        
        // Detect if the user has clicked on a filter
        filterList.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            // Get the filter
            String userSelection = filterList.getValue();
            // If the filter is empty, display all items
            if (userSelection == null || userSelection.isEmpty()) {
                databaseTable.setItems(wholeDataSet.getDataPoints());
            }
            // If the filter is not empty, display a filtered list of items 
            else if (userSelection != null) {
                databaseTable.setItems(wholeDataSet.search(userSelection));
            }
            // Update summary data
            summaryTable.getItems().clear();
            summaryData = summaryData.newSummary(wholeDataSet.allCrimeIndices(wholeDataSet.search(userSelection)));
            summaryTable.getItems().add(summaryData);
        });

        // Detect if user double clicks a row
        databaseTable.setRowFactory( table -> {

            TableRow<DataPoint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // Add the DataPoint in the clicked row and display stage
                    datapointTable.getItems().clear();
                    datapointTable.getItems().add(row.getItem());
                    popUpStage.show();

                }
            });
            return row;

        });

        // Detect if the user typed into the filter
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // If the text field is empty, display all data points
            if (newValue == null || newValue.isEmpty()) {
                databaseTable.setItems(wholeDataSet.getDataPoints());
            }
            // If the text field is not empty, display the data points containing the search value 
            else {
                databaseTable.setItems(wholeDataSet.search(newValue));
            }
            // Update summary data
            summaryTable.getItems().clear();
            summaryData = summaryData.newSummary(wholeDataSet.allCrimeIndices(wholeDataSet.search(newValue)));
            summaryTable.getItems().add(summaryData);
        });

        // Switch graphs if button is pressed
        switchGraphsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                graphVBox.getChildren().clear();
                if (isLineChart) {
                    graphVBox.getChildren().addAll(switchGraphsButton, pieChart);
                    switchGraphsButton.setText("Crime index by province");
                }
                else {
                    graphVBox.getChildren().addAll(switchGraphsButton, lineChart);
                    switchGraphsButton.setText("Year vs crime index");
                }
                isLineChart = !isLineChart;
            }

        });
        
        // Configure primary stage
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Crime index over time");
        primaryStage.show();

    }

    private ObservableList<DataPoint> importData() throws IOException {
        
        BufferedReader file = new BufferedReader(new FileReader("data.csv"));

        ObservableList<DataPoint> temporaryList;
        String strLine;

        temporaryList = FXCollections.observableArrayList();
        // Reads first line of csv file (e.g. headings)
        strLine = file.readLine();

        while (strLine != null) {
            strLine = file.readLine();
            if (strLine == null || strLine.equals("")) {
                break;
            }
            String[] split = strLine.split(",");
            for (int i = 0; i < split.length; ++i) {
                split[i] = split[i].replace("\"", "");
            }
            temporaryList.add(new DataPoint(split[1], Integer.parseInt(split[0]), Double.parseDouble(split[2])));
        }

        file.close();

        return temporaryList;

    }

    public Parent createLineGraph() {
        
        // Declare variables
        LineChart<Integer, Double> tempLineChart;
        NumberAxis xAxis;
        NumberAxis yAxis;

        // Initialize variables
        xAxis = new NumberAxis("Year", 2010, 2019, 1);
        yAxis = new NumberAxis("Crime index", 0, 160, 15);
        tempLineChart = new LineChart(xAxis, yAxis);

        // Set names 
        bcSeries.setName("BC");
        abSeries.setName("AB");
        skSeries.setName("SK");
        mbSeries.setName("MB");
        onSeries.setName("ON");
        qcSeries.setName("QC");
        nbSeries.setName("NB");
        nsSeries.setName("NS");
        peSeries.setName("PE");
        nlSeries.setName("NL");

        // Add data points into graph depending on its province
        for (DataPoint data: wholeDataSet.getDataPoints()) {
            switch (data.getProvince()) {
                case "British Columbia" :
                    bcSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "Alberta" :
                    abSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "Saskatchewan" :
                    skSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "Manitoba" :
                    mbSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "Ontario" :
                    onSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "Quebec" :
                    qcSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "New Brunswick" :
                    nbSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "Nova Scotia" :
                    nsSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                case "Prince Edward Island" :
                    peSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
                default:
                    nlSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
            }
        }

        // Add series into line chart
        tempLineChart.getData().addAll(bcSeries, abSeries, skSeries, mbSeries, onSeries, qcSeries, nbSeries, nsSeries, peSeries, nlSeries);

        // Add tooltip to data points
        for (XYChart.Series<Integer, Double> s : tempLineChart.getData()) {
            for (XYChart.Data<Integer, Double> d : s.getData()) {
                tooltip = new Tooltip("Province: " + s.getName() + ", Year: " + d.getXValue() + ", Crime index: " + d.getYValue());
                Tooltip.install(d.getNode(), tooltip);

                bindTooltip(d.getNode(), tooltip);
            }
        }

        // Return line chart
        return tempLineChart;

    }

    public Parent createPieChart() {
        // Declare varaibles
        String provinces[] = { "British Columbia", "Alberata", "Saskatchewan", "Manitoba", "Ontario", "Quebec", "New Brunswick", "Nova Scoita", "Prince Edward Island", "Newfoundland and Labrador" };
        double count[];
        double total;
        ObservableList<PieChart.Data> pieChartData;

        // Initialize variables
        pieChart = new PieChart();
        count = new double[10];
        total = 0;
        
        // Get the total crime index for each of the provinces
        for (DataPoint data: wholeDataSet.getDataPoints()) {
            for (int i = 0; i < 10; ++i) {
                if (data.getProvince().equals(provinces[i])) {
                    count[i] += data.getCrimeIndex();
                    total += data.getCrimeIndex();
                }
            }
        }

        // Create pie chart data
        pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("BC", count[0]),
            new PieChart.Data("AB", count[1]),
            new PieChart.Data("SK", count[2]),
            new PieChart.Data("MB", count[3]),
            new PieChart.Data("ON", count[4]),
            new PieChart.Data("QC", count[5]),
            new PieChart.Data("NB", count[6]),
            new PieChart.Data("NS", count[7]),
            new PieChart.Data("PE", count[8]),
            new PieChart.Data("NL", count[9])
        );

        pieChart = new PieChart(pieChartData);

        // Add tooltip to data points
        for (PieChart.Data data: pieChartData) {
            int percentage;
            
            percentage = (int)(100.0 * data.getPieValue() / total);
            tooltip = new Tooltip("Province: " + data.getName() + ", Crime index: " + Math.round(100.0 * data.getPieValue()) / 100.0 + ", " + percentage + "%");
            Tooltip.install(data.getNode(), tooltip);
            bindTooltip(data.getNode(), tooltip);
        }

        return pieChart;

    }

    // Make the tooltip appear instantly
    public static void bindTooltip(final Node node, final Tooltip tooltip) {
        node.setOnMouseMoved(new EventHandler<MouseEvent>() {
           @Override  
           public void handle(MouseEvent event) {
              tooltip.show(node, event.getScreenX(), event.getScreenY() + 15);
           }
        });  
        node.setOnMouseExited(new EventHandler<MouseEvent>(){
           @Override
           public void handle(MouseEvent event){
              tooltip.hide();
           }
        });
    }

}