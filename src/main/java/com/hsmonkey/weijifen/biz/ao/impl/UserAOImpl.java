package com.hsmonkey.weijifen.biz.ao.impl;

import com.hsmonkey.weijifen.biz.bean.AdminBean;
import com.hsmonkey.weijifen.biz.bean.AlarmNewBean;
import com.hsmonkey.weijifen.biz.bean.MobileDeviceBean;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.biz.result.StringResultCode;
import wint.help.codec.MD5;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.FileUtil;
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

	private String filePath;
	
	static final String appCheckVersionJson = "{"+
											    "\"code\": 0,"+
											    "\"data\": {"+
											     "   \"lastversion\": 3,"+
											     "   \"downurl\": \"http://static.yun.eefield.com/wenshiplatformclient-1.3.apk\""+
											    "}"+
											"}";

	@Override
	public Result login(FlowData flowData, UserBean userBean) {
		Result result = new ResultSupport(false);
		try {
			String content = loginCall(userBean);
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
	public Result register(FlowData flowData, AdminBean userBean) {
		Result result = new ResultSupport(false);
		try {

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "register");
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("user", userBean.getUserName());
			argsMap.put("password", MD5.encrypt(userBean.getNewPsw()));
			argsMap.put("name", userBean.getNick());
			argsMap.put("phone", userBean.getPhone());
			String body = JsonUtil.mapToJson(argsMap);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			JSONObject json = JsonUtil.getJsonObject(content);
			String msg = JsonUtil.getString(json, "msg", "");
			result.setSuccess(isSuccess(content));
			if(!result.isSuccess()) {
				result.setResultCode(new StringResultCode(msg));
			}
		} catch (Exception e) {
			log.error("registerError", e);
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
			userBean.setUser(fromSessionUser.getUser());
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
			
			if (!isSuccess(loginCall(userBean))) {
				result.setResultCode(new StringResultCode("原密码错误"));
				return result;
			}

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "modifyPass");
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("user", fromSessionUser.getUser());
			argsMap.put("oldPass", MD5.encrypt(userBean.getPassword()));
			argsMap.put("newPass", MD5.encrypt(userBean.getNewPsw()));
			String body = JsonUtil.mapToJson(argsMap);
//			System.out.println(body);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
//			System.out.println("updatePsw:" + content);
			if(isSuccess(content)) {
				result.setSuccess(true);
				return result;
			}
			int code = getCode(content);
			String msg = getMessage(content);
			
			if(!StringUtil.isBlank(msg)) {
			    result.setResultCode(new StringResultCode(msg));
			} else {
              result.setResultCode(new StringResultCode("修改失败，请先绑定邮箱才能修改密码，稍后再试"));
			}
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

			if(CollectionUtils.isEmpty(beanList)) {
				result.getModels().put("beanList", beanList);
				result.getModels().put("userBean", userBean);
				result.getModels().put("areaList", areaList);
				result.getModels().put("area", area);
				result.setSuccess(true);
				return result;
			}

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
					dataBean = getDeviceDataBean(bean);
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
					
					if(o1.getDataBean().getAbnormal() == null) {
					    return 1;
					}
					
					if(o2.getDataBean().getAbnormal() == null) {
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

			int successNum = 0;
			int notConnectionNum = 0;
			int kaiguanNum = 0;
			int otherNum = 0;

			if(beanList != null) {
				DeviceDataBean deviceDataBean = null;
				for(DeviceBean deviceBean : beanList) {
					if ((deviceDataBean = deviceBean.getDataBean()) == null) {
						continue;
					}
					if (deviceDataBean.isSuccess()) {
						successNum++;
						continue;
					}
					if (deviceDataBean.isNotConnection()) {
						notConnectionNum++;
						continue;
					}
					if (deviceDataBean.isKaiguan()) {
						kaiguanNum++;
						continue;
					}
					otherNum++;

				}
			}

			result.getModels().put("beanList", beanList);
			result.getModels().put("showMp3", showMp3);
			result.getModels().put("userBean", userBean);
			result.getModels().put("areaList", areaList);
			result.getModels().put("area", area);

			result.getModels().put("successNum", successNum);
			result.getModels().put("notConnectionNum", notConnectionNum);
			result.getModels().put("kaiguanNum", kaiguanNum);
			result.getModels().put("otherNum", otherNum);



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
			result.getModels().put("userBean", userBean);
			if(StringUtil.isBlank(deviceDataBean.getSnaddr())) {
				result.setSuccess(true);
				return result;
			}
			
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getHisData");
			String body = JsonUtil.fields("snaddr,startTime,endTime,rangeTime", deviceDataBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			JSONObject mainJson = JsonUtil.getJsonObject(content);
            if (isSuccess(content)) {
                JSONObject jsonObject = JsonUtil.getJSONObject(mainJson, "array");
                JSONArray timeList = JsonUtil.getJsonArray(jsonObject, "timeList");
                JSONArray humiList = JsonUtil.getJsonArray(jsonObject, "humiList");
                JSONArray tempList = JsonUtil.getJsonArray(jsonObject, "tempList");
                if (timeList != null && humiList != null && tempList != null && timeList.length() > 0 && timeList.length() == humiList.length()
                    && timeList.length() == tempList.length()) {
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
            }
			
			result.getModels().put("deviceDataBean", deviceDataBean);
			result.setSuccess(true);
			
		} catch (Exception e) {
			log.error("historyDataError", e);
		}
		return result;
	}
	
	
	@Override
	public Result historyCurve(FlowData flowData, DeviceDataBean deviceDataBean) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<DeviceBean> beanList = getAllDevice(userBean);
			result.getModels().put("beanList", beanList);
			result.getModels().put("userBean", userBean);
			if(CollectionUtils.isEmpty(beanList)) {
				result.setSuccess(true);
				return result;
			}
			if(StringUtil.isBlank(deviceDataBean.getSnaddr())) {
				// 设定一个默认的设备
				deviceDataBean.setSnaddr(beanList.get(0).getSnaddr());
			}
			
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getHisData");
			String body = JsonUtil.fields("snaddr,startTime,endTime,rangeTime", deviceDataBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
            if (isSuccess(content)) {
                JSONObject mainJson = JsonUtil.getJsonObject(content);
                JSONObject jsonObject = JsonUtil.getJSONObject(mainJson, "array");
                JSONArray timeList = JsonUtil.getJsonArray(jsonObject, "timeList");
                JSONArray humiList = JsonUtil.getJsonArray(jsonObject, "humiList");
                JSONArray tempList = JsonUtil.getJsonArray(jsonObject, "tempList");
                if (timeList != null && humiList != null && tempList != null && timeList.length() > 0 && timeList.length() == humiList.length()
                    && timeList.length() == tempList.length()) {
                    List<DeviceDataBean> dataList = CollectionUtils.newArrayList(timeList.length());
                    for (int i = 0, size = timeList.length(); i < size; i++) {
                        DeviceDataBean bean = new DeviceDataBean();
                        bean.setTemp(tempList.getString(i));
                        bean.setHumi(humiList.getString(i));
                        bean.setTime(DateUtil.format(DateUtil.parse(timeList.getString(i)), DateUtil.DATE_FMT_MD_AND_HM));
                        dataList.add(bean);
                    }
                    result.getModels().put("dataList", dataList);
                }
            }
			
			
			
			result.getModels().put("deviceDataBean", deviceDataBean);
			result.setSuccess(true);
			
		} catch (Exception e) {
			log.error("historyCurveError", e);
		}
		return result;
	}
	
	@Override
	public Result historyDataExport(FlowData flowData, DeviceDataBean deviceDataBean, String exportType) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			String content = getDevInfo(userBean.getUser(), deviceDataBean.getSnaddr());
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
//			JSONObject jsonObject = JsonUtil.getJsonObject(content);
			DeviceBean fromDBDeviceBean = JsonUtil.jsonToBean(content, DeviceBean.class);
			
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getHisData");
			String body = JsonUtil.fields("snaddr,startTime,endTime,rangeTime", deviceDataBean);
			content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			JSONObject mainJson = JsonUtil.getJsonObject(content);
			if(isSuccess(content)) {
			    JSONObject jsonObject = JsonUtil.getJSONObject(mainJson, "array");
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
			if(isSuccess(content)) {
			    JSONObject mainJson = JsonUtil.getJsonObject(content);
			    JSONArray array = JsonUtil.getJsonArray(mainJson, "array");
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
	public Result alarmList2(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<AlarmNewBean> alarmBeanList = CollectionUtils.newArrayList();
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getUnresolvedError");
			String body = JsonUtil.fields("user", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if(isSuccess(content)) {
				JSONObject mainJson = JsonUtil.getJsonObject(content);
				JSONArray array = JsonUtil.getJsonArray(mainJson, "array");
				JSONObject json = null;
				AlarmNewBean alarmBean = null;

				for (int i = 0, size = array.length(); i < size; i++) {
						json = array.getJSONObject(i);
						if (json == null) {
							continue;
						}
						alarmBean = new AlarmNewBean();
					  alarmBean.setAlarmId(JsonUtil.getString(json, "alarmId", null));
					  alarmBean.setDevName(JsonUtil.getString(json, "devName", null));
					  alarmBean.setSnaddr(JsonUtil.getString(json, "snaddr", null));
					  alarmBean.setInfo(JsonUtil.getString(json, "info", null));
					  alarmBean.setHandle(JsonUtil.getString(json, "handle", null));
					  alarmBean.setStartTime(JsonUtil.getString(json, "startTime", null));
					  alarmBean.setEndTime(JsonUtil.getString(json, "endTime", null));
					  alarmBean.setAlarmState(JsonUtil.getString(json, "alarmState", null));
					  alarmBean.setAdditionInfo(JsonUtil.getString(json, "additionInfo", null));
					 alarmBeanList.add(alarmBean);
				}
			}
			result.getModels().put("userBean", userBean);
			result.getModels().put("alarmBeanList", alarmBeanList);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("alarmList2Error", e);
		}
		return result;
	}

	@Override
	public Result writeAlarmNote(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "dealwithTheError");
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("user", userBean.getUser());
			argsMap.put("snaddr", flowData.getParameters().getString("snaddr"));
			argsMap.put("alarmId", flowData.getParameters().getString("alarmId"));
			argsMap.put("additionInfo", flowData.getParameters().getString("userNote"));
			String body = JsonUtil.mapToJson(argsMap);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			result.setSuccess(isSuccess(content));
		} catch (Exception e) {
			log.error("alarmList2Error", e);
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
	        JSONObject mainJson = JsonUtil.getJsonObject(content);
			JSONObject json = JsonUtil.getJSONObject(mainJson, "array");
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
	public Result alarmHistoryList2(FlowData flowData, AlarmQuery alarmQuery) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			List<DeviceBean> beanList = getAllDevice(userBean);
			List<AlarmNewBean> alarmBeanList = CollectionUtils.newArrayList();
			if (StringUtil.isBlank(alarmQuery.getSnaddr()) && !CollectionUtils.isEmpty(beanList)) {
				alarmQuery.setSnaddr(beanList.get(0).getSnaddr());
			}
			if (StringUtil.isBlank(alarmQuery.getStartTime()) || StringUtil.isBlank(alarmQuery.getEndTime())) {
				alarmQuery.setStartTime(DateUtil.format(DateUtil.changeMonth(new Date(), -1)));
				alarmQuery.setEndTime(DateUtil.format(new Date()));
			}
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getHistoryAlarm");

			String body = JsonUtil.fields("snaddr,startTime,endTime", alarmQuery);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			String devName = null;
			if(isSuccess(content)) {
				JSONObject mainJson = JsonUtil.getJsonObject(content);
				devName = JsonUtil.getString(mainJson, "devName", null);
				JSONArray array = JsonUtil.getJsonArray(mainJson, "detail");
				JSONObject json = null;
				AlarmNewBean alarmBean = null;

				for (int i = 0, size = array.length(); i < size; i++) {
					json = array.getJSONObject(i);
					if (json == null) {
						continue;
					}
					alarmBean = new AlarmNewBean();
					alarmBean.setAlarmId(JsonUtil.getString(json, "alarmId", null));
					alarmBean.setDevName(devName);
					alarmBean.setSnaddr(alarmQuery.getSnaddr());
					alarmBean.setInfo(JsonUtil.getString(json, "info", null));
					alarmBean.setHandle(JsonUtil.getString(json, "handle", null));
					alarmBean.setStartTime(JsonUtil.getString(json, "startTime", null));
					alarmBean.setEndTime(JsonUtil.getString(json, "endTime", null));
					alarmBean.setAlarmState(JsonUtil.getString(json, "alarmState", null));
					alarmBean.setAdditionInfo(JsonUtil.getString(json, "additionInfo", null));
					alarmBean.setHandleUser(JsonUtil.getString(json, "handleUser", null));
					alarmBeanList.add(alarmBean);
				}
			}



			result.getModels().put("userBean", userBean);
			result.getModels().put("alarmBeanList", alarmBeanList);
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
//			Map<String, String> headerMap = new HashMap<String, String>();
//			headerMap.put("TYPE", "getDevInfo");
			for (DeviceBean deviceBean : beanList) {
			    String content = getDevInfo(userBean.getUser(), deviceBean.getSnaddr());
//				String body = JsonUtil.fields("snaddr", deviceBean);
//				String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
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
    public Result areaList(FlowData flowData) {
        Result result = new ResultSupport(false);
        try {
            UserBean userBean = getUserBean(flowData);
            List<String> areaList = getAllArea(userBean);
            
            result.getModels().put("userBean", userBean);
            result.getModels().put("areaList", areaList);
            result.setSuccess(true);

        } catch (Exception e) {
            log.error("areaListError", e);
        }
        return result;
    }
	
    @Override
    public Result doEditArea(FlowData flowData, String oldArea, String newArea) {
        Result result = new ResultSupport(false);
        try {

            UserBean userBean = getUserBean(flowData);

            if (StringUtil.isBlank(newArea)) {
                result.setResultCode(new StringResultCode("区域名不能为空"));
                return result;
            }
            newArea = newArea.trim();
            if (newArea.indexOf("\n") >= 0) {
                result.setResultCode(new StringResultCode("区域名不能换行"));
                return result;
            }

            if (newArea.equals(oldArea)) {
                result.setResultCode(new StringResultCode("当前区域没有修改"));
                return result;
            }

            List<String> areaList = getAllArea(userBean);
            if (areaList != null) {
                for (String tmpArea : areaList) {
                    if (newArea.equals(tmpArea)) {
                        result.setResultCode(new StringResultCode("该区域名已经存在"));
                        return result;
                    }
                }
            }

            Map<String, String> tmpMap = new HashMap<String, String>();
            tmpMap.put("TYPE", "setAreaInfo");
            String tmpBody = "{\"user\":\"" + userBean.getUser() + "\",\"oldArea\":\"" + oldArea + "\",\"newArea\":\"" + newArea + "\"}";
            String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
            if (!isSuccess(tmpContent)) {
                String msg = getMessage(tmpContent);
                if(StringUtil.isBlank(msg)) {
                    msg = "区域名称修改失败请重试";
                }
                result.setResultCode(new StringResultCode(msg));
                return result;
            }

            result.setSuccess(true);

        } catch (Exception e) {
            log.error("doEditAreaError", e);
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
			    String tmpContent = updateDevArea(userBean.getUser(), deviceBean.getSnaddr(), deviceBean.getArea());
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
			String content = getDevInfo(userBean.getUser(), snaddr);
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
			DeviceBean deviceBean = JsonUtil.jsonToBean(content, DeviceBean.class);
			deviceBean.setSnaddr(snaddr);

			// 获取设备的详细信息
			deviceBean.setDeviceExtendBean(getDeviceExtendInfo(deviceBean));

            String nodeIdContent = getNodeIdInfo(userBean.getUser(), snaddr);
            String nodeId = "";
            if(isSuccess(nodeIdContent)) {
				JSONObject jsonData = JsonUtil.getJSONObject(JsonUtil.getJsonObject(nodeIdContent), "array");
				nodeId = JsonUtil.getString(jsonData, "nodeId", "");
			}
			deviceBean.setNodeId(nodeId);

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
			if(!StringUtil.isBlank(deviceBean.getNodeId()) && deviceBean.getNodeId().trim().length() != 8) {
				result.setResultCode(new StringResultCode("输入 无线温湿度节点SN 有误，请重新编辑！"));
				return result;
			}

		    UserBean userBean = getUserBean(flowData);
			deviceBean.setUser(userBean.getUser());
		    String content = getDevInfo(userBean.getUser(), deviceBean.getSnaddr());
			if (!isSuccess(content)) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
			DeviceBean fromDBDeviceBean = JsonUtil.jsonToBean(content, DeviceBean.class);

			// 1.修改区域
			if(!StringUtil.isBlank(deviceBean.getArea()) && !deviceBean.getArea().equals(fromDBDeviceBean.getArea())) {
			    String tmpContent = updateDevArea(userBean.getUser(), deviceBean.getSnaddr(), deviceBean.getArea());
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改区域失败请重试"));
					return result;
				}
			}
			
			// 2.修改设备名称
			if(!StringUtil.isBlank(deviceBean.getDevName()) && !deviceBean.getDevName().equals(fromDBDeviceBean.getDevName())) {
			    String tmpContent = setDevName(userBean.getUser(), deviceBean.getSnaddr(), deviceBean.getDevName());
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改设备名称失败请重试"));
					return result;
				}
			}
			
			// 3.修改设备上传间隔
			if(!StringUtil.isBlank(deviceBean.getDevGap()) && !deviceBean.getDevGap().equals(fromDBDeviceBean.getDevGap())) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "modifyDeviceGap");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user", userBean.getUser());
				map.put("snaddr", deviceBean.getSnaddr());
				map.put("devGap", deviceBean.getDevGap());
				String tmpBody = JsonUtil.mapToJson(map);
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
				deviceBean.setUser(userBean.getUser());
				deviceBean.getDeviceExtendBean().setUser(deviceBean.getUser());
				tmpMap.put("TYPE", "modifyTH");
				String tmpBody = JsonUtil.fields("snaddr,user,maxTemp,minTemp,maxHumi,minHumi,tempHC,humiHC", deviceBean.getDeviceExtendBean());
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("修改设备温湿度上下限信息失败请重试"));
					return result;
				}
			}

			// 5.设置nodeId
			if(!StringUtil.isBlank(deviceBean.getNodeId())) {
				Map<String, String> tmpMap = new HashMap<String, String>();
				tmpMap.put("TYPE", "setNodeId");
				String tmpBody = JsonUtil.fields("snaddr,user,nodeId", deviceBean);
				String tmpContent = client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
				if(!isSuccess(tmpContent)) {
					result.setResultCode(new StringResultCode("输入SN有误，设置失败，请重新编辑！"));
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
			String content = getDevInfo(deviceBean.getUser(), deviceBean.getSnaddr());
			if (isSuccess(content)) {
			    Map<String, String> headerMap = new HashMap<String, String>();
				headerMap.put("TYPE", "delDevice");
				String body = JsonUtil.fields("snaddr,user", deviceBean);
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
	public Result viewPermissionDevice(FlowData flowData, String snaddr) {
		Result result = viewEditDevice(flowData, snaddr);
		if (result.isSuccess()) {
			DeviceBean deviceBean = (DeviceBean) result.getModels().get("deviceBean");
			if (!deviceBean.hasAuth()) {
				result.setSuccess(false);
				result.setResultCode(new StringResultCode("当前没有权限"));
				return result;
			}
			return result;
		}
		return result;
	}

	@Override
	public Result doOptPermissionDevice(FlowData flowData, String snaddr, String newUser) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			DeviceBean deviceBean = new DeviceBean();
			deviceBean.setUser(userBean.getUser());
			deviceBean.setSnaddr(snaddr);
			deviceBean.setNewUser(newUser);
			String content = getDevInfo(deviceBean.getUser(), deviceBean.getSnaddr());
			if (isSuccess(content)) {
				Map<String, String> headerMap = new HashMap<String, String>();
				headerMap.put("TYPE", "handOverAuthority");
				String body = JsonUtil.fields("snaddr,user,newUser", deviceBean);
				content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
				if (!isSuccess(content)) {
					result.setResultCode(new StringResultCode("操作失败，确保拥有权限"));
					return result;
				}
			} else {
				result.setResultCode(new StringResultCode("操作失败，确保拥有权限"));
				return result;
			}
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("doOptPermissionDeviceError", e);
		}
		return result;
	}

	@Override
	public Result viewAlarmDevice(FlowData flowData, String snaddr) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			String content = getDevInfo(userBean.getUser(), snaddr);
			if (!isSuccess(content)) {
				return result;
			}
			DeviceBean deviceBean = JsonUtil.jsonToBean(content, DeviceBean.class);
			List<String> accountMobileList = getAccountMobileList(userBean.getUser());
			List<String> deviceSmsPhoneList = deviceSmsPhones(snaddr, userBean.getUser());
			result.getModels().put("accountMobileList", accountMobileList);
			result.getModels().put("deviceSmsPhoneList", deviceSmsPhoneList);
			result.getModels().put("userBean", userBean);
			result.getModels().put("deviceBean", deviceBean);
			result.getModels().put("snaddr", snaddr);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewAlarmDeviceError", e);
		}
		return result;
	}

	@Override
	public Result doAlarmDevice(FlowData flowData, String snaddr, String[] smsPhones, boolean open) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			String content = getDevInfo(userBean.getUser(), snaddr);
			if (!isSuccess(content)) {
				return result;
			}
			DeviceBean deviceBean = JsonUtil.jsonToBean(content, DeviceBean.class);
			deviceBean.setUser(userBean.getUser());
			deviceBean.setSnaddr(snaddr);
			List<String> snaddrs = new ArrayList<String>();
			snaddrs.add(snaddr);

			// 需要取消绑定的手机号列表
			List<String> cannelSmsPhoneList = new ArrayList<String>();
			// 添加绑定的手机号列表
			List<String> addSmsPhoneList = new ArrayList<String>();

			List<String> deviceSmsPhoneList = deviceSmsPhones(snaddr, userBean.getUser());
			List<String> newDevSmsPhoneList = new ArrayList<String>();
			if(smsPhones != null) {
				for(String tmp : smsPhones) {
					newDevSmsPhoneList.add(tmp);
				}
			}
			if (deviceSmsPhoneList == null || deviceSmsPhoneList.size() == 0) {
				addSmsPhoneList = newDevSmsPhoneList;
			} else {
				for (String oldSmsPhone : deviceSmsPhoneList) {
					boolean has = false;
					for (String newSmsPhone : newDevSmsPhoneList) {
						if (oldSmsPhone.equals(newSmsPhone)) {
							has = true;
							break;
						}
					}
					if (!has) {
						cannelSmsPhoneList.add(oldSmsPhone);
						continue;
					}

					newDevSmsPhoneList.remove(oldSmsPhone);

				}
				addSmsPhoneList = newDevSmsPhoneList;
			}

			String cannelContent = null;
			String newContent = null;

			if (cannelSmsPhoneList != null && cannelSmsPhoneList.size() > 0) {
				MobileDeviceBean bean = new MobileDeviceBean();
				bean.setUser(userBean.getUser());
				bean.setSnaddr(snaddrs);
				bean.setDeviceMobileList(cannelSmsPhoneList);
				Map<String, String> headerMap = new HashMap<String, String>();
				headerMap.put("TYPE", "delMobileToDevice");
				String body = JsonUtil.fields("user,snaddr,deviceMobileList", bean);
				cannelContent = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);

			}

			if (newDevSmsPhoneList != null && newDevSmsPhoneList.size() > 0) {
				MobileDeviceBean bean = new MobileDeviceBean();
				bean.setUser(userBean.getUser());
				bean.setSnaddr(snaddrs);
				bean.setDeviceMobileList(newDevSmsPhoneList);
				Map<String, String> headerMap = new HashMap<String, String>();
				headerMap.put("TYPE", "addMobileToDevice");
				String body = JsonUtil.fields("user,snaddr,deviceMobileList", bean);
				newContent = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			}


			// 设置蜂鸣器开关报警
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "setRealAlarm");
			if(open) {
				deviceBean.setBeepStatus("1");
			} else {
				deviceBean.setBeepStatus("0");

			}
			String body = JsonUtil.fields("user,snaddr,beepStatus", deviceBean);
			String hummerContent = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			log.error("hummerContentResult->" + hummerContent);
			if (
					((!CollectionUtil.isEmpty(cannelSmsPhoneList) && isSuccess(cannelContent)) || CollectionUtil.isEmpty(cannelSmsPhoneList))
					&& (!CollectionUtil.isEmpty(newDevSmsPhoneList) && isSuccess(newContent) || CollectionUtil.isEmpty(newDevSmsPhoneList))
					) {
				result.setSuccess(true);
			}


		} catch (Exception e) {
			log.error("doAlarmDeviceError", e);
		}
		return result;
	}


	@Override
	public Result alarmManage(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);

			List<String> phoneList = getAccountMobileList(userBean.getUser());

			// 获取报警总开关
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getAllAlarmStatus");
			String body = JsonUtil.fields("user", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (isSuccess(content)) {
				JSONObject json = JsonUtil.getJsonObject(content);
				userBean.setAllAlarmStatus(JsonUtil.getString(json, "allAlarmStatus", null));
				JSONArray array = JsonUtil.getJsonArray(json, "alarmChannel");
				List<String> alarmChannelList = new ArrayList<String>();
				if(array != null) {
					for(int i = 0; i < array.length(); i++ ) {
						alarmChannelList.add(array.getString(i));
					}
				}
				userBean.setState_push(alarmChannelList.contains("state_push") ? "1" : "0");
				userBean.setState_msm(alarmChannelList.contains("state_msm") ? "1" : "0");
				userBean.setState_call(alarmChannelList.contains("state_call") ? "1" : "0");
				userBean.setState_mail(alarmChannelList.contains("state_mail") ? "1" : "0");

				userBean.setData_push(alarmChannelList.contains("data_push") ? "1" : "0");
				userBean.setData_msm(alarmChannelList.contains("data_msm") ? "1" : "0");
				userBean.setData_call(alarmChannelList.contains("data_call") ? "1" : "0");
				userBean.setData_mail(alarmChannelList.contains("data_mail") ? "1" : "0");
			}

			result.getModels().put("phoneList", phoneList);
			result.getModels().put("userBean", userBean);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("alarmManageError", e);
		}
		return result;
	}



	@Override
	public Result doAddAlarmPhone(FlowData flowData, String phone) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			userBean.setMobile(phone);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "addMobileToAccount");
			String body = JsonUtil.fields("user, mobile", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (isSuccess(content)) {
				result.setSuccess(true);
			}
			result.getModels().put("userBean", userBean);

		} catch (Exception e) {
			log.error("doAddAlarmPhoneError", e);
		}
		return result;
	}

	@Override
	public Result deleteAlarmPhone(FlowData flowData, String phone) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			userBean.setMobile(phone);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "delMobileToAccount");
			String body = JsonUtil.fields("user, mobile", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (isSuccess(content)) {
				result.setSuccess(true);
			}
			result.getModels().put("userBean", userBean);
		} catch (Exception e) {
			log.error("deleteAlarmPhoneError", e);
		}
		return result;
	}


	@Override
	public Result optAlarmTotalMenu(FlowData flowData, boolean open) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);
			userBean.setAllAlarmStatus( open ? "1" : "0");
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "switchAllAlarm");
			String body = JsonUtil.fields("user, allAlarmStatus", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			if (isSuccess(content)) {
				result.setSuccess(true);
			}
			result.getModels().put("userBean", userBean);

		} catch (Exception e) {
			log.error("viewAlarmTotalMenuError", e);
		}
		return result;
	}

	@Override
	public Result optDetailAlarm(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = getUserBean(flowData);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "switchAlarmChannel");
			String[] channels = {"state_push", "state_msm", "state_call", "state_mail", "data_push", "data_msm", "data_call", "data_mail"};

			for (String channel : channels) {
				userBean.setChannel(channel);
				userBean.setAlarmStatus(flowData.getParameters().getString(channel));
				String body = JsonUtil.fields("user, channel, alarmStatus", userBean);
				String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
				log.error("optDetailAlarmChannel->" + channels + ",result=" + content);
			}

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("optDetailAlarmError", e);
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
			String oldMail = flowData.getParameters().getString("bindMailValue");
			if(!StringUtil.isBlank(oldMail) && oldMail.equals(mail)) {
				result.setResultCode(new StringResultCode("请勿绑定当前邮箱"));
				return result;
			}
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
		if(!isSuccess(content)) {
		   return null; 
		}
		JSONObject jsonObject = JsonUtil.getJsonObject(content);
		JSONObject json = JsonUtil.getJSONObject(jsonObject, "array");
		return JsonUtil.jsonToBean(json.toString(), DeviceExtendBean.class);
	}
	

	
	@Override
	public Result version(FlowData flowData) {
		Result result = new ResultSupport(true);
		String content = appCheckVersionJson;
		try {
			if (StringUtil.isBlank(filePath)) {
				return result;
			}
			
			if(!FileUtil.exist(filePath)) {
				File file = new File(filePath);
				file.createNewFile();
				FileUtil.writeContent(file, content);
			}

			String tmp = FileUtil.readAsString(new File(filePath));
			if (!StringUtil.isBlank(tmp)) {
				content = tmp;
			}
		} catch (Exception e) {
			log.error("versionEror", e);
		}
		result.getModels().put("content", content);
		return result;
	}

	@Override
	public Result showUserDetail(FlowData flowData) {
		Result result = new ResultSupport(true);
		try {
			UserBean userBean = getUserBean(flowData);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getAlarmAccountInfo");
			String body = JsonUtil.fields("user", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);

			// {"msg_remain": 4.0, "msg_count": 396, "code": 0, "credit_money": 0.4}
			// 剩余短信条数          已发短信条数                    账户余额
			JSONObject json = JsonUtil.getJsonObject(content);
			int remain = JsonUtil.getInt(json, "msg_remain", 0);
			int count = JsonUtil.getInt(json, "msg_count", 0);
			double money = JsonUtil.getDouble(json, "credit_money", 0.d);

			result.getModels().put("userBean", userBean);
			result.getModels().put("remain", remain);
			result.getModels().put("count", count);
			result.getModels().put("money", money);
		} catch (Exception e) {
			log.error("showUserDetailError", e);
		}
		return result;
	}

	@Override
	public Result jsonRecentlyAlarmList(FlowData flowData, String user, Date requestTime) {
		Result result = new ResultSupport(false);
		try {
			UserBean userBean = new UserBean();
			userBean.setUser(user);
			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("TYPE", "getAccountErr");
			String body = JsonUtil.fields("user", userBean);
			String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
			Date lastAlarmTime = requestTime;
			List<DeviceBean> deviceBeanList = null;
            if (isSuccess(content)) {
                JSONObject mainJson = JsonUtil.getJsonObject(content);
                JSONArray array = JsonUtil.getJsonArray(mainJson, "array");
                deviceBeanList = CollectionUtils.newArrayList(array.length());
                JSONObject json = null;
                JSONArray alarmArray = null;
                AlarmBean alarmBean = null;
                DeviceBean bean = null;
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

                    for (int j = 0, alarmLenth = alarmArray.length(); j < alarmLenth; j++) {
                        alarmBean = JsonUtil.jsonToBean(alarmArray.getJSONObject(j).toString(), AlarmBean.class);
                        // 过滤时间不符合条件的数据
                        if (alarmBean.isDateAfter(requestTime)) {
                            bean.setAlarmBean(alarmBean);
                            deviceBeanList.add(bean);
                        }
                    }
                }
            }
			
            // 更新最近一次报警时间，返回给客户端
			if(deviceBeanList != null) {
			    for(DeviceBean deviceBean : deviceBeanList) {
			        Date alarmTime = DateUtil.parseNoException(deviceBean.getAlarmBean().getAlarmTime());
			        if(alarmTime != null && DateUtil.isDateAfter(alarmTime, lastAlarmTime)) {
			            lastAlarmTime = alarmTime;
			        }
			    }
			}

//			result.getModels().put("requsetTime", DateUtil.format(new Date()));
			result.getModels().put("requsetTime", DateUtil.format(lastAlarmTime));
			result.getModels().put("deviceBeanList", deviceBeanList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("jsonRecentlyAlarmListError", e);
		}
		return result;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
