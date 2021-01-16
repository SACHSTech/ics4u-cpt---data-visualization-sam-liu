package datavisualization;

import java.io.*;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DataVisualization extends Application {

    // Instance variables
    private VBox vBox = new VBox(10);

    private TableView<DataPoint> tableView;
    private TableView<DataPoint> singleTable;
    private TableColumn<DataPoint, String> provinceCol;
    private TableColumn<DataPoint, Integer> yearCol;
    private TableColumn<DataPoint, Double> crimeCol;
    private TableColumn<DataPoint, String> provinceColCopy;
    private TableColumn<DataPoint, Integer> yearColCopy;
    private TableColumn<DataPoint, Double> crimeColCopy;
    private DataSet wholeDataSet;
    private Stage popUp;

	private TextField filterField;

    public static void main(String args[]) {
        launch(args);
    }

    // TECHNICALLY, ALL VARIABLES SHOULD BE DECLARED AT THE TOP OF THE METHOD

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Crime index over time");

        // Initialize variables
        filterField = new TextField();

        provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        crimeCol = new TableColumn<>("Crime index");
        crimeCol.setCellValueFactory(new PropertyValueFactory<>("crimeIndex"));

        // Add columns to table view
        tableView = new TableView<>();
        tableView.getColumns().addAll(provinceCol, yearCol, crimeCol);

        // Initially add all data points into the table view
        wholeDataSet = new DataSet(importData());
        tableView.setItems(wholeDataSet.getDataPoints());
        
        /*
        tableView.setOnSort(event -> {
            System.out.println("hi");
        });
        */

        tableView.setRowFactory( tv -> {

            TableRow<DataPoint> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {

                    // Create pop up window
                    popUp = new Stage();
                    popUp.setTitle("Data value");

                    provinceColCopy = new TableColumn<>("Province");
                    provinceColCopy.setCellValueFactory(new PropertyValueFactory<>("province"));
                    yearColCopy = new TableColumn<>("Year");
                    yearColCopy.setCellValueFactory(new PropertyValueFactory<>("year"));
                    crimeColCopy = new TableColumn<>("crimeIndex");
                    crimeColCopy.setCellValueFactory(new PropertyValueFactory<>("crimeIndex"));

                    singleTable = new TableView<>();
                    singleTable.getColumns().addAll(provinceColCopy, yearColCopy, crimeColCopy);
                    singleTable.getItems().add(row.getItem());

                    Scene singleScene = new Scene(singleTable, 400, 300);
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
        
        // Add text field and table view into a vertical box
        vBox.getChildren().addAll(filterField, tableView);

        // Create and set scene
        Scene scene = new Scene(vBox, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static ObservableList<DataPoint> importData() throws IOException {
        
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