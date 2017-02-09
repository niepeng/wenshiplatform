package com.hsmonkey.weijifen.web.action;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultCode;
import wint.help.biz.result.ResultSupport;
import wint.help.biz.result.results.CommonResultCodes;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.Session;
import wint.mvc.template.Context;

import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.common.SessionKeys;
import com.hsmonkey.weijifen.common.http.HttpClient;

/**
 * @author niepeng
 *
 */
public class BaseAction {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected static HttpClient client = new HttpClient(false);
	
	protected static final String API_URL = "http://42.121.53.218:2500";

	protected boolean checkUserSession(FlowData flowData, Context context) {
		Session session = flowData.getSession();
		String userName = (String) session.getAttribute(SessionKeys.USER_NAME);
		if (StringUtil.isBlank(userName)) {
			return false;
		}
		return true;
	}
	
	protected UserBean getUserBean(FlowData flowData) {
		UserBean userBean = new UserBean();
		Session session = flowData.getSession();
		userBean.setUser((String) session.getAttribute(SessionKeys.USER_NAME));
		if (StringUtil.isBlank(userBean.getUser())) {
			return null;
		}
		return userBean;
	}
	
	

	protected boolean checkUserSessionNeedRedrect(FlowData flowData, Context context) {
		boolean isSuccess = checkUserSession(flowData, context);
		if (!isSuccess) {
			flowData.redirectTo("userModule", "login");
			return false;
		}
		return true;
	}

	protected void handleResult(Result result, FlowData flowData, Context context) {
		if(!result.isSuccess()) {
		    result2Context(result, context);
			handleError(result, flowData, context);
			return;
		}
		result2Context(result, context);
	}

	protected void handleError(Result result, FlowData flowData, Context context) {
		ResultCode resultCode = result.getResultCode();

		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		}
//		else if (AdminResultCodes.USER_NOT_LOGIN == resultCode) {
//			flowData.redirectTo("adminModule", "login");
//			return;
//		}
		flowData.redirectTo("baseModule", "message").param("msg", urlEncode(resultCode.getMessage()));
	}
	
	 protected String urlEncode(String msg) {
			try {
				return URLEncoder.encode(msg, "utf-8");
			} catch (Exception e) {
			}
			return null;
		}

	    protected String urlDecode(String msg) {
			if (msg == null) {
				return null;
			}

			try {
				return URLDecoder.decode(msg, "utf-8");
			} catch (Exception e) {
			}
			return null;
		}


	protected void result2Context(Result result, Context context) {
		for (Map.Entry<String, Object> entry : result.getModels().entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
		ResultCode resultCode = result.getResultCode();
		// 这里可能是上一个页面重定向过来的，保留错误信息，如：修改操作失败后返回到编辑页面
		if(resultCode != null) {
			context.put("resultmessage", resultCode);
		}

	}

}