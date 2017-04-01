package com.hsmonkey.weijifen.web.action.coop.api;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.hsmonkey.weijifen.biz.ao.ApiAO;
import com.hsmonkey.weijifen.web.action.BaseAction;

/**
 * <p>标题: 获取accessToken</p>
 * <p>描述: </p>
 * <p>创建时间: 2017年4月1日  下午8:39:44</p>
 * <p>作者：niepeng</p>
 */
public class Token extends BaseAction {
	
	private ApiAO apiAO;

	public void execute(FlowData flowData, Context context) {
		String user = flowData.getParameters().getString("user");
		String psw = flowData.getParameters().getString("psw");
		Result result = apiAO.getAccessToken(flowData, user, psw);
		handleApiResult(result, flowData, context);
	}

	public void setApiAO(ApiAO apiAO) {
		this.apiAO = apiAO;
	}
	
}
