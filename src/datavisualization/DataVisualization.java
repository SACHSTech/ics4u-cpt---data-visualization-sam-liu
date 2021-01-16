package datavisualization;

import java.io.*;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class DataVisualization extends Application {

    // Instance variables
    private VBox vBox = new VBox(10);

    private TableView<DataPoint> tableView;
    private TableColumn<DataPoint, String> provinceCol;
    private TableColumn<DataPoint, Integer> yearCol;
    private TableColumn<DataPoint, Double> crimeCol;
    private DataSet wholeDataSet;

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