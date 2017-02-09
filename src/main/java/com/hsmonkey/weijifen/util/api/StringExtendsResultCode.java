package com.hsmonkey.weijifen.util.api;

import wint.help.biz.result.ResultCode;

/**
 * <p>标题: 可以同时赋值错误信息和错误code</p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Dec 5, 2013  3:45:57 PM</p>
 * <p>作者：聂鹏</p>
 */
public class StringExtendsResultCode extends ResultCode {

	private static final long serialVersionUID = -8389234614607613263L;

	private String message;
	
	public StringExtendsResultCode(String message, int code) {
		super();
		this.message = message;
		setCode(code);
	}

	public String getMessage() {
		return message;
	}

}

