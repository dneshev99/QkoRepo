package com.elsys.refpro.refprowatch.models;

import android.widget.TextView;

public class Timer {

    private int mainTimerMinutes, extraTimerMinutes;
    private int mainTimerSeconds, extraTimerSeconds;

    private TextView mainTimer, extraTimer;
    private String setTime;

    private boolean isExtraTimerStarted;

    public Timer() {

        this.mainTimerMinutes = 0;
        this.extraTimerMinutes = 0;
        this.extraTimerSeconds = 0;
        this.mainTimerSeconds = 0;
        this.setTime = "00:00";
        this.isExtraTimerStarted = false;
    }

    public void MainTimerFormat() {

        if (mainTimerMinutes == 0) {
            if (mainTimerSeconds < 10)
                setTime = "00:0" + mainTimerSeconds;
            else
                setTime = "00:" + mainTimerSeconds;
        } else if (mainTimerMinutes < 10) {
            if (mainTimerSeconds < 10)
                setTime = "0" + mainTimerMinutes + ":0" + mainTimerSeconds;
            else
                setTime = "0" + mainTimerMinutes + ":" + mainTimerSeconds;
        } else {
            if (mainTimerSeconds < 10)
                setTime = mainTimerMinutes + ":0" + mainTimerSeconds;
            else
                setTime = mainTimerMinutes + ":" + mainTimerSeconds;
        }

        mainTimerSeconds++;

        if (mainTimerSeconds >= 60) {

            mainTimerMinutes++;
            mainTimerSeconds = 0;
        }
    } // GETS minutes and seconds of all timers and change them to format HH:mm

    public void ExtraTimerFormat() {

        if (extraTimerMinutes == 0) {
            if (extraTimerSeconds < 10)
                setTime = "00:0" + extraTimerSeconds;
            else
                setTime = "00:" + extraTimerSeconds;
        } else if (extraTimerMinutes < 10) {
            if (extraTimerSeconds < 10)
                setTime = "0" + extraTimerMinutes + ":0" + extraTimerSeconds;
            else
                setTime = "0" + extraTimerMinutes + ":" + extraTimerSeconds;
        } else {
            if (extraTimerSeconds < 10)
                setTime = extraTimerMinutes + ":0" + extraTimerSeconds;
            else
                setTime = extraTimerMinutes + ":" + extraTimerSeconds;
        }

        extraTimerSeconds++;

        if (extraTimerSeconds >= 60) {

            extraTimerMinutes++;
            extraTimerSeconds = 0;
        }
    } // GETS minutes and seconds of all timers and change them to format HH:mm

    public TextView getMainTimer() {
        return mainTimer;
    }

    public void setMainTimerText() {

        mainTimer.setText(setTime);
    }

    public void setMainTimer(TextView mainTimer) {
        this.mainTimer = mainTimer;
    }

    public TextView getExtraTimer() {
        return extraTimer;
    }

    public void setExtraTimerText() {

        extraTimer.setText(setTime);
    }

    public void setExtraTimer(TextView extraTimer) {
        this.extraTimer = extraTimer;
    }

    public int getMainTimerMinutes() {
        return mainTimerMinutes;
    }

    public void setMainTimerMinutes(int mainTimerMinutes) {
        this.mainTimerMinutes = mainTimerMinutes;
    }

    public int getExtraTimerMinutes() {
        return extraTimerMinutes;
    }

    public void setExtraTimerMinutes(int extraTimerMinutes) {
        this.extraTimerMinutes = extraTimerMinutes;
    }

    public int getMainTimerSeconds() {
        return mainTimerSeconds;
    }

    public void setMainTimerSeconds(int mainTimerSeconds) {
        this.mainTimerSeconds = mainTimerSeconds;
    }

    public int getExtraTimerSeconds() {
        return extraTimerSeconds;
    }

    public void setExtraTimerSeconds(int extraTimerSeconds) {
        this.extraTimerSeconds = extraTimerSeconds;
    }

    public String getSetTime() {
        return setTime;
    }

    public boolean isExtraTimerStarted() {
        return isExtraTimerStarted;
    }

    public void setExtraTimerStarted(boolean extraTimerStarted) {
        isExtraTimerStarted = extraTimerStarted;
    }
}
