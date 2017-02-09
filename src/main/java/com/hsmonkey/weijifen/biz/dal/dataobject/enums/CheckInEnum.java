package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年7月12日  上午10:07:06</p>
 * <p>作者：niepeng</p>
 */
public enum CheckInEnum {

	day(1, "每天奖励"),
	other(2, "累计到一定天数额外奖励"),
	rankDay(3, "当天名次奖励");

	private final int id;

	private final String meaning;

	private CheckInEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int id) {
		for(CheckInEnum tmp : CheckInEnum.values()) {
			if(tmp.getId() == id) {
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
