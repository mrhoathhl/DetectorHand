package com.monster.handscan.protecthealth.model;

public class ScanHistoryModel {
    private String percent;
    private String time;

    public ScanHistoryModel(String percent, String time) {
        this.percent = percent;
        this.time = time;
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
}
