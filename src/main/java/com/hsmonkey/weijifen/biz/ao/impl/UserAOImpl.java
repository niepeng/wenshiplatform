package com.hsmonkey.weijifen.biz.ao.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.biz.result.StringResultCode;
import wint.help.codec.MD5;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.Session;

import com.hsmonkey.weijifen.biz.ao.BaseAO;
import com.hsmonkey.weijifen.biz.ao.UserAO;
import com.hsmonkey.weijifen.biz.bean.AlarmBean;
import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
import com.hsmonkey.weijifen.biz.bean.DeviceExtendBean;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.biz.query.AlarmQuery;
import com.hsmonkey.weijifen.biz.query.DeviceQuery;
import com.hsmonkey.weijifen.common.SessionKeys;
import com.hsmonkey.weijifen.util.ChangeUtil;
import com.hsmonkey.weijifen.util.CollectionUtils;
import com.hsmonkey.weijifen.util.DateUtil;
import com.hsmonkey.weijifen.util.JsonUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年1月19日  下午2:32:58</p>
 * <p>作者：niepeng</p>
 */
public class UserAOImpl extends BaseAO implements UserAO {

	@Override
	public Result login(FlowData flowData, UserBean userBean) {
		Result result = new ResultSupport(false);
		try {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "login");
			userBean.setPassword(MD5.encrypt(userBean.getPassword()));
			String body = JsonUtil.fields("user,password", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (isSuccess(content)) {
				Session session = flowData.getSession();
				session.setAttribute(SessionKeys.USER_NAME, userBean.getUser());
				result.setSuccess(true);
				return result;
			}

			result.setResultCode(new StringResultCode("用户名或密码错误"));

		} catch (Exception e) {
			log.error("loginError", e);
		}
		return result;
	}
	
