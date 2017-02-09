package com.hsmonkey.weijifen.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import wint.help.codec.MD5;

public class ParameterUtil {

    public static String getSignData(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)||"sign_type".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null) {
                try {
					content.append((i == 0 ? "" : "&") + key + "=" + URLEncoder.encode(value, "utf-8"));
				} catch (Exception e) {
				}
            } else {
                content.append((i == 0 ? "" : "&") + key + "=");
            }

        }

        return content.toString();
    }

	public static String getSignValue(String params, String secretKey) {
		try {
			String prepareKey = secretKey + URLEncoder.encode(params, "utf-8") + secretKey;
			return MD5.encrypt(prepareKey);
		} catch (Exception e) {
		}
		return null;

	}

    public static String mapToUrl(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (isFirst) {
                sb.append(key + "=" + URLEncoder.encode(value, "utf-8"));
                isFirst = false;
            } else {
                if (value != null) {
                    sb.append("&" + key + "=" + URLEncoder.encode(value, "utf-8"));
                } else {
                    sb.append("&" + key + "=");
                }
            }
        }
        return sb.toString();
    }

    public static String getParameter(String url, String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        name = name + "=";
        int start = url.indexOf(name);
        if (start < 0) {
            return null;
        }
        start += name.length();
        int end = url.indexOf("&", start);
        if (end == -1) {
            end = url.length();
        }
        return url.substring(start, end);
    }
}