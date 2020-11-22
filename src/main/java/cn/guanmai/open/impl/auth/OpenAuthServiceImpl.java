package cn.guanmai.open.impl.auth;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.auth.OpenStationInfoBean;
import cn.guanmai.open.interfaces.auth.OpenAuthService;
import cn.guanmai.open.url.AuthURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

public class OpenAuthServiceImpl implements OpenAuthService {

	private OpenRequest openRequest;

	public OpenAuthServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public OpenStationInfoBean stationInfo() throws Exception {

		String url = AuthURL.station_info_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenStationInfoBean.class)
				: null;

	}
}
