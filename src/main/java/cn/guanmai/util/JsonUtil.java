package cn.guanmai.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * @author liming
 * @date Oct 31, 2018 4:00:35 PM
 * @todo TODO
 * @version 1.0
 */
public class JsonUtil {
	public static String objectToStr(Object obj) {
		return JSONObject.toJSONString(obj);
		// return new GsonBuilder().setLenient().create().toJson(obj);
	}

	/**
	 * Json字符串转换为具体类对象
	 * 
	 * @param str
	 * @param classOfT
	 * @return
	 */
	public static <E> E strToClassObject(String str, Class<E> calzz) {
		E e = JSONObject.parseObject(str, calzz);
		return e;

	}

	public static <T> List<T> strToClassList(String str, Class<T> calzz) {
		List<T> tList = JSONArray.parseArray(str, calzz);
		return tList;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> objectToMap(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		List<Field> fields = getFieldList(obj.getClass());
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			try {
				Object value = field.get(obj);
				if (value != null) {
					if (field.getType().isInterface() || field.getType().isLocalClass()
							|| field.getType().isMemberClass()) {
						map.put(field.getName(), objectToStr(value));
					} else {
						map.put(field.getName(), String.valueOf(value));
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return map;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	private static List<Field> getFieldList(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		Set<String> filedNames = new HashSet<>();
		for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
			try {
				Field[] list = c.getDeclaredFields();
				for (Field field : list) {
					String name = field.getName();
					if (filedNames.contains(name)) {
						continue;
					}
					filedNames.add(field.getName());
					fields.add(field);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return fields;
	}
}
