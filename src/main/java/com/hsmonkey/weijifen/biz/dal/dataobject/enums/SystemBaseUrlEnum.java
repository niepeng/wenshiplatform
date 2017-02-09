package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import java.util.ArrayList;
import java.util.List;

public enum SystemBaseUrlEnum {
	
	grzx(0,"/user/userCenter.htm?shopId=","个人中心"),
	jfcx(1,"/user/myRank.htm?shopId=","我的积分"),
	dhzx(2,"/user/exchangeCenter.htm?shopId=","兑换中心"),
	jfphb(3,"/user/rankList.htm?shopId=","积分排行榜"),
	hpfx(2,"/wx/goodEvaluate/showSelf.htm?shopId=","好评返现"),
	qrsh(2,"/wx/orderConfirm/showSelf.htm?shopId=","确认收货"),
	
//	bdsj(4,"/user/youzan/bindPhoneNumber.htm?shopId=","绑定手机号"),/user/userCenter.htm
	yjmx(5,"/user/userCenterPage/commissionDetail.htm?shopId=","佣金明细"),
	sqtx(6,"/user/userCenterPage/myCommission.htm?shopId=","我的财富"),
	txgl(7,"user/userCenterPage/commissionDetailList.html?shopId=","提现管理");
    private SystemBaseUrlEnum(long id, String url, String meanning) {
		this.id = id;
		this.url = url;
		this.meanning = meanning;
	}
	private long id ;
	
	private String url ;
	
	private String  meanning;     
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMeanning() {
		return meanning;
	}

	public void setMeanning(String meanning) {
		this.meanning = meanning;
	}

	public static List<SystemBaseUrlEnum> getAllUrlList(){
		List<SystemBaseUrlEnum> list =new ArrayList<SystemBaseUrlEnum>();
		for (SystemBaseUrlEnum tmp :SystemBaseUrlEnum.values()) {
			list.add(tmp);
		}
		return list;
	}

}
