package datavisualization;

import java.io.*;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class DataVisualization extends Application {

    // Instance variables
    private VBox vBox;
    private HBox hBox;
    private HBox wholeScreen;

    private TableView<DataPoint> tableView;
    private TableView<DataPoint> singleTable;
    private TableView<SummaryData> summaryTable;

    private TableColumn<DataPoint, String> provinceCol;
    private TableColumn<DataPoint, Integer> yearCol;
    private TableColumn<DataPoint, Double> crimeCol;
    private TableColumn<DataPoint, String> provinceColCopy;
    private TableColumn<DataPoint, Integer> yearColCopy;
    private TableColumn<DataPoint, Double> crimeColCopy;
    private TableColumn<SummaryData, Integer> countCol;
    private TableColumn<SummaryData, Double> maxCol;
    private TableColumn<SummaryData, Double> minCol;
    private TableColumn<SummaryData, Double> meanCol;
    private TableColumn<SummaryData, Double> medianCol;
    private TableColumn<SummaryData, Double> standardDeviationCol;

    private LineChart<Integer, Double> lineChart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private ObservableList<XYChart.Series<Integer,Double>> lineChartData;
    private PieChart pieChart;

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
    private SummaryData summaryData;
    private ComboBox<String> filterList;
    private TextField filterField;
    private Stage popUp;
    private Scene mainScene;
    private Scene popUpScene;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Crime index over time");

        // Initialize variables
        vBox = new VBox(10);
        hBox = new HBox(10);
        wholeScreen = new HBox(30);

        tableView = new TableView<>();
        singleTable = new TableView<>();
        summaryTable = new TableView<>();

        provinceCol = new TableColumn<>("Province");
        yearCol = new TableColumn<>("Year");
        crimeCol = new TableColumn<>("Crime index");
        provinceColCopy = new TableColumn<>("Province");
        yearColCopy = new TableColumn<>("Year");
        crimeColCopy = new TableColumn<>("Crime index");
        countCol = new TableColumn<>("Count");
        maxCol = new TableColumn<>("Max");
        minCol = new TableColumn<>("Min");
        meanCol = new TableColumn<>("μ");
        medianCol = new TableColumn<>("Median");
        standardDeviationCol = new TableColumn<>("σ");

        wholeDataSet = new DataSet(importData());
        summaryData = new SummaryData(wholeDataSet.allCrimeIndices(wholeDataSet.getDataPoints()));
        filterList = new ComboBox<>();
        filterField = new TextField();
        popUp = new Stage();
        mainScene = new Scene(wholeScreen);
        popUpScene = new Scene(singleTable, 410, 80);

        // Initialize table view columns
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        crimeCol.setCellValueFactory(new PropertyValueFactory<>("crimeIndex"));

        provinceColCopy.setCellValueFactory(new PropertyValueFactory<>("province"));
        yearColCopy.setCellValueFactory(new PropertyValueFactory<>("year"));
        crimeColCopy.setCellValueFactory(new PropertyValueFactory<>("crimeIndex"));

        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        maxCol.setCellValueFactory(new PropertyValueFactory<>("max"));
        minCol.setCellValueFactory(new PropertyValueFactory<>("min"));
        meanCol.setCellValueFactory(new PropertyValueFactory<>("mean"));
        medianCol.setCellValueFactory(new PropertyValueFactory<>("median"));
        standardDeviationCol.setCellValueFactory(new PropertyValueFactory<>("standardDeviation"));

        // Fix width of columns
        provinceCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.48));
        provinceCol.setResizable(false);
        yearCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.23));
        yearCol.setResizable(false);
        crimeCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.23));
        crimeCol.setResizable(false);

        provinceColCopy.prefWidthProperty().bind(singleTable.widthProperty().multiply(0.48));
        provinceColCopy.setResizable(false);
        yearColCopy.prefWidthProperty().bind(singleTable.widthProperty().multiply(0.23));
        yearColCopy.setResizable(false);
        crimeColCopy.prefWidthProperty().bind(singleTable.widthProperty().multiply(0.23));
        crimeColCopy.setResizable(false);

        // Add columns to respective table views
        tableView.getColumns().addAll(provinceCol, yearCol, crimeCol);
        singleTable.getColumns().addAll(provinceColCopy, yearColCopy, crimeColCopy);
        summaryTable.getColumns().addAll(countCol, maxCol, minCol, meanCol, medianCol, standardDeviationCol);

        // Add padding 
        vBox.setPadding(new Insets(10, 10, 10, 10));
        singleTable.setPadding(new Insets(10, 10, 10, 10));

        // Remove default additional column of table view
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        singleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        summaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Fix number of rows
        summaryTable.setFixedCellSize(25);
        summaryTable.prefHeightProperty().bind(Bindings.size(summaryTable.getItems()).multiply(summaryTable.getFixedCellSize()).add(40));

        // Set default items to table views
        tableView.setItems(wholeDataSet.getDataPoints());
        summaryTable.getItems().add(summaryData);

        // Place nodes into horizontal and vertical box
        hBox.getChildren().addAll(filterField, filterList);
        HBox.setHgrow(filterField, Priority.ALWAYS);
        HBox.setHgrow(filterList, Priority.ALWAYS);
        vBox.getChildren().addAll(hBox, tableView, summaryTable);
        wholeScreen.getChildren().addAll(createLineGraph(), vBox);

        // Configure popup stage
        popUp.setTitle("Data value");
        popUp.setScene(popUpScene);

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
                tableView.setItems(wholeDataSet.getDataPoints());
            }
            // If the filter is not empty, display a filtered list of items 
            else if (userSelection != null) {
                tableView.setItems(wholeDataSet.search(userSelection));
            }
            // Update summary data
            summaryTable.getItems().clear();
            summaryData = summaryData.newSummary(wholeDataSet.allCrimeIndices(wholeDataSet.search(userSelection)));
            summaryTable.getItems().add(summaryData);
        });

        // Detect if user double clicks a row
        tableView.setRowFactory( table -> {

            TableRow<DataPoint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // Add the DataPoint in the clicked row and display stage
                    singleTable.getItems().clear();
                    singleTable.getItems().add(row.getItem());
                    popUp.show();

                }
            });
            return row;

        });

        // Detect if the user typed into the filter
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // If the text field is empty, display all data points
            if (newValue == null || newValue.isEmpty()) {
                tableView.setItems(wholeDataSet.getDataPoints());
            }
            // If the text field is not empty, display the data points containing the search value 
            else {
                tableView.setItems(wholeDataSet.search(newValue));
            }
            // Update summary data
            summaryTable.getItems().clear();
            summaryData = summaryData.newSummary(wholeDataSet.allCrimeIndices(wholeDataSet.search(newValue)));
            summaryTable.getItems().add(summaryData);
        });
        
        // Set scene and show primary stage
        primaryStage.setScene(mainScene);
        primaryStage.show();

        Scene sc = new Scene(createPieGraph());
        Stage st = new Stage();
        st.setScene(sc);
        st.show();

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
        
        // Initialize variables
        xAxis = new NumberAxis("Year", 2010, 2019, 1);
        yAxis = new NumberAxis("Crime index", 0, 160, 15);
        lineChart = new LineChart(xAxis, yAxis);

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

        // Set names 
        lineChart.setTitle("Year vs Crime Index");
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
        lineChart.getData().addAll(bcSeries, abSeries, skSeries, mbSeries, onSeries, qcSeries, nbSeries, nsSeries, peSeries, nlSeries);

        // Return line chart
        return lineChart;

    }

    public Parent createPieGraph() {
        // Declare varaibles
        String provinces[] = { "British Columbia", "Alberata", "Saskatchewan", "Manitoba", "Ontario", "Quebec", "New Brunswick", "Nova Scoita", "Prince Edward Island", "Newfoundland and Labrador" };
        double count[];
        ObservableList<PieChart.Data> pieChartData;

        // Initialize variables
        pieChart = new PieChart();
        count = new double[10];
        
        // Get the total crime index for each of the provinces
        for (DataPoint data: wholeDataSet.getDataPoints()) {
            for (int i = 0; i < 10; ++i) {
                if (data.getProvince().equals(provinces[i])) {
                    count[i] += data.getCrimeIndex();
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
        pieChart.setTitle("Crime Index by Province");

        return pieChart;

    }

}