package com.hsmonkey.weijifen.biz.ao.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.codec.MD5;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;

import com.hsmonkey.weijifen.biz.ao.ApiAO;
import com.hsmonkey.weijifen.biz.ao.BaseAO;
import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.biz.dal.dataobject.ApiAccessTokenDO;
import com.hsmonkey.weijifen.common.ApiResultCodes;
import com.hsmonkey.weijifen.common.Constant;
import com.hsmonkey.weijifen.util.CollectionUtils;
import com.hsmonkey.weijifen.util.DateUtil;
import com.hsmonkey.weijifen.util.RandomUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年4月1日  下午8:46:04</p>
 * <p>作者：niepeng</p>
 */
public class ApiAOImpl extends BaseAO implements ApiAO {

	// accessToken的过期时间，单位：秒
	static final int ACCESS_TOKEN_EXPIRE = 7 * Constant.Time.DAY;
	
	@Override
	public Result getAccessToken(FlowData flowData, String user, String psw) {
		Result result = new ResultSupport(false);
		try {
			if(StringUtil.isBlank(user) || StringUtil.isBlank(psw)) {
				result.setResultCode(ApiResultCodes.ARGS_ERROR);
				return result;
			}
			
			UserBean userBean = new UserBean();
			userBean.setUser(user);
			userBean.setPassword(psw);
			if (!isSuccess(loginCall(userBean))) {
				result.setResultCode(ApiResultCodes.USER_PSW_ERROR);
				return result;
			}
			
			// 1分钟内只能生成一次
			Date now = new Date();
			ApiAccessTokenDO apiAccessTokenDO = apiAccessTokenDAO.queryByUserLimit1(user);
			if(apiAccessTokenDO != null && DateUtil.distanceSeconds(apiAccessTokenDO.getGmtCreate(), now) < Constant.Time.MIN) {
				result.setResultCode(ApiResultCodes.API_CALL_ERROR);
				return result;
			}
			
			ApiAccessTokenDO tmp = new ApiAccessTokenDO();
			tmp.setUser(user);
			tmp.setPsw(psw);
			String accessToken = genAccessToken();
			tmp.setExpireDate(DateUtil.changeMin(now, ACCESS_TOKEN_EXPIRE/60));
			tmp.setAccessToken(accessToken);
			tmp.setExpiresIn(ACCESS_TOKEN_EXPIRE);
			
			apiAccessTokenDAO.create(tmp);
			result.getModels().put("apiAccessTokenDO", tmp);
			result.setSuccess(true);
		} catch(Exception e) {
			log.error("getAccessTokenError", e);
		}
		return result;
	}
	

	@Override
	public Result getDeviceList(FlowData flowData, String accessToken) {
		Result result = new ResultSupport(false);
		try {
			boolean success = checkAccessToken(accessToken, result);
			if (!success) {
				return result;
			}
			ApiAccessTokenDO apiAccessTokenDO = (ApiAccessTokenDO) result.getModels().get("apiAccessTokenDO");

			// 获取设备列表数据
			UserBean userBean = new UserBean();
			userBean.setUser(apiAccessTokenDO.getUser());
			List<DeviceBean> deviceList = getAllDevice(userBean);
			result.getModels().put("deviceList", deviceList);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("getDeviceListError", e);
		}
		return result;
	}
	
	@Override
	public Result intimeData(FlowData flowData, String accessToken, String snaddrs) {
		Result result = new ResultSupport(false);
		try {
			if (StringUtil.isBlank(snaddrs)) {
				result.setResultCode(ApiResultCodes.ARGS_ERROR);
				return result;
			}

			boolean success = checkAccessToken(accessToken, result);
			if (!success) {
				return result;
			}

			ApiAccessTokenDO apiAccessTokenDO = (ApiAccessTokenDO) result.getModels().get("apiAccessTokenDO");
			// 检查当前参数中的snaddr是否为当前用户的设备
			UserBean userBean = new UserBean();
			userBean.setUser(apiAccessTokenDO.getUser());
			List<DeviceBean> deviceList = getAllDevice(userBean);
			Map<String, Object> userSnaddrMap = CollectionUtils.newHashMap();
			for (DeviceBean tmpDeviceBean : deviceList) {
				userSnaddrMap.put(tmpDeviceBean.getSnaddr(), tmpDeviceBean);
			}

			List<String> snaddrList = CollectionUtils.newArrayList(snaddrs.split(Constant.SPLIT));
			String tmpSnaddr = null;
			for (int i = 0, size = snaddrList.size(); i < size;) {
				tmpSnaddr = snaddrList.get(i);
				if (StringUtil.isBlank(tmpSnaddr)) {
					snaddrList.remove(i);
					continue;
				}
				if (!userSnaddrMap.containsKey(tmpSnaddr)) {
					result.setResultCode(ApiResultCodes.SNADDR_ERROR);
					return result;
				}
				i++;
			}

			if (CollectionUtils.isEmpty(snaddrList)) {
				result.setResultCode(ApiResultCodes.SNADDR_ERROR);
				return result;
			}

			List<DeviceDataBean> dataList = CollectionUtils.newArrayList(snaddrList.size());
			DeviceDataBean tmpDeviceDataBean = null;
			for (String tmp : snaddrList) {
				tmpDeviceDataBean = getDeviceDataBean(tmp);
				if (tmpDeviceDataBean == null) {
					continue;
				}
				tmpDeviceDataBean.setSnaddr(tmp);
				dataList.add(tmpDeviceDataBean);
			}

			result.getModels().put("dataList", dataList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("intimeDataError", e);
		}
		return result;
	}


	private boolean checkAccessToken(String accessToken, Result result) {
		if (StringUtil.isBlank(accessToken)) {
			result.setResultCode(ApiResultCodes.ARGS_ERROR);
			return false;
		}

		ApiAccessTokenDO fromDB = apiAccessTokenDAO.queryByAccessToken(accessToken);
		if (fromDB == null) {
			result.setResultCode(ApiResultCodes.ACCESS_TOKEN_ERROR);
			return false;
		}

		Date now = new Date();
		// 这里考虑到时间有误差，多送了10秒给用户来做缓存，不过期
		if (now.getTime() - Constant.MicroTime.SECOND * 10 > fromDB.getExpireDate().getTime()) {
			result.setResultCode(ApiResultCodes.ACCESS_TOKEN_ERROR);
			return false;
		}
		
		result.getModels().put("apiAccessTokenDO", fromDB);
		return true;
	}


	private String genAccessToken() {
		String f = RandomUtil.getRandomString(17) + System.currentTimeMillis() % RandomUtil.randomInt(100, 600);
		return MD5.encrypt(f) + RandomUtil.getRandomString(10).toLowerCase();
	}

}
