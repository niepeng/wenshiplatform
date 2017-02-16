package com.hsmonkey.weijifen.biz.ao;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;

import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.biz.query.AlarmQuery;
import com.hsmonkey.weijifen.biz.query.DeviceQuery;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年1月19日  下午2:28:32</p>
 * <p>作者：niepeng</p>
 */
public interface UserAO {

	public Result login(FlowData flowData, UserBean userBean);

	public Result viewUpdatePsw(FlowData flowData);

	public Result doUpdatePsw(FlowData flowData, UserBean userBean);

	public Result index(FlowData flowData, String area);

	public Result alarmList(FlowData flowData);

	public Result alarmHistoryList(FlowData flowData, AlarmQuery alarmQuery);

	public Result deviceList(FlowData flowData, DeviceQuery deviceQuery);
	
	public Result viewEditDevice(FlowData flowData, String snaddr);

	public Result doEditDevice(FlowData flowData, DeviceBean deviceBean);
	
	public Result showBindMail(FlowData flowData);

	public Result bindMail(FlowData flowData, String mail);

	public Result doFindPsw(FlowData flowData, String user);
}
