package com.hsmonkey.weijifen.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentUtil {
	protected static final Logger log = LoggerFactory.getLogger(ContentUtil.class);

	public static String getContent(String url, int timeout, String charset) {
		String response = "";
		try {
			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();

			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(is, charset));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			response = buffer.toString();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	public static String getContent(String url, int timeout, String charset, boolean isStopRedirect) {
	    String response = "";
	    String cookie = "";
	    try {
	        URL serverUrl = new URL(url);
	        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
	        if(isStopRedirect){
	            if(cookie.length() != 0){
	                conn.setRequestProperty("Cookie", cookie);
	            }
	            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0)");
	            conn.setInstanceFollowRedirects(false);
	        }

	        conn.setConnectTimeout(timeout);
	        conn.setReadTimeout(timeout);
	        conn.setRequestMethod("GET");
	        conn.setDoOutput(true);
	        conn.connect();

	        InputStream is = conn.getInputStream();

	        BufferedReader in = new BufferedReader(new InputStreamReader(is, charset));
	        StringBuffer buffer = new StringBuffer();
	        String line = "";
	        while ((line = in.readLine()) != null) {
	            buffer.append(line);
	        }
	        response = buffer.toString();
	        conn.disconnect();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return response;

	}


	public static String getContent(String url, String referUrl, int timeout, String charset) {
		String response = "";
		try {
			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			conn.addRequestProperty("Referer", referUrl);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(is, charset));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			response = buffer.toString();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}


    public static String getContent(String url, int timeout) {
        return getContent(url, timeout, "UTF-8");
    }

    public static String getContent(String url, int timeout, boolean isStopRedirect) {
        return getContent(url, timeout, "UTF-8", isStopRedirect);
    }

	public static String getContent(String url, String referUrl, int timeout) {
		return getContent(url, referUrl,timeout, "UTF-8");
	}
}
