package cn.guanmai.common;

import cn.guanmai.okhttp.util.ClassUtils;
import cn.guanmai.util.JsonUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: test_station
 * @description: 类方法
 * @author: weird
 * @create: 2019-01-11 15:41
 **/
public class GmClassUtil {
	public static Map<String, String> objectToMap(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		List<Field> fields = ClassUtils.getFieldList(obj.getClass());
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			try {
				Object value = field.get(obj);
				if (value != null) {
					if (value instanceof List | value instanceof Map) {
						map.put(field.getName(), JsonUtil.objectToStr(value));
					} else if (value instanceof String) {
						map.put(field.getName(), (String) value);
					} else {
						map.put(field.getName(), value.toString());
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return map;
	}
}
