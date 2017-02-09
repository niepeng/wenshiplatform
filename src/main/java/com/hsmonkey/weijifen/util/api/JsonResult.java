package com.hsmonkey.weijifen.util.api;

import org.json.JSONObject;

import com.hsmonkey.weijifen.util.JsonUtil;

public class JsonResult {

	private int code;

	private String message;

	private JSONObject jsonObject;



	public void parse(String jsonString) {
		JSONObject jsonObject = JsonUtil.getJsonObject(jsonString);
		this.code = JsonUtil.getInt(jsonObject, "code", -1);
		if (!isSuccess()) {
			this.message = JsonUtil.getString(jsonObject, "message", null);
			return;
		}
		this.jsonObject = JsonUtil.getJSONObject(jsonObject, "data");
	}

	public boolean isSuccess() {
		return this.code == 0;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

}