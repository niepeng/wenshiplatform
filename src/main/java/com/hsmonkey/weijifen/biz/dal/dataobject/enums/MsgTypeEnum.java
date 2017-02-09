package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年5月27日  下午6:18:13</p>
 * <p>作者：niepeng</p>
 */
public enum MsgTypeEnum {

	text(1, "文本"),
	img_text(2, "图文"),
	system(3, "系统回复"),
	imgs(4,"图片");

	private final int id;

	private final String meaning;

	private MsgTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(MsgTypeEnum tmp : MsgTypeEnum.values()) {
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
