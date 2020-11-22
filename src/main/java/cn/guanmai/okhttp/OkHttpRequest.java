package cn.guanmai.okhttp;

import java.util.List;
import java.util.Map;

import cn.guanmai.okhttp.PostRequest.FileInfo;
import cn.guanmai.okhttp.callback.Callback;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 
 * @author icecooly
 *
 */
public abstract class OkHttpRequest {
	protected int id;
	protected String url;
	protected Map<String, String> params;
	protected Map<String, String> encodedParams;
	protected Map<String, String> headers;
	protected String body;
	protected List<FileInfo> fileInfos;
	protected MultipartBody multipartBody;
	protected Request.Builder builder = new Request.Builder();
	//
	protected OkHttpRequest(String url, Object tag, Map<String, String> params,
			Map<String, String> headers,
			List<FileInfo> fileInfos,
			String body,
			MultipartBody multipartBody,
			int id) {
		this(url, tag, params, null, headers, fileInfos, body, multipartBody, id);
	}
	//
	protected OkHttpRequest(String url, Object tag, Map<String, String> params,
			Map<String, String> encodedParams,
			Map<String, String> headers,
			List<FileInfo> fileInfos,
			String body,
			MultipartBody multipartBody,
			int id) {
		this.url = url;
		this.params = params;
		this.encodedParams=encodedParams;
		this.headers = headers;
		this.fileInfos=fileInfos;
		this.body=body;
		this.multipartBody=multipartBody;
		this.id = id;
		if (url==null) {
			throw new IllegalArgumentException("url can not be null.");
		}
		builder.url(url).tag(tag);
		appendHeaders();
	}

	protected abstract RequestBody buildRequestBody();
	
	protected abstract Request buildRequest(RequestBody requestBody);

	public RequestCall build(OkHttpClient okHttpClient) {
		return new RequestCall(this,okHttpClient);
	}

	public Request createRequest(Callback callback) {
		RequestBody requestBody=buildRequestBody();
		Request request = buildRequest(requestBody);
		return request;
	}

	protected void appendHeaders() {
		Headers.Builder headerBuilder = new Headers.Builder();
		if (headers == null || headers.isEmpty())
			return;
		for (String key : headers.keySet()) {
			headerBuilder.add(key, headers.get(key));
		}
		builder.headers(headerBuilder.build());
	}

	public int getId() {
		return id;
	}

}
