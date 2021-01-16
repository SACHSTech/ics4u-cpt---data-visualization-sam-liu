package datavisualization;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DataVisualization extends Application {

    // Instance variables
    private VBox vBox;
    private HBox hBox;

    private TableView<DataPoint> tableView;
    private TableView<DataPoint> singleTable;
    private TableView<SummaryData> summaryInformation;

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

    private DataSet wholeDataSet;
    private SummaryData summaryData;
    private Stage popUp;
    private ComboBox<String> filterList;
	private TextField filterField;

    public static void main(String args[]) {
        launch(args);
    }

    // TECHNICALLY, ALL VARIABLES SHOULD BE DECLARED AT THE TOP OF THE METHOD

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Crime index over time");

        // Initialize variables
        vBox = new VBox(10);
        hBox = new HBox(10);

        tableView = new TableView<>();
        singleTable = new TableView<>();
        summaryInformation = new TableView<>();

        provinceCol = new TableColumn<>("Province");
        yearCol = new TableColumn<>("Year");
        crimeCol = new TableColumn<>("Crime index");
        provinceColCopy = new TableColumn<>("Province");
        yearColCopy = new TableColumn<>("Year");
        crimeColCopy = new TableColumn<>("crimeIndex");
        countCol = new TableColumn<>("Count");
        maxCol = new TableColumn<>("Max");
        minCol = new TableColumn<>("Min");
        meanCol = new TableColumn<>("μ");
        medianCol = new TableColumn<>("Median");
        standardDeviationCol = new TableColumn<>("σ");

        wholeDataSet = new DataSet(importData());
        summaryData = new SummaryData(wholeDataSet.allCrimeIndices());
        popUp = new Stage();
        filterList = new ComboBox<>();
        filterField = new TextField();

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

        // Fix width of province, year, and crime columns in tableView
        provinceCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.48));
        provinceCol.setResizable(false);
        yearCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.23));
        yearCol.setResizable(false);
        crimeCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.23));
        crimeCol.setResizable(false);

        // Add columns to respective table views
        tableView.getColumns().addAll(provinceCol, yearCol, crimeCol);
        singleTable.getColumns().addAll(provinceColCopy, yearColCopy, crimeColCopy);
        summaryInformation.getColumns().addAll(countCol, maxCol, minCol, meanCol, medianCol, standardDeviationCol);

        // Remove default additional column of table view
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        singleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        summaryInformation.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Initially add all data points into the table view
        tableView.setItems(wholeDataSet.getDataPoints());

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
        
        filterList.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            String userSelection = filterList.getValue();
            if (userSelection == null || userSelection.isEmpty()) {
                tableView.setItems(wholeDataSet.getDataPoints());
            }
            if (userSelection != null) {
                tableView.setItems(wholeDataSet.search(userSelection));
            }
        });

        tableView.setRowFactory( tv -> {

            TableRow<DataPoint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {

                    // Create pop up window
                    popUp.setTitle("Data value");

                    singleTable.getItems().add(row.getItem());
                    

                    Scene singleScene = new Scene(singleTable, 400, 60);
                    popUp.setScene(singleScene);
                    popUp.show();

                }
            });
            return row;

        });

        // Check to see if user types
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            // If the text field is empty, display all data points
            if (newValue == null || newValue.isEmpty()) {
                tableView.setItems(wholeDataSet.getDataPoints());
            }
            // If the text field is not empty, display the data points containing the search value
            else {
                tableView.setItems(wholeDataSet.search(newValue));
            }
        });

        summaryInformation.getItems().add(summaryData);
        
        // Add text field and filter list into horizontal box
        hBox.getChildren().addAll(filterField, filterList);
        // Add text field and table view into a vertical box
        vBox.getChildren().addAll(hBox, tableView, summaryInformation);

        // Create and set scene
        Scene scene = new Scene(vBox, 400, 400);
        primaryStage.setScene(scene);
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

}