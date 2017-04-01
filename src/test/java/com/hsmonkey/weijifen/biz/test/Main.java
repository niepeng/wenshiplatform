package com.hsmonkey.weijifen.biz.test;

import wint.help.codec.MD5;

import com.hsmonkey.weijifen.util.RandomUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年5月5日  下午6:27:22</p>
 * <p>作者：niepeng</p>
 */
public class Main {
	
	public static void main(String[] args) {
		String f = RandomUtil.getRandomString(17) + System.currentTimeMillis() % RandomUtil.randomInt(100, 600);
		String value = MD5.encrypt(f) + RandomUtil.getRandomString(10).toLowerCase();
		System.out.println(value);
	}

	public static void main2(String[] args) {
		String value = "🍀🍀茼蒿🍀🍀";
//		String value = "@内我陪你过爱对方的2432fd++--@#￥%……&*";
		System.out.println(trimSp(value));
	}

	private static String trimSp(String value) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < value.length(); i++) {
			int c = value.codePointAt(i);
//			char c = (char)value.codePointAt(i);
			if (c < 0x0000 || c > 0xffff) {
				System.out.println("fffff");
				continue;
			}
			
			sb.append((char)c);
		}
		
//		if(sb.toString().indexOf('?') > 0) {
//			System.out.println("sss");
//		}
		
		return sb.toString();
	}
}
