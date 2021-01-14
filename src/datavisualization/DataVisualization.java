package datavisualization;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class DataVisualization extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Crime index over time");

        ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

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
                System.out.println(split[i]);
            }
            System.out.println(split.length);
            dataPoints.add(new DataPoint(split[1], Integer.parseInt(split[0]), Double.parseDouble(split[2])));
        }
        file.close();
        
        for (DataPoint d: dataPoints) {
            System.out.println(d);
        }

        primaryStage.show();
    }

}