package com.hsmonkey.weijifen.biz.bean;

public class TestEquBean {
    private long address;
    
    // EqutypeEnum
    private int equtype;
    
    // 标记 == 名称
    private String mark;
    
    // 温度值
    private long tempValue;

    // 湿度值
    private long humiValue;
    
    // 采集数据客户端的区域id
    private long referId;

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public int getEqutype() {
        return equtype;
    }

    public void setEqutype(int equtype) {
        this.equtype = equtype;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public long getTempValue() {
        return tempValue;
    }

    public void setTempValue(long tempValue) {
        this.tempValue = tempValue;
    }

    public long getHumiValue() {
        return humiValue;
    }

    public void setHumiValue(long humiValue) {
        this.humiValue = humiValue;
    }

    public long getReferId() {
        return referId;
    }

    public void setReferId(long referId) {
        this.referId = referId;
    }
    
}
