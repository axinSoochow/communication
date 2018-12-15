package com.axin.communication.domain.enums;

public enum NetworkCodeTypeEnum {

    NUMBER(1, "接接收端数量变化"), PACKETLOSS(2, "接收端数量变化"), CACHE(3, "缓存变化"), TTL(4, "TTL变化"),DELAY(5,"计算时延");


    private int type;
    private String desc;

    NetworkCodeTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
