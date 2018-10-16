package com.hsmonkey.weijifen.biz.bean;


/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/10/16
 */

public class AlarmNewBean {
/**
 "type":"设备类型",
 "handleUser": "处理报警的用户名",
 "area": "设备所在区域"
 */

  /**
   * 报警id
   */
  private String alarmId;

  /**
   * 设备名称
   */
  private String devName;

  /**
   * 设备SNADDR
   */
  private String snaddr;

  /**
   * 报警内容
   */
  private String info;

  /**
   * 是否处理的标识位，如果已处理，handle=1，如果未处理handle=0
   */
  private String handle;

  /**
   * 第一次报警的时间
   */
  private String startTime;

  /**
   * 报警结束时间，如果设备尚未结束报警，该参数为最后一次上传报警信息的时间
   */
  private String endTime;


  /**
   * 报警状态 0 正在报警, 1 已结束
   */
  private String alarmState;

  /**
   * 用户备注信息
   */
  private String additionInfo;

  /**
   * 处理用户
   */
  private String handleUser;


  // ================== normal method ======================

  public boolean isAlarmEnd() {
    return "1".equals(alarmState);
  }




  // ================== setter/getter ======================


  public String getAlarmId() {
    return alarmId;
  }

  public void setAlarmId(String alarmId) {
    this.alarmId = alarmId;
  }

  public String getDevName() {
    return devName;
  }

  public void setDevName(String devName) {
    this.devName = devName;
  }

  public String getSnaddr() {
    return snaddr;
  }

  public void setSnaddr(String snaddr) {
    this.snaddr = snaddr;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getHandle() {
    return handle;
  }

  public void setHandle(String handle) {
    this.handle = handle;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getAlarmState() {
    return alarmState;
  }

  public void setAlarmState(String alarmState) {
    this.alarmState = alarmState;
  }

  public String getAdditionInfo() {
    return additionInfo;
  }

  public void setAdditionInfo(String additionInfo) {
    this.additionInfo = additionInfo;
  }

  public String getHandleUser() {
    return handleUser;
  }

  public void setHandleUser(String handleUser) {
    this.handleUser = handleUser;
  }
}