	@Override
	public Result viewUpdatePsw(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			result.getModels().put("userBean", userBean);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewUpdatePswError", e);
		}
		return result;
	}


	@Override
	public Result doUpdatePsw(FlowData flowData, UserBean userBean) {
		Result result = new ResultSupport(false);
		try {
			UserBean fromSessionUser = getUserBean(flowData);
			
			if(StringUtil.isBlank(userBean.getPassword())) {
				result.setResultCode(new StringResultCode("原密码不能为空"));
				return result;
			}
			
			if(StringUtil.isBlank(userBean.getNewPsw()) || StringUtil.isBlank(userBean.getNewPsw())) {
				result.setResultCode(new StringResultCode("新密码和确认密码都不能为空"));
				return result;
			}
			
			if(!userBean.getNewPsw().equals(userBean.getNewPsw2())) {
				result.setResultCode(new StringResultCode("新密码和确认密码不一致"));
				return result;
			}
			
			if(userBean.getNewPsw().length() < 6) {
				result.setResultCode(new StringResultCode("新密码长度至少需要6位"));
				return result;
			}
			
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "login");
			userBean.setUser(fromSessionUser.getUser());
			userBean.setPassword(MD5.encrypt(userBean.getPassword()));
			String body = JsonUtil.fields("user,password", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("原密码错误"));
				return result;
			}

			headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "modifyPass");
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("user", fromSessionUser.getUser());
			argsMap.put("oldPass", userBean.getPassword());
			argsMap.put("newPass", MD5.encrypt(userBean.getNewPsw()));
			body = JsonUtil.mapToJson(argsMap);
			System.out.println(body);
			content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			System.out.println("updatePsw:" + content);
			if(isSuccess(content)) {
				result.setSuccess(true);
				return result;
			}
			result.setResultCode(new StringResultCode("修改失败，请先绑定邮箱才能修改密码，稍后再试"));

		} catch (Exception e) {
			log.error("doUpdatePswError", e);
		}
		return result;
	}

	@Override
	public Result index(FlowData flowData, String area) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			// 1.获取用户名下所有设备列表,区域列表
			List<DeviceBean> beanList = getAllDevice(userBean);
			List<String> areaList = getAllArea(userBean);

			if(!CollectionUtils.isEmpty(beanList) && !CollectionUtils.isEmpty(areaList) && !StringUtil.isBlank(area)) {
				for (int i = 0; i < beanList.size();) {
					if (area.equals(beanList.get(i).getArea())) {
						i++;
						continue;
					}
					beanList.remove(i);
				}
			}
			
			// 2.获取每一个设备的实时温湿度获取
			DeviceDataBean dataBean = null;
			for(DeviceBean bean : beanList) {
				if(!StringUtil.isBlank(bean.getSnaddr())) {
					dataBean = requestDeviceDataBean(bean);
					bean.setDataBean(dataBean);
				}
			}
			
			Collections.sort(beanList, new Comparator<DeviceBean>() {
				@Override
				public int compare(DeviceBean o1, DeviceBean o2) {
					if (o1.getDataBean() == null) {
						return 1;
					}

					if (o2.getDataBean() == null) {
						return -1;
					}

					if (o1.getDataBean().getAbnormal().equals(o2.getDataBean().getAbnormal())) {
						return o1.getSnaddr().compareTo(o2.getSnaddr());
					}

					if (o1.getDataBean().isSuccess()) {
						return -1;
					}

					if (o2.getDataBean().isSuccess()) {
						return 1;
					}
					return o1.getSnaddr().compareTo(o2.getSnaddr());
				}
			});
			
			boolean showMp3 = false;
			if(beanList != null) {
				for(DeviceBean bean: beanList) {
					if(bean.getDataBean() != null && bean.getDataBean().getTempStatus() != 0) {
						showMp3 = true;
						break;
					}
				}
			}
			
			result.getModels().put("beanList", beanList);
			result.getModels().put("showMp3", showMp3);
			result.getModels().put("userBean", userBean);
			result.getModels().put("areaList", areaList);
			result.getModels().put("area", area);

			result.setSuccess(true);
			
		} catch (Exception e) {
			log.error("indexError", e);
		}
		return result;
	}
	
	@Override
	public Result historyData(FlowData flowData, DeviceDataBean deviceDataBean) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<DeviceBean> beanList = getAllDevice(userBean);
			result.getModels().put("beanList", beanList);
			if(StringUtil.isBlank(deviceDataBean.getSnaddr())) {
				result.setSuccess(true);
				return result;
			}
			
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getHisData");
			String body = JsonUtil.fields("snaddr,startTime,endTime,rangeTime", deviceDataBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			JSONObject jsonObject = JsonUtil.getJsonObject(content);
			JSONArray timeList = JsonUtil.getJsonArray(jsonObject, "timeList");
			JSONArray humiList = JsonUtil.getJsonArray(jsonObject, "humiList");
			JSONArray tempList = JsonUtil.getJsonArray(jsonObject, "tempList");
			if (timeList != null && humiList != null && tempList != null && timeList.length() > 0
					&& timeList.length() == humiList.length() && timeList.length() == tempList.length()) {
				List<DeviceDataBean> dataList = CollectionUtils.newArrayList(timeList.length());
				for (int i = 0, size = timeList.length(); i < size; i++) {
					DeviceDataBean bean = new DeviceDataBean();
					bean.setTemp(tempList.getString(i));
					bean.setHumi(humiList.getString(i));
					bean.setTime(timeList.getString(i));
					dataList.add(bean);
				}
				result.getModels().put("dataList", dataList);
			}
			
			
			result.getModels().put("deviceDataBean", deviceDataBean);
			result.setSuccess(true);
			
		} catch (Exception e) {
			log.error("historyDataError", e);
		}
		return result;
	}
	
	@Override
	public Result historyDataExport(FlowData flowData, DeviceDataBean deviceDataBean, String exportType) {
		Result result = new ResultSupport(false);
		try {
//			UserBean userBean = getUserBean(flowData);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getDevInfo");
			String body = JsonUtil.fields("snaddr", deviceDataBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
			DeviceBean fromDBDeviceBean = JsonUtil.jsonToBean(content, DeviceBean.class);
			
			headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getHisData");
			body = JsonUtil.fields("snaddr,startTime,endTime,rangeTime", deviceDataBean);
			content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			JSONObject jsonObject = JsonUtil.getJsonObject(content);
			JSONArray timeList = JsonUtil.getJsonArray(jsonObject, "timeList");
			JSONArray humiList = JsonUtil.getJsonArray(jsonObject, "humiList");
			JSONArray tempList = JsonUtil.getJsonArray(jsonObject, "tempList");
			if (timeList != null && humiList != null && tempList != null && timeList.length() > 0
					&& timeList.length() == humiList.length() && timeList.length() == tempList.length()) {
				List<DeviceDataBean> dataList = CollectionUtils.newArrayList(timeList.length());
				for (int i = 0, size = timeList.length(); i < size; i++) {
					DeviceDataBean bean = new DeviceDataBean();
					bean.setTemp(tempList.getString(i));
					bean.setHumi(humiList.getString(i));
					bean.setTime(timeList.getString(i));
					dataList.add(bean);
				}
				fromDBDeviceBean.setDeviceDataBeanList(dataList);
			}
			
			if(fromDBDeviceBean != null) {
				fromDBDeviceBean.setDataBean(deviceDataBean);
			}
			
			result.getModels().put("deviceBean", fromDBDeviceBean);
			result.setSuccess(true);
			
		} catch (Exception e) {
			log.error("historyDataExportError", e);
		}
		return result;
	}

	@Override
	public Result alarmList(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<DeviceBean> beanList = CollectionUtils.newArrayList();
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getAccountErr");
			String body = JsonUtil.fields("user", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			JSONArray array = JsonUtil.getJsonArray(content);
			
			JSONObject json = null;
			DeviceBean bean = null;
			JSONArray alarmArray = null;
			AlarmBean alarmBean = null;
			List<AlarmBean> alarmBeanList = null;
			for (int i = 0, size = array.length(); i < size; i++) {
				json = array.getJSONObject(i);
				if (json == null) {
					continue;
				}
				bean = new DeviceBean();
				bean.setSnaddr(JsonUtil.getString(json, "snaddr", null));
				bean.setDevName(JsonUtil.getString(json, "devName", null));
				bean.setArea(JsonUtil.getString(json, "area", null));
				alarmArray = json.getJSONArray("detail");
				alarmBeanList = CollectionUtils.newArrayList();
				for (int j = 0, alarmLenth = alarmArray.length(); j < alarmLenth; j++) {
					alarmBean = JsonUtil.jsonToBean(alarmArray.getJSONObject(j).toString(), AlarmBean.class);
					alarmBeanList.add(alarmBean);
				}
				bean.setAlarmBeanList(alarmBeanList);
				beanList.add(bean);
			}
			
			result.getModels().put("userBean", userBean);
			result.getModels().put("beanList", beanList);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("alarmListError", e);
		}
		return result;
	}
	
	@Override
	public Result alarmHistoryList(FlowData flowData, AlarmQuery alarmQuery) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<DeviceBean> beanList = getAllDevice(userBean);
			if (StringUtil.isBlank(alarmQuery.getSnaddr()) && !CollectionUtils.isEmpty(beanList)) {
				alarmQuery.setSnaddr(beanList.get(0).getSnaddr());
			}
			if (StringUtil.isBlank(alarmQuery.getStartTime()) || StringUtil.isBlank(alarmQuery.getEndTime())) {
				alarmQuery.setStartTime(DateUtil.format(DateUtil.changeMonth(new Date(), -1)));
				alarmQuery.setEndTime(DateUtil.format(new Date()));
			}
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getDeviceErr");

			// alarmQuery.setSnaddr("35340401911111400");
			// alarmQuery.setStartTime("2017-01-19 10:19:00");
			// alarmQuery.setEndTime("2017-01-19 13:19:00");

			String body = JsonUtil.fields("snaddr,startTime,endTime", alarmQuery);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			JSONObject json = JsonUtil.getJsonObject(content);
			DeviceBean bean = null;
			if (json != null) {
				bean = new DeviceBean();
				bean.setSnaddr(JsonUtil.getString(json, "snaddr", null));
				bean.setDevName(JsonUtil.getString(json, "devName", null));
				bean.setArea(JsonUtil.getString(json, "area", null));
				JSONArray alarmArray = JsonUtil.getJsonArray(json, "detail");
				List<AlarmBean> alarmBeanList = CollectionUtils.newArrayList();
				AlarmBean alarmBean = null;
				if (alarmArray != null) {
					for (int j = 0, alarmLenth = alarmArray.length(); j < alarmLenth; j++) {
						alarmBean = JsonUtil.jsonToBean(alarmArray.getJSONObject(j).toString(), AlarmBean.class);
						alarmBeanList.add(alarmBean);
					}
				}
				bean.setAlarmBeanList(alarmBeanList);
			}

			result.getModels().put("userBean", userBean);
			result.getModels().put("devcieBean", bean);
			result.getModels().put("beanList", beanList);
			result.getModels().put("alarmQuery", alarmQuery);

			result.setSuccess(true);

		} catch (Exception e) {
			log.error("alarmHistoryListError", e);
		}
		return result;
	}
	

	@Override
	public Result deviceList(FlowData flowData, DeviceQuery deviceQuery) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<DeviceBean> beanList = getAllDevice(userBean);
			List<String> areaList = getAllArea(userBean);
			
			// 区域筛选
			DeviceBean bean = null;
			if (!StringUtil.isBlank(deviceQuery.getArea())) {
				for (int i = 0; i < beanList.size();) {
					bean = beanList.get(i);
					if (!deviceQuery.getArea().equals(bean.getArea())) {
						beanList.remove(i);
						continue;
					}
					i++;
				}
			}
			
			// 名称筛选
			if (!StringUtil.isBlank(deviceQuery.getDeviceName())) {
				String tmpName = deviceQuery.getDeviceName().trim();
				for (int i = 0; i < beanList.size();) {
					bean = beanList.get(i);
					if (!StringUtil.isBlank(bean.getDevName()) && bean.getDevName().indexOf(tmpName) >= 0) {
						i++;
						continue;
					}
					beanList.remove(i);
				}
			}
			
			// 循环：获取设备的详细信息
			for(DeviceBean deviceBean : beanList) {
				deviceBean.setDeviceExtendBean(getDeviceExtendInfo(deviceBean));
			}
			
			// 获取设备上传时间间隔
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getDevInfo");
			for (DeviceBean deviceBean : beanList) {
				String body = JsonUtil.fields("snaddr", deviceBean);
				String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
				if (!isSuccess(content)) {
					continue;
				}
				JSONObject json = JsonUtil.getJsonObject(content);
				deviceBean.setDevGap(JsonUtil.getString(json, "devGap", null));
			}
			
			result.getModels().put("userBean", userBean);
			result.getModels().put("areaList", areaList);
			result.getModels().put("beanList", beanList);
			result.getModels().put("deviceQuery", deviceQuery);

			result.setSuccess(true);

		} catch (Exception e) {
			log.error("deviceListError", e);
		}
		return result;
	}
	
	
	@Override
	public Result viewAddDevice(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<String> areaList = getAllArea(userBean);

			result.getModels().put("userBean", userBean);
			result.getModels().put("areaList", areaList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewAddDeviceError", e);
		}
		return result;
	}

	@Override
	public Result addDevice(FlowData flowData, DeviceBean deviceBean) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			deviceBean.setUser(userBean.getUser());
