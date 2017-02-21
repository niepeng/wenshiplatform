package com.hsmonkey.weijifen;

import java.util.HashMap;
import java.util.Map;

import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.common.http.HttpClient;
import com.hsmonkey.weijifen.util.JsonUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年2月21日  下午10:13:42</p>
 * <p>作者：niepeng</p>
 */
public class TestAPI {
	
	protected static final String API_URL = "http://42.121.53.218:2500";
	protected static HttpClient client = new HttpClient(false);

	public static void main(String[] args) {
		String snaddr = "W2000901";
		String user = "xsf";
		String ac = "5d68371a";
		getDevice(user, snaddr);
		
//		addDevice(user, snaddr, ac);
//		deleteDevice(user, snaddr);
	}

	private static void getDevice(String user, String snaddr) {
		DeviceBean deviceBean = new DeviceBean();
		deviceBean.setUser(user);
		deviceBean.setSnaddr(snaddr);
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "getDevInfo");
		String body = JsonUtil.fields("snaddr", deviceBean);
		String content = client.subPostFrom(API_URL, body, "utf-8", headerMap);
		System.out.println("getDeviceResult=" + content);
	}

	private static void deleteDevice(String user, String snaddr) {
		DeviceBean deviceBean = new DeviceBean();
		deviceBean.setSnaddr(snaddr);
		deviceBean.setUser(user);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "delDevice");
		String body = JsonUtil.fields("snaddr,user", deviceBean);
		String content = client.subPostFrom(API_URL, body, "utf-8", headerMap);
		System.out.println(content);
	}

	private static void addDevice(String user, String snaddr, String ac) {
		DeviceBean deviceBean = new DeviceBean();
		deviceBean.setAc(ac);
		deviceBean.setSnaddr(snaddr);
		deviceBean.setDevName("niepengadd");
		deviceBean.setUser(user);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "addDeviceBySN");
		String body = JsonUtil.fields("snaddr,user,ac,devName", deviceBean);
		String content = client.subPostFrom(API_URL, body, "utf-8", headerMap);
		System.out.println("addDevice: "+content);
	}
	
	
}
