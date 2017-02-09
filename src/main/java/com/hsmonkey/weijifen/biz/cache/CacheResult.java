package com.hsmonkey.weijifen.biz.cache;

import java.io.Serializable;

public class CacheResult  implements Serializable {

	private static final long serialVersionUID = 9011454429646403225L;

	private int code = Codes.SUCCESS.getValue();
	
	private Object key;
	
	private Object value;

	@Override
	public String toString() {
		return "CacheResult [code=" + code + ", key=" + key + ", value=" + value + "]";
	}

	public CacheResult() {
		super();
	}

	public CacheResult(int code) {
		super();
		this.code = code;
	}

	public CacheResult(Object key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public boolean isSuccess() {
		return Codes.SUCCESS.getValue() == code;
	}
	
	public static enum Codes {
		SUCCESS(0),
		NOT_EXIST(10),
		EXPIRED(20),
		NOT_ENABLE(30),
		REQUEST_FAIL(40);
		
		
		private final int value;
		private Codes(int v) {
			value = v;
		}
		public int getValue() {
			return value;
		}
	}
	
}