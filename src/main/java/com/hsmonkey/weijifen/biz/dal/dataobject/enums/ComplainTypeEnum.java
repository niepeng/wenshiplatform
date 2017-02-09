/**
 * 
 */
package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

/**
 * @author zcy
 *
 * @date  2016年9月29日
 *
 * @time  上午10:03:47
 */
public enum ComplainTypeEnum {
	
	type1(1,"欺诈",""),
	type2(2,"色情",""),
	type3(3,"政治谣言",""),
	type4(4,"常识性谣言",""),
	type5(5,"诱导分享",""),
	type6(6,"恶意营销",""),
	type7(7,"隐私信息收集",""),
	type8(8,"抄袭公众号文章",""),
	type9(9,"其他侵权类（冒名、诽谤、抄袭）","");
	
	
	private int id ;
	
	private String meaning;
	
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private ComplainTypeEnum(int id, String meaning, String name) {
		this.id = id;
		this.meaning = meaning;
		this.name = name;
	}
	
	

}
