package com.lianyg.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

public class CommonUtil {

	public static String toJson(List<?> list) throws Exception {
		StringBuffer result = new StringBuffer();
		if (list == null || 0 == list.size()) {
			return result.toString();
		}
		Object obj = list.get(0);
		Method[] method = obj.getClass().getDeclaredMethods();

		result.append("[");
		for (int i = 0; i < list.size(); i++) {
			obj = list.get(i);
			result.append("{");
			for (int j = 0; j < method.length; j++) {
				if (method[j].getName().startsWith("get")) {
					result.append("\"");
					result.append(toFieldName(method[j].getName()));
					result.append("\"");
					result.append(":");
					result.append("\"");
					result.append(method[j].invoke(obj));
					result.append("\"");
					result.append(",");
				}
			}
			result.deleteCharAt(result.length() - 1);
			if (i < list.size() - 1) {
				result.append("},");
			} else {
				result.append("}");
			}

		}
		result.append("]");
		System.out.println(result.toString());
		return result.toString();
	}

	public static String toFieldName(String methodName) {
		String result = "";
		result += (char) (methodName.charAt(3) + 32);
		for (int i = 4; i < methodName.length(); i++) {
			result += methodName.charAt(i);
		}
		return result;
	}

	public static String isNullString(Object value) {
		if ("null".equals(String.valueOf(value)) || String.valueOf(value).equals("")) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}

	public static String isNullString1(Object value) {
		if ("null".equals(String.valueOf(value)) || String.valueOf(value).equals("")) {
			return "-";
		} else {
			return String.valueOf(value);
		}
	}

	public static String isNullString2(Object value) {
		if ("null".equals(String.valueOf(value))) {
			return "-1";
		} else {
			return String.valueOf(value);
		}
	}

	public static String readProperties(String key) throws IOException {
		InputStream inputStream = CommonUtil.class.getClassLoader().getResourceAsStream("/config/config.properties");
		Properties p = new Properties();
		p.load(inputStream);
		return p.getProperty(key);
	}
}
