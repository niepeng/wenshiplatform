package com.hsmonkey.weijifen.biz.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wint.help.codec.MD5;
import wint.lang.utils.StringUtil;

import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.biz.dal.daointerface.ApiAccessTokenDAO;
import com.hsmonkey.weijifen.biz.dal.daointerface.KeyValueDAO;
import com.hsmonkey.weijifen.util.ChangeUtil;
import com.hsmonkey.weijifen.util.CollectionUtils;
import com.hsmonkey.weijifen.util.JsonUtil;
import com.hsmonkey.weijifen.web.action.BaseAction;

public class BaseAO extends BaseAction {
	
	protected KeyValueDAO keyValueDAO;
	
	protected ApiAccessTokenDAO apiAccessTokenDAO;
	
	protected boolean isSuccess(String content) {
		JSONObject json = JsonUtil.getJsonObject(content);
		return JsonUtil.getInt(json, "code", -1) == 0;
	}
	
    protected int getCode(String content) {
        JSONObject json = JsonUtil.getJsonObject(content);
        return JsonUtil.getInt(json, "code", -1);
    }
    
    protected String getMessage(String content) {
        JSONObject json = JsonUtil.getJsonObject(content);
        return JsonUtil.getString(json, "msg", null);
    }
	
	protected String getDevInfo(String user, String snaddr) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("TYPE", "getDevInfo");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("snaddr", snaddr);
        String body = JsonUtil.mapToJson(map);
        return client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
    }
	
	protected String setDevName(String user, String snaddr, String devName) {
        Map<String, String> tmpMap = new HashMap<String, String>();
        tmpMap.put("TYPE", "setDevName");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("snaddr", snaddr);
        map.put("devName", devName);
        String tmpBody = JsonUtil.mapToJson(map);
        return client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
    }
	
	protected String updateDevArea(String user, String snaddr, String area) {
	    Map<String, String> tmpMap = new HashMap<String, String>();
        tmpMap.put("TYPE", "setDevArea");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("snaddr", snaddr);
        map.put("area", area);
        String tmpBody = JsonUtil.mapToJson(map);
        return client.subPostForOnlyOneClient(API_URL, tmpBody, "utf-8", tmpMap);
    }
	
	protected String loginCall(String user, String psw, boolean isPswAlreadyMd5) {
		UserBean userBean = new UserBean();
		userBean.setUser(user);
		if (isPswAlreadyMd5) {
			userBean.setPassword(psw);
		} else {
			userBean.setPassword(MD5.encrypt(psw));
		}

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "login");
		String body = JsonUtil.fields("user,password", userBean);
		return client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
	}
	
	protected String loginCall(UserBean userBean) {
		return loginCall(userBean.getUser(), userBean.getPassword(), false);
	}
	
	protected List<DeviceBean> getAllDevice(UserBean userBean) {
		List<DeviceBean> result = CollectionUtils.newArrayList();
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "getAllDevice");
		String body = JsonUtil.fields("user", userBean);
		String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
		if(!isSuccess(content)) {
		    return null;
		}
		JSONObject mainJson =  JsonUtil.getJsonObject(content);
		JSONArray array = JsonUtil.getJsonArray(mainJson, "array");
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
	
	protected List<String> getAllArea(UserBean userBean) {
		List<String> result = CollectionUtils.newArrayList();
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("TYPE", "getAreaInfo");
		String body = JsonUtil.fields("user", userBean);
		String content = client.subPostForOnlyOneClient(API_URL, body, "utf-8", headerMap);
		if(!isSuccess(content)) {
            return null;
        }
        JSONObject mainJson =  JsonUtil.getJsonObject(content);
        JSONArray array = JsonUtil.getJsonArray(mainJson, "array");
//		JSONArray array = JsonUtil.getJsonArray(content);
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
	
	protected DeviceDataBean getDeviceDataBean(String snaddr) {
		DeviceBean bean = new DeviceBean();
		bean.setSnaddr(snaddr);
		return getDeviceDataBean(bean);
	}

	protected DeviceDataBean getDeviceDataBean(DeviceBean bean) {
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
		JSONObject jsonObject = JsonUtil.getJsonObject(content);
		if(!isSuccess(content)) {
		   return null; 
		}
		JSONObject json = JsonUtil.getJSONObject(jsonObject, "array");
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

	public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}

	public void setApiAccessTokenDAO(ApiAccessTokenDAO apiAccessTokenDAO) {
		this.apiAccessTokenDAO = apiAccessTokenDAO;
	}
	
}
