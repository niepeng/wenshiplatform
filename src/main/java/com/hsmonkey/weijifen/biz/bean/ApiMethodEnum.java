package com.hsmonkey.weijifen.biz.bean;


/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年4月5日  下午8:43:04</p>
 * <p>作者：niepeng</p>
 */
public enum ApiMethodEnum {
	
	deviceList(1),
	inTimeData(2);
	
	private ApiMethodEnum(int type) {
		this.type = type;
	}

	private final int type;
	
	public int getType() {
		return type;
	}
	
	
}
