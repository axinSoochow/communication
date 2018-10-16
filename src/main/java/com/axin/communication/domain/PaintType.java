package com.axin.communication.domain;

public enum PaintType {

    LINR("折线图"),Pie("饼图"),BAR("柱形图");

    private String desc;

    PaintType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
