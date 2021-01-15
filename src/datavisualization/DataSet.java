package datavisualization;

import java.util.*;

public class DataSet {
    
    private ArrayList<DataPoint> dataPoints;
    private int size;

    public DataSet(ArrayList<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        this.size = dataPoints.size();
    }

    public ArrayList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public int getSize() {
        return size;
    }

    public void addData(DataPoint data) {
        dataPoints.add(data);
    }

    /*
    private void print() {
        for (DataPoint data: dataPoints) {
            System.out.println(data);
        }
    }
    */

    public void sort(String sortBy, boolean reverse) {
        mergeSort(new ArrayList<DataPoint>(dataPoints), 0, size - 1, sortBy, reverse);
    }

    public DataSet search(String key) {
        ArrayList<DataPoint> tempList;
        DataSet searched;

        tempList = new ArrayList<DataPoint>();

        for (DataPoint data: dataPoints) {
            if (!isNumeric(key) && data.getProvince().contains(key)) {
                tempList.add(data);
            }
            if (isNumeric(key) && (data.getYear() == Double.parseDouble(key) || data.getCrimeIndex() == Double.parseDouble(key))) {
                tempList.add(data);
            }
        }

        searched = new DataSet(tempList);

        return searched;
    }

    private boolean isNumeric(String key) {
        try {
            Double.parseDouble(key);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
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
