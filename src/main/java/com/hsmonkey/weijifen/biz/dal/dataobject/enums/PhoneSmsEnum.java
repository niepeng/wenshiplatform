package com.hsmonkey.weijifen.biz.dal.dataobject.enums;


/**
 * <p>标题: 短信通道模板</p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年8月5日  上午10:57:34</p>
 * <p>作者：niepeng</p>
 */
public enum PhoneSmsEnum {

	PHONE_BANDING(1, "您的手机绑定验证码为:%s 【微露】技术支持", 300);

	// 唯一编号
	private final int id;

	// 短信内容，验证码用%s代替，到时候需要替换
	private final String content;

	// 有效期:秒数
	private final int seconds;

	private PhoneSmsEnum(int id, String content, int second) {
		this.id = id;
		this.content = content;
		this.seconds = second;
	}

	public static PhoneSmsEnum get(int id) {
		for (PhoneSmsEnum phoneSmsEnum : PhoneSmsEnum.values()) {
			if (phoneSmsEnum.getId() == id) {
				return phoneSmsEnum;
			}
		}
		return null;
	}
	
	public static String getContent(int id) {
		PhoneSmsEnum tmp = get(id);
		return tmp != null ? tmp.getContent() : null;
	}
	
	public static int getSecond(int id) {
		PhoneSmsEnum tmp = get(id);
		return tmp != null ? tmp.getSeconds() : 0;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public int getSeconds() {
		return seconds;
	}

}
