package com.hsmonkey.weijifen.web.action;

import java.util.Date;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.Session;
import wint.mvc.form.Form;
import wint.mvc.module.annotations.Action;
import wint.mvc.template.Context;

import com.hsmonkey.weijifen.biz.ao.UserAO;
import com.hsmonkey.weijifen.biz.bean.AdminBean;
import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
import com.hsmonkey.weijifen.biz.bean.DeviceExtendBean;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.biz.query.AlarmQuery;
import com.hsmonkey.weijifen.biz.query.DeviceQuery;
import com.hsmonkey.weijifen.common.SessionKeys;
import com.hsmonkey.weijifen.util.DateUtil;
import com.hsmonkey.weijifen.web.common.excel.ExcelUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: </p>
 * <p>创建时间: 2017年1月19日  下午2:21:56</p>
 * <p>作者：niepeng</p>
 */
public class User extends BaseAction {
	
	private UserAO userAO;

	public void login(FlowData flowData, Context context) {
		if (checkUserSession(flowData, context)) {
			flowData.redirectTo("userModule", "index");
			return;
		}
	}

	@Action(defaultTarget="user/login")
	public void doLogin(FlowData flowData, Context context) {
		Form form = flowData.getForm("login");
		AdminBean bean = new AdminBean();
		if (!form.apply(bean)) {
			return;
		}
		UserBean user = new UserBean();
		user.setUser(bean.getUserName());
		user.setPassword(bean.getPsw());

		Result result = userAO.login(flowData, user);
		if (result.isSuccess()) {
			flowData.redirectTo("userModule", "index");
			return;
		}
		result2Context(result, context);
	}
	
	public void updatePsw(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		String msg = urlDecode(flowData.getParameters().getString("msg"));
		Result result = userAO.viewUpdatePsw(flowData);
		handleResult(result, flowData, context);
		context.put("msg", msg);
	}
	
	public void doUpdatePsw(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		UserBean userBean = new UserBean();
		userBean.setPassword(flowData.getParameters().getString("password"));
		userBean.setNewPsw(flowData.getParameters().getString("newPsw"));
		userBean.setNewPsw2(flowData.getParameters().getString("newPsw2"));
		Result result = userAO.doUpdatePsw(flowData, userBean);
		if (result.isSuccess()) {
			flowData.redirectTo("userModule", "loginOut").param("msg", urlEncode("修改密码成功"));
			return;
		}
		String msg = "操作失败";
		if (result.getResultCode() != null) {
			msg = result.getResultCode().getMessage();
		}
		flowData.redirectTo("userModule", "updatePsw").param("msg", urlEncode(msg));
	}
	
	
	public void index(FlowData flowData, Context context) {
		if(!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		String area = flowData.getParameters().getString("area");
		Result result = userAO.index(flowData, area);
		handleResult(result, flowData, context);
	}
	
	public void historyData(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}

		DeviceDataBean deviceDataBean = new DeviceDataBean();
		deviceDataBean.setSnaddr(flowData.getParameters().getString("snaddr"));
		deviceDataBean.setStartTime(flowData.getParameters().getString("startTime"));
		deviceDataBean.setEndTime(flowData.getParameters().getString("endTime"));
		deviceDataBean.setRangeTime(flowData.getParameters().getString("rangeTime"));

		Result result = userAO.historyData(flowData, deviceDataBean);
		handleResult(result, flowData, context);
	}
	
	public void historyDataExport(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}

