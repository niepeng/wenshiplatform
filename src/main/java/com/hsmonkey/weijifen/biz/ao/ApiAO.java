package com.hsmonkey.weijifen.biz.ao;

import wint.help.biz.result.Result;

import wint.mvc.flow.FlowData;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年4月1日  下午8:45:11</p>
 * <p>作者：niepeng</p>
 */
public interface ApiAO {

	public Result getAccessToken(FlowData flowData, String user, String psw);
}
