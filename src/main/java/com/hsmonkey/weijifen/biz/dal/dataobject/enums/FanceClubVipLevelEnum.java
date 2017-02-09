/**
 * 
 */
package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

/**
 * @author zcy
 *
 * @date  2016年8月16日
 *
 * @time  下午2:49:45
 */
public enum FanceClubVipLevelEnum {
	
	freeVip(0,"免费会员"),
	normalVip(1,"正式会员");
	
	
	private int id ;
	
	private String vipName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	private FanceClubVipLevelEnum(int id, String vipName) {
		this.id = id;
		this.vipName = vipName;
	}

	public static String getVipName(int vipLevel) {
		for(FanceClubVipLevelEnum tmp :FanceClubVipLevelEnum.values()){
			if(vipLevel == tmp.getId()){
				return tmp.getVipName();
			}
		}
		return null;
	}

	public static FanceClubVipLevelEnum getTmpById(int change) {
		for(FanceClubVipLevelEnum tmp :FanceClubVipLevelEnum.values()){
			if(change == tmp.getId()){
				return tmp;
			}
		}
		return null;
	}
	
	

}
