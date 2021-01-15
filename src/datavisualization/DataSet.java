package datavisualization;

import java.util.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class DataSet {
    
    private ObservableList<DataPoint> dataPoints;
    private int size;

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

    public void sort(String sortBy, boolean reverse) {
        mergeSort(new ArrayList<DataPoint>(dataPoints), 0, size - 1, sortBy, reverse);
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

    private void mergeSort(ArrayList<DataPoint> temporaryList, int from, int to, String sortBy, boolean reverse) {
        if (to - from >= 1) {
            int mid = (from + to) / 2;
            // Recursive call
            mergeSort(temporaryList, from, mid, sortBy, reverse);
            mergeSort(temporaryList, mid + 1, to, sortBy, reverse);

            merge(temporaryList, from, mid, to, sortBy, reverse);
        }
    }

    private void merge(ArrayList<DataPoint> temporaryList, int from, int mid, int to, String sortBy, boolean reverse) {
        int p1;
        int p2;
        int p3;

        p1 = from;
        p2 = mid + 1;
        p3 = from;

        // Note that sorting by province, year, and crime index is the same function besides the if statement in the initial for loop
        // So, we merely need to create a boolean that varies for each category
        while (p1 <= mid && p2 <= to) {
            boolean isTrue;
            if (sortBy.equals("province")) {
                if (reverse) { // Sort by province from z -> a
                    isTrue = dataPoints.get(p1).getProvince().compareTo(dataPoints.get(p2).getProvince()) > 0;
                }
                else { // Sort by province from a -> z
                    isTrue = dataPoints.get(p1).getProvince().compareTo(dataPoints.get(p2).getProvince()) < 0;
                }
            }
            else if (sortBy.equals("year")) {
                if (reverse) { // Sort by year from greatest to least
                    isTrue = dataPoints.get(p1).getYear() > dataPoints.get(p2).getYear();
                }
                else { // Sort by year from least to greatest
                    isTrue = dataPoints.get(p1).getYear() < dataPoints.get(p2).getYear();
                }
            }
            else {
                if (reverse) { // Sort by crime index from greatest to least
                    isTrue = dataPoints.get(p1).getCrimeIndex() > dataPoints.get(p2).getCrimeIndex();
                }
                else { // Sort by crime index from least to greatest
                    isTrue = dataPoints.get(p1).getCrimeIndex() < dataPoints.get(p2).getCrimeIndex();
                }
            }

            if (isTrue) {
                temporaryList.set(p3, dataPoints.get(p1));
                ++p1;
            }
            else {
                temporaryList.set(p3, dataPoints.get(p2));
                ++p2;
            }
            ++p3;
        }

        while (p1 <= mid) {
            temporaryList.set(p3, dataPoints.get(p1));
            ++p1;
            ++p3;
        }

        while (p2 <= to) {
            temporaryList.set(p3, dataPoints.get(p2));
            ++p2;
            ++p3;
        }

        for (int i = from; i <= to; ++i) {
            dataPoints.set(i, temporaryList.get(i));
        }
    }

}
