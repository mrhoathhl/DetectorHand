package com.monster.handscan.protecthealth.model;

public class DayChallengeModel {
    private int dayLabel;
    private boolean isDay;
    private boolean isNight;

    public DayChallengeModel(int dayLabel, boolean isDay, boolean isNight) {
        this.dayLabel = dayLabel;
        this.isDay = isDay;
        this.isNight = isNight;
    }

    public int getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(int dayLabel) {
        this.dayLabel = dayLabel;
    }

    public boolean isDay() {
        return isDay;
    }

    public void setDay(boolean day) {
        isDay = day;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }
}
