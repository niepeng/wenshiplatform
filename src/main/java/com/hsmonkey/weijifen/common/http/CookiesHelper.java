package com.hsmonkey.weijifen.common.http;

import java.util.HashMap;
import java.util.Map;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: </p>
 * <p>创建时间: 2015-5-30  上午1:58:05</p>
 * <p>作者：niepeng</p>
 */
public class CookiesHelper {

	public static Map<String, String> getCookies(String cookies) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtil.isBlank(cookies)) {
			return map;
		}

		String[] strs = cookies.split(";");
		String[] tmp = null;
		for (String str : strs) {
			tmp = str.split("=");
			if (tmp.length != 2) {
				continue;
			}
			map.put(tmp[0], tmp[1]);
		}

		return map;
	}
}