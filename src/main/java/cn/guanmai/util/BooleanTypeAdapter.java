package cn.guanmai.util;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

/**
 * @author liming
 * @date 2020年1月9日
 * @time 下午7:27:33
 * @des TODO
 */

public class BooleanTypeAdapter implements ObjectSerializer {

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {
		boolean fieldValue = false;
		if (object != null) {
			String value = String.valueOf(object);
			if (value.equalsIgnoreCase("true") || value.equals("1")) {
				fieldValue = true;
			}
		}
		serializer.write(fieldValue);
	}

}
