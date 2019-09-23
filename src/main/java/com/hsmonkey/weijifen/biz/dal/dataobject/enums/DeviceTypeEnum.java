package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

public enum DeviceTypeEnum {

    temp(1,"温度"),
    humi(2,"湿度");

    final int id;
    final String name;

    private DeviceTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
