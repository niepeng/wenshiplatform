package com.hsmonkey.weijifen.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wint.lang.utils.StringUtil;

import com.hsmonkey.weijifen.biz.dal.dataobject.enums.CommissionOutTypeEnum;
import com.hsmonkey.weijifen.biz.dal.dataobject.enums.ExpressEnum;
import com.hsmonkey.weijifen.biz.dal.dataobject.enums.GoodEvaluateStatusEnum;
import com.hsmonkey.weijifen.biz.dal.dataobject.enums.OrderConfirmStatusEnum;
import com.hsmonkey.weijifen.biz.dal.dataobject.enums.OrderStatusEnum;
import com.hsmonkey.weijifen.biz.dal.dataobject.enums.RoleEnum;
import com.hsmonkey.weijifen.common.Constant;



/**
 * @author niepeng
 *
 * @date 2012-10-12 下午6:25:03
 */
public class ApplicationUtil {
	private static DecimalFormat dec =new DecimalFormat("0.00");
	
	static Map<Long, String> map = new HashMap<Long, String>();
	static {
		map.put(38L, "D币");
	}
	
	private static final String default_score_name = "积分";
	
	public static String scoreName(long shopId) {
		String s = map.get(shopId);
		if (s == null) {
			return default_score_name;
		}
		return s;
	}
	
	public static String showTime(Date date) {
		Date now = new Date();
		int distanceSeconds = DateUtil.distanceSeconds(date, now);
		if (distanceSeconds < Constant.Time.DAY) {
			if (distanceSeconds < Constant.Time.HOUR) {
				return "刚刚";
			}
			return distanceSeconds / Constant.Time.HOUR + "小时前";
		}
		return distanceSeconds / Constant.Time.DAY + "天前";
	}
	
	public static ExpressEnum[] expressAll() {
		return ExpressEnum.values();
	}
	
	public static OrderStatusEnum[] orderStatus() {
		return OrderStatusEnum.values();
	}
	
	public static String getOrderStatusMeaning(int id) {
		return OrderStatusEnum.getMeaningById(id);
	}
	
	public static RoleEnum[] allRoles() {
		return RoleEnum.values();
	}
	
	public static String gooodEvaluate(int status) {
		return  GoodEvaluateStatusEnum.getMeaning(status);
	}
	
	public static String orderConfirm(int status) {
		return  OrderConfirmStatusEnum.getMeaning(status);
	}
	
	/**
	 * 96代表96宽高的图片
	 * 46代表46宽高的图片
	 * 0默认全尺寸
	 * @param width
	 * @return
	 */
	public static String showPic(int width, String pic) {
		if(StringUtil.isBlank(pic)) {
			return pic;
		}
		return pic.substring(0,pic.lastIndexOf('/')+1) + width;
	}
	
	public static int[] showPageCounts() {
		return new int[]{20,50,100};
	}
	
	public static String showStr(String str) {
		return str;
	}
	
	public static String getValue(double d){
		double value = Double.parseDouble(dec.format(d/10000));
		if(value>100){
			return  (int)value+"";
		}
	    return 	dec.format(d/10000);
	}
	
	public static double getValue2Bit(double d){
	    return Double.parseDouble(dec.format(d/10000))	;
	}
	
	public static double getBaseValue2Bit(String d){
	    return Double.parseDouble(dec.format(d))	;
	}
	
	public static String getMoneyDouble(String money) {
		if(StringUtil.isBlank(money)) {
			return money;
		}
		return String.valueOf(Math.abs(Integer.parseInt(money))/100.0);
	}
	
	public static String getMoneyDoubleNoAbs(String money) {
		if(StringUtil.isBlank(money)) {
			return money;
		}
		return String.valueOf(Integer.parseInt(money)/100.0);
	}
	
	
	public  static  String getOutTypeStatus(String typeId){
		if(StringUtil.isBlank(typeId)) {
			return "未知状态";
		}
		return String.valueOf(CommissionOutTypeEnum.getMeaning(Integer.parseInt(typeId)));
	}
	
	
	
}
