package com.hsmonkey.weijifen.util.api;

import java.util.HashMap;
import java.util.Map.Entry;

import wint.help.codec.MD5;

public class JdApi {
	/**
	 * 调用京东服务(京东魔盒) 接口
	 * 
	 *  @author zcy
	 *  
	 *  @time 2016年7月4日11:25:27
	 * 
	 */
	
	private static  final String signKey="abdccwewe111";
	
	
	
	public static String   getSign(HashMap<String ,String> map){
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, String> entry : map.entrySet()) {
			buffer.append(entry.getValue());
		}
		buffer.append(signKey);
		return MD5.encrypt(buffer.toString());
	}
	
}
