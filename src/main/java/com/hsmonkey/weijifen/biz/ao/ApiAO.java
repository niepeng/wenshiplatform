package com.hsmonkey.weijifen.biz.ao;

import wint.help.biz.result.Result;

import wint.mvc.flow.FlowData;

/**
 * <p>标题: 对外开放的api接口</p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年4月1日  下午8:45:11</p>
 * <p>作者：niepeng</p>
 */
public interface ApiAO {

	/**
	 * 获取某个用户的accessToken的值
	 * 
	 * @param flowData
	 * @param user
	 * @param psw
	 * @return
	 */
	public Result getAccessToken(FlowData flowData, String user, String psw);
	
	/**
	 * 获取accessToken这个对应用户的设备列表
	 * 
	 * @param flowData
	 * @param accessToken
	 * @return
	 */
	public Result getDeviceList(FlowData flowData, String accessToken);

	/**
	 * 根据提供的snaddrs，获取实时数据
	 * 
	 * @param flowData
	 * @param accessToken
	 * @param snaddrs，以英文逗号分隔的snaddr数据集
	 * @return
	 */
	public Result intimeData(FlowData flowData, String accessToken, String snaddrs);
}
