package com.hsmonkey.weijifen.util;

import java.lang.reflect.Method;

public class ReflectUtil {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void invokeSetMethod(Object obj, String attributeName, Object value, Class argsClass) {
		try {
			Class clazz = obj.getClass();
			Method m = clazz.getDeclaredMethod("set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1), argsClass);
			m.invoke(obj, value);
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getValue(Object obj, String attributeName) {
		try {
			Class clazz = obj.getClass();
			Method m = clazz.getDeclaredMethod("get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1));
			return m.invoke(obj);
		} catch (Exception e) {
		}
		return null;
	}

	public static int getIntValue(Object obj, String attributeName) {
		Object value = getValue(obj, attributeName);
		if(value == null) {
			return 0;
		}
		return (Integer)value;
	}

	public static void main(String[] args) {
		
	}
}