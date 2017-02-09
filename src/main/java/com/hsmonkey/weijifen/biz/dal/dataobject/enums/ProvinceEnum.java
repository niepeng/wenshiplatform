/**
 * 
 */
package com.hsmonkey.weijifen.biz.dal.dataobject.enums;

import com.hsmonkey.weijifen.util.StringsUtil;

/**
 * @author zcy
 * 
 * @date 2016年11月15日
 * 
 * @time 上午11:12:30
 */
public enum ProvinceEnum {
	province_0(0, "全部" ,true), 
	province_1(1, "北京",true), 
	province_2(2, "天津",true),
	province_3(3, "河北",false),
	province_4(4, "山西",false), 
	province_5(5, "内蒙古",false), 
	province_6(6, "辽宁",false), 
	province_7(7, "吉林",false), 
	province_8(8, "黑龙江",false), 
	province_9(9, "上海",true), 
	province_10(10, "江苏",false), 
	province_11(11, "浙江",false),
	province_12(12, "安徽",false), 
	province_13(13, "福建",false), 
	province_14(14, "江西",false),
	province_15(15, "山东",false), 
	province_16(16, "河南",false), 
	province_17(17,"湖北",false),
	province_18(18, "湖南",false), 
	province_19(19, "广东",false),
	province_20(20, "广西",false), 
	province_21(21, "海南",false), 
	province_22(22, "重庆",true),
	province_23(23, "四川",false), 
	province_24(24, "贵州",false), 
	province_25(25,"云南",false),
	province_26(26, "西藏",false), 
	province_27(27, "陕西",false),
	province_28(28, "甘肃",false), 
	province_29(29, "青海",false), 
	province_30(30, "宁夏",false),
	province_31(31, "新疆",false),
	province_32(32, "香港",true),
	province_33(33,"澳门",true),
	province_34(34, "台湾",true);
	
	private int id;
	private String name;
	private boolean isCity;
	
	private ProvinceEnum(int id, String name , boolean isCity) {
		this.id = id;
		this.name = name;
		this.isCity = isCity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCity() {
		return isCity;
	}

	public void setCity(boolean isCity) {
		this.isCity = isCity;
	}

	//返回 -1  表示未找到
	public static ProvinceEnum getByName(String province) {
		if(StringsUtil.isBlank(province)){
			return null;
		}
		for(ProvinceEnum tmp :ProvinceEnum.values()){
			if(tmp.getName().contains(province)){
				return tmp;
			}
		}
		return null;
	}

}
