package com.hsmonkey.weijifen.biz.query;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年1月20日  上午10:31:31</p>
 * <p>作者：niepeng</p>
 */
public class AlarmQuery extends BaseQuery {
	
	private static final long serialVersionUID = 7774696838426160601L;

	private String snaddr;
	
	private String startTime;
	
	private String endTime;
	

	public String getSnaddr() {
		return snaddr;
	}

	public void setSnaddr(String snaddr) {
		this.snaddr = snaddr;
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
	
}
