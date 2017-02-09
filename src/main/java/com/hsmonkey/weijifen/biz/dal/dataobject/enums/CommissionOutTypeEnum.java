package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * @memo  提现申请状态
 * 
 * @author zcy
 * 
 * @time 2016年6月24日17:33:38
 */

public enum CommissionOutTypeEnum {

	all(0, "全部"),
	appliction(1, "申请中"),
	checksuccess(2,"审核通过,待支付"),
	checkfail(3,"审核失败"),
	checked(4,"审核通过,已支付");

	private final int id;

	private final String meaning;

	private CommissionOutTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(CommissionOutTypeEnum tmp : CommissionOutTypeEnum.values()) {
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
