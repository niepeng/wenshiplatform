package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

/**
 * @author zcy
 *
 * @date  2016年8月16日
 *
 * @time  下午2:49:45
 */
public enum WxPublicNumberTypeEnum {
	
	wxPersonnalNumber(0,"个人号"),
	wxPublicNumber(1,"服务号"),
	wxSubscribeNumber(2,"订阅号");
	
	
	private int id ;
	
	private String subscribe;

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private WxPublicNumberTypeEnum(int id, String subscribe) {
		this.id = id;
		this.subscribe = subscribe;
	}

	public static String getTypeName(int type) {
	    
		for (WxPublicNumberTypeEnum tmp  : WxPublicNumberTypeEnum.values()) {
			if(type == tmp.getId()){
				return tmp.getSubscribe();
			}
		}
		return null;
	}


	
	

}
