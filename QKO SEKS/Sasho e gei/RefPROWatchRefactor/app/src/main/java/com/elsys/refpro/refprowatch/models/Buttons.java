package com.elsys.refpro.refprowatch.models;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class Buttons implements View.OnClickListener{

    public ImageButton startButton;
    public Button terminateButton, extraTimeButton, teamBackButton, settingsBackButton, goalButton, yellowCardButton, redCardButton, subButton, logButton, logBack, endHalf, undoButton;

    public ArrayList<Button> playerButtons = new ArrayList<>();
    public ArrayList<Button> subsButtons = new ArrayList<>();

    public Buttons(ImageButton startButton, Button teamBackButton, Button settingsBackButton, Button goalButton, Button yellowCardButton, Button redCardButton, Button subButton, Button logButton, Button logBack, Button endHalf, Button undoButton, Button extraTimeButton, Button terminateButton) {

        this.startButton = startButton;
        this.terminateButton = terminateButton;
        this.extraTimeButton = extraTimeButton;
        this.teamBackButton = teamBackButton;
        this.settingsBackButton = settingsBackButton;
        this.goalButton = goalButton;
        this.yellowCardButton = yellowCardButton;
        this.redCardButton = redCardButton;
        this.logButton = logButton;
        this.logBack = logBack;
        this.endHalf = endHalf;
        this.subButton = subButton;
        this.undoButton = undoButton;
    }

    public Buttons() {
    }

    @Override
    public void onClick(View v) {

    }

    public ArrayList<Button> getPlayerButtons() {
        return playerButtons;
    }

    public void setPlayerButtons(ArrayList<Button> playerButtons) {
        this.playerButtons = playerButtons;
    }

    public ArrayList<Button> getSubsButtons() {
        return subsButtons;
    }

    public void setSubsButtons(ArrayList<Button> subsButtons) {
        this.subsButtons = subsButtons;
    }
}
