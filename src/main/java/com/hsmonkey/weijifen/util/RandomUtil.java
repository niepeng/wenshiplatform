package com.hsmonkey.weijifen.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import wint.lang.utils.CollectionUtil;

public class RandomUtil {

	/**
	 * 在 start 和 end之间，随机生成一个整数， start < end
	 * @param start
	 * @param end
	 * @return [start,end)
	 */
	public static int randomInt(int start, int end) {
		int value = (int) (Math.random() * (end - start)) + start;
		return value;
	}

	public static int[] random(int start, int end, int num) {
		int size = end - start;
		if (size <= num) {
			return null;
		}
		Random rnd = new Random();
		int startIndex = rnd.nextInt(size);
		int skipNum = randomSkipNum(size, num);

		int[] result = new int[num];
		for (int i = 0; i < num; i++) {
			int index = startIndex + skipNum * i;
			if (index >= size) {
				index = index % size;
			}
			result[i] = index;
		}
		return result;
	}

	public static <T> List<T> randomList(List<T> data, int num) {
		int size = 0;
		if (data == null || (size = data.size()) == 0) {
			return null;
		}

		if(size <= num) {
			return data;
		}

		int[] randomList = random(0, size, num);
		List<T> result = CollectionUtil.newArrayList();
		if (randomList != null && randomList.length > 0) {
			for (int index : randomList) {
				result.add(data.get(index));
			}
		}
		return result;
	}

	private static int randomSkipNum(int size, int num) {
		int maxSkipNum = size / num + 1;
		if (maxSkipNum >= 2) {
			return (int) (Math.random() * (maxSkipNum - 1)) + 1;
		}
		return maxSkipNum;
	}

	public static void main(String[] args) {
//		for(int time=0;time<100;time++) {
//			int[] result = random(0, 10, 5);
//			for(int i : result) {
//				System.out.print(i + ", ");
//			}
//			System.out.println();
//		}
		for(int i=0;i<10;i++) {
			System.out.println( i + "  ->" + getRandomString(6));
		}
	}


	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "ABCDEFGHKMNPQRSTWXY3456789";  // 去掉了一些字母和数字比较难分辨的值
	    Random random = new Random();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < length; i++) {
	        int number = random.nextInt(base.length());
	        sb.append(base.charAt(number));
	    }
	    return sb.toString().toUpperCase();
	 }

	public static String showDate() {
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

}
