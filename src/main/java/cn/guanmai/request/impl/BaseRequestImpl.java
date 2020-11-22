package cn.guanmai.request.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.FastHttpClient;
import cn.guanmai.okhttp.PostRequest.FileInfo;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.okhttp.Response;
import cn.guanmai.okhttp.util.FileUtil;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.FilesUtil;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Feb 18, 2019 11:53:50 AM 
* @todo TODO
* @version 1.0 
*/
@SuppressWarnings("unchecked")
public class BaseRequestImpl implements BaseRequest {
	private static Logger logger = LoggerFactory.getLogger(BaseRequestImpl.class);
	private Map<String, String> headers;
	private String uuid;

	public BaseRequestImpl(Map<String, String> headers) {
		this.headers = headers;
	}

	public JSONObject baseRequest(String url, RequestType type, Object obj) throws Exception {
		ReporterCSS.log("前端接口: " + url);
		logger.info("前端接口: " + url);

		uuid = UUID.randomUUID().toString().replaceAll("-", "");
		headers.put("X-Guanmai-Request-Id", uuid);
		ReporterCSS.log("RequestID: " + uuid);

		Map<String, String> paramMap = null;
		if (obj instanceof Map) {
			paramMap = (Map<String, String>) obj;
		} else {
			paramMap = JsonUtil.objectToMap(obj);
		}
		String paramStr = JsonUtil.objectToStr(paramMap);

		ReporterCSS.log("传递参数: " + paramStr);
		logger.info("传递参数: " + paramStr);

		Response response = FastHttpClient.requestType(type).url(url).addHeaders(headers).addEncodedParams(paramMap)
				.build().execute();

		int code = response.code();
		String str = response.string();
		if (code != 200) {
			ReporterCSS.warn("接口没有正确响应,响应值为  " + code);
			logger.info("接口没有正确响应,响应值为  " + code);
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}

		String temp_str = StringUtil.interceptString(str, 2000);
		JSONObject retObj = null;
		try {
			retObj = JSON.parseObject(str);
		} catch (Exception e) {
			throw new Exception("接口 " + url + " 返回的数据为非JSON格式,返回信息: " + temp_str);
		}
		ReporterCSS.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		return retObj;
	}

	public JSONObject baseRequest(String url, RequestType type) throws Exception {
		ReporterCSS.log("前端接口: " + url);
		logger.info("前端接口: " + url);

		uuid = UUID.randomUUID().toString().replaceAll("-", "");
		headers.put("X-Guanmai-Request-Id", uuid);
		ReporterCSS.log("RequestID: " + uuid);

		ReporterCSS.log("传递参数: 无");
		logger.info("传递参数: 无");

		Response response = FastHttpClient.requestType(type).url(url).addHeaders(headers).build().execute();
		int code = response.code();
		String str = response.string();
		if (code != 200) {
			ReporterCSS.warn("接口没有正确响应,响应值为  " + code);
			logger.info("接口没有正确响应,响应值为  " + code);
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}

		String temp_str = StringUtil.interceptString(str, 2000);
		JSONObject retObj = null;
		try {
			retObj = JSON.parseObject(str);
		} catch (Exception e) {
			throw new Exception("接口 " + url + " 返回的数据为非JSON格式,返回信息: " + temp_str);
		}
		ReporterCSS.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		return retObj;
	}

	@Override
	public JSONObject baseRawRequest(String url, Object obj) throws Exception {
		ReporterCSS.log("前端接口: " + url);
		logger.info("前端接口: " + url);

		uuid = UUID.randomUUID().toString().replaceAll("-", "");
		headers.put("X-Guanmai-Request-Id", uuid);
		ReporterCSS.log("RequestID: " + uuid);

		String params = JsonUtil.objectToStr(obj);
		ReporterCSS.log("传递参数: " + params);
		logger.info("传递参数: " + params);

		Response response = FastHttpClient.post().url(url).addHeaders(headers).body(params).build().execute();

		int code = response.code();
		String str = response.string();
		if (code != 200) {
			ReporterCSS.warn("接口没有正确响应,响应值为  " + code);
			logger.info("接口没有正确响应,响应值为  " + code);
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}

		String temp_str = StringUtil.interceptString(str, 2000);
		JSONObject retObj = null;
		try {
			retObj = JSON.parseObject(str);
		} catch (Exception e) {
			throw new Exception("接口 " + url + " 返回的数据为非JSON格式,返回信息: " + temp_str);
		}
		ReporterCSS.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		return retObj;
	}

	@Override
	public String baseExport(String url, RequestType type, Object obj, String fileName) throws Exception {
		ReporterCSS.log("前端接口: " + url);
		logger.info("前端接口: " + url);

		uuid = UUID.randomUUID().toString().replaceAll("-", "");
		headers.put("X-Guanmai-Request-Id", uuid);
		ReporterCSS.log("RequestID: " + uuid);

		Map<String, String> paramMap = null;
		if (obj instanceof Map) {
			paramMap = (Map<String, String>) obj;
		} else {
			paramMap = JsonUtil.objectToMap(obj);
		}
		String paramStr = JsonUtil.objectToStr(paramMap);

		ReporterCSS.log("传递参数: " + paramStr);
		logger.info("传递参数: " + paramStr);

		Response response = FastHttpClient.requestType(type).url(url).addHeaders(headers).addEncodedParams(paramMap)
				.build().execute();
		int code = response.code();
		if (code != 200) {
			ReporterCSS.warn("接口没有正确响应,响应值为  " + code);
			logger.warn("接口没有正确响应,响应值为  " + code);
			ReporterCSS.warn("返回结果: " + response.string());
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}
		String contentType = response.body().contentType().toString();

		boolean result = true;
		if (contentType.contentEquals("application/json; charset=utf-8")) {
			String retStr = response.string();
			JSONObject retObj = JSON.parseObject(retStr);
			logger.info("返回结果: " + retStr);
			ReporterCSS.log("返回结果: " + retStr);
			result = retObj.getInteger("code") == 0;
		} else {
			byte[] bye = response.body().bytes();
			result = FilesUtil.saveTempFile(bye, fileName);
		}
		ReporterCSS.log("返回数据: 导出结果" + result);
		logger.info("返回数据: 导出结果" + result);
		String file_path = null;
		if (result) {
			file_path = System.getProperty("user.dir") + "/temp/" + fileName;
		}
		return file_path;
	}

