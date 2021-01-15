package datavisualization;

import java.io.*;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
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

        // Create filter field
        filterField = new TextField();

        // Set properties for table columns
        provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        crimeCol = new TableColumn<>("Crime index");
        crimeCol.setCellValueFactory(new PropertyValueFactory<>("crimeIndex"));

        // Add columns to table view
        tableView = new TableView<>();
        tableView.getColumns().addAll(provinceCol, yearCol, crimeCol);

        wholeDataSet = new DataSet(importData());
        tableView.setItems(wholeDataSet.getDataPoints());

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tableView.setItems(wholeDataSet.getDataPoints());
            }
            else {
                tableView.setItems(wholeDataSet.search(newValue));
            }
		});

        Button sortCrimeButton = new Button("Sort by crime index");
        sortCrimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Sort the data set
                wholeDataSet.sort("year", false);
            }
        });
        
        vBox.getChildren().addAll(filterField, tableView);

        // Create and set scene
        Scene scene = new Scene(vBox, 600, 600);
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