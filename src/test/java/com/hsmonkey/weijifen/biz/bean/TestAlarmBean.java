package com.hsmonkey.weijifen.biz.bean;

import java.util.Date;

public class TestAlarmBean {

    private long address;

    // 报警问题
    private String reason;

    // 首次报警时间
    private Date alarmTime;

    // 最近报警时间
    private Date lastAlarmTime;

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Date getLastAlarmTime() {
        return lastAlarmTime;
    }

    public void setLastAlarmTime(Date lastAlarmTime) {
        this.lastAlarmTime = lastAlarmTime;
    }
    
}
