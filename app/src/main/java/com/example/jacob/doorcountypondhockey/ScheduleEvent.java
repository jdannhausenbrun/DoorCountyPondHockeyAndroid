package com.example.jacob.doorcountypondhockey;

/**
 * Created by Jacob on 4/24/2017.
 */

public class ScheduleEvent {
    private static final String TIME_DIVIDER = ":";
    private static final String TIME_DIVIDER_UNDER_TEN = ":0";
    private String teamA;
    private String teamB;
    private int rinkNumber;
    private int hour;
    private int minute;

    public ScheduleEvent() {

    }

    public ScheduleEvent(String teamA, String teamB, int rinkNumber, int hour, int minute) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.rinkNumber = rinkNumber;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Obtains a properly formatted time to display to the user
     *
     * @return a string containing the formatted time
     */
    public String getGameTime() {
        if (minute > 9) {
            return hour + TIME_DIVIDER + minute;
        }
        return hour + TIME_DIVIDER_UNDER_TEN + minute;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public int getRinkNumber() {
        return rinkNumber;
    }

    public void setRinkNumber(int rinkNumber) {
        this.rinkNumber = rinkNumber;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ScheduleEvent)) {
            return false;
        }

        ScheduleEvent scheduleEvent = (ScheduleEvent) obj;

        if (!this.teamA.equals(scheduleEvent.teamA)) {
            return false;
        }
        if (!this.teamB.equals(scheduleEvent.teamB)) {
            return false;
        }
        if (this.rinkNumber != scheduleEvent.rinkNumber) {
            return false;
        }
        if (this.hour != scheduleEvent.hour) {
            return false;
        }
        if (this.minute != scheduleEvent.minute) {
            return false;
        }
        return true;
    }
}
