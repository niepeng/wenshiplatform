package com.hsmonkey.weijifen.biz.ao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.codec.MD5;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.Session;

import com.hsmonkey.weijifen.biz.ao.ApiAO;
import com.hsmonkey.weijifen.biz.ao.BaseAO;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.biz.dal.dataobject.ApiAccessTokenDO;
import com.hsmonkey.weijifen.common.ApiResultCodes;
import com.hsmonkey.weijifen.common.Constant;
import com.hsmonkey.weijifen.common.SessionKeys;
import com.hsmonkey.weijifen.util.DateUtil;
import com.hsmonkey.weijifen.util.JsonUtil;
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

	private String genAccessToken() {
		String f = RandomUtil.getRandomString(17) + System.currentTimeMillis() % RandomUtil.randomInt(100, 600);
		return MD5.encrypt(f) + RandomUtil.getRandomString(10).toLowerCase();
	}

}
