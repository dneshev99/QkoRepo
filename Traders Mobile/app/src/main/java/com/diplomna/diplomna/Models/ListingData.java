package com.diplomna.diplomna.Models;

public class ListingData {
    private String productName;
    private String dealerName;
    private String pricePerUnit;
    private String units;

    private byte[] image;

    public ListingData(String productName, String dealerName, String pricePerUnit, String units, byte[] image) {
        this.productName = productName;
        this.dealerName = dealerName;
        this.pricePerUnit = pricePerUnit;
        this.units = units;
        this.image = image;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(String pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}