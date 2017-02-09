package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年6月27日  下午5:11:18</p>
 * <p>作者：niepeng</p>
 */
public enum ScanTypeEnum {


	normal(0, "普通的马上涨粉类型"),
	commission(1,"用户推广获取佣金类型");

	private final int id;

	private final String meaning;

	private ScanTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(ScanTypeEnum tmp : ScanTypeEnum.values()) {
			if(tmp.getId() == status) {
				return tmp.getMeaning();
			}
		}
		return StringUtil.EMPTY;
	}

	public int getId() {
		return id;
	}

	public String getMeaning() {
		return meaning;
	}

}