//			Map<String, String> headerMap = new HashMap<String, String>();
//			headerMap.put("TYPE", "getDevInfo");
//			String body = JsonUtil.fields("snaddr", deviceBean);
//			String content = client.subPostFrom(API_URL, body, "utf-8", headerMap);
//			if (isSuccess(content)) {
//				result.setResultCode(new StringResultCode("当前设备已经存在"));
//				return result;
//			}
			
			// 0.添加设备
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "addDeviceBySN");
			String body = JsonUtil.fields("snaddr,user,ac,devName", deviceBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("添加设备失败,当前设备已经存在 或 SN和AC码对应不正确"));
				return result;
			}
			
			result.setSuccess(true);
			
			// 1.设置区域
			if(!StringUtil.isBlank(deviceBean.getArea())) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "setDevArea");
				String tmpBody = JsonUtil.fields("snaddr,area", deviceBean);
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改区域失败请重试"));
					return result;
				}
			}
			
			// 2.修改名称
			if(!StringUtil.isBlank(deviceBean.getDevName())) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "setDevName");
				String tmpBody = JsonUtil.fields("snaddr,devName", deviceBean);
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改设备名称失败请重试"));
					return result;
				}
			}
			
			
			// 2.设置设备上传间隔
//			if(!StringUtil.isBlank(deviceBean.getDevGap())) {
//				Map<String, String> tmpMap = new HashMap<String, String>();
//				tmpMap.put("TYPE", "modifyTH");
//				String tmpBody = JsonUtil.fields("snaddr,devGap", deviceBean.getDeviceExtendBean());
//				String tmpContent = client.subPostFrom(API_URL, tmpBody, "utf-8", tmpMap);
//				if(!isSuccess(tmpContent)) {
//					result.setResultCode(new StringResultCode("设备上传间隔失败请重试"));
//					return result;
//				}
//			}
			
			// 3.设置设备温湿度上下限信息
