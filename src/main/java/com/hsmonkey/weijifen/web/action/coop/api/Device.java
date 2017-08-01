package com.hsmonkey.weijifen.web.action.coop.api;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.hsmonkey.weijifen.biz.ao.ApiAO;
import com.hsmonkey.weijifen.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年4月1日  下午8:38:49</p>
 * <p>作者：niepeng</p>
 */
public class Device extends BaseAction {
	
	private ApiAO apiAO;

	// 设备列表
	public void list(FlowData flowData, Context context) {
		String accessToken = flowData.getParameters().getString("accessToken");
		Result result = apiAO.getDeviceList(flowData, accessToken);
		handleApiResult(result, flowData, context);
	}

	// 实时数据
	public void intimeData(FlowData flowData, Context context) {
		String accessToken = flowData.getParameters().getString("accessToken");
		String snaddrs = flowData.getParameters().getString("snaddrs");
		Result result = apiAO.intimeData(flowData, accessToken, snaddrs);
		handleApiResult(result, flowData, context);
	}

	// 实时数据包含大气压接口
	public void intimeDataHasPress(FlowData flowData, Context context) {
		String accessToken = flowData.getParameters().getString("accessToken");
		String snaddrs = flowData.getParameters().getString("snaddrs");
		Result result = apiAO.intimeData(flowData, accessToken, snaddrs);
		handleApiResult(result, flowData, context);
	}

	public void setApiAO(ApiAO apiAO) {
		this.apiAO = apiAO;
	}
	
}
