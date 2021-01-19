package datavisualization;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


/**
 * SummaryData class file
 * 
 * @author S. Liu
 */
public class SummaryData {
    
    // Instance variables
    private int count;
    private double max;
    private double min;
    private double mean;
    private double median;
    private double standardDeviation;
    private ObservableList<Double> crimeIndices;

    /**
     * Constructor - Creates a new SummaryData instance
     * 
     * @param crimeIndices - A list of all the crime indices
     */
    public SummaryData(ObservableList<Double> crimeIndices) {
        this.crimeIndices = crimeIndices;
        computeSummary();
    }

    /**
     * Computes all the summary values
     */
    private void computeSummary() {
        // If the list is empty, set all values to 0
        if (crimeIndices.size() == 0) {
            count = 0;
            max = 0;
            min = 0;
            mean = 0;
            median = 0;
            standardDeviation = 0;
            return;
        }

        // Declare variables
        double sum;

        // Initialize variables
        sum = 0;
        standardDeviation = 0;

        // The count variables is the size of the crimeIndices list
        count = crimeIndices.size();

        // Sort the list
        sort();
        
        // Get the maximum and minimum values at the end and beginning of the list respectively
        max = crimeIndices.get(count - 1);
        min = crimeIndices.get(0);

        // Get the total crime index
        for (int i = 0; i < count; ++i) {
            sum += crimeIndices.get(i);
        }

        // Compute the mean
        mean = Math.round(sum / count * 100.0) / 100.0;

        // If there is an even number of indices
        if (count % 2 == 0) {
            median = crimeIndices.get(count / 2) + crimeIndices.get(count / 2 - 1);
            median /= 2;
            median = Math.round(median * 100.0) / 100.0;
        }
        // If there is an odd number of indices
        else {
            median = crimeIndices.get((count - 1) / 2);
        }

        // Compute the standard deviation
        for (int i = 0; i < count; ++i) {
            standardDeviation += Math.pow(crimeIndices.get(i) - mean, 2);
        }
        standardDeviation /= count;
        standardDeviation = Math.sqrt(standardDeviation);
        standardDeviation = Math.round(standardDeviation * 100.0) / 100.0;
    }

    /**
     * Getter method for count
     * 
     * @return The size of the crimeIndices list
     */
    public int getCount() {
        return count;
    }

    /**
     * Getter method for max
     * 
     * @return The maximum crime index
     */
    public double getMax() {
        return max;
    }
    
    /**
     * Getter method for min
     * 
     * @return The minimum crime index
     */
    public double getMin() {
        return min;
    }

    /**
     * Getter method for mean
     * 
     * @return The mean
     */
    public double getMean() {
        return mean;
    }

    /**
     * Getter method for the median
     * 
     * @return The median
     */
    public double getMedian() {
        return median;
    }

    /** 
     * Getter method for standard deviation
     * 
     * @return The standard deviation
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * Sorts the list of crime indices
     */
    private void sort() {
        // Declare variables
        ObservableList<Double> tempList;

        // Initialize variables
        tempList = FXCollections.observableArrayList();

        // Copy over the crimeIndices list
        for (Double value: crimeIndices) {
            tempList.add(value);
        }
        
        // Call the merge sort
        mergeSort(tempList, 0, count - 1);
    }

    /**
     * Recursion sort function helper
     * 
     * @param temporaryList - The temporary list
     * @param from - Starting index of sort
     * @param to - End index of sort
     */
    private void mergeSort(ObservableList<Double> temporaryList, int from, int to) {
        // If the size of the array is at least 1
        if (to - from >= 1) {
            int mid = (from + to) / 2;

            // Recursive call
            mergeSort(temporaryList, from, mid);
            mergeSort(temporaryList, mid + 1, to);

            // Call the merge function
            merge(temporaryList, from, mid, to);
        }
    }

    /**
     * Merge sorts within the inputted lower and upper bounds
     * 
     * @param temporaryList - The temporary list
     * @param from - The starting sorting index
     * @param mid - The middle sorting index
     * @param to - The final sorting index
     */
    private void merge(ObservableList<Double> temporaryList, int from, int mid, int to) {
        // Declare variables
        int p1;
        int p2;
        int p3;

        // Initialize variables
        p1 = from;
        p2 = mid + 1;
        p3 = from;

        // While the first pointer is less than or equal to mid and the second pointer is less than or equal to to, add the lowest value
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

        // Add the remaining values in [from, mid]
        while (p1 <= mid) {
            temporaryList.set(p3, crimeIndices.get(p1));
            ++p1;
            ++p3;
        }

        // Add the remaining values in (from, to]
        while (p2 <= to) {
            temporaryList.set(p3, crimeIndices.get(p2));
            ++p2;
            ++p3;
        }

        // Copy the sorted part of the temporary list to the crime index list
        for (int i = from; i <= to; ++i) {
            crimeIndices.set(i, temporaryList.get(i));
        }
    }

}
