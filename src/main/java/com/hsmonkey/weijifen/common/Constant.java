package com.hsmonkey.weijifen.common;

/**
 * <p>标题: 常量类 </p>
 * <p>描述: </p>
 * <p>版权: </p>
 * <p>创建时间: Sep 24, 2014  2:53:17 PM</p>
 * <p>作者：聂鹏</p>
 */
public interface Constant {

	public static final String SPLIT = ",";
	
	public static final String COLON = ":";
	
	// 上传文件的目录名称
	public static final String UPLOAD_FOLDER = "uploadfiles";
	
	public static final String UPLOAD_AUTHTXT_FOLDER = "wxauthtxt";
	
	public static final String FILE_SPLIT = "/";
	
	public static final String WILDCARD_CHARACTER = "%";
	
	// 又拍云后缀，压缩图片使用
	public static final String UPYUN_COMPRESS = "!60";
	
	// 订单买家签收后的N天交易订单结束
	public static final int orderExpireDay = 15;
	
	public static final String jd_access_token_error = "京东授权信息不正确，请重新授权";
	public static final String youzan_appId_appSecret_error = "有赞的appId和appSecret不对，请重新配置";
	
	// 微信预支付交易时间限制，微信为120分钟，这里控制为115分钟，单位是：秒
	public static final int wx_preayId_expire_time = 115 * Time.MIN;

	public interface KeyValue {
		
		// 下线扫码关注公众号后，通知一级、二级上线
		public static final String upFanceNotifyMsgPrefix = "upFanceNotifyMsg_";

		// 用户推广:下线扫码关注公众号后，通知一级、二级上线
		public static final String commissionNotifyMsgPrefix = "commissionNotifyMsgPrefix_";
		
		// 群分享二维码
		public static final String upLoadTicket = "upLoadTicket";

		// 上一次获取外部订单的时间, platformType + recentlyGetOutOrderDate +shopId
		public static final String recentlyGetOutOrderDate = "_recentlyGetOutOrderDate_";

		// 刷粉安全配置
		public static final String fansSecConfigPrefix = "fansSecConfig_";
		
		// 微信模板消息：新用户加入，weixinTemplateMessageNewUser + shopId
		public static final String weixinTemplateMessageNewUserPrefix = "weixinTemplateMessageNewUser_";

		// 微信模板消息：佣金提醒，weixinTemplateMessageCommission + shopId
		public static final String weixinTemplateMessageCommissionPrefix = "weixinTemplateMessageCommission_";
		public static final String weixinTemplateMessageRmindGroupByPrefix = "weixinTemplateMessageRmindGroupBy_";
		public static final String weixinTemplateMessageLogisticsNoticePrefix = "weixinTemplateMessageLogisticsNotice_";
		
		public static final String weixinTemplateMessageSendArticleNotice = "weixinTemplateMessageSendArticleNotice";
		public static final String sendArticleNoticeContent = "sendArticleNoticeContent";
		public static final String sendSystemNoticeContent = "sendSystemNoticeContent_";

		
		
		// 测试人脸相似度的时候的存储数据： actUserInfo + openId
		public static final String actUserInfo = "actUserInfo_";
		public static final String actUserInfo2 = "actUserInfo2_";
		
		// 文章分享送红包基础配置 + shopId
//		public static final String articleMoneyConfig = "articleMoneyConfig_";
		// 文章分享送红包惊喜配置 + shopId
//		public static final String articleSurpriseMoneyConfig = "articleSurpriseMoneyConfig_";
		
		// 文章分享送红包基础配置 + articleId
		public static final String shopArticleMoneyConfig = "shopArticleMoneyConfig_";
		// 文章分享送红包惊喜配置 + articleId
		public static final String shopArticleSurpriseMoneyConfig = "shopArticleSurpriseMoneyConfig_";
		
	}
	
	public interface Cache {

		public static final int NAMESPACES = 1;

		public static final String VERSION = "version";

		public static final String WX_BASE_ACCESS_TOKEN = "wx_base_access_token_";

		public static final String WX_JSAPI_TICKET = "wx_jsapi_ticket_";
		
		// 文章内容缓存，后跟文章表id
		public static final String ARTICLE = "article_";
		
		// 生成普通海报的时候，海报底图的内容缓存，后跟shopId
		public static final String GEN_NORMAL_MEDIA = "genNormalMedia_";

	}

	// 注意：单位是毫秒
	public interface MicroTime {

			public static final int SECOND = 1 * 1000;

			public static final int MIN = 60 * SECOND;

			public static final int HOUR = 60 * MIN;

			public static final int DAY = 24 * HOUR;
		}


	// 注意：单位是秒，不是毫秒
	public interface Time {

		public static final int SECOND = 1;

		public static final int MIN = 60 * SECOND;

		public static final int HOUR = 60 * MIN;

		public static final int DAY = 24 * HOUR;
	}
	
	// byte K M 单位的
	public interface Length {
		
		public static final int byteUnit = 1;
		
		public static final int k = 1024 * byteUnit;
		
		public static final int m =  k * 1024;

	}
}

