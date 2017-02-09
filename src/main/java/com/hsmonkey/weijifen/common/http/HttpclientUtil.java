package com.hsmonkey.weijifen.common.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.hsmonkey.weijifen.biz.bean.CookieBean;
import com.hsmonkey.weijifen.util.ScriptEngineUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年1月11日  上午11:28:45</p>
 * <p>作者：niepeng</p>
 */
public class HttpclientUtil {
	
	public static int appkey = 12574478;
	
	private static HttpClient client = new HttpClient(true);
	
	
	public static HttpClient getQiangHttpclient() {
		return client;
	}

	public static HttpClient getQiangHttpclient(CookieBean bean) {
		client.clearCookies();
//		client.setCookie("_m_h5_tk", bean.get_m_h5_tk(), ".taobao.com", "/");
//		client.setCookie("_m_h5_tk_enc", bean.get_m_h5_tk_enc(), ".taobao.com", "/");
		client.setCookie("_m_h5_tk", bean.get_m_h5_tk(), "api.m.taobao.com", "/");
		client.setCookie("_m_h5_tk_enc", bean.get_m_h5_tk_enc(), "api.m.taobao.com", "/");
		return client;
	}
	
	public static String genUrl(String cookiePrefix, long t, int page) {
		int offset = page * 50;
		return "https://api.m.taobao.com/h5/mtop.msp.qianggou.queryitembyordertype/1.0/?v=1.0&api=mtop.msp.qianggou.queryItemByOrderType&appKey=" + appkey + "&t=" + t + "&callback=a&type=jsonp&sign="
				+ genQiangSign(cookiePrefix, t, offset) + "&data=%7B%22orderType%22%3A%22sales%22%2C%22offset%22%3A" + offset + "%2C%22limit%22%3A50%7D";
	}
	
	public static String getSign(long t, String data) {
		String value = client.getCookie("_m_h5_tk");
		String cookiePrefix = null;
		if(value != null) {
			cookiePrefix = value.split("_")[0];
		}
		if(cookiePrefix == null) {
			return null;
		}
		String prepareKey = cookiePrefix + "&" + t + "&" + appkey + "&" + data;
		return ScriptEngineUtil.taoEncryption(prepareKey);
	}
	
	
	public static String getSign(String cookiePrefix, long t, String data) {
		String prepareKey = cookiePrefix + "&" + t + "&" + appkey + "&" + data;
		return ScriptEngineUtil.taoEncryption(prepareKey);
	}

	public static String genQiangSign(String string, long t, int offset) {
		return genQiangSign(string, t, appkey, offset);
	}
	
	private static String genQiangSign(String string, long t, int appKey, int offset) {
		String prepareKey = string + "&" + t + "&" + appKey + "&" + "{\"orderType\":\"sales\",\"offset\":" + offset + ",\"limit\":50}";
		return ScriptEngineUtil.taoEncryption(prepareKey);
	}
	
	public static void main1(String[] args) {
		System.out.println(System.currentTimeMillis());
		// 1d0f43819a0ee64fc66193fe74eb5d02_1452582063630
//		String prepareKey = "1d0f43819a0ee64fc66193fe74eb5d02&1452580885853&12574478&{\"batchId\":\"14863\"}";
		String prepareKey = "1d0f43819a0ee64fc66193fe74eb5d02&1452582372049&12574478&{\"batchId\":\"14918\"}";
		String value = ScriptEngineUtil.taoEncryption(prepareKey);
		System.out.println(value);
	}
	
	public static void main2(String[] args) {
//		String cookie = "38b3b507bdf380a61747f92b700057be_1452587097655";
//		client.setCookie("_m_h5_tk", cookie, ".taobao.com", "/");
//		client.setCookie("_m_h5_tk_enc", "451060dd95578052f1ae92907259f15b", ".taobao.com", "/");
//		long t = DateUtil.parse("2016-01-11 16:23:23").getTime();
//		String data = "{\"batchId\":\"14918\"}";
//		String sign = getSign(cookie.split("_")[0], t, data);
//		String url = "https://api.m.taobao.com/h5/mtop.msp.qianggou.queryitembybatchid/3.1/?v=3.1&api=mtop.msp.qianggou.queryItemByBatchId&appKey=12574478&t="+t+"&callback=mtopjsonp1&type=jsonp&sign="+sign+"&data=" + StringsUtil.urlEecode(data);
//		String content = client.subGet(url, "https://qiang.taobao.com", "utf-8");
//		System.out.println(content);
		String content = client.subGet("https://qiang.taobao.com/", "utf-8");
		System.out.println(content);
		Document doc = Jsoup.parse(content);
		Element element = doc.getElementsByClass("scrollable-panel").get(0);
		for (Element childElement : element.children()) {
			String batchId = childElement.attr("data-batchId");
			System.out.println(batchId);
			Element timeElement = childElement.getElementsByClass("time").get(0);
			System.out.println(timeElement.html());
			Element statusElement = childElement.getElementsByClass("text").get(0);
			System.out.println(statusElement.html());
		}
	}

}
