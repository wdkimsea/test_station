package cn.guanmai.request.intefaces;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;

/* 
* @author liming 
* @date May 5, 2019 5:08:15 PM 
* @des OpenAPI 专用封装请求
* @version 1.0 
*/
public interface OpenRequest {
	/**
	 * 对象形式的参数
	 * 
	 * @param url
	 * @param type
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseRequest(String url, RequestType type, Object obj) throws Exception;

	/**
	 * 不带参数
	 * 
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseRequest(String url, RequestType type) throws Exception;

}
