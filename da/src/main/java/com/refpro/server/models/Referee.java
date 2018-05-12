package com.refpro.server.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Referee {
    @Id
    String id;

    @NotNull(message = "Full name must not be null")
    private String fullName;
    private int height;
    private int weight;
    @Indexed
    private int experience;
    private String birthday;
    private String pictureID;

    private List<Double> marks = new ArrayList<>();

    public Referee(String fullName, int height, int weight, int experience, String birthday, List<Double> marks) {
        this.fullName = fullName;
        this.height = height;
        this.weight = weight;
        this.experience = experience;
        this.birthday = birthday;
        this.marks = marks;
    }

    public Referee() {
    }

    public String getId() {
        return id;
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

    public List<Double> getMarks() {
        return marks;
    }

    public void setMarks(List<Double> marks) {
        this.marks = marks;
    }

    public void addMark(Double mark) {
        this.marks.add(mark);
    }

    public String getPictureID() {
        return pictureID;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
    }
}
