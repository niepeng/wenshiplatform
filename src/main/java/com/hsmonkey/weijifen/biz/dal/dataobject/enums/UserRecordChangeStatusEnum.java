package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年9月24日  下午11:55:53</p>
 * <p>作者：niepeng</p>
 */
public enum UserRecordChangeStatusEnum {

	init(0, "初始状态"),
	wxSuccess(1, "从微信下载成功"),
	wxFail(2, "从微信下载失败"),
	changeFail(3, "转成格式失败"),
	changeSucess(4, "转成格式成功")
	;

	private final int id;

	private final String meaning;

	private UserRecordChangeStatusEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int id) {
		for(UserRecordChangeStatusEnum tmp : UserRecordChangeStatusEnum.values()) {
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
