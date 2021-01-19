package datavisualization;

/**
 * DataPoint class file
 * 
 * @author S. Liu
 */
public class DataPoint {

    // Instance variables
    private String province;
    private int year;
    private double crimeIndex;

    /**
     * Constructor - Creates a new DataPoint instance
     * 
     * @param province - The province
     * @param year - The year
     * @param crimeIndex - The province's crime index at the given year
     */
    DataPoint(String province, int year, double crimeIndex) {
        this.province = province;
        this.year = year;
        this.crimeIndex  = crimeIndex;
    }

    /**
     * Getter method for province
     * 
     * @return The province
     */
    public String getProvince() {
        return province;
    }


    /**
     * Getter method for year
     * 
     * @return The year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter method for crimeIndex
     * 
     * @return The crime index
     */
    public double getCrimeIndex() {
        return crimeIndex;
    }

}