package com.hsmonkey.weijifen.web.action;

import java.util.Date;

import com.hsmonkey.weijifen.util.DateUtil;

import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

public class Index extends BaseAction {
	
	static boolean flag = true;

	public void execute(FlowData flowData, Context context) {
		context.put("key", "context put key is success,time=" + DateUtil.format(new Date()));
//		if(flag) {
//			flag = false;
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					if(Login_Browser.isNeedCatchCookie) {
//						Login_Browser.main(null);
//						SystemClock.sleep(20 * 1000);
//					}
//				}
//			}).start();
//		}
//		SystemClock.sleep(60* 1000);
//		context.put("cookie", Login_Browser.cookie);
//		context.put("cookieTime", Login_Browser.cookieTime);
	}

}
