package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年7月27日  下午6:07:07</p>
 * <p>作者：niepeng</p>
 */
public enum BlackWhitesEnum {

	black(1, "黑名单"),
	white(2, "白名单");

	private final int id;

	private final String meaning;
	
	private BlackWhitesEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}
	
	public static BlackWhitesEnum get(int id) {
		for (BlackWhitesEnum tmp : BlackWhitesEnum.values()) {
			if (tmp.getId() == id) {
				return tmp;
			}
		}
		return null;
	}

	public static String getMeaning(int id) {
		BlackWhitesEnum tmp = get(id);
		if(tmp != null) {
			return tmp.getMeaning();
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
