package com.hsmonkey.weijifen.biz.dal.dataobject;


/**
 * 
 * @author niepeng
 *
 */
public class KeyValueDO extends BaseDO {

	private static final long serialVersionUID = 300865358134216807L;

	/**
	 * 常量类
	 * com.hsmonkey.weijifen.common.Constant
	 */
	private String keyName;

	private String value;

	/**
	 * type类型具体看枚举
	 * com.hsmonkey.qiangtao.biz.dal.dataobject.enums.KeyValueTypeEnum
	 */
	private int type;

	// -------------- extend attribute -----------------------

	// -------------- normal moethod -------------------------

	// -------------- setter/getter --------------------------

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