//			Map<String, String> tmpMap = new HashMap<String, String>();
//			tmpMap.put("TYPE", "modifyTH");
//			String tmpBody = JsonUtil.fields("snaddr,maxTemp,minTemp,maxHumi,minHumi,tempHC,humiHC", deviceBean.getDeviceExtendBean());
//			String tmpContent = client.subPostFrom(API_URL, tmpBody, "utf-8", tmpMap);
//			if(!isSuccess(tmpContent)) {
//				result.setResultCode(new StringResultCode("设备温湿度上下限信息失败请重试"));
//				return result;
//			}
			
		} catch (Exception e) {
			log.error("addDeviceError", e);
		}
		return result;
	}

	@Override
	public Result viewEditDevice(FlowData flowData, String snaddr) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<String> areaList = getAllArea(userBean);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getDevInfo");
			DeviceBean deviceBean = new DeviceBean();
			deviceBean.setSnaddr(snaddr);
			String body = JsonUtil.fields("snaddr", deviceBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
			JSONObject json = JsonUtil.getJsonObject(content);
			deviceBean.setDevName(JsonUtil.getString(json, "devName", null));
			deviceBean.setArea(JsonUtil.getString(json, "area", null));
			deviceBean.setDevGap(JsonUtil.getString(json, "devGap", null));
			
			// 获取设备的详细信息
			deviceBean.setDeviceExtendBean(getDeviceExtendInfo(deviceBean));
						
			
			result.getModels().put("userBean", userBean);
			result.getModels().put("areaList", areaList);
			result.getModels().put("deviceBean", deviceBean);

			result.setSuccess(true);

		} catch (Exception e) {
			log.error("viewEditDeviceError", e);
		}
		return result;
	}
	
	@Override
	public Result doEditDevice(FlowData flowData, DeviceBean deviceBean) {
		Result result = new ResultSupport(false);
		try {
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getDevInfo");
			String body = JsonUtil.fields("snaddr", deviceBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
			DeviceBean fromDBDeviceBean = JsonUtil.jsonToBean(content, DeviceBean.class);

			// 1.修改区域
			if(!StringUtil.isBlank(deviceBean.getArea()) && !deviceBean.getArea().equals(fromDBDeviceBean.getArea())) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "setDevArea");
				String tmpBody = JsonUtil.fields("snaddr,area", deviceBean);
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改区域失败请重试"));
					return result;
				}
			}
			
			// 2.修改设备名称
			if(!StringUtil.isBlank(deviceBean.getDevName()) && !deviceBean.getDevName().equals(fromDBDeviceBean.getDevName())) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "setDevName");
				String tmpBody = JsonUtil.fields("snaddr,devName", deviceBean);
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改设备名称失败请重试"));
					return result;
				}
			}
			
			// 3.修改设备上传间隔
			if(!StringUtil.isBlank(deviceBean.getDevGap()) && !deviceBean.getDevGap().equals(fromDBDeviceBean.getDevGap())) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "modifyDeviceGap");
				String tmpBody = JsonUtil.fields("snaddr,devGap", deviceBean);
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改设备上传间隔失败请重试"));
					return result;
				}
			}
			
			// 4.修改设备温湿度上下限信息
			DeviceExtendBean deviceExtendBean = getDeviceExtendInfo(deviceBean);
			if(deviceBean.getDeviceExtendBean().isDataChange(deviceExtendBean)) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "modifyTH");
				String tmpBody = JsonUtil.fields("snaddr,maxTemp,minTemp,maxHumi,minHumi,tempHC,humiHC", deviceBean.getDeviceExtendBean());
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改设备温湿度上下限信息失败请重试"));
					return result;
				}
			}
			
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("doEditDeviceError", e);
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public Result deleteDevice(FlowData flowData, String snaddr) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			DeviceBean deviceBean = new DeviceBean();
			deviceBean.setUser(userBean.getUser());
			deviceBean.setSnaddr(snaddr);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getDevInfo");
			String body = JsonUtil.fields("snaddr", deviceBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (isSuccess(content)) {
				headerMap = new HashMap<String, String>();
				headerMap.put("TYPE", "delDevice");
				body = JsonUtil.fields("snaddr,user", deviceBean);
				content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
				if (!isSuccess(content)) {
					result.setResultCode(new StringResultCode("删除失败，确保拥有权限"));
					return result;
				}
			}
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("deleteDeviceError", e);
		}
		return result;
	}

	@Override
	public Result showBindMail(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "userInfo");
			String body = JsonUtil.fields("user", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (isSuccess(content)) {
				JSONObject json = JsonUtil.getJsonObject(content);
				userBean.setMail(JsonUtil.getString(json, "mail", null));
			}

			result.getModels().put("userBean", userBean);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("showBindMailError", e);
		}
		return result;
	}

	@Override
	public Result bindMail(FlowData flowData, String mail) {
		Result result = new ResultSupport(false);
		try {
			Matcher matcher = emailPatten.matcher(mail);
			if (!matcher.matches()) {
				result.setResultCode(new StringResultCode("请输入正确的邮箱格式"));
				return result;
			}
			UserBean userBean = getUserBean(flowData);
			userBean.setMail(mail);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "bindMailbox");
			String body = JsonUtil.fields("user,mail", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("绑定邮箱失败，请重试"));
				return result;
			}

			result.getModels().put("userBean", userBean);
			result.getModels().put("msg", "已发送一条激活链接到邮箱，请进入邮箱点击激活链接才能成功绑定");

			result.setSuccess(true);

		} catch (Exception e) {
			log.error("bindMailError", e);
		}
		return result;
	}
	
	@Override
	public Result doFindPsw(FlowData flowData, String user) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = new UserBean();
			userBean.setUser(user);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "retrievePass");
			String body = JsonUtil.fields("user", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("账户未绑定邮箱"));
				return result;
			}
			
			result.getModels().put("userBean", userBean);
			result.getModels().put("msg", "密码已发送到绑定的邮箱中，请进入邮箱查看");
			
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("doFindPswError", e);
		}
		return result;
	}

	private DeviceExtendBean getDeviceExtendInfo(DeviceBean deviceBean) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "getThreshold");
		String body = JsonUtil.fields("snaddr", deviceBean);
		String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
		return JsonUtil.jsonToBean(content, DeviceExtendBean.class);
	}

	private List<DeviceBean> getAllDevice(UserBean userBean) {
		List<DeviceBean> result = CollectionUtils.newArrayList();
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "getAllDevice");
		String body = JsonUtil.fields("user", userBean);
		String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
		JSONArray array = JsonUtil.getJsonArray(content);
		try {
			if (array != null) {
				JSONObject jsonObject = null;
				DeviceBean bean = null;
				for (int i = 0, size = array.length(); i < size; i++) {
					jsonObject = array.getJSONObject(i);
					bean = JsonUtil.jsonToBean(jsonObject.toString(), DeviceBean.class);
					result.add(bean);
				}
			}
		} catch (JSONException e) {
		}

		return result;
	}
	
	private List<String> getAllArea(UserBean userBean) {
		List<String> result = CollectionUtils.newArrayList();
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "getAreaInfo");
		String body = JsonUtil.fields("user", userBean);
		String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
		JSONArray array = JsonUtil.getJsonArray(content);
		try {
			if (array != null) {
				for (int i = 0, size = array.length(); i < size; i++) {
					result.add(array.getString(i));
				}
			}
		} catch (JSONException e) {
		}
		return result;
	}
	

	private DeviceDataBean requestDeviceDataBean(DeviceBean bean) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "getRTData");
		String body = JsonUtil.fields("snaddr,curve", bean);
		String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
		/*
		 *  {
		 *  "humi": { "value"："52.59","status":"-1"}, 
		 *  "in1": "0000", 
		 *  "temp": { "value"："25.59","status":"1"}, 
		 *  "time": "16-10-10 10:18", 
		 *  "abnormal": "0"
		 *  }
		 */
		JSONObject json = JsonUtil.getJsonObject(content);
		if(json == null) {
			return null;
		}
		DeviceDataBean dataBean = new DeviceDataBean();
		dataBean.setAbnormal(JsonUtil.getString(json, "abnormal", null));
		dataBean.setTime(JsonUtil.getString(json, "time", null));
		JSONObject humiJson = JsonUtil.getJSONObject(json, "humi");
		dataBean.setHumi(JsonUtil.getString(humiJson, "value", null));
		dataBean.setHumiStatus(ChangeUtil.str2int(JsonUtil.getString(humiJson, "status", null)));
		
		JSONObject tempJson = JsonUtil.getJSONObject(json, "temp");
		dataBean.setTemp(JsonUtil.getString(tempJson, "value", null));
		dataBean.setTempStatus(ChangeUtil.str2int(JsonUtil.getString(tempJson, "status", null)));
		
		return dataBean;
	}

	private boolean isSuccess(String content) {
		JSONObject json = JsonUtil.getJsonObject(content);
		return JsonUtil.getInt(json, "code", -1) == 0;
	}
}
