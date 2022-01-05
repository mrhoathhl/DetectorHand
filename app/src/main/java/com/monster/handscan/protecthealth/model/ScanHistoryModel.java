package com.monster.handscan.protecthealth.model;

public class ScanHistoryModel {
    private int id;
    private String percent;
    private String time;
    private int dayLabel;
    private boolean isDay;
    private boolean isNight;
    private String type;

    public ScanHistoryModel(int id, String percent, String time, int dayLabel, boolean isDay, boolean isNight, String type) {
        this.id = id;
        this.percent = percent;
        this.time = time;
        this.dayLabel = dayLabel;
        this.isDay = isDay;
        this.isNight = isNight;
        this.type = type;
    }

    public ScanHistoryModel() {

    }

    public ScanHistoryModel(int dayLabel, boolean isDay, boolean isNight) {
        this.dayLabel = dayLabel;
        this.isDay = isDay;
        this.isNight = isNight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "ScanHistoryModel{" +
                "id=" + id +
                ", percent='" + percent + '\'' +
                ", time='" + time + '\'' +
                ", dayLabel=" + dayLabel +
                ", isDay=" + isDay +
                ", isNight=" + isNight +
                ", type='" + type + '\'' +
                '}';
    }
}
