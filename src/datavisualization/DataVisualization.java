package datavisualization;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class DataVisualization extends Application {

    // Instance variables
    private ScrollPane scrollPane;
    private GridPane grid;
    private DataSet wholeDataSet;

    public static void main(String args[]) {
        launch(args);
    }

    // TECHNICALLY, ALL VARIABLES SHOULD BE DECLARED AT THE TOP OF THE METHOD

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Crime index over time");

        // Initialize variables
        scrollPane = new ScrollPane();
        grid = new GridPane();
        scrollPane.setContent(grid);

        wholeDataSet = new DataSet(importData());

        // Constraints
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);
        
        for (int i = 0; i < wholeDataSet.getSize(); ++i) {
            DataPoint data = wholeDataSet.getDataPoints().get(i);

            // Province label
            Label province = new Label(data.getProvince());
            grid.add(province, 0, i);

            // Year label
            Label year = new Label(String.valueOf(data.getYear()));
            grid.add(year, 1, i);

            // Crime index label
            Label crimeIndex = new Label(String.valueOf(data.getCrimeIndex()));
            grid.add(crimeIndex, 2, i);
        }

        Button sortCrimeButton = new Button("Sort by crime index");
        sortCrimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("test");
                wholeDataSet.sort("year", false);
            }
        });

        grid.add(sortCrimeButton, 4, 0);
        
        // Create and set scene
        Scene scene = new Scene(scrollPane, 600, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private static ArrayList<DataPoint> importData() throws IOException {
        
        BufferedReader file = new BufferedReader(new FileReader("data.csv"));

        ArrayList<DataPoint> temporaryList;
        String strLine;

        temporaryList = new ArrayList<DataPoint>();
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