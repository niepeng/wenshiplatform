package com.hsmonkey.weijifen.common;

import wint.help.biz.result.ResultCode;
import wint.help.biz.result.StringResultCode;

public class ApiResultCodes extends ResultCode {

	private static final long serialVersionUID = 7652974006641057038L;

	public static final ResultCode CHECK_SIGN_ERROR = new StringResultCode(1,"参数签名出错");

	// 每个参数的内容是否为空
	public static final ResultCode ARGS_ERROR = new StringResultCode(2,"参数错误");

	public static final ResultCode API_CALL_ERROR = new StringResultCode(3,"api调用数量限制");

	public static final ResultCode USER_PSW_ERROR = new StringResultCode(4,"用户名或密码错误");

	public static final ResultCode ACCESS_TOKEN_ERROR = new StringResultCode(5,"accessToken不正确或已过期");

	public static final ResultCode SNADDR_ERROR = new StringResultCode(6,"snaddr错误或不合法");

}
