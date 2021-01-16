package datavisualization;

import java.util.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class SummaryData {
    
    private int count;
    private double max;
    private double min;
    private double mean;
    private double median;
    private double standardDeviation;
    private ObservableList<Double> crimeIndices;

    public SummaryData(ObservableList<Double> crimeIndices) {
        this.crimeIndices = crimeIndices;
        calculate();
    }

    public int getCount() {
        return count;
    }

    public double getMax() {
        return max;
    }
    
    public double getMin() {
        return min;
    }

    public double getMean() {
        return mean;
    }

    public double getMedian() {
        return median;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    private void calculate() {
        count = crimeIndices.size();
        sort();
        max = crimeIndices.get(count - 1);
        min = crimeIndices.get(0);

        double sum;
        sum = 0;
        for (int i = 0; i < count; ++i) {
            sum += crimeIndices.get(i);
        }
        mean = Math.round(sum / count * 100.0) / 100.0;

        if (count % 2 == 0) {
            median = crimeIndices.get(count / 2) + crimeIndices.get(count / 2 - 1);
            median /= 2;
            median = Math.round(median * 100.0) / 100.0;
        }
        else {
            median = crimeIndices.get((count - 1) / 2);
        }

        double tmp;
        tmp = 0;
        for (int i = 0; i < count; ++i) {
            tmp += Math.pow(crimeIndices.get(i) - mean, 2);
        }
        tmp /= count;
        tmp = Math.sqrt(tmp);
        standardDeviation = Math.round(tmp * 100.0) / 100.0;
    }

    private void sort() {
        ObservableList<Double> tempList;

        tempList = FXCollections.observableArrayList();

        for (Double value: crimeIndices) {
            tempList.add(value);
        }
        mergeSort(tempList, 0, count - 1);
    }

    private void mergeSort(ObservableList<Double> temporaryList, int from, int to) {
        if (to - from >= 1) {
            int mid = (from + to) / 2;
            // Recursive call
            mergeSort(temporaryList, from, mid);
            mergeSort(temporaryList, mid + 1, to);

            merge(temporaryList, from, mid, to);
        }
    }

    private void merge(ObservableList<Double> temporaryList, int from, int mid, int to) {
        int p1;
        int p2;
        int p3;

        p1 = from;
        p2 = mid + 1;
        p3 = from;

        while (p1 <= mid && p2 <= to) {
            if (crimeIndices.get(p1) < crimeIndices.get(p2)) {
                temporaryList.set(p3, crimeIndices.get(p1));
                ++p1;
            }
            else {
                temporaryList.set(p3, crimeIndices.get(p2));
                ++p2;
            }
            ++p3;
        }

        while (p1 <= mid) {
            temporaryList.set(p3, crimeIndices.get(p1));
            ++p1;
            ++p3;
        }

        while (p2 <= to) {
            temporaryList.set(p3, crimeIndices.get(p2));
            ++p2;
            ++p3;
        }

        for (int i = from; i <= to; ++i) {
            crimeIndices.set(i, temporaryList.get(i));
        }
    }

}
