package datavisualization;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


/**
 * DataSet class file
 * 
 * @author S. Liu
 */
public class DataSet {
    
    // Instance variables
    private ObservableList<DataPoint> dataPoints;
    private int size;

    /**
     * Constructor - Creates a new DataSet instance
     * 
     * @param dataPoints - An observable list of DataPoints
     */
    public DataSet(ObservableList<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        this.size = dataPoints.size();
    }

    /**
     * Getter method for dataPoints
     * 
     * @return The datapoints list
     */
    public ObservableList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    /**
     * Setter method for dataPoints
     * 
     * @param newList - The new list of datapoints that will be set
     */
    public void setDataPoints(ObservableList<DataPoint> newList) {
        dataPoints = newList;
    }

    /**
     * Getter method for size
     * 
     * @return The size of the dataPoints list
     */
    public int getSize() {
        return size;
    }

    /**
     * Add a new DataPoint to the dataPoints list
     * 
     * @param data - The new DataPoint
     */
    public void addData(DataPoint data) {
        dataPoints.add(data);
    }

    /**
     * Gets a list of all the crime indices in a dataPoints list
     * 
     * @param dataValues - The list of dataPoints
     * @return An observable list containing all the crime indices
     */
    public static ObservableList<Double> allCrimeIndices(ObservableList<DataPoint> dataValues) {
        // Declare variables
        ObservableList<Double> indexList;

        // Initialize variables
        indexList = FXCollections.observableArrayList();

        // Add the crime index of each dataPoint
        for (DataPoint data: dataValues) {
            indexList.add(data.getCrimeIndex());
        }

        // Return the list
        return indexList;
    }

    /**
     * Searches the dataPoints list for dataPoints that contain the inputted key
     * 
     * @param key - The key to search for
     * @return An observable list of datapoints containing the key
     */
    public ObservableList<DataPoint> search(String key) {
        // Declare variables
        ObservableList<DataPoint> tempList;

        // Initialize variables
        tempList = FXCollections.observableArrayList();
        key = key.toLowerCase();

        // For each dataPoint, check if the province, year, or crime index contains the inputted key
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

        // Return the list
        return tempList;
    }

}
