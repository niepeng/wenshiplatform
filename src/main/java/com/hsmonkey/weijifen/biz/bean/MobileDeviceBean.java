package com.hsmonkey.weijifen.biz.bean;


import java.util.List;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lisenbiao@51huadian.cn
 * @date 17/10/28
 */

public class MobileDeviceBean {

  private String user;

  private List<String> snaddr;

  private List<String> deviceMobileList;

  public List<String> getDeviceMobileList() {
    return deviceMobileList;
  }

  public void setDeviceMobileList(List<String> deviceMobileList) {
    this.deviceMobileList = deviceMobileList;
  }

  public List<String> getSnaddr() {
    return snaddr;
  }

  public void setSnaddr(List<String> snaddr) {
    this.snaddr = snaddr;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }
}