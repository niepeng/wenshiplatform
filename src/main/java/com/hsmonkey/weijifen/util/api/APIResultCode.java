package com.hsmonkey.weijifen.util.api;

import wint.help.biz.result.ResultCode;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>创建时间: Dec 5, 2014  3:41:59 PM</p>
 */
public class APIResultCode extends ResultCode {

	private static final long serialVersionUID = 7089256730906687852L;

	//总的返回码
	public static final ResultCode SUCCESS = new StringExtendsResultCode("成功", 0);
	public static final ResultCode SYSTEM_ERROR = new StringExtendsResultCode("系统错误", 1);
	public static final ResultCode UNKNOWN_ERROR = new StringExtendsResultCode("未知错误", 2);
	public static final ResultCode SIGN_ERROR = new StringExtendsResultCode("签名错误", 3);
	public static final ResultCode SOURCE_NAME_ERROR = new StringExtendsResultCode("合作方名称错误", 4);
	public static final ResultCode DATA_FORMAT_ERROR = new StringExtendsResultCode("数据格式错误", 5);
	public static final ResultCode ARGS_ERROR = new StringExtendsResultCode("参数错误", 6);
	public static final ResultCode NO_DATA = new StringExtendsResultCode("没有接收到参数", 7);

	public static String getMessage(ResultCode resultCode) {
        if (resultCode instanceof StringExtendsResultCode) {
            return ((StringExtendsResultCode) resultCode).getMessage();
        }
        return null;
    }

}

