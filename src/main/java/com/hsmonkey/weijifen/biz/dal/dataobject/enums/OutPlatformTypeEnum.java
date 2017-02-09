package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: 绑定的平台</p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年6月22日  下午3:51:16</p>
 * <p>作者：niepeng</p>
 */
public enum OutPlatformTypeEnum {

	youzan(1, "有赞"),
	jd(2, "京东");

	private final int id;

	private final String meaning;

	private OutPlatformTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(OutPlatformTypeEnum tmp : OutPlatformTypeEnum.values()) {
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
