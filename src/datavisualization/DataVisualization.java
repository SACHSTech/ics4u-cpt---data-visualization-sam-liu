package datavisualization;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class DataVisualization extends Application {

    // Instance variables
    private ScrollPane scrollPane;
    private ArrayList<DataPoint> dataPoints;
    private GridPane grid;

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Crime index over time");

        // Initialize scroll pane
        scrollPane = new ScrollPane();

        // Obtain data from file
        dataPoints = new ArrayList<DataPoint>();
        importData(dataPoints);

        // Initialize grid
        grid = new GridPane();
        scrollPane.setContent(grid);

        // Constraints
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);
        
        for (int i = 0; i < dataPoints.size(); ++i) {
            DataPoint data = dataPoints.get(i);

            // Province label
            Label province = new Label(data.getProvince());
            grid.add(province, 0, i);

            // Year label
            Label year = new Label(String.valueOf(data.getYear()));
            grid.add(year, 1, i);

            // Crime index label
            Label crimeIndex = new Label(String.valueOf(data.getYear()));
            grid.add(crimeIndex, 2, i);
        }
        
        // Create and set scene
        Scene scene = new Scene(scrollPane, 600, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private static void importData(ArrayList<DataPoint> dataPoints) throws IOException {
        
        BufferedReader file = new BufferedReader(new FileReader("data.csv"));

        String strLine;
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
            dataPoints.add(new DataPoint(split[1], Integer.parseInt(split[0]), Double.parseDouble(split[2])));
        }

        file.close();

    }

}