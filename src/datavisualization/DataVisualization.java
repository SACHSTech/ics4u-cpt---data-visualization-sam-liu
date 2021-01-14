package datavisualization;

import java.io.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class DataVisualization extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Crime index over time");

        BufferedReader file = new BufferedReader(new FileReader("data.csv"));
        String strLine;
        strLine = "";
        while (strLine != null) {
            strLine = file.readLine();
            System.out.println(strLine);
        }
        file.close();

        primaryStage.show();
    }

}