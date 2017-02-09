package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import java.util.List;

public enum FanceClubExchangeTypeEnum {
	
	picTextsType(1,"多图文下第二条图文"),
	fristPicTextType(2,"首条图文"),
	unionActivityType(3,"联合活动"),
	advertisementTyle(4,"可以承接广告"),
	others(1,"其他")
	;
	
	
	private int id ;
	
	
	private String meaning;
	
	
	


	private FanceClubExchangeTypeEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getMeaning() {
		return meaning;
	}


	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	
	

}
