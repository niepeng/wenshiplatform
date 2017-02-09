package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年6月16日  下午5:04:34</p>
 * <p>作者：niepeng</p>
 */
public enum MappingKeyValueEnum {

	nickname("nickname"),
	totalscore("totalscore");
	
	private final String value;
	
	private MappingKeyValueEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
