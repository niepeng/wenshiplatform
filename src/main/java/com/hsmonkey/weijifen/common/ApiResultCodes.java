package com.hsmonkey.weijifen.common;

import wint.help.biz.result.ResultCode;
import wint.help.biz.result.StringResultCode;

public class ApiResultCodes extends ResultCode {

	private static final long serialVersionUID = 7652974006641057038L;

	public static final ResultCode CHECK_SIGN_ERROR = new StringResultCode("参数签名出错");

	public static final ResultCode ARGS_ERROR = new StringResultCode("参数错误");

}