		DeviceDataBean deviceDataBean = new DeviceDataBean();
		deviceDataBean.setSnaddr(flowData.getParameters().getString("snaddr"));
		deviceDataBean.setStartTime(flowData.getParameters().getString("startTime"));
		deviceDataBean.setEndTime(flowData.getParameters().getString("endTime"));
		deviceDataBean.setRangeTime(flowData.getParameters().getString("rangeTime"));
		// pdf or excel
		String exportType = flowData.getParameters().getString("exportType");
		Result result = userAO.historyDataExport(flowData, deviceDataBean, exportType);
		if (result.isSuccess()) {
			DeviceBean deviceBean = (DeviceBean) result.getModels().get("deviceBean");
			if ("excel".equals(exportType)) {
				handleExcel(flowData, result, ExcelUtil.genExcel(deviceBean), "export-" + DateUtil.format(new Date(), DateUtil.DATE_YMDHMS) + ".xls", "utf-8");
				return;
			}

			if ("pdf".equals(exportType)) {
				handlePdf(flowData, result, deviceBean, "export-" + DateUtil.format(new Date(), DateUtil.DATE_YMDHMS) + ".pdf", "utf-8");
				return;
			}
		}
		handleResult(result, flowData, context);
	}
	
	public void alarmList(FlowData flowData, Context context) {
		if(!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		Result result = userAO.alarmList(flowData);
		handleResult(result, flowData, context);
	}
	
	public void alarmHistoryList(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		AlarmQuery alarmQuery = new AlarmQuery();
		alarmQuery.setSnaddr(flowData.getParameters().getString("snaddr"));
		alarmQuery.setStartTime(flowData.getParameters().getString("startTime"));
		alarmQuery.setEndTime(flowData.getParameters().getString("endTime"));
		
		Result result = userAO.alarmHistoryList(flowData, alarmQuery);
		handleResult(result, flowData, context);
	}
	

	public void deviceList(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		String msg = urlDecode(flowData.getParameters().getString("msg"));
		DeviceQuery deviceQuery = new DeviceQuery();
		deviceQuery.setArea(flowData.getParameters().getString("area"));
		deviceQuery.setDeviceName(flowData.getParameters().getString("deviceName"));
		
		Result result = userAO.deviceList(flowData, deviceQuery);
		context.put("msg", msg);
		handleResult(result, flowData, context);
	}
	
	public void addDevice(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}

		String msg = urlDecode(flowData.getParameters().getString("msg"));
		Result result = userAO.viewAddDevice(flowData);
		context.put("msg", msg);
		handleResult(result, flowData, context);
	}
	
	public void doAddDevice(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		
		DeviceBean deviceBean = new DeviceBean();
		deviceBean.setSnaddr(flowData.getParameters().getString("snaddr"));
		deviceBean.setAc(flowData.getParameters().getString("ac"));
		deviceBean.setArea(flowData.getParameters().getString("area"));
		deviceBean.setDevName(flowData.getParameters().getString("devName"));
//		deviceBean.setDevGap(flowData.getParameters().getString("devGap"));
//		DeviceExtendBean deviceExtendBean = new DeviceExtendBean();
//		deviceExtendBean.setSnaddr(deviceBean.getSnaddr());
//		deviceExtendBean.setMaxTemp(flowData.getParameters().getString("maxTemp"));
//		deviceExtendBean.setMinTemp(flowData.getParameters().getString("minTemp"));
//		deviceExtendBean.setTempHC(flowData.getParameters().getString("tempHC"));
//		deviceExtendBean.setMaxHumi(flowData.getParameters().getString("maxHumi"));
//		deviceExtendBean.setMinHumi(flowData.getParameters().getString("minHumi"));
//		deviceExtendBean.setHumiHC(flowData.getParameters().getString("humiHC"));
//		deviceBean.setDeviceExtendBean(deviceExtendBean);

		Result result = userAO.addDevice(flowData, deviceBean);
		if (result.isSuccess()) {
			flowData.redirectTo("userModule", "deviceList").param("msg", urlEncode("操作成功"));
			return;
		}
		String str = "操作失败";
		if (result.getResultCode() != null) {
			str = result.getResultCode().getMessage();
		}
		flowData.redirectTo("userModule", "addDevice").param("msg", urlEncode(str));
		
	}
	
	
	public void editDevice(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		String snaddr = flowData.getParameters().getString("snaddr");
		Result result = userAO.viewEditDevice(flowData, snaddr);
		handleResult(result, flowData, context);
	}
	
	public void doEditDevice(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}

		DeviceBean deviceBean = new DeviceBean();
		deviceBean.setSnaddr(flowData.getParameters().getString("snaddr"));
		deviceBean.setArea(flowData.getParameters().getString("area"));
		deviceBean.setDevName(flowData.getParameters().getString("devName"));
		deviceBean.setDevGap(flowData.getParameters().getString("devGap"));
		DeviceExtendBean deviceExtendBean = new DeviceExtendBean();
		deviceExtendBean.setSnaddr(deviceBean.getSnaddr());
		deviceExtendBean.setMaxTemp(flowData.getParameters().getString("maxTemp"));
		deviceExtendBean.setMinTemp(flowData.getParameters().getString("minTemp"));
		deviceExtendBean.setTempHC(flowData.getParameters().getString("tempHC"));
		deviceExtendBean.setMaxHumi(flowData.getParameters().getString("maxHumi"));
		deviceExtendBean.setMinHumi(flowData.getParameters().getString("minHumi"));
		deviceExtendBean.setHumiHC(flowData.getParameters().getString("humiHC"));
		deviceBean.setDeviceExtendBean(deviceExtendBean);

		Result result = userAO.doEditDevice(flowData, deviceBean);
		if (result.isSuccess()) {
			flowData.redirectTo("userModule", "deviceList").param("msg", urlEncode("操作成功"));
			return;
		}
		String str = "操作失败";
		if (result.getResultCode() != null) {
			str = result.getResultCode().getMessage();
		}
		flowData.redirectTo("userModule", "deviceList").param("msg", urlEncode(str));
	}
	
	public void deleteDevice(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		String snaddr = flowData.getParameters().getString("snaddr");
		Result result = userAO.deleteDevice(flowData, snaddr);
		String str = "操作成功";
		if (result.getResultCode() != null) {
			str = result.getResultCode().getMessage();
		}
		flowData.redirectTo("userModule", "deviceList").param("msg", urlEncode(str));
	}
	
	public void bindMail(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}
		String msg = flowData.getParameters().getString("msg");
		Result result = userAO.showBindMail(flowData);
		context.put("msg", urlDecode(msg));
		handleResult(result, flowData, context);
	}
	
	public void doBindMail(FlowData flowData, Context context) {
		if (!checkUserSessionNeedRedrect(flowData, context)) {
			return;
		}

		String mail = flowData.getParameters().getString("mail");
		Result result = userAO.bindMail(flowData, mail);
		String msg = null;
		if (result.isSuccess()) {
			msg = (String) result.getModels().get("msg");
		} else {
			msg = result.getResultCode().getMessage();
		}
		flowData.redirectTo("userModule", "bindMail").param("msg", urlEncode(msg));
	}

		
		
	
	public void loginOut(FlowData flowData, Context context) {
		Result result = new ResultSupport(true);
		String msg = urlDecode(flowData.getParameters().getString("msg"));
		Session session = flowData.getSession();
		session.removeAttribute(SessionKeys.USER_NAME);
		context.put("msg", msg);
		if (result.isSuccess()) {
//			flowData.redirectTo("userModule", "login");
			return;
		}
		handleResult(result, flowData, context);
	}
	
	public void findPsw(FlowData flowData, Context context) {
		String msg = flowData.getParameters().getString("msg");
		context.put("msg", urlDecode(msg));
	}
	
	public void doFindPsw(FlowData flowData, Context context) {
		String user = flowData.getParameters().getString("user");
		Result result = userAO.doFindPsw(flowData, user);
		String msg = null;
		if (result.isSuccess()) {
			msg = (String) result.getModels().get("msg");
		} else {
			msg = result.getResultCode().getMessage();
		}
		flowData.redirectTo("userModule", "findPsw").param("msg", urlEncode(msg));
	}
	
	public void version(FlowData flowData, Context context) {
		Result result = userAO.version(flowData);
		handleJsonResult(result, flowData, context);
	}
	
	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}
	
}
