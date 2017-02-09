package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年7月4日  下午2:35:52</p>
 * <p>作者：niepeng</p>
 */
public enum MessageCenterTypeEnum {

	jd_access_token(1, "京东accesstoken无效或未空"),
	youzan_access_token(2, "有赞的appId和appSecret出错");

	private final int id;

	private final String meaning;

	private MessageCenterTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(MessageCenterTypeEnum tmp : MessageCenterTypeEnum.values()) {
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
