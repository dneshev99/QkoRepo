package com.elsys.refpro.refprowatch;

/**
 * Created by user on 16.12.2017 г..
 */

public class WatchData {

    private boolean isStarted = false, smallT = false, isHome = true, vibrator = false;
    private int BigSeconds = 0, SmallSeconds = 0, BigMinutes = 0, SmallMinutes = 0;
    private int type = 0; // 0 GOAL, 1 SUB, 2 YELLOW, 3 RED
    private int undoType = 0; // 1 GOAL, 2 SUB. 3 YELLOW, 4 RED, 5 HALF
    private String setTime = "", log = "", logUndo = "";
    private int homeResult = 0, awayResult = 0;
    private String subName, PlayerToSub;
    private String lastClickedPlayer;
    private int half;

}
