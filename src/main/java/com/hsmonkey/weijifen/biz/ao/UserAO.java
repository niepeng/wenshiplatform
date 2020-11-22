package com.hsmonkey.weijifen.biz.ao;

import com.hsmonkey.weijifen.biz.bean.AdminBean;
import java.util.Date;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;

import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
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

	public Result register(FlowData flowData, AdminBean userBean);

	public Result viewUpdatePsw(FlowData flowData);

	public Result doUpdatePsw(FlowData flowData, UserBean userBean);

	public Result index(FlowData flowData, String area);
	
	public Result historyData(FlowData flowData, DeviceDataBean deviceDataBean);
	public Result historyCurve(FlowData flowData, DeviceDataBean deviceDataBean);
	public Result historyDataExport(FlowData flowData, DeviceDataBean deviceDataBean, String exportType);

	public Result alarmList(FlowData flowData);
	public Result alarmList2(FlowData flowData);
	public Result writeAlarmNote(FlowData flowData);

	public Result alarmHistoryList(FlowData flowData, AlarmQuery alarmQuery);
	public Result alarmHistoryList2(FlowData flowData, AlarmQuery alarmQuery);

	public Result deviceList(FlowData flowData, DeviceQuery deviceQuery);

	public Result doEditArea(FlowData flowData, String oldArea, String newArea);

	public Result areaList(FlowData flowData);
	
	public Result viewAddDevice(FlowData flowData);

	public Result addDevice(FlowData flowData, DeviceBean deviceBean);

	public Result viewEditDevice(FlowData flowData, String snaddr);
	public Result doEditDevice(FlowData flowData, DeviceBean deviceBean);
	public Result deleteDevice(FlowData flowData, String snaddr);

	public Result viewPermissionDevice(FlowData flowData, String snaddr);
	public Result doOptPermissionDevice(FlowData flowData, String snaddr, String newUser);

	public Result viewAlarmDevice(FlowData flowData, String snaddr);
	public Result doAlarmDevice(FlowData flowData, String snaddr, String[] smsPhones,  boolean open);

	public Result alarmManage(FlowData flowData);
	public Result doAddAlarmPhone(FlowData flowData, String phone);
	public Result deleteAlarmPhone(FlowData flowData, String phone);
	public Result optAlarmTotalMenu(FlowData flowData, boolean open);
	public Result optDetailAlarm(FlowData flowData);


	public Result showBindMail(FlowData flowData);

	public Result bindMail(FlowData flowData, String mail);

	public Result bindRemoveMail(FlowData flowData);


	public Result doFindPsw(FlowData flowData, String user);

	public Result version(FlowData flowData);

	public Result showUserDetail(FlowData flowData);

	/**
	 * 获取最近报警的信息
	 * @param flowData
	 * @param user
	 * @return
	 */
	public Result jsonRecentlyAlarmList(FlowData flowData, String user, Date requestTime);
}
