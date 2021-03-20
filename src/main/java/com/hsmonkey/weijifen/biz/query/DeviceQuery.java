package com.hsmonkey.weijifen.biz.query;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年1月20日  上午11:43:08</p>
 * <p>作者：niepeng</p>
 */
public class DeviceQuery extends BaseQuery  {

	private static final long serialVersionUID = -623287361571140748L;

	private String area;
	
	private String deviceName;

	private int pageIndex;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
}
