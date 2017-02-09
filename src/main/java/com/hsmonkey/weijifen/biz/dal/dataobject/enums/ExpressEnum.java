package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

/**
 * <p>标题: 快递公司枚举</p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年9月5日  下午9:56:43</p>
 * <p>作者：niepeng</p>
 */
public enum ExpressEnum {
	
	YUNDA(1,"韵达"),
	SHUNFNEG(2,"顺丰"),
	YUANTONG(3,"圆通"),
	SHENTONG(4,"申通"),
	EMS(5,"EMS"),
	ZHONGTONG(6,"中通"),
	BAISHI(7,"百世汇通"),
	ZAIJISONG(8,"宅急送"),
	TIANTIAN(9,"天天"),
	DEBANG(10,"德邦"),
	XINBANG(11,"新邦"),
	TIANYU(12,"天地华宇"),
	DHL(13,"DHL"),
	UPS(14,"UPS"),
	TNT(15,"TNT"),
	FEDEX(16,"FEDEX"),
	QUANYI(17,"全一"),
	QUANSHEN(18,"全峰"),
	SHENGHUI(19,"盛辉"),
	ZHONGTIE(20,"中铁快运"),
	OTHER(50,"其他")
	;
	
	private final int id;
	
	private final String meaning;
	
	private ExpressEnum(int id, String meaning) {
		this.id = id;
		this.meaning = meaning;
	}
	
	public static ExpressEnum get(int id) {
		for(ExpressEnum tmp : ExpressEnum.values()) {
			if(tmp.getId() == id) {
				return tmp;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public String getMeaning() {
		return meaning;
	}
	
}