	@Override
	public JSONObject baseUploadRequest(String url, Map<String, String> paramMap, String fileKey, String filePath)
			throws Exception {
		ReporterCSS.log("前端接口: " + url);
		logger.info("前端接口: " + url);

		uuid = UUID.randomUUID().toString().replaceAll("-", "");
		headers.put("X-Guanmai-Request-Id", uuid);
		ReporterCSS.log("RequestID: " + uuid);

		String file_name = filePath.substring(filePath.lastIndexOf("/") + 1);

		String paramStr = JsonUtil.objectToStr(paramMap);

		ReporterCSS.log("传递参数: " + paramStr);
		logger.info("传递参数: " + paramStr);

		byte[] fileBytes = FileUtil.getBytes(filePath);
		Response response = FastHttpClient.post().url(url).addHeaders(headers).addParams(paramMap)
				.addFile(fileKey, file_name, fileBytes).build().execute();
		int code = response.code();
		String str = response.string();
		if (code != 200) {
			ReporterCSS.warn("接口没有正确响应,响应值为  " + code);
			logger.warn("接口没有正确响应,响应值为  " + code);
			ReporterCSS.warn("返回结果: " + response.string());
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}

		String temp_str = StringUtil.interceptString(str, 2000);
		JSONObject retObj = null;
		try {
			retObj = JSON.parseObject(str);
		} catch (Exception e) {
			throw new Exception("接口 " + url + " 返回的数据为非JSON格式,返回信息: " + temp_str);
		}
		ReporterCSS.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		return retObj;
	}

	@Override
	public JSONObject baseUploadRequest(String url, Map<String, String> paramMap, Map<String, String> fileMap)
			throws Exception {
		ReporterCSS.log("前端接口: " + url);
		logger.info("前端接口: " + url);

		uuid = UUID.randomUUID().toString().replaceAll("-", "");
		headers.put("X-Guanmai-Request-Id", uuid);
		ReporterCSS.log("RequestID: " + uuid);

		String file_name = null;
		String file_path = null;
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		FileInfo fileInfo = null;
		for (String file_key : fileMap.keySet()) {
			fileInfo = new FileInfo();
			file_path = fileMap.get(file_key);
			file_name = file_path.substring(file_path.lastIndexOf("/") + 1);
			byte[] fileBytes = FileUtil.getBytes(file_path);

			fileInfo.partName = file_key;
			fileInfo.fileName = file_name;
			fileInfo.fileContent = fileBytes;
			fileInfos.add(fileInfo);
		}

		String paramStr = JsonUtil.objectToStr(paramMap);

		ReporterCSS.log("传递参数: " + paramStr);
		logger.info("传递参数: " + paramStr);

		Response response = FastHttpClient.post().url(url).addHeaders(headers).addParams(paramMap).addFile(fileInfos)
				.build().execute();
		int code = response.code();
		String str = response.string();
		if (code != 200) {
			ReporterCSS.warn("接口没有正确响应,响应值为  " + code);
			logger.warn("接口没有正确响应,响应值为  " + code);
			ReporterCSS.warn("返回结果: " + response.string());
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}

		String temp_str = StringUtil.interceptString(str, 2000);
		JSONObject retObj = null;
		try {
			retObj = JSON.parseObject(str);
		} catch (Exception e) {
			throw new Exception("接口 " + url + " 返回的数据为非JSON格式,返回信息: " + temp_str);
		}
		ReporterCSS.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		return retObj;
	}

	@Override
	public JSONObject baseRequestWithoutCookie(String url, RequestType type, Object obj) throws Exception {
		ReporterCSS.log("前端接口: " + url);
		logger.info("前端接口: " + url);

		Map<String, String> paramMap = null;
		if (obj instanceof Map) {
			paramMap = (Map<String, String>) obj;
		} else {
			paramMap = JsonUtil.objectToMap(obj);
		}
		String paramStr = JsonUtil.objectToStr(paramMap);

		ReporterCSS.log("传递参数: " + paramStr);
		logger.info("传递参数: " + paramStr);

		Response response = FastHttpClient.requestType(type).url(url).addEncodedParams(paramMap).build().execute();

		int code = response.code();
		String str = response.string();
		if (code != 200) {
			ReporterCSS.warn("接口没有正确响应,响应值为  " + code);
			logger.info("接口没有正确响应,响应值为  " + code);
			throw new Exception("接口 " + url + " 没有正确响应,响应值为 " + code);
		}

		String temp_str = StringUtil.interceptString(str, 2000);
		JSONObject retObj = null;
		try {
			retObj = JSON.parseObject(str);
		} catch (Exception e) {
			throw new Exception("接口 " + url + " 返回的数据为非JSON格式,返回信息: " + temp_str);
		}
		ReporterCSS.log("返回数据: " + temp_str);
		logger.info("返回数据: " + temp_str);
		return retObj;
	}
}
