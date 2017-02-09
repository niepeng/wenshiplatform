package com.hsmonkey.weijifen.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;

public class MathUtil {

	public static final int RANDOM_FLAG = 100000000;
	public static final int RANDOM_FLAG_LENGTH = String.valueOf(RANDOM_FLAG).length() - 1;


	/**
	 * 生成num数量、长度为RANDOM_FLAG_LENGTH 的数字列表
	 *
	 * @param num
	 *            数量
	 * @return 如： 90564244 02991411 34587392
	 */
	public static String[] genRandoms(int num) {
		if (num < 1) {
			return null;
		}
		Random random = new Random();
		Map<String, Object> map = new HashMap<String, Object>();
		while (num > map.size()) {
			int value = Math.abs(random.nextInt()) % RANDOM_FLAG;
			map.put(fillZero(value, RANDOM_FLAG_LENGTH), null);
		}

		String[] result = new String[num];
		int index = 0;
		for (Entry<String, Object> entry : map.entrySet()) {
			result[index] = entry.getKey();
			index++;
		}
		return result;
	}

	/*
	 * 经纬度转换
	 */
	public static double lnglat2double(int value) {
		return value/(100 * 10000.d);
	}

	/*
	 * 经纬度转换
	 */
	public static int lnglat2int(double d) {
		return (int)(d * 100 * 10000);
	}

	/**
	 * 如果value的值，没有到达lengthNum的长度，在前面填补0
	 *
	 * @param value
	 * @param lengthNum
	 * @return
	 */
	private static String fillZero(int value, int lengthNum) {
		String result = String.valueOf(value);
		int size = result.length();
		for (int i = 0, fillSize = lengthNum - size; i < fillSize; i++) {
			result = "0" + result;
		}
		return result;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[-]?[0-9]*[.]?[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isPureNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*[.]?[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static void main1(String[] args){
		String num = "-44.5";
		System.out.println("---------------");
		if (MathUtil.isPureNumeric(num)){
			System.out.println("isPureNumeric is num");
		}

		if (MathUtil.isNumeric(num)){
		    System.out.println("isNumeric is num");
		}

	}

	
	public static void main(String[] args) {
		for (int i = 0; i <1000; i++) {
			System.out.println(getRandomInt(100));
		}
	}
	/**
	 * double 保留小数位数，四舍五入
	 * @param val double的数字
	 * @param precision 保留小数位数
	 * @return
	 */
	public static Double roundDouble(double val, int precision) {
        Double ret = null;
        try {
            double factor = Math.pow(10, precision);
            ret = Math.floor(val * factor + 0.5) / factor;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

	
	/**
	 * 
	 * @param max 最大值
	 * @return  
	 */
	public static int getRandomInt( int max){
		Random ran = new Random();
		return ran.nextInt(max);
	}
	
	
}
