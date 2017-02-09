package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: 店铺佣金类型</p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年6月22日  下午4:06:11</p>
 * <p>作者：niepeng</p>
 */
public enum ShopCommissionTypeEnum {


	ALL(1, "整个店铺"),
	item(2,"单品");

	private final int id;

	private final String meaning;

	private ShopCommissionTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(ShopCommissionTypeEnum tmp : ShopCommissionTypeEnum.values()) {
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
