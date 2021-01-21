package datavisualization;

import java.io.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The main file
 * @author S. Liu
 * 
 */
public class DataVisualization extends Application {

    // Declare instance variables
    private VBox databaseVBox;
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
    private TabPane tabPane;
    private Tab lineChartTab;
    private Tab pieChartTab;
    private Tooltip tooltip;

    private SummaryData summaryData;
    private ComboBox<String> filterList;
    private TextField filterField;
    private String optionFilter;
    private String textFilter;

    private Stage popUpStage;
    private Scene mainScene;
    private Scene popUpScene;

    /**
     * The main method
     */
    public static void main(String args[]) {
        launch(args);
    }

    /**
     * The method that starts the program
     * 
     * @param primaryStage - The primary stage
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        // Initialize variables
        databaseVBox = new VBox(10);
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
        tabPane = new TabPane();
        lineChartTab = new Tab("Line chart", lineChart);
        pieChartTab = new Tab("Pie chart", pieChart);

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

        // Add columns to respective TableViews
        databaseTable.getColumns().addAll(provinceCol, yearCol, crimeCol);
        datapointTable.getColumns().addAll(provinceCol2, yearCol2, crimeCol2);
        summaryTable.getColumns().addAll(countCol, maxCol, minCol, meanCol, medianCol, standardDeviationCol);

        // Add padding to screens
        screenHBox.setPadding(new Insets(15, 15, 15, 15));
        datapointTable.setPadding(new Insets(10, 10, 10, 10));

        // Remove the default additional column from the TableViews
        databaseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        datapointTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        summaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Fix the number of rows in the summaryTable
        summaryTable.setFixedCellSize(25);
        summaryTable.prefHeightProperty().bind(Bindings.size(summaryTable.getItems()).multiply(summaryTable.getFixedCellSize()).add(40));

        // Initially, fill the databaseTable and summaryTable with all data points
        databaseTable.setItems(wholeDataSet.getDataPoints());
        summaryTable.getItems().add(summaryData);

        // Configure tabPane
        tabPane.getTabs().addAll(lineChartTab, pieChartTab);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        // Place nodes into horizontal and vertical boxes
        filterHBox.getChildren().addAll(filterField, filterList);
        databaseVBox.getChildren().addAll(filterHBox, databaseTable, summaryTable);
        screenHBox.getChildren().addAll(tabPane, databaseVBox);

        // Make sure the filter field and filter text take up the entire width of the screen
        HBox.setHgrow(filterField, Priority.ALWAYS);
        HBox.setHgrow(filterList, Priority.ALWAYS);

        // Configure popUpStage
        popUpStage.setTitle("Data Value");
        popUpStage.setScene(popUpScene);

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
            // Store filter
            optionFilter = filterList.getValue();

            // Update the dataset
            updateDataset();
        });

        // Detect if user double clicked on a row
        databaseTable.setOnMouseClicked( event -> {
            // Check if user clicked on a non-empty row twice
            if (event.getClickCount() == 2 && !databaseTable.getSelectionModel().isEmpty()) {

                // Add row item to the datapointTable and display the popUpStage
                datapointTable.getItems().clear();
                datapointTable.getItems().add(databaseTable.getSelectionModel().getSelectedItem());
                popUpStage.show();

            }
        });
        
        // Detect if the user typed into the filter field
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Store new value
            textFilter = newValue;

            // Update the dataset
            updateDataset();
        });

        // Configure primary stage
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Crime Index Over Time");
        primaryStage.show();

    }

    /**
     * Imports data from the csv file 
     * 
     * @return An observable list containing all the data points
     */
    private ObservableList<DataPoint> importData() throws IOException {
        
        // Declare variables
        BufferedReader file;
        ObservableList<DataPoint> dataList;
        String strLine;
        String[] split;

        // Initialize variables
        file = new BufferedReader(new FileReader("data.csv"));
        dataList = FXCollections.observableArrayList();

        // Read the heading line of the csv file
        strLine = file.readLine();

        // Continue reading from the file until the end of the file is reached
        while (strLine != null) {
            strLine = file.readLine();
            if (strLine == null || strLine.isEmpty()) {
                break;
            }

            // Split the line by commas, and add the substrings into a String array
            split = strLine.split(",");
            for (int i = 0; i < split.length; ++i) {
                // Remove all quotations marks from the substrings
                split[i] = split[i].replace("\"", "");
            }

            // Create a new DataPoint object and add it to the observable list
            dataList.add(new DataPoint(split[1], Integer.parseInt(split[0]), Double.parseDouble(split[2])));
        }

        // Close the file
        file.close();

        // Return the list of DataPoints
        return dataList;

    }

