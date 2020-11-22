package cn.guanmai.request.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.FastHttpClient;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.okhttp.Response;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date May 5, 2019 5:10:49 PM 
* @des OpenAPI 专用封装请求
* @version 1.0 
*/
@SuppressWarnings("unchecked")
public class OpenRequestImpl implements OpenRequest {
	private static Logger logger = LoggerFactory.getLogger(OpenRequestImpl.class);
	private String access_token;

	public OpenRequestImpl(String access_token) {
		this.access_token = access_token;
	}

	@Override
	public JSONObject baseRequest(String url, RequestType type, Object obj) throws Exception {
		Reporter.log("前端接口: " + url);
		logger.info("前端接口: " + url);
		Map<String, String> paramMap = null;
		if (obj instanceof Map) {
			paramMap = (Map<String, String>) obj;
		} else {
			paramMap = JsonUtil.objectToMap(obj);
		}

		if (type == RequestType.GET) {
			paramMap.put("access_token", access_token);
		} else {
			url = url + "?access_token=" + access_token;
		}

		String paramStr = JsonUtil.objectToStr(paramMap);

		ReporterCSS.log("传递参数: " + paramStr);
		logger.info("传递参数: " + paramStr);

		Response resonse = FastHttpClient.requestType(type).url(url).addEncodedParams(paramMap).build().execute();

		int code = resonse.code();
		String str = resonse.string();
		if (code != 200) {
			Reporter.log("接口没有正确响应,响应值为  " + code);
			logger.info("接口没有正确响应,响应值为  " + code);
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}
		String temp_str = StringUtil.interceptString(str, 2000);
		Reporter.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		JSONObject retObj = JSON.parseObject(str);
		return retObj;
	}

	@Override
	public JSONObject baseRequest(String url, RequestType type) throws Exception {
		url = url + "?access_token=" + access_token;
		Reporter.log("前端接口: " + url);
		logger.info("前端接口: " + url);
		Reporter.log("传递参数: 无");
		logger.info("传递参数: 无");
		Response resonse = FastHttpClient.requestType(type).url(url).build().execute();
		int code = resonse.code();
		String str = resonse.string();
		if (code != 200) {
			Reporter.log("接口没有正确响应,响应值为  " + code);
			logger.info("接口没有正确响应,响应值为  " + code);
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}
		String temp_str = StringUtil.interceptString(str, 2000);
		Reporter.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		JSONObject retObj = JSON.parseObject(str);
		return retObj;
	}

}
