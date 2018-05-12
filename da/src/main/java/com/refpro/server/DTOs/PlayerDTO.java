package com.refpro.server.DTOs;

import com.refpro.server.models.Player;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
public class PlayerDTO {

    private String team;

    @NotNull(message = "First name must not be null")
    private String firstName;
    @NotNull(message = "Last name must not be null")
    private String lastName;
    @NotNull(message = "Birthday must not be null")
    private String birthday;
    @DecimalMin(value="1",inclusive = true)
    private int shirtNumber;
    @NotNull(message = "Shirt name must not be null")
    private String shirtName;
    private String pictureId;
    private String playerId;

    public PlayerDTO(){}

    public PlayerDTO(Player player) {
        this.team = player.getTeam().getName();
        this.firstName = player.getFirstName();
        this.lastName = player.getLastName();
        this.birthday = player.getBirthday();
        this.shirtNumber = player.getShirtNumber();
        this.shirtName = player.getShirtName();
        this.pictureId = player.getPictureId();
        this.playerId = player.getId();
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public String getShirtName() {
        return shirtName;
    }

    public void setShirtName(String shirtName) {
        this.shirtName = shirtName;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "team='" + team + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", shirtNumber=" + shirtNumber +
                ", shirtName='" + shirtName + '\'' +
                '}';
    }
}
