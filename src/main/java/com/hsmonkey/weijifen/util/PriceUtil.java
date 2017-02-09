package com.hsmonkey.weijifen.util;

import java.text.DecimalFormat;



public class PriceUtil {

	public static int double2int(double d) {
		 try {
			double doubleResult = d * 100;
			 DecimalFormat df1 = new DecimalFormat("###0");
			 String parValue = df1.format(doubleResult);
			 return Integer.valueOf(parValue);
		} catch (Exception e) {
			// 这个做法有问题，但如果上面出错了，只能这么做, 可以用16.9测试一下
			return (int)(d * 100);
		}
	}
	
	public static double int2double(int value) {
		double d = Double.valueOf(value);
		return ArithUtil.div(d, 100);
	}
	

	public static int chu100(int value) {
		return value/100;
	}
	
	public static double int2double(int total,int num) {
		//double d = Double.valueOf(total/num);
		double d = ArithUtil.div(ArithUtil.div(total, num), 100);
		return d;
	}
	
	public static int strYuan2int(String s) {
		try {
			double d = Double.valueOf(s);
			return double2int(d);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double int2doubleForKg(int value) {
		double d = ArithUtil.div(value, 1000);
		return d;
	}
	
	public static double int2doubleForKm(int value) {
	    double d = ArithUtil.div(value, 1000);
	    return d;
	}
	
	public static double totalPrice(int price,int num) { 
		return  int2double(price*num);
	}
	
	/**
	 * 获取两个数差价的百分比，转换为百分比形式。(a-b)/a*100
	* @Title: getPercent 
	* @Description: 
	* @param a
	* @param b
	* @return double  
	* @throws
	 */
	public static double getPercent(int a, int b){
	    double percent = ArithUtil.div(b, a, 4);
	    return ArithUtil.mul(percent, 100);
	}
	
	/**
	 * 获取两数的折扣，(b/a)*10
	* @Title: getDiscount 
	* @Description: 获取两数的折扣，(b/a)*10
	* @param a 原价
	* @param b 优惠价
	* @return double  
	* @throws
	 */
	public static double getDiscount(int a, int b){
	    double percent = ArithUtil.div(b, a, 2);
        return ArithUtil.mul(percent, 10);
	}
	
	/**
	 * 两个价格相加
	* @Title: addPrice 
	* @param a 扩大100倍的金额
	* @param b 扩大100倍的金额
	* @return double  
	* @throws
	 */
	public static double addPrice(int a, int b) {
	    return ArithUtil.div(ArithUtil.add(a, b), 100);
	}
	
	/*
	 * 2个数相除， a/b
	 */
	public static double div(double a, int b) {
		return ArithUtil.div(a,b);
	}
	
	/**
	 * 两数相减。a-b
	* @Title: sub 
	* @Description: 
	* @param a
	* @param b
	* @return double  
	* @throws
	 */
	public static int sub(int a, int b){
	    return (int) ArithUtil.sub(a, b);
	}
	
	
	public static void main(String[] args) {
//		double d = 161.97d;
//		int intD = double2int(d);
//		System.out.println(intD);
		System.out.println(int2double(-192));
		
//		System.out.println(getDiscount(97, 95));
	}
}
