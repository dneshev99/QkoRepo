package org.elsys.ip.rest.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;

public class Camera {

    private long id;
    private String manufacturer;
    private String model;
    private double megapixels;
    private int minISO;
    private int maxISO;
    private String shootingSpeed;
    private String videoRecording;
    private int afPoints;
    private int crossTypeAFPoints;
    private Lens lens;
    private Display display;
    private int price;

    public Camera(long id,
                  String manufacturer,
                  String model,
                  double megapixels,
                  int minISO,
                  int maxISO,
                  String shootingSpeed,
                  String videoRecording,
                  int afPoints,
                  int crossTypeAFPoints,
                  Lens lens,
                  Display display,
                  int price) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.megapixels = megapixels;
        this.minISO = minISO;
        this.maxISO = maxISO;
        this.shootingSpeed = shootingSpeed;
        this.videoRecording = videoRecording;
        this.afPoints = afPoints;
        this.crossTypeAFPoints = crossTypeAFPoints;
        this.lens = lens;
        this.display = display;
        this.price = price;
    }

    public Camera() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public double getMegapixels() {
        return megapixels;
    }

    public void setMegapixels(double megapixels) {
        this.megapixels = megapixels;
    }

    public int getMinISO() {
        return minISO;
    }

    public void setMinISO(int minISO) {
        this.minISO = minISO;
    }

    public int getMaxISO() {
        return maxISO;
    }

    public void setMaxISO(int maxISO) {
        this.maxISO = maxISO;
    }

    public String getShootingSpeed() {
        return shootingSpeed;
    }

    public void setShootingSpeed(String shootingSpeed) {
        this.shootingSpeed = shootingSpeed;
    }

    public String getVideoRecording() {
        return videoRecording;
    }

    public void setVideoRecording(String videoRecording) {
        this.videoRecording = videoRecording;
    }

    public int getAfPoints() {
        return afPoints;
    }

    public void setAfPoints(int afPoints) {
        this.afPoints = afPoints;
    }

    public int getCrossTypeAFPoints() {
        return crossTypeAFPoints;
    }

    public void setCrossTypeAFPoints(int crossTypeAFPoints) {
        this.crossTypeAFPoints = crossTypeAFPoints;
    }

    public Lens getLens() {
        return lens;
    }

    public void setLens(Lens lens) {
        this.lens = lens;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
