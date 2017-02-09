package com.hsmonkey.weijifen.biz.test;

import com.hsmonkey.weijifen.common.http.HttpClient;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年4月20日  下午3:40:07</p>
 * <p>作者：niepeng</p>
 */
public class TestHttpClient {

	public static void main(String[] args) {
		HttpClient client = new HttpClient(true);
		String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		String content = client.subGet(url, "utf-8");
		System.out.println(content);
	}
}
