package cn.guanmai.util;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

/* 
* @author liming 
* @date Jan 3, 2019 5:56:07 PM 
* @todo TODO
* @version 1.0 
*/
public class IntTypeAdapter implements ObjectSerializer {

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {
		int fieldValue = 0;
		if (object != null) {
			String value = String.valueOf(object);
			if (value.equalsIgnoreCase("true") || value.equals("1")) {
				fieldValue = 1;
			}
		}
		serializer.write(fieldValue);

	}

}
