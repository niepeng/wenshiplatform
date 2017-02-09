/**
 * 
 */
package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import com.hsmonkey.weijifen.util.StringsUtil;

/**
 * @author zcy
 *
 * @date  2016年11月17日
 *
 * @time  下午3:38:23
 */
public enum SystemNoticeEnum {
	
	FREQ_LIMIT("FREQ_LIMIT","对同一用户转账操作过于频繁,请稍候重试."),
	NOTENOUGH("NOTENOUGH","帐号余额不足，请用户充值或更换支付卡后再支付."),
	PRODUCT_AUTHORITY_UNOPEN("PRODUCT_AUTHORITY_UNOPEN","你的商户号未开通该产品权限，请联系管理员到产品中心开通"),
	SIGN_ERROR("SIGN_ERROR" , "签名错误"),
	SYSTEMERROR("SYSTEMERROR","网络繁忙[2476],请原单号再试."),
	RED_LIMIT("RED_LIMIT","红包已达上限，请及时更改上限金额.");
	
	private String id;
	
	private String meaning;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	private SystemNoticeEnum(String id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}
	
	public static SystemNoticeEnum getByStr(String typeStr){
		if(StringsUtil.isBlank(typeStr)){
			return null;
		}
		
		for (SystemNoticeEnum tmp :SystemNoticeEnum.values()) {
			if(typeStr.equals(tmp.getId())){
				return tmp;
			}
		}
		return null;
	}

}
