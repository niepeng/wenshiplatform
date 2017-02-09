package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: </p>
 * <p>创建时间: Sep 24, 2014  2:50:28 PM</p>
 * <p>作者：聂鹏</p>
 */
public enum KeyValueTypeEnum {

	upfanceNotifyMsg(1, "upfanceNotifyMsg关注通知上级的文案内容"),
	upLoadTicket(2, "upLoadTicket群分享二维码图片地址"),
	wxBaseAccessToken(3,"微信baseAccessToken"),
	getOutOrderDate(4,"上一次获取外部订单的时间(京东店铺是店铺开启佣金模式时间)"),
	commissionNotifyMsg(5,"commission关注通知上级的文案内容"),
	weixinTemplateMessage(6, "微信的模板消息内容相关"),
	fansSecConfig(7, "粉丝安全配置"),
	actUserInfo(10, "测试人脸相似度的时候的存储数据"),
	actUserInfo2(11, "测试人脸相似度的时候的存储数据2"),
	wxJsapiTicket(20,"微信js分享给朋友和朋友圈自定义需要的内容"),
	articleMoneyConfig(21,"文章分享送红包基础配置信息"),
	articleSurpriseMoneyConfig(22,"文章分享送红包惊喜配置信息"),
	sendArticleContent(23,"发送新文章通知内容");
	
	

	private final int id;

	private final String meaning;

	private KeyValueTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}

	public static String getMeaning(int status) {
		for(KeyValueTypeEnum tmp : KeyValueTypeEnum.values()) {
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
