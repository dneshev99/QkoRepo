package org.elsys.ip.rest.model;

public class Lens {
    private String manufacturer;
    private String model;
    private String focalLength;
    private double maximumAperture;
    private double minimumAperture;
    private int filerSize;

    public Lens(String manufacturer, String model, String focalLength, double maximumAperture, double minimumAperture, int filerSize) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.focalLength = focalLength;
        this.maximumAperture = maximumAperture;
        this.minimumAperture = minimumAperture;
        this.filerSize = filerSize;
    }

    public Lens() {
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public double getMaximumAperture() {
        return maximumAperture;
    }

    public void setMaximumAperture(double maximumAperture) {
        this.maximumAperture = maximumAperture;
    }

    public double getMinimumAperture() {
        return minimumAperture;
    }

    public void setMinimumAperture(double minimumAperture) {
        this.minimumAperture = minimumAperture;
    }

    public int getFilerSize() {
        return filerSize;
    }

    public void setFilerSize(int filerSize) {
        this.filerSize = filerSize;
    }
}
