package cn.guanmai.util;

import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;


/* 
* @author liming 
* @date Dec 29, 2018 5:40:42 PM 
* @des 用来统计用例的执行结果
* @version 1.0 
*/
public class TestResult {
	// 统计用例个数
	public static AtomicInteger pass = new AtomicInteger(0);
	public static AtomicInteger fail = new AtomicInteger(0);
	public static AtomicInteger skip = new AtomicInteger(0);

	public static JSONObject getTestResult() {
		JSONObject result = new JSONObject();
		result.put("pass", pass);
		result.put("fail", fail);
		result.put("skip", skip);
		return result;
	}

}
