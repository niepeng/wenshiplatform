package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年8月23日  下午5:04:13</p>
 * <p>作者：niepeng</p>
 */
public enum FunctionEnum {

	shareRedArticle(1, "分享文章获得红包功能权限"),
	transaction(2, "交易系统功能权限"),
	bargainItem(3,"商品砍价功能权限"),
	remindGroupBuy(4,"开团提醒功能权限"),
	logisticsNotice(5,"物流通知"),
	record(6,"千里传音权限"),
	jifenbao(7,"积分宝权限"),
	advArticle(8,"贴片广告文章权限")
	;

	private final int id;

	private final String meaning;

	private FunctionEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int id) {
		for (FunctionEnum tmp : FunctionEnum.values()) {
			if (tmp.getId() == id) {
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
