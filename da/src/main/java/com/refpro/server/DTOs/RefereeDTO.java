package com.refpro.server.DTOs;

public class RefereeDTO {
    private String fullName;
    private int height;
    private int weight;
    private int experience;
    private String birthday;
    private double avaregeMark;

    public RefereeDTO(String fullName, int height, int weight, int experience, String birthday, double avaregeMark) {
        this.fullName = fullName;
        this.height = height;
        this.weight = weight;
        this.experience = experience;
        this.birthday = birthday;
        this.avaregeMark = avaregeMark;
    }

    public RefereeDTO() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getAvaregeMark() {
        return avaregeMark;
    }

    public void setAvaregeMark(double avaregeMark) {
        this.avaregeMark = avaregeMark;
    }
}