    /**
     * Creates a line graph that includes all datapoints
     * 
     * @return The line graph as a Parent object
     */
    private Parent createLineGraph() {
        
        // Declare variables
        NumberAxis xAxis;
        NumberAxis yAxis;
        LineChart<Integer, Double> tempLineChart;

        // Initialize variables
        xAxis = new NumberAxis("Year", 2010, 2019, 1);
        yAxis = new NumberAxis("Crime Index", 30, 165, 15);
        tempLineChart = new LineChart(xAxis, yAxis);
        tempLineChart.setTitle("Year vs Crime Index");

        // Name the series
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

        // Add data points into their appropriate series depending on their province
        for (DataPoint data : wholeDataSet.getDataPoints()) {
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
                default :
                    nlSeries.getData().add(new XYChart.Data<Integer, Double>(data.getYear(), data.getCrimeIndex()));
                    break;
            }
        }

        // Add all series into the LineChart
        tempLineChart.getData().addAll(bcSeries, abSeries, skSeries, mbSeries, onSeries, qcSeries, nbSeries, nsSeries, peSeries, nlSeries);

        // Add tooltips to the data points
        for (XYChart.Series<Integer, Double> series : tempLineChart.getData()) {
            for (XYChart.Data<Integer, Double> data : series.getData()) {
                // Display the province, year, and crime index of the data point
                tooltip = new Tooltip("Province: " + series.getName() + ", Year: " + data.getXValue() + ", Crime index: " + data.getYValue());
                
                Tooltip.install(data.getNode(), tooltip);
                bindTooltip(data.getNode(), tooltip);
            }
        }

        // Return line chart
        return tempLineChart;

    }

    /**
     * Create a pie chart that includes all data points
     * 
     * @return The pie chart in the form of a Parent object
     */
    private Parent createPieChart() {
        // Declare variables
        PieChart tempPieChart;
        String provinces[] = { "British Columbia", "Alberta", "Saskatchewan", "Manitoba", "Ontario", "Quebec", "New Brunswick", "Nova Scotia", "Prince Edward Island", "Newfoundland and Labrador" };
        double count[];
        double total;
        ObservableList<PieChart.Data> pieChartData;
        int percentage;

        // Initialize variables
        count = new double[10];
        total = 0;
        
        // Get the total crime index for each province
        for (DataPoint data : wholeDataSet.getDataPoints()) {
            for (int i = 0; i < 10; ++i) {
                // Increment both the province and total count
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

        // Create the pie chart
        tempPieChart = new PieChart(pieChartData);
        tempPieChart.setTitle("Total Crime Index by Province");

        // Add tooltip to all data points
        for (PieChart.Data data : pieChartData) {
            percentage = (int)(100.0 * data.getPieValue() / total);

            // Display the province, total crime index, and relative crime index for each data point
            tooltip = new Tooltip("Province: " + data.getName() + ", Crime index: " + Math.round(100.0 * data.getPieValue()) / 100.0 + ", " + percentage + "%");
            
            Tooltip.install(data.getNode(), tooltip);
            bindTooltip(data.getNode(), tooltip);
        }

        // Return the pie chart
        return tempPieChart;

    }
    
    /**
     * A function that updates the data displayed in the table depending on the active filters
     */
    private void updateDataset() {

        // Declare variables
        DataSet newSet;

        // Initialize variables
        newSet = new DataSet(FXCollections.observableArrayList(wholeDataSet.getDataPoints()));

        // If a filter has been selected, update the data points
        if (optionFilter != null && !optionFilter.isEmpty()) {
            newSet.setDataPoints(newSet.search(optionFilter));
        }

        if (textFilter != null && !textFilter.isEmpty()) {
            newSet.setDataPoints(newSet.search(textFilter));
        }

        // Set the new list of data points
        databaseTable.setItems(newSet.getDataPoints());

        // Update the summary table
        summaryTable.getItems().clear();
        summaryData = new SummaryData(wholeDataSet.allCrimeIndices(newSet.getDataPoints()));
        summaryTable.getItems().add(summaryData);

    }

    /**
     * Allows Tooltip to appear instantly, instead of the built in delay
     * 
     * @param node - The node that the Tooltip is binded to
     * @param tooltip - The Tooltip variable
     */
    private static void bindTooltip(Node node, Tooltip tooltip) {
        // If the mouse moves on the node
        node.setOnMouseMoved(new EventHandler<MouseEvent>() {

            /**
             * Instantly show the Tooltip
             * 
             * @param event - The event
             */
           @Override  
           public void handle(MouseEvent event) {
              tooltip.show(node, event.getScreenX(), event.getScreenY() + 10);
           }
        });  
        
        // If the mouse leaves the node
        node.setOnMouseExited(new EventHandler<MouseEvent>(){

            /**
             * Hide the tooltip
             * 
             * @param event - The event
             */
           @Override
           public void handle(MouseEvent event){
              tooltip.hide();
           }
        });
    }

}
