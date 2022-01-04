package com.monster.handscan.protecthealth.model;

public class AdviceModel {
    private String adviceText;
    private int imageId;

    public AdviceModel(String adviceTxt, int imageId) {
        this.adviceText = adviceTxt;
        this.imageId = imageId;
    }

    public String getAdviceText() {
        return adviceText;
    }

    public void setAdviceText(String adviceText) {
        this.adviceText = adviceText;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
