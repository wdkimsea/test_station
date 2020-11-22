package cn.guanmai.okhttp;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 
 * @author icecooly
 *
 */
public class GetRequest extends OkHttpRequest {
	//
	public GetRequest(String url, Object tag, Map<String, String> params, 
			Map<String, String> headers, int id) {
		super(url,tag,params,headers,null,null,null,id);
	}
	
	public GetRequest(String url, Object tag, Map<String, String> params, 
			Map<String, String> encodedParams,
			Map<String, String> headers, int id) {
		super(url,tag,params,encodedParams,headers,null,null,null,id);
	}

	@Override
	protected RequestBody buildRequestBody() {
		return null;
	}

	@Override
	protected Request buildRequest(RequestBody requestBody) {
		return builder.get().build();
	}
}
