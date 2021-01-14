package datavisualization;

class DataPoint {

    String province;
    int year;
    double crimeIndex;

    DataPoint(String province, int year, double crimeIndex) {
        this.province = province;
        this.year = year;
        this.crimeIndex  = crimeIndex;
    }

    public String toString() {
        return "Province: " + province + ", Year: " + year + ", Crime index: " + crimeIndex;
    }

    public String getProvince() {
        return province;
    }

    public int getYear() {
        return year;
    }

    public double getCrimeIndex() {
        return crimeIndex;
    }

}