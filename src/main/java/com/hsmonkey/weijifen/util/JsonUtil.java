
package com.hsmonkey.weijifen.util;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.magic.Property;
import wint.lang.magic.Transformer;
import wint.lang.magic.reflect.ReflectMagicObject;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.lang.utils.Tuple;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author niepeng
 *
 * @date 2012-9-13 下午10:41:36
 */
public class JsonUtil {

	protected final static Logger log = LoggerFactory.getLogger(JsonUtil.class);

	public static <T> String mfields(final String fields, List<T> items, final String dateFormat) {
		if(CollectionUtil.isEmpty(items)) {
			items = CollectionUtil.newArrayList();
		}
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Transformer<T, String> transformer = new Transformer<T, String>() {
			public String transform(Object object) {
				if (object == null) {
					return StringUtil.EMPTY;
				}
				return fields(fields, object, false, dateFormat);
			}
		};
		sb.append(joinNew(items, ",", transformer));
		sb.append(']');
		return sb.toString();
	}

	public static <T> String mfields(final String fields, List<T> items) {
		return mfields(fields, items, null);
	}


	public static <T> String joinNew(Collection<T> c, String token, Transformer<T, String> valueTransformer) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (T object : c) {
			if (first) {
				first = false;
			} else {
				sb.append(token);
			}
			if (valueTransformer != null) {
				sb.append(valueTransformer.transform(object));
			} else {
				sb.append(object);
			}
		}
		return sb.toString();
	}


	public static <T> String mfieldsHasSub(final String fields, List<T> items) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Transformer<T, String> transformer = new Transformer<T, String>() {
			public String transform(Object object) {
				if (object == null) {
					return StringUtil.EMPTY;
				}
				return fields(fields, object, true);
			}
		};
		sb.append(CollectionUtil.join(items, ",", transformer));
		sb.append(']');
		return sb.toString();
	}
	
	public static String fields(String fields,  Object item) {
		return fields(fields, item, null);
	}

	public static String fields(String fields,  Object item, String dateFormat) {
		StringBuilder sb = new StringBuilder();
		List<String> propertyNames = StringUtil.splitTrim(fields, ",");
		fieldsImpl(propertyNames, item, sb, false, dateFormat);
		return sb.toString();
	}





	public static String fields(String fields,  Object item, boolean hasSub) {
		return fields(fields, item, hasSub, null);
	}

	public static String fields(String fields,  Object item, boolean hasSub, String dateFormat) {
		StringBuilder sb = new StringBuilder();
		List<String> propertyNames = StringUtil.splitTrim(fields, ",");
		fieldsImpl(propertyNames, item, sb, hasSub, dateFormat);
		return sb.toString();
	}

	private static void fieldsImpl(List<String> propertyNames, Object item, StringBuilder sb, boolean hasSub, String dateFormat) {
		ReflectMagicObject ref = new ReflectMagicObject(item);
		Map<String, Property> properties = CollectionUtils.newHashMap();
		//存放json的key,可能有重复
		ArrayList<String> jsonKey = CollectionUtil.newArrayList();
		for(String field : propertyNames) {
			int pointSplitIndex = field.indexOf(".");
			if( pointSplitIndex> 0) {
				String objName = field.substring(0,pointSplitIndex);
				String objAttributeName = field.substring(pointSplitIndex + 1);
				Property property = ref.getMagicClass().getProperty(objName);
                properties.put(objAttributeName, property);
			} else {
				Property property = ref.getMagicClass().getProperty(field);
				properties.put(field, property);
			}
			//生成json的key
			String attributeName = field.substring(field.lastIndexOf(".") + 1);
			jsonKey.add(attributeName);
		}
		try {
			sb.append(toJson(properties, item, jsonKey, dateFormat));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

	}

	private static String toJson(Map<String, Property> properties, Object item, ArrayList<String> jsonKey, String dateFormat) throws JSONException {
        StringWriter stringWriter = new StringWriter();
        JSONWriter jsonWriter = new JSONWriter(stringWriter);
        jsonWriter.object();
        for (Map.Entry<String, Property> propertyEntry : properties.entrySet()) {
            String fieldName = propertyEntry.getKey();
            Property property = propertyEntry.getValue();

            //add by wull 2014-03-27 支持三级属性
            int thirdIndex = fieldName.indexOf(".");
            String thirdObjAttributeName = "";
            if(thirdIndex > 0){
                String thirdObjName = fieldName.substring(0, thirdIndex);
                thirdObjAttributeName = fieldName.substring(thirdIndex + 1);
                //如果key中存在多个一样的名称需要增加前面的类名
                if(jsonKey.indexOf(thirdObjAttributeName) != jsonKey.lastIndexOf(thirdObjAttributeName)){
                    jsonWriter.key(StringsUtil.replacePointToUpper(fieldName));
                }else{
                    jsonWriter.key(thirdObjAttributeName);
                }

                fieldName = thirdObjName;
            }else{
                jsonWriter.key(fieldName);
            }

            Object value = property.getValue(item);
            if (value instanceof Date) {
            	if(dateFormat == null) {
            		jsonWriter.value(DateUtil.format((Date) value, DateUtil.DEFAULT_DATE_FMT));
            	} else {
            		jsonWriter.value(DateUtil.format((Date) value, dateFormat));
            	}

            } else {
                if (value == null) {
                    jsonWriter.value(StringUtil.EMPTY);
                }
                
                /*
                else if(value instanceof AccountMainUserDO){
                    Object object = getObjctAttributeValue(value, fieldName);
//                    if (object instanceof ProductDO ) {
//                        jsonWriter.value(getObjctAttributeValue(object, thirdObjAttributeName));
//                    }else{
                        jsonWriter.value(object);
//                    }
                }
     			*/
                
//                else if(value instanceof ProductDO /*|| value instanceof CashCouponDO || value instanceof PromotionDO*/){
//                    jsonWriter.value(getObjctAttributeValue(value, fieldName));
//                }
                else {
                    jsonWriter.value(value);
                }
            }
        }
        jsonWriter.endObject();
        return stringWriter.toString();
    }


	private static Object getObjctAttributeValue(Object object, String attribute) {
		ReflectMagicObject ref = new ReflectMagicObject(object);
		Property property = ref.getMagicClass().getProperty(attribute);
		if (property != null) {
			Object attributeValue = property.getValue(object);
			if(attributeValue == null) {
				return StringUtil.EMPTY;
			}
			if (attributeValue instanceof Date) {
				return DateUtil.format((Date) attributeValue, DateUtil.DEFAULT_DATE_FMT);
			}
			return attributeValue;
		}
		return StringUtil.EMPTY;
	}

	public static JSONObject getJsonObject(Object obj) {
		if (obj == null) {
			return null;
		}

		if(obj instanceof JSONObject) {
			return (JSONObject)obj;
		}

		String content = (String) obj;
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			JSONObject json = new JSONObject(content);
			return json;
		} catch (JSONException e) {
			return null;
		}
	}

	public static JSONArray getJsonArray(Object obj) {
		if (obj == null) {
			return null;
		}

		if(obj instanceof JSONArray) {
			return (JSONArray)obj;
		}

		String content = (String) obj;

		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			JSONArray json = new JSONArray(content);
			return json;
		} catch (JSONException e) {
			return null;
		}
	}


	public static JSONArray getJsonArray(JSONObject json, String name) {
		if (json == null || name == null) {
			return null;
		}
		if (!json.has(name)) {
			return null;
		}
		try {
			return json.getJSONArray(name);
		} catch (JSONException e) {
			return null;
		}
	}

	public static JSONObject getJSONObject(JSONObject json, String name) {
		if (json == null || name == null) {
			return null;
		}
		if (!json.has(name)) {
			return null;
		}
		try {
			return json.getJSONObject(name);
		} catch (JSONException e) {
			return null;
		}
	}

	public static String getString(JSONObject json, String name, String defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getString(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static int getInt(JSONObject json, String name, int defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getInt(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static long getLong(JSONObject json, String name, long defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getLong(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static double getDouble(JSONObject json, String name, double defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getDouble(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static boolean getBoolean(JSONObject json, String name, boolean defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getBoolean(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static <T> String beanToJson(T t) {
//		return net.sf.json.JSONObject.fromObject(t).toString();
		Gson gson = new Gson();
		return gson.toJson(t);
	}

	public static <T> String mapToJson(Map<String, T> map) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(map);
		return jsonStr;
	}

	public static <T> String listToJson(T o) {
//		return net.sf.json.JSONArray.fromObject(o).toString();
		Gson gson = new Gson();
		return gson.toJson(o);
	}

	public static <T> T jsonToBeanSelf(String jsonString, Class<T> beanCalss) {
		if (!StringUtil.isBlank(jsonString)) {
			jsonString = StringsUtil.urlDecode(jsonString);
		}
		return jsonToBeanForList(jsonString, beanCalss);
	}
	
	public static <T> T jsonToBeanForList(String jsonString, Class<T> beanCalss) {
		try {
			T obj = (T) beanCalss.newInstance();
			JSONObject json = getJsonObject(jsonString);
			Iterator it = json.keys();
			Map<String, Object> map = new HashMap<String, Object>();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object valueObj = json.get(key);
				map.put(key, valueObj);
			}

			Tuple<Method[], Field[]> tuple = getMethodsAndFiles(beanCalss);
			Method methods[] = tuple.getT1();
			Field fields[] = tuple.getT2();

			for (Field field : fields) {
				String fldtype = field.getType().getSimpleName();
				String fldSetName = field.getName();
				String setMethod = pareSetName(fldSetName);
				if (!checkMethod(methods, setMethod)) {
					continue;
				}
				Object value = map.get(fldSetName);
				if (value == null) {
					continue;
				}
				Method method = getMethod(beanCalss, field, setMethod);
				if (null != value && method != null) {
					if ("String".equals(fldtype)) {
						method.invoke(obj, (String) value);
					} else if ("double".equals(fldtype) || "Double".equals(fldtype)) {
						double dou = 0;
						if(value instanceof Integer) {
							dou = ((Integer)value).intValue();
						} else {
							dou = (Double)value;
						}
						method.invoke(obj, dou);
					} else if ("int".equals(fldtype)) {
						intInvoke(obj, value, method);
					} else if("boolean".equals(fldtype)) {
						intInvoke(obj, value, method);
					} else if ("float".equals(fldtype)) {
						float f = 0.f;
						if(value instanceof Integer) {
							f = ((Integer)value).intValue();
						} else {
							f = (float)Double.parseDouble(value.toString());
						}
						method.invoke(obj, f);
					} else if ("long".equals(fldtype)) {
						long lon = 0;
						if(value instanceof Integer) {
							lon = ((Integer)value).intValue();
						} else {
							lon = (Long)value;
						}
						method.invoke(obj, lon);
					} else if ("Date".equals(fldtype)) {
						if (((String) value).length() > 10) {
							method.invoke(obj, DateUtil.parse((String) value));
						} else {
							method.invoke(obj, DateUtil.parseNoException((String) value, DateUtil.DEFAULT_DATE_FMT_NO));
						}
					}
				}

			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("jsonToBeanSelf error", e);
		}
		return null;
	}
	

	private static <T> void intInvoke(T obj, Object value, Method method) {
		try {
			if (value instanceof String) {
				method.invoke(obj, StringsUtil.str2int((String) value));
				return;
			}
			method.invoke(obj, value);
			return;
		} catch (Exception e) {
		}
	}

    /**
     * 拼接某属性set 方法
     * @param fldname
     * @return
     */
    private static String pareSetName(String fldname){
        if(null == fldname || "".equals(fldname)){
            return null;
        }
        String pro = "set"+fldname.substring(0,1).toUpperCase()+fldname.substring(1);
        return pro;
    }
    /**
     * 判断该方法是否存在
     * @param methods
     * @param met
     * @return
     */
    private static boolean checkMethod(Method methods[],String met){
        if(null != methods ){
            for(Method method:methods){
                if(met.equals(method.getName())){
                    return true;
                }
            }
        }
        return false;
    }




	private static <T> Method getMethod(Class<T> beanCalss, Field field, String setMethod) {
		try {
			return beanCalss.getMethod(setMethod, field.getType());
		} catch (Exception e1) {
		}

		if (int.class == field.getType()) {
			try {
				return beanCalss.getMethod(setMethod, Integer.class);
			} catch (Exception e1) {
			}
		}

		if (long.class == field.getType()) {
			try {
				return beanCalss.getMethod(setMethod, Long.class);
			} catch (Exception e1) {
			}
		}

		if (float.class == field.getType()) {
			try {
				return beanCalss.getMethod(setMethod, Float.class);
			} catch (Exception e1) {
			}
		}

		return null;
	}

	private static <T> Tuple<Method[], Field[]> getMethodsAndFiles(Class<T> beanCalss) {
		Method[] resultMethod = null;
		Field[] resultField = null;
		Class obj = beanCalss.getSuperclass();
		if (obj.getName().equals("java.lang.Object")) {
			return new Tuple<Method[], Field[]>(beanCalss.getDeclaredMethods(), beanCalss.getDeclaredFields());
		}

		Method[] m = beanCalss.getDeclaredMethods();
		Method[] m2 = obj.getDeclaredMethods();

		Field[] f = beanCalss.getDeclaredFields();
		Field[] f2 = obj.getDeclaredFields();

		if (m2 != null) {
			resultMethod = new Method[m.length + m2.length];
			for (int i = 0; i < resultMethod.length; i++) {
				if (i < m.length) {
					resultMethod[i] = m[i];
					continue;
				}
				resultMethod[i] = m2[i - m.length];

			}
		}

		if (f2 != null) {
			resultField = new Field[f.length + f2.length];
			for (int i = 0; i < resultField.length; i++) {
				if (i < f.length) {
					resultField[i] = f[i];
					continue;
				}
				resultField[i] = f2[i - f.length];
			}
		}

		return new Tuple<Method[], Field[]>(resultMethod, resultField);
	}


	public static <T> T jsonToBeanForUnDecode(String jsonString, Class<T> beanCalss) {

//		jsonString = StringsUtil.urlDecode(jsonString);
		 try {
			Gson gson = new Gson();
			 return gson.fromJson(jsonString, beanCalss);
		} catch (Exception e) {
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		T jsonCat = gson.fromJson(jsonString, beanCalss);
		 return jsonCat;

	}



	public static <T> T jsonToBean(String jsonString, Class<T> beanCalss) {

//		jsonString = StringsUtil.urlDecode(jsonString);
		 /*try {
			Gson gson = new Gson();
			 return gson.fromJson(jsonString, beanCalss);
		} catch (Exception e) {
		}

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		T jsonCat = gson.fromJson(jsonString, beanCalss);
		 return jsonCat;
		 */
		return jsonToBeanSelf(jsonString, beanCalss);
	}

	public static <T> List<T> jsonToList(String jsonString, Class<T> beanClass) {
		List<T> result = new ArrayList<T>();

		try {
			if(!StringUtil.isBlank(jsonString)) {
				jsonString =StringsUtil.urlDecode(jsonString);
			}
			JSONArray jsonArray = getJsonArray(jsonString);
			if (jsonArray == null || jsonArray.length() == 0) {
				return result;
			}

			for (int i = 0, size = jsonArray.length(); i < size; i++) {
				T t = jsonToBeanForList(getJsonObject(jsonArray.get(i)).toString(), beanClass);
				result.add(t);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static <T> List<T> jsonToListForUnDecode(String jsonString, Class<T> beanClass) {
		List<T> result = new ArrayList<T>();

		try {
			if(!StringUtil.isBlank(jsonString)) {
				jsonString =StringsUtil.urlDecode(jsonString);
			}
			JSONArray jsonArray = getJsonArray(jsonString);
			if (jsonArray == null || jsonArray.length() == 0) {
				return result;
			}

			for (int i = 0, size = jsonArray.length(); i < size; i++) {
				T t = jsonToBeanForUnDecode(getJsonObject(jsonArray.get(i)).toString(), beanClass);
				result.add(t);
			}
		} catch (Exception e) {
		}
		return result;
	}



}
