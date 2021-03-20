package com.hsmonkey.weijifen.biz.ao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.codec.MD5;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;

import com.google.common.util.concurrent.RateLimiter;
import com.hsmonkey.weijifen.biz.ao.ApiAO;
import com.hsmonkey.weijifen.biz.ao.BaseAO;
import com.hsmonkey.weijifen.biz.bean.ApiMethodEnum;
import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
import com.hsmonkey.weijifen.biz.bean.UserApiCallBean;
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
	
	static Map<String, UserApiCallBean> apiCallLimiterMap = CollectionUtils.newHashMap();
	static Map<String, RateLimiter> accessTokenMap = CollectionUtils.newHashMap();
	// 1秒钟限制调用次数， 1/60 表示1分钟限制1次调用
	static double accessTokenCallLimit = 1.d/60;
	
	@Override
	public Result getAccessToken(FlowData flowData, String user, String psw) {
		Result result = new ResultSupport(false);
		try {
			if(StringUtil.isBlank(user) || StringUtil.isBlank(psw)) {
				result.setResultCode(ApiResultCodes.ARGS_ERROR);
				return result;
			}

			if (!isSuccess(loginCall(user, psw, true))) {
				result.setResultCode(ApiResultCodes.USER_PSW_ERROR);
				return result;
			}
			
			RateLimiter rateLimiter = accessTokenMap.get(user);
			if (rateLimiter == null) {
				rateLimiter = RateLimiter.create(accessTokenCallLimit);
				rateLimiter.acquire();
				accessTokenMap.put(user, rateLimiter);
			} else if (!rateLimiter.tryAcquire()) {
				result.setResultCode(ApiResultCodes.API_CALL_ERROR);
				return result;
			}
			
			ApiAccessTokenDO apiAccessTokenDO = apiAccessTokenDAO.queryByUserLimit1(user);
			if (apiAccessTokenDO != null) {
				apiCallLimiterMap.remove(apiAccessTokenDO.getAccessToken());
			}
			
			// 1分钟内只能调用一次，采用了上面的guava的rateLimiter实现了
//			ApiAccessTokenDO apiAccessTokenDO = apiAccessTokenDAO.queryByUserLimit1(user);
//			if(apiAccessTokenDO != null && DateUtil.distanceSeconds(apiAccessTokenDO.getGmtCreate(), now) < Constant.Time.MIN) {
//				result.setResultCode(ApiResultCodes.API_CALL_ERROR);
//				return result;
//			}
			
			Date now = new Date();
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
			
			if (!checkApiCallLimit(result, accessToken, ApiMethodEnum.deviceList)) {
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

			ApiAccessTokenDO accessTokenDO = (ApiAccessTokenDO)result.getModels().get("apiAccessTokenDO");
			String user = accessTokenDO.getUser();
			
			if (!checkApiCallLimit(result, accessToken, ApiMethodEnum.inTimeData)) {
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
			setDeviceDataBeans(dataList, snaddrList, deviceList.size(), user);

			List<DeviceDataBean> resultDataList = new ArrayList<DeviceDataBean>();
			for(DeviceDataBean tmp : dataList) {
				if(tmp == null) {
					continue;
				}
				resultDataList.add(tmp);
			}
			result.getModels().put("dataList", resultDataList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("intimeDataError", e);
		}
		return result;
	}


	/**
	 *  根据snaddr的数量，实时获取数据方式调整
	 * 	1.如果参数snaddr的数量少于10个，直接单个查询
	 * 	2.如果参数snaddr的数量超过该账号的一半，走全量获取设备实时数据
	 * @param dataList
	 * @param snaddrList
	 * @param totalSize
	 * @param user
	 */
	private void setDeviceDataBeans(List<DeviceDataBean> dataList, List<String> snaddrList, int totalSize, String user) {
		if (snaddrList.size() <= 10 && snaddrList.size() <= totalSize / 2) {
			DeviceDataBean tmpDeviceDataBean = null;
			for (String tmp : snaddrList) {
				tmpDeviceDataBean = getDeviceDataBean(tmp);
				if (tmpDeviceDataBean == null) {
					continue;
				}
				tmpDeviceDataBean.setSnaddr(tmp);
				dataList.add(tmpDeviceDataBean);
			}
			return;
		}

		UserBean userBean = new UserBean();
		userBean.setUser(user);
		List<DeviceBean> deviceBeanList = CollectionUtils.newArrayList(snaddrList.size());
		DeviceBean tmpBean;
		for (String snaddr : snaddrList) {
			tmpBean = new DeviceBean();
			tmpBean.setSnaddr(snaddr);
			deviceBeanList.add(tmpBean);
		}

		setBeanDatas(deviceBeanList, userBean);

		for (DeviceBean tmp : deviceBeanList) {
			if(tmp == null) {
				continue;
			}
			dataList.add(tmp.getDataBean());
		}

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
	
	private boolean checkApiCallLimit(Result result, String accessToken, ApiMethodEnum apiMethodEnum) {
		UserApiCallBean apiCallBean = apiCallLimiterMap.get(accessToken);
		if (apiCallBean == null) {
			apiCallBean = new UserApiCallBean();
			apiCallLimiterMap.put(accessToken, apiCallBean);
		}
		RateLimiter tmpRateLimiter = null;
		if (ApiMethodEnum.deviceList == apiMethodEnum) {
			tmpRateLimiter = apiCallBean.getDeviceListLimiter();
		} else if (ApiMethodEnum.inTimeData == apiMethodEnum) {
			tmpRateLimiter = apiCallBean.getInTimeDataLimiter();
		}

		if (!tmpRateLimiter.tryAcquire()) {
			result.setResultCode(ApiResultCodes.API_CALL_ERROR);
			return false;
		}

		return true;
	}


	private String genAccessToken() {
		String f = RandomUtil.getRandomString(17) + System.currentTimeMillis() % RandomUtil.randomInt(100, 600);
		return MD5.encrypt(f) + RandomUtil.getRandomString(10).toLowerCase();
	}

}
