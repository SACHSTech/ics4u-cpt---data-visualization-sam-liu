package datavisualization;

import java.util.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class DataSet {
    
    // Instance variables
    private ObservableList<DataPoint> dataPoints;
    private int size;

    // Constructor
    public DataSet(ObservableList<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        this.size = dataPoints.size();
    }

    public ObservableList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public int getSize() {
        return size;
    }

    public void addData(DataPoint data) {
        dataPoints.add(data);
    }

    public ObservableList<DataPoint> search(String key) {
        key = key.toLowerCase();
        ObservableList<DataPoint> tempList;

        tempList = FXCollections.observableArrayList();

        for (DataPoint data: dataPoints) {
            if (data.getProvince().toLowerCase().contains(key)) {
                tempList.add(data);
            }
            else if (String.valueOf(data.getYear()).toLowerCase().contains(key)) {
                tempList.add(data);
            }
            else if (String.valueOf(data.getCrimeIndex()).toLowerCase().contains(key)) {
                tempList.add(data);
            }
        }

        return tempList;
    }

}
