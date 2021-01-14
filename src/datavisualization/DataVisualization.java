package datavisualization;

import javafx.application.Application;
import javafx.stage.Stage;

public class DataVisualization extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Crime index over time");
        primaryStage.show();
    }

}