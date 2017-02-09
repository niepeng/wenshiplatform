package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: 好评状态</p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年5月30日  下午3:49:39</p>
 * <p>作者：niepeng</p>
 */
public enum GoodEvaluateStatusEnum {

	ing(0, "审核中"),
	checkSuccess(1, "审核通过"),
	paySuccess(2, "已支付");

	private final int id;

	private final String meaning;

	private GoodEvaluateStatusEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(GoodEvaluateStatusEnum tmp : GoodEvaluateStatusEnum.values()) {
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
