package org.elsys.ip.rest.model;

public class Display {
    private String type;
    private double size;
    private int dots;

    public Display(String type, double size, int dots) {
        this.type = type;
        this.size = size;
        this.dots = dots;
    }

    public Display() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }
}
