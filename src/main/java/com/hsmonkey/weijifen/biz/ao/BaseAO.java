package com.hsmonkey.weijifen.biz.ao;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import wint.help.codec.MD5;

import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.biz.dal.daointerface.ApiAccessTokenDAO;
import com.hsmonkey.weijifen.biz.dal.daointerface.KeyValueDAO;
import com.hsmonkey.weijifen.util.JsonUtil;
import com.hsmonkey.weijifen.web.action.BaseAction;

public class BaseAO extends BaseAction {
	
	protected KeyValueDAO keyValueDAO;
	
	protected ApiAccessTokenDAO apiAccessTokenDAO;
	
	protected boolean isSuccess(String content) {
		JSONObject json = JsonUtil.getJsonObject(content);
		return JsonUtil.getInt(json, "code", -1) == 0;
	}
	
	protected String loginCall(UserBean userBean) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "login");
		userBean.setPassword(MD5.encrypt(userBean.getPassword()));
		String body = JsonUtil.fields("user,password", userBean);
		return client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
	}

	public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}

	public void setApiAccessTokenDAO(ApiAccessTokenDAO apiAccessTokenDAO) {
		this.apiAccessTokenDAO = apiAccessTokenDAO;
	}
	
}
