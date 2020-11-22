package cn.guanmai.request.intefaces;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;

/* 
* @author liming 
* @date Feb 18, 2019 11:53:04 AM 
* @des 封装的请求方式
* @version 1.0 
*/
public interface BaseRequest {
	/**
	 * 带参数的请求
	 * 
	 * @param url
	 * @param type
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseRequest(String url, RequestType type, Object obj) throws Exception;

	/**
	 * 不带参数的请求
	 * 
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseRequest(String url, RequestType type) throws Exception;

	/**
	 * Raw参数格式的请求
	 * 
	 * @param url
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseRawRequest(String url, Object obj) throws Exception;

	/**
	 * 导出文件,返回文件路径
	 * 
	 * @param url
	 * @param type
	 * @param paramMap
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String baseExport(String url, RequestType type, Object obj, String fileName) throws Exception;

	/**
	 * 上传请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param fileName
	 * @param fileKey
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseUploadRequest(String url, Map<String, String> paramMap, String fileKey, String filePath)
			throws Exception;

	/**
	 * 多文件上传请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param fileMap  (key对应请求中的key,value对应文件路径)
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseUploadRequest(String url, Map<String, String> paramMap, Map<String, String> fileMap)
			throws Exception;

	/**
	 * 请求不携带Cookie
	 * 
	 * @param url
	 * @param type
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public JSONObject baseRequestWithoutCookie(String url, RequestType type, Object obj) throws Exception;

}
