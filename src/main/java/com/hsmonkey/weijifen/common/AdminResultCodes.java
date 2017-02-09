package com.hsmonkey.weijifen.common;

import wint.help.biz.result.ResultCode;
import wint.help.biz.result.StringResultCode;

public class AdminResultCodes extends ResultCode {

	private static final long serialVersionUID = 793016245306904288L;
	
	public static final ResultCode USER_NOT_LOGIN = create();
	
	public static final ResultCode USER_NOT_PERMISSION = new StringResultCode("当前用户没有权限访问或操作本次请求");
	
	public static final ResultCode ARGS_ERROR = new StringResultCode("参数错误");
	
    public static final ResultCode CHECK_SIGN_ERROR = new StringResultCode("参数签名出错");

    public static final ResultCode DATE_EXPIRE = new StringResultCode("服务已到期,请续费！");

    public static final ResultCode NO_PERMISSION_OR_DATE_EXPIRE = new StringResultCode("服务已到期 或 未开通该权限");

    public static final ResultCode MARK_SUCCESS = new StringResultCode("success");

